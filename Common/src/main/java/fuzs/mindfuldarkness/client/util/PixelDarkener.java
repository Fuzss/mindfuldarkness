package fuzs.mindfuldarkness.client.util;

import net.minecraft.network.chat.Component;

public enum PixelDarkener {
    GRAYSCALE_AND_HSP("screen.daytime_switcher.algorithm.grayscale_and_hsp") {

        @Override
        public int processPixel(int pixel, double multiplier) {
            int newPixel = RGBBrightnessUtil.multiplyColorComponentsBy(pixel, multiplier * multiplier, true);
            if (newPixel == pixel) {
                newPixel = RGBBrightnessUtil.darkenColorHSP(pixel, multiplier);
            }
            return newPixel;
        }
    }, HSP("screen.daytime_switcher.algorithm.hsp") {

        @Override
        public int processPixel(int pixel, double multiplier) {
            return RGBBrightnessUtil.darkenColorHSP(pixel, multiplier);
        }
    }, GRAYSCALE_AND_HSV("screen.daytime_switcher.algorithm.grayscale_and_hsv") {

        @Override
        public int processPixel(int pixel, double multiplier) {
            int newPixel = RGBBrightnessUtil.multiplyColorComponentsBy(pixel, multiplier * multiplier, true);
            if (newPixel == pixel) {
                newPixel = RGBBrightnessUtil.darkenColorHSV(pixel, multiplier);
            }
            return newPixel;
        }
    }, HSV("screen.daytime_switcher.algorithm.hsv") {

        @Override
        public int processPixel(int pixel, double multiplier) {
            return RGBBrightnessUtil.darkenColorHSV(pixel, multiplier);
        }
    }, GRAYSCALE_AND_HSL("screen.daytime_switcher.algorithm.grayscale_and_hsl") {

        @Override
        public int processPixel(int pixel, double multiplier) {
            int newPixel = RGBBrightnessUtil.multiplyColorComponentsBy(pixel, multiplier * multiplier, true);
            if (newPixel == pixel) {
                newPixel = RGBBrightnessUtil.darkenColorHSL(pixel, multiplier);
            }
            return newPixel;
        }
    }, HSL("screen.daytime_switcher.algorithm.hsl") {

        @Override
        public int processPixel(int pixel, double multiplier) {
            return RGBBrightnessUtil.darkenColorHSL(pixel, multiplier);
        }
    }, GRAYSCALE_AND_LINEAR("screen.daytime_switcher.algorithm.grayscale_and_linear") {

        @Override
        public int processPixel(int pixel, double multiplier) {
            int newPixel = RGBBrightnessUtil.multiplyColorComponentsBy(pixel, multiplier * multiplier, true);
            if (newPixel == pixel) {
                return RGBBrightnessUtil.darkenColor(pixel, 1.0 - multiplier);
            }
            return newPixel;
        }
    }, LINEAR("screen.daytime_switcher.algorithm.linear") {

        @Override
        public int processPixel(int pixel, double multiplier) {
            return RGBBrightnessUtil.darkenColor(pixel, 1.0 - multiplier);
        }
    };

    private final Component component;

    PixelDarkener(String translationKey) {
        this.component = Component.translatable(translationKey);
    }

    public abstract int processPixel(int pixel, double multiplier);

    public Component getComponent() {
        return this.component;
    }
}
