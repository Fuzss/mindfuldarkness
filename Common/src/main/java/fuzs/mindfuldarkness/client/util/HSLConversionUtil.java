package fuzs.mindfuldarkness.client.util;

import net.minecraft.util.Mth;

public class HSLConversionUtil {

    /**
     * Convert a RGB Color to it corresponding HSL values.
     *
     * @return an array containing the 3 HSL values.
     */
    public static double[] RGBtoHSL(double r, double g, double b) {

        //	Minimum and Maximum RGB values are used in the HSL calculations

        double min = Math.min(r, Math.min(g, b));
        double max = Math.max(r, Math.max(g, b));

        //  Calculate the Hue

        double h = 0;

        if (max == min) h = 0;
        else if (max == r) h = ((60 * (g - b) / (max - min)) + 360) % 360;
        else if (max == g) h = (60 * (b - r) / (max - min)) + 120;
        else if (max == b) h = (60 * (r - g) / (max - min)) + 240;

        //  Calculate the Luminance

        double l = (max + min) / 2;

        //  Calculate the Saturation

        double s = 0;

        if (max == min) s = 0;
        else if (l <= .5f) s = (max - min) / (max + min);
        else s = (max - min) / (2 - max - min);

        return new double[]{h, s, l};
    }

    /**
     * Convert HSL values to a RGB Color.
     *
     * @param h     Hue is specified as degrees in the range 0 - 360.
     * @param s     Saturation is specified as a percentage in the range 1 - 100.
     * @param l     Lumanance is specified as a percentage in the range 1 - 100.
     * @return the RGB Color object
     */
    public static double[] HSLtoRGB(double h, double s, double l) {

        s = Mth.clamp(s, 0.0, 1.0);
        l = Mth.clamp(l, 0.0, 1.0);

        //  Formula needs all values between 0 - 1.

        h = h % 360.0f;
        h /= 360f;

        double q = 0;

        if (l < 0.5) q = l * (1 + s);
        else q = (l + s) - (s * l);

        double p = 2 * l - q;

        double r = Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f)));
        double g = Math.max(0, HueToRGB(p, q, h));
        double b = Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f)));

        r = Math.min(r, 1.0f);
        g = Math.min(g, 1.0f);
        b = Math.min(b, 1.0f);

        return new double[]{r, g, b};
    }

    private static double HueToRGB(double p, double q, double h) {
        if (h < 0) h += 1;

        if (h > 1) h -= 1;

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1) {
            return q;
        }

        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }

        return p;
    }
}
