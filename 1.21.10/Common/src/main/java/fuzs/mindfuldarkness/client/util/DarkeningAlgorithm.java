package fuzs.mindfuldarkness.client.util;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum DarkeningAlgorithm implements StringRepresentable {
    GRAYSCALE_AND_HSP("grayscale_and_hsp") {
        @Override
        public int processPixel(int pixel, double multiplier) {
            int newPixel = RGBBrightnessUtil.multiplyColorComponentsBy(pixel, multiplier * multiplier, true);
            if (newPixel == pixel) {
                newPixel = RGBBrightnessUtil.darkenColorHSP(pixel, multiplier);
            }
            return newPixel;
        }
    }, HSP("hsp") {
        @Override
        public int processPixel(int pixel, double multiplier) {
            return RGBBrightnessUtil.darkenColorHSP(pixel, multiplier);
        }
    }, GRAYSCALE_AND_HSL("grayscale_and_hsl") {
        @Override
        public int processPixel(int pixel, double multiplier) {
            int newPixel = RGBBrightnessUtil.multiplyColorComponentsBy(pixel, multiplier * multiplier, true);
            if (newPixel == pixel) {
                newPixel = RGBBrightnessUtil.darkenColorHSL(pixel, multiplier);
            }
            return newPixel;
        }
    }, HSL("hsl") {
        @Override
        public int processPixel(int pixel, double multiplier) {
            return RGBBrightnessUtil.darkenColorHSL(pixel, multiplier);
        }
    }, GRAYSCALE_AND_LINEAR("grayscale_and_linear") {
        @Override
        public int processPixel(int pixel, double multiplier) {
            int newPixel = RGBBrightnessUtil.multiplyColorComponentsBy(pixel, multiplier * multiplier, true);
            if (newPixel == pixel) {
                return RGBBrightnessUtil.darkenColor(pixel, 1.0 - multiplier);
            }
            return newPixel;
        }
    }, LINEAR("linear") {
        @Override
        public int processPixel(int pixel, double multiplier) {
            return RGBBrightnessUtil.darkenColor(pixel, 1.0 - multiplier);
        }
    };

    private final String name;

    DarkeningAlgorithm(String name) {
        this.name = name;
    }

    public abstract int processPixel(int pixel, double multiplier);

    public Component getComponent() {
        return Component.translatable("screen.daytime_switcher.algorithm." + this.name);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
