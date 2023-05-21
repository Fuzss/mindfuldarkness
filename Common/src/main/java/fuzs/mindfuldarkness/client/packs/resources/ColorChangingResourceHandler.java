package fuzs.mindfuldarkness.client.packs.resources;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.NativeImage;
import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.handler.ColorChangedAssetsManager;
import fuzs.mindfuldarkness.client.packs.resources.ColorChangingResource;
import fuzs.mindfuldarkness.client.util.PixelDarkener;
import fuzs.mindfuldarkness.config.ClientConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Function;

public class ColorChangingResourceHandler {
    @Nullable
    private List<String> normalizedDomains;
    @Nullable
    private List<Function<String, Boolean>> validPaths;

    public Optional<Resource> getResource(ResourceLocation location, Optional<Resource> resource) {
        if (resource.isPresent() && this.matchesPath(location)) {
            ColorChangedAssetsManager.INSTANCE.add(location);
            if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
                double textureDarkness = MindfulDarkness.CONFIG.get(ClientConfig.class).textureDarkness.get();
                PixelDarkener algorithm = MindfulDarkness.CONFIG.get(ClientConfig.class).darkeningAlgorithm.get();
                ByteArrayInputStream inputStream = adjustImage(resource.get(), textureDarkness, algorithm);
                Resource newResource = new ColorChangingResource(resource.get(), () -> inputStream);
                return Optional.of(newResource);
            }
        }
        return Optional.empty();
    }

    private static ByteArrayInputStream adjustImage(Resource resource, double textureDarkness, PixelDarkener algorithm) {
        try (InputStream open = resource.open(); NativeImage image = NativeImage.read(open)) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int pixel = image.getPixelRGBA(x, y);
                    int alpha = NativeImage.getA(pixel);
                    if (alpha != 0) {
                        image.setPixelRGBA(x, y, algorithm.processPixel(pixel, textureDarkness) | alpha << 24);
                    }
                }
            }
            return new ByteArrayInputStream(image.asByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean matchesPath(ResourceLocation location) {
        if (!MindfulDarkness.CONFIG.getHolder(ClientConfig.class).isAvailable()) return false;
        if (this.normalizedDomains == null || this.validPaths == null) {
            List<String> paths = MindfulDarkness.CONFIG.get(ClientConfig.class).paths;
            this.normalizedDomains = compileNormalizedDomains(paths);
            this.validPaths = compileValidPaths(paths);
        }
        return matchesPath(this.normalizedDomains, this.validPaths, location);
    }

    private static List<String> compileNormalizedDomains(List<String> paths) {
        // Compiles a list of directories with resources we want to modify, child directories are replaced if the parent is included, too.
        // This is supposed to provide a relatively cheap check for a location if it's even interesting to us,
        // so usually just something starting with 'textures/gui'.
        List<String> normalized = Lists.newArrayList();
        paths: for (String path : paths) {
            if (path.startsWith("!")) continue;
            path = path.replaceAll(".+:", "");
            if (path.startsWith("/")) path = path.substring(1);
            if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
            if (path.matches(".*\\.\\w*")) {
                path = path.substring(0, path.lastIndexOf("/"));
            }
            if (path.isEmpty()) continue;
            ListIterator<String> iterator = normalized.listIterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                if (s.startsWith(path)) {
                    iterator.set(path);
                    continue paths;
                } else if (path.startsWith(s)) {
                    continue paths;
                }
            }
            normalized.add(path);
        }
        return normalized;
    }

    private static List<Function<String, Boolean>> compileValidPaths(List<String> paths) {
        // This compiles the filter paths into proper regex and then into predicates which can return true, false (if inverse) or null (if not applicable).
        List<Function<String, Boolean>> pathFilters = Lists.newArrayList();
        for (String path : paths) {
            boolean inverse = path.startsWith("!");
            if (inverse) path = path.substring(1);
            if (!path.contains(":")) path = "\\w+:" + path;
            path = path.replaceAll("\\*", "[^/]+").replaceAll("\\.", "\\\\.");
            String filter = path;
            pathFilters.add(s -> s.matches(filter) ? !inverse : null);
        }
        Collections.reverse(pathFilters);
        return pathFilters;
    }

    private static boolean matchesPath(List<String> domains, List<Function<String, Boolean>> filters, ResourceLocation resourceLocation) {
        String path = resourceLocation.toString();
        for (String domain : domains) {
            if (resourceLocation.getPath().startsWith(domain)) {
                for (Function<String, Boolean> filter : filters) {
                    Boolean result = filter.apply(path);
                    if (result != null) return result;
                }
                return false;
            }
        }
        return false;
    }
}
