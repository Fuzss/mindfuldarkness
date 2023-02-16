package fuzs.mindfuldarkness.client.util;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.util.Mth;

import static fuzs.mindfuldarkness.client.util.HSPConversionUtil.HSPtoRGB;
import static fuzs.mindfuldarkness.client.util.HSPConversionUtil.RGBtoHSP;
import static fuzs.mindfuldarkness.client.util.CompactConversionsUtil.*;

/**
 * Adapted from <a href="https://stackoverflow.com/questions/141855/programmatically-lighten-a-color">Programmatically Lighten a Color</a>.
 */
public class RGBBrightnessUtil {
    private static final int GRAYSCALE_DELTA = 3;

    public static int darkenColorHSP(int packedColor, double brightness) {
        double[] hspColorArray = unpackRGBToHSP(packedColor);
        double[] rgbColorArray = HSPtoRGB(hspColorArray[0], hspColorArray[1], hspColorArray[2] * brightness);
        return packRGBColor(rgbColorArray);
    }

    public static double[] unpackRGBToHSP(int packedColor) {
        int red = packedColor >> 16 & 255;
        int green = packedColor >> 8 & 255;
        int blue = packedColor & 255;
        return RGBtoHSP(red / 255.0, green / 255.0, blue / 255.0);
    }

    public static int darkenColorHSV(int packedColor, double brightness) {
        double[] hsvColorArray = unpackRGBToHSV(packedColor);
        double[] rgbColorArray = hsv2rgb((float) hsvColorArray[0], (float) hsvColorArray[1], (float) (hsvColorArray[2] * brightness));
        return packRGBColor(rgbColorArray);
    }

    public static double[] unpackRGBToHSV(int packedColor) {
        int red = packedColor >> 16 & 255;
        int green = packedColor >> 8 & 255;
        int blue = packedColor & 255;
        return rgb2hsv(red, green, blue);
    }

    public static int darkenColorHSL(int packedColor, double brightness) {
        double[] hslColorArray = unpackRGBToHSL(packedColor);
        double[] rgbColorArray = hsl2rgb((float) hslColorArray[0], (float) hslColorArray[1], (float) (hslColorArray[2] * brightness));
        return packRGBColor(rgbColorArray);
    }

    public static double[] unpackRGBToHSL(int packedColor) {
        int red = packedColor >> 16 & 255;
        int green = packedColor >> 8 & 255;
        int blue = packedColor & 255;
        return rgb2hsl(red / 255.0, green / 255.0, blue / 255.0);
    }

    public static int packRGBColor(double[] rgbColorArray) {
        return packRGBColor((int) (rgbColorArray[0] * 255.0), (int) (rgbColorArray[1] * 255.0), (int) (rgbColorArray[2] * 255.0));
    }

    public static int packRGBColor(int red, int green, int blue) {
        return Mth.clamp(red, 0, 255) << 16 | Mth.clamp(green, 0, 255) << 8 | Mth.clamp(blue, 0, 255);
    }

    public static int multiplyColorComponentsBy(int packedColor, double multiplier, boolean onlyGrayscale) {
        multiplier = Math.max(multiplier, 0.0);
        int r = NativeImage.getR(packedColor);
        int g = NativeImage.getG(packedColor);
        int b = NativeImage.getB(packedColor);
        if (!onlyGrayscale || isGrayscaleColor(r, g, b)) {
            r = (int) Math.min(r * multiplier, 255);
            g = (int) Math.min(g * multiplier, 255);
            b = (int) Math.min(b * multiplier, 255);
            return r << 16 | g << 8 | b;
        }
        return packedColor;
    }

    private static boolean isGrayscaleColor(int r, int g, int b) {
        int average = (r + g + b) / 3;
        return Math.abs(average - r) < GRAYSCALE_DELTA && Math.abs(average - g) < GRAYSCALE_DELTA && Math.abs(average - b) < GRAYSCALE_DELTA;
    }

    public static int brightenColor(int packedColor, double brightenBy) {
        brightenBy = Math.max(brightenBy, 0.0);
        int red = packedColor >> 16 & 255;
        int green = packedColor >> 8 & 255;
        int blue = packedColor & 255;
        int average = (red + green + blue) / 3;
        int increase = (int) (average * (1.0 + brightenBy));
        int[] colorArray = redistributeBrightenedColorComponents(red + increase, green + increase, blue + increase);
        return colorArray[0] << 16 | colorArray[1] << 8 | colorArray[0];
    }

    private static int[] redistributeBrightenedColorComponents(int red, int green, int blue) {
        int[] colorArray = new int[]{red, green, blue};
        int max = Math.max(red, Math.max(green, blue));
        if (max > 255) {
            int total = red + green + blue;
            if (total > 255 * 3) {
                colorArray[0] = colorArray[1] = colorArray[2] = 255;
            } else {
                int x = (3 * 255 - total) / (3 * max - total);
                int gray = 255 - x * max;
                colorArray[0] = gray + x * red;
                colorArray[1] = gray + x * green;
                colorArray[2] = gray + x * blue;
            }
        }
        return colorArray;
    }

    public static int darkenColor(int packedColor, double darkenBy) {
        darkenBy = Mth.clamp(darkenBy, 0.0, 1.0);
        int red = packedColor >> 16 & 255;
        int green = packedColor >> 8 & 255;
        int blue = packedColor & 255;
        int average = (red + green + blue) / 3;
        int decrease = (int) (average * darkenBy);
        int[] colorArray = redistributeDarkenedColorComponents(red - decrease, green - decrease, blue - decrease);
        return colorArray[0] << 16 | colorArray[1] << 8 | colorArray[2];
    }

    private static int[] redistributeDarkenedColorComponents(int red, int green, int blue) {
        int[] colorArray = new int[]{red, green, blue};
        int min = Math.min(red, Math.min(green, blue));
        if (min < 0) {
            int total = red + green + blue;
            if (total < 0) {
                colorArray[0] = colorArray[1] = colorArray[2] = 0;
            } else {
                int x = (total) / (total - 3 * min);
                int gray = x * min;
                colorArray[0] = gray - x * red;
                colorArray[1] = gray - x * green;
                colorArray[2] = gray - x * blue;
            }
        }
        return colorArray;
    }
}
