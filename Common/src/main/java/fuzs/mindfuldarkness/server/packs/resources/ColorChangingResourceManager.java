package fuzs.mindfuldarkness.server.packs.resources;

import com.mojang.blaze3d.platform.NativeImage;
import fuzs.mindfuldarkness.MindfulDarkness;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static fuzs.mindfuldarkness.util.HSPConversionUtil.HSPtoRGB;
import static fuzs.mindfuldarkness.util.HSPConversionUtil.RGBtoHSP;

public class ColorChangingResourceManager implements CloseableResourceManager {
    private final CloseableResourceManager resourceManager;

    public ColorChangingResourceManager(CloseableResourceManager resourceManager) {
        this.resourceManager = resourceManager;
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
    public List<Resource> getResourceStack(ResourceLocation pLocation) {
        return this.resourceManager.getResourceStack(pLocation);
    }

    @Override
    public Map<ResourceLocation, Resource> listResources(String pPath, Predicate<ResourceLocation> pFilter) {
        return this.resourceManager.listResources(pPath, pFilter);
    }

    @Override
    public Map<ResourceLocation, List<Resource>> listResourceStacks(String pPath, Predicate<ResourceLocation> pFilter) {
        return this.resourceManager.listResourceStacks(pPath, pFilter);
    }

    @Override
    public Stream<PackResources> listPacks() {
        return this.resourceManager.listPacks();
    }

    @Override
    public Optional<Resource> getResource(ResourceLocation pLocation) {
        Optional<Resource> resource = this.resourceManager.getResource(pLocation);
        if (resource.isPresent() && (pLocation.getPath().matches("textures/gui/[^/]*\\.png") || pLocation.getPath().matches("textures/gui/container/.*\\.png"))) {
            MindfulDarkness.LOGGER.info("{}", pLocation);

            try (InputStream open = resource.get().open()) {
                NativeImage read = NativeImage.read(open);
                for (int i = 0; i < read.getWidth(); i++) {
                    for (int j = 0; j < read.getHeight(); j++) {
                        int pixelRGBA = read.getPixelRGBA(i, j);
                        if (NativeImage.getA(pixelRGBA) != 0) {
                            int i1 = multiplyColorBy(pixelRGBA, 0.2, true);
                            if (i1 == pixelRGBA) {

                                int pAbgrColor = darken2(pixelRGBA) | 0xFF000000;
                                read.setPixelRGBA(i, j, pAbgrColor);
                            } else {
                                read.setPixelRGBA(i, j, i1 | 0xFF000000);
                            }
                        }
                    }
                }
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(read.asByteArray());
                return Optional.of(new ColorChangingResource(resource.get(), () -> byteArrayInputStream));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
        return resource;
    }

    private static int multiplyColorBy(int packedColor, double multiplier, boolean onlyGrayscale) {
        multiplier = Math.max(multiplier, 0.0);
        int r = NativeImage.getR(packedColor);
        int g = NativeImage.getG(packedColor);
        int b = NativeImage.getB(packedColor);
        if (!onlyGrayscale || r == g && r == b) {
            r = Math.min((int) (r * multiplier) << 16, 255);
            g = Math.min((int) (g * multiplier) << 8, 255);
            b = Math.min((int) (b * multiplier), 255);
            return r | g | b;
        }
        return packedColor;
    }

    private static int reduceGrayscale3(int color) {
        int minus = 127;
        int r = NativeImage.getR(color);
        int g = NativeImage.getG(color);
        int b = NativeImage.getB(color);
        int rn = r - minus;
        int gn = g - minus;
        int bn = b - minus;
        int overflow = Math.min(0, rn) + Math.min(0, gn) + Math.min(0, bn);

        float mult = .35F;
        if (r == g && r == b) {
        }
        return (int) (r * mult) << 16 | (int) (g * mult) << 8 | (int) (b * mult);
//        return color;
    }

    private static int brightenColor(int potionColor) {
        int red = potionColor >> 16 & 255;
        int green = potionColor >> 8 & 255;
        int blue = potionColor & 255;
        if (red + green + blue > 127) {
            int max = Math.max(red, Math.max(green, blue));
            int decrease = 127 + max;
            final int[] color = {red - decrease, green - decrease, blue - decrease};
//            redistributeColors(color);
            return color[0] << 16 | color[1] << 8 | color[0];
        }
        return potionColor;
    }

    private static int darken2(int potionColor) {
        int alpha = potionColor >> 24 & 255;
        int red = potionColor >> 16 & 255;
        int green = potionColor >> 8 & 255;
        int blue = potionColor & 255;
        double[] doubles0 = new double[]{red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0};
        double[] doubles = RGBtoHSP(red / 255.0, green / 255.0, blue / 255.0);
        double[] doubles1 = HSPtoRGB(doubles[0], doubles[1], doubles[2] * .5);
        return (int) (doubles1[0] * 255.0) << 16 | (int) (doubles1[1] * 255.0) << 8 | (int) (doubles1[2] * 255.0);
//        float[] floats = Color.RGBtoHSB(red, green, blue, null);
//        return Color.HSBtoRGB(floats[0], floats[1], floats[2] * 0.25F);
    }

    /**
     * from <a href="https://stackoverflow.com/questions/141855/programmatically-lighten-a-color">programmatically-lighten-a-color</a>
     */
    private static void redistributeColors(int[] color) {
        int min = Math.min(color[0], Math.min(color[1], color[2]));
        if (min < 0) {
            int total = color[0] + color[1] + color[2];
            if (total < 0) {
                color[0] = color[1] = color[2] = 0;
            } else {
                int x = (3 * 255 - total) / (3 * min - total);
                int gray = 255 - x * min;
                color[0] = gray + x * color[0];
                color[1] = gray + x * color[1];
                color[2] = gray + x * color[2];
            }
        }
    }
}
