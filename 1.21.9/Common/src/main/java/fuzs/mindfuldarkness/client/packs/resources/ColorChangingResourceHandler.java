package fuzs.mindfuldarkness.client.packs.resources;

import com.mojang.blaze3d.platform.NativeImage;
import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.handler.ColorChangedAssetsManager;
import fuzs.mindfuldarkness.client.util.DarkeningAlgorithm;
import fuzs.mindfuldarkness.client.util.RGBBrightnessUtil;
import fuzs.mindfuldarkness.config.ClientConfig;
import fuzs.puzzleslib.api.client.packs.v1.NativeImageHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

public class ColorChangingResourceHandler {
    public static final String VALID_RESOURCE_LOCATION_NAMESPACE = "[a-z0-9_.-]+";
    public static final String VALID_RESOURCE_LOCATION_PATH = "[a-z0-9/._-]+";
    /**
     * Path may begin with '!' if it is an exclusion.
     * <p>Path can optionally begin with '<code>namespace</code>:'.
     * <p>Path can end with either a valid resource location path, or an optional resource location path followed by '/*',
     * or just '*' if directed at root.
     */
    public static final String VALID_MINDFUL_DARKNESS_PATH = "^!?([a-z0-9_.-]+:)?([a-z0-9/_.-]+|(([a-z0-9/_.-]*/|)\\*(\\.\\w+)?))$";
    public static final ColorChangingResourceHandler INSTANCE = new ColorChangingResourceHandler();

    @Nullable
    private List<String> normalizedDomains;
    @Nullable
    private List<Function<String, Boolean>> validPaths;

    private ColorChangingResourceHandler() {
        // NO-OP
    }

