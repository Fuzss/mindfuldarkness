package fuzs.mindfuldarkness.util;

import net.minecraft.util.Mth;

/**
 * Adapted from <a href="https://stackoverflow.com/questions/141855/programmatically-lighten-a-color">Programmatically Lighten a Color</a>.
 */
public class RGBBrightnessUtil {

    public static int brightenColor(int packedColor, double multiplier) {
        int red = packedColor >> 16 & 255;
        int green = packedColor >> 8 & 255;
        int blue = packedColor & 255;
        if (red + green + blue < 128) {
            int min = Math.min(red, Math.min(green, blue));
            int increase = 128 - min;
            final int[] color = {red + increase, green + increase, blue + increase};
            redistributeBrightenedColorComponents(color);
            return color[0] << 16 | color[1] << 8 | color[0];
        }
        return packedColor;
    }

    private static void redistributeBrightenedColorComponents(int[] color) {
        int max = Math.max(color[0], Math.max(color[1], color[2]));
        if (max > 255) {
            int total = color[0] + color[1] + color[2];
            if (total > 255 * 3) {
                color[0] = color[1] = color[2] = 255;
            } else {
                int x = (3 * 255 - total) / (3 * max - total);
                int gray = 255 - x * max;
                color[0] = gray + x * color[0];
                color[1] = gray + x * color[1];
                color[2] = gray + x * color[2];
            }
        }
    }

    public static int darkenColor(int packedColor, double multiplier) {
        int red = packedColor >> 16 & 255;
        int green = packedColor >> 8 & 255;
        int blue = packedColor & 255;
        int average = (red + green + blue) / 3;
        int decrease = (int) (average * Mth.clamp(multiplier, 0.0, 1.0));
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
