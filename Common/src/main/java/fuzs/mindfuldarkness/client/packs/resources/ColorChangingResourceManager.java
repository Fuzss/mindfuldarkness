package fuzs.mindfuldarkness.client.packs.resources;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.NativeImage;
import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.handler.ColorChangedResourcesHandler;
import fuzs.mindfuldarkness.client.util.PixelDarkener;
import fuzs.mindfuldarkness.config.ClientConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// we need to extend net.minecraft.server.packs.resources.MultiPackResourceManager instead of just net.minecraft.server.packs.resources.CloseableResourceManager
// since Fabric Api adds an interface which is required for the whole net.fabricmc.fabric.api.resource.ResourceManagerHelper to work
// this makes a lot of unnecessary preparations in super constructor, but apart from that nothing is different from CloseableResourceManager
public class ColorChangingResourceManager extends MultiPackResourceManager {
    private final CloseableResourceManager resourceManager;
    private final Predicate<ResourceLocation> filter;

    public ColorChangingResourceManager(PackType type, CloseableResourceManager resourceManager) {
        super(type, resourceManager.listPacks().collect(Collectors.toList()));
        this.resourceManager = resourceManager;
        this.filter = getResourceLocationFilter();
    }

    private static Predicate<ResourceLocation> getResourceLocationFilter() {
        List<String> paths = MindfulDarkness.CONFIG.get(ClientConfig.class).paths;
        List<String> normalizedDomains = compileNormalizedDomains(paths);
        List<Function<String, Boolean>> validPaths = compileValidPaths(paths);
        return id -> matchesPath(normalizedDomains, validPaths, id);
    }

    @Override
    public void close() {
        this.resourceManager.close();
    }

    @Override
    public Set<String> getNamespaces() {
        return this.resourceManager.getNamespaces();
    }

    @Override
    public List<Resource> getResourceStack(ResourceLocation location) {
        return this.resourceManager.getResourceStack(location);
    }

    @Override
    public Map<ResourceLocation, Resource> listResources(String path, Predicate<ResourceLocation> filter) {
        return this.resourceManager.listResources(path, filter);
    }

    @Override
    public Map<ResourceLocation, List<Resource>> listResourceStacks(String path, Predicate<ResourceLocation> filter) {
        return this.resourceManager.listResourceStacks(path, filter);
    }

    @Override
    public Stream<PackResources> listPacks() {
        return this.resourceManager.listPacks();
    }

    @Override
    public Optional<Resource> getResource(ResourceLocation location) {
        Optional<Resource> resource = this.resourceManager.getResource(location);
        if (resource.isPresent() && this.filter.test(location)) {
            ColorChangedResourcesHandler.INSTANCE.add(location);
            if (MindfulDarkness.CONFIG.getHolder(ClientConfig.class).isAvailable() && MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
                double textureDarkness = MindfulDarkness.CONFIG.get(ClientConfig.class).textureDarkness.get();
                PixelDarkener algorithm = MindfulDarkness.CONFIG.get(ClientConfig.class).darkeningAlgorithm.get();
                ByteArrayInputStream inputStream = adjustImage(resource.get(), textureDarkness, algorithm);
                Resource newResource = new ColorChangingResource(resource.get(), () -> inputStream);
                return Optional.of(newResource);
            }
        }
        return resource;
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