    public Optional<Resource> getResource(ResourceLocation location, Optional<Resource> resource) {
        if (resource.isPresent() && this.matchesPath(location)) {
            ColorChangedAssetsManager.INSTANCE.add(location);
            if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
                double textureDarkness = MindfulDarkness.CONFIG.get(ClientConfig.class).textureDarkness.get();
                DarkeningAlgorithm algorithm = MindfulDarkness.CONFIG.get(ClientConfig.class).darkeningAlgorithm.get();
                ByteArrayInputStream inputStream = adjustImage(resource.get(), textureDarkness, algorithm);
                Resource newResource = new ForwardingResource(resource.get(), () -> inputStream);
                return Optional.of(newResource);
            }
        }
        return Optional.empty();
    }

    private static ByteArrayInputStream adjustImage(Resource resource, double textureDarkness, DarkeningAlgorithm algorithm) {
        try (InputStream open = resource.open(); NativeImage image = NativeImage.read(open)) {
            processImage(image, textureDarkness, algorithm);
            return new ByteArrayInputStream(NativeImageHelper.asByteArray(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void processResource(ResourceLocation resourceLocation, NativeImage nativeImage) {
        if (this.matchesPath(resourceLocation)) {
            ColorChangedAssetsManager.INSTANCE.add(resourceLocation);
            if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
                double textureDarkness = MindfulDarkness.CONFIG.get(ClientConfig.class).textureDarkness.get();
                DarkeningAlgorithm algorithm = MindfulDarkness.CONFIG.get(ClientConfig.class).darkeningAlgorithm.get();
                processImage(nativeImage, textureDarkness, algorithm);
            }
        }
    }

    public static void processImage(NativeImage nativeImage, double textureDarkness, DarkeningAlgorithm algorithm) {
        for (int x = 0; x < nativeImage.getWidth(); x++) {
            for (int y = 0; y < nativeImage.getHeight(); y++) {
                int pixel = nativeImage.getPixel(x, y);
                int alpha = RGBBrightnessUtil.getA(pixel);
                if (alpha != 0) {
                    nativeImage.setPixel(x, y, algorithm.processPixel(pixel, textureDarkness) | alpha << 24);
                }
            }
        }
    }

    public void clear() {
        this.normalizedDomains = null;
        this.validPaths = null;
    }

    private boolean matchesPath(ResourceLocation location) {
        if (!MindfulDarkness.CONFIG.getHolder(ClientConfig.class).isAvailable()) return false;
        return matchesPath(this.getNormalizedDomains(), this.getValidPaths(), location);
    }

    private synchronized List<String> getNormalizedDomains() {
        List<String> normalizedDomains = this.normalizedDomains;
        if (normalizedDomains == null) {
            List<String> paths = MindfulDarkness.CONFIG.get(ClientConfig.class).paths;
            this.normalizedDomains = normalizedDomains = compileNormalizedDomains(paths);
        }
        return normalizedDomains;
    }

    private synchronized List<Function<String, Boolean>> getValidPaths() {
        List<Function<String, Boolean>> validPaths = this.validPaths;
        if (validPaths == null) {
            List<String> paths = MindfulDarkness.CONFIG.get(ClientConfig.class).paths;
            this.validPaths = validPaths = compileValidPaths(paths);
        }
        return validPaths;
    }

    private static List<String> compileNormalizedDomains(List<String> paths) {
        // Compiles a list of directories with resources we want to modify, child directories are replaced if the parent is included, too.
        // This is supposed to provide a relatively cheap check for a location if it's even interesting to us,
        // so usually just something starting with 'textures/gui'.
        List<String> normalized = new ArrayList<>();
        paths: for (String path : paths) {
            if (path.startsWith("!")) continue;
            // remove namespace, we do this 'optimization' globally
            path = path.replaceAll(".+:", "");
            // remove file part, we only want to optimize for directories
            // file part is not removed if there is no directory before it
            if (path.matches(".+/(.+\\.\\w+|\\*(\\.\\w+)?)")) {
                path = path.substring(0, path.lastIndexOf("/"));
            }
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            if (!path.isEmpty()) {
                // disable this when wildcard is in root, shouldn't really happen though
                if (path.matches("\\*(\\.\\w+)?")) return List.of("");
                ListIterator<String> iterator = normalized.listIterator();
                while (iterator.hasNext()) {
                    String s = iterator.next();
                    String prefix = StringUtils.getCommonPrefix(s, path);
                    if (!prefix.isEmpty() && (prefix.length() >= s.length() || s.charAt(prefix.length()) == '/')) {
                        iterator.set(prefix);
                        continue paths;
                    }
                }
                normalized.add(path);
            }
        }
        return normalized;
    }

    private static List<Function<String, Boolean>> compileValidPaths(List<String> paths) {
        // This compiles the filter paths into proper regex and then into predicates which can return true, false (if inverse) or null (if not applicable).
        List<Function<String, Boolean>> pathFilters = new ArrayList<>();
        for (String path : paths) {
            boolean inverse = path.startsWith("!");
            if (inverse) path = path.substring(1);
            if (!path.contains(":")) path = VALID_RESOURCE_LOCATION_NAMESPACE + ":" + path;
            // wildcard is only valid for files, so use regex match without '/' which vanilla normally supports
            if (path.endsWith("/")) {
                path = path.concat(VALID_RESOURCE_LOCATION_NAMESPACE);
            }
            path = path.replaceAll("\\*", VALID_RESOURCE_LOCATION_PATH).replaceAll("\\.", "\\\\.");
            String filter = "^" + path + "$";
            pathFilters.add((String s) -> s.matches(filter) ? !inverse : null);
        }
        Collections.reverse(pathFilters);
        return pathFilters;
    }

    private static boolean matchesPath(List<String> domains, List<Function<String, Boolean>> filters, ResourceLocation resourceLocation) {
        // hardcode to png, otherwise we would mess with all sorts of files, even when they are no image
        // implementation works for all file extensions otherwise if this check is removed
        if (resourceLocation.getPath().startsWith("textures/") && !resourceLocation.getPath().endsWith(".png")) {
            return false;
        }

        String path = resourceLocation.toString();
        for (String domain : domains) {
            if (resourceLocation.getPath().startsWith(domain)) {
                for (Function<String, Boolean> filter : filters) {
                    Boolean result = filter.apply(path);

                    if (result != null) {
                        return result;
                    }
                }

                return false;
            }
        }

        return false;
    }
}
