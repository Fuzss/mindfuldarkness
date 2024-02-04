package fuzs.mindfuldarkness.client.util;

import java.util.function.DoubleUnaryOperator;

/**
 * Copied from the following sources:
 *
 * <p><a href="https://stackoverflow.com/questions/17242144/javascript-convert-hsb-hsv-color-to-rgb-accurately/54024653#54024653">hsv2rgb</a>
 * <p><a href="https://stackoverflow.com/questions/8022885/rgb-to-hsv-color-in-javascript/54070620#54070620">rgb2hsv</a>
 * <p><a href="https://stackoverflow.com/questions/2348597/why-doesnt-this-javascript-rgb-to-hsl-code-work/54071699#54071699">rgb2hsl</a>
 * <p><a href="https://stackoverflow.com/questions/2353211/hsl-to-rgb-color-conversion/64090995#64090995">hsl2rgb</a>
 */
public class CompactConversionsUtil {

    // input: h as an angle in [0,360] and s,l in [0,1] - output: r,g,b in [0,1]
    public static double[] hsl2rgb(double h, double s, double l) {
        var a = s * Math.min(l, 1 - l);
        DoubleUnaryOperator f = (n) -> {
            var k = (n + h / 30) % 12;
            return l - a * Math.max(Math.min(Math.min(k - 3, 9 - k), 1), -1);
        };
        return new double[]{f.applyAsDouble(0), f.applyAsDouble(8), f.applyAsDouble(4)};
    }

    // in: r,g,b in [0,1], out: h in [0,360) and s,l in [0,1]
    public static double[] rgb2hsl(double r, double g, double b) {
        double v = Math.max(Math.max(r, g), b), c = v - Math.min(Math.min(r, g), b), f = (1 - Math.abs(v + v - c - 1));
        var h = c != 0.0 ? ((v == r) ? (g - b) / c : ((v == g) ? 2 + (b - r) / c : 4 + (r - g) / c)) : 0.0;
        return new double[]{60 * (h < 0 ? h + 6 : h), f != 0.0 ? c / f : 0, (v + v - c) / 2};
    }

    // input: r,g,b in [0,1], out: h in [0,360) and s,v in [0,1]
    public static double[] rgb2hsv(double r, double g, double b) {
        double v = Math.max(Math.max(r, g), b), c = v - Math.min(Math.min(r, g), b);
        var h = c != 0.0 ? ((v == r) ? (g - b) / c : ((v == g) ? 2 + (b - r) / c : 4 + (r - g) / c)) : 0.0;
        return new double[]{60 * (h < 0 ? h + 6 : h), v != 0.0 ? c / v : 0.0, v};
    }

    // input: h in [0,360] and s,v in [0,1] - output: r,g,b in [0,1]
    public static double[] hsv2rgb(double h, double s, double v) {
        DoubleUnaryOperator f = (n) -> {
            var k = (n + h / 60) % 6;
            return v - v * s * Math.max(Math.min(Math.min(k, 4 - k), 1), 0);
        };
        return new double[]{f.applyAsDouble(5), f.applyAsDouble(3), f.applyAsDouble(1)};
    }
}
