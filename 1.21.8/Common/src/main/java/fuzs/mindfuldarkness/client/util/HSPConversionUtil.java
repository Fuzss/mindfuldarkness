package fuzs.mindfuldarkness.client.util;

/**
 * Adapted from <a href="http://alienryderflex.com/hsp.html">HSP Color Model — Alternative to HSV (HSB) and HSL</a>.
 * <p>Thanks to <a href="https://stackoverflow.com/a/46552667">CosmicGiant</a> for referencing the above writeup.
 */
public class HSPConversionUtil {
    //Perceived brightness to Red ratio.
    private static final double Pr = .299;
    //Perceived brightness to Green ratio.
    private static final double Pg = .587;
    //Perceived brightness to Blue ratio.
    private static final double Pb = .114;

    /**
     *   public domain function by Darel Rex Finley, 2006
     *
     *   <p>This function expects the passed-in values to be on a scale
     *   of 0 to 1, and uses that same scale for the return values.
     *
     *   <p>See description/examples at alienryderflex.com/hsp.html
     */
    public static double[] RGBtoHSP(double R, double G, double B) {

        double H, S, P;

        //  Calculate the Perceived brightness.
        P = Math.sqrt(R * R * Pr + G * G * Pg + B * B * Pb);

        //  Calculate the Hue and Saturation.  (This part works
        //  the same way as in the HSV/B and HSL systems???.)
        if (R == G && R == B) {
            H = 0.;
            S = 0.;
            return new double[]{H, S, P};
        }
        if (R >= G && R >= B) {   //  R is largest
            if (B >= G) {
                H = 6. / 6. - 1. / 6. * (B - G) / (R - G);
                S = 1. - G / R;
            } else {
                H = 0. / 6. + 1. / 6. * (G - B) / (R - B);
                S = 1. - B / R;
            }
        } else if (G >= R && G >= B) {   //  G is largest
            if (R >= B) {
                H = 2. / 6. - 1. / 6. * (R - B) / (G - B);
                S = 1. - B / G;
            } else {
                H = 2. / 6. + 1. / 6. * (B - R) / (G - R);
                S = 1. - R / G;
            }
        } else {   //  B is largest
            if (G >= R) {
                H = 4. / 6. - 1. / 6. * (G - R) / (B - R);
                S = 1. - R / B;
            } else {
                H = 4. / 6. + 1. / 6. * (R - G) / (B - G);
                S = 1. - G / B;
            }
        }
        return new double[]{H, S, P};
    }

    /**
     *   public domain function by Darel Rex Finley, 2006
     *
     *   <p>This function expects the passed-in values to be on a scale
     *   of 0 to 1, and uses that same scale for the return values.
     *
     *   <p>Note that some combinations of HSP, even if in the scale
     *   0-1, may return RGB values that exceed a value of 1.  For
     *   example, if you pass in the HSP color 0,1,1, the result
     *   will be the RGB color 2.037,0,0.
     *
     *   <p>See description/examples at alienryderflex.com/hsp.html
     */
    public static double[] HSPtoRGB(double H, double S, double P) {

        double R, G, B;

        double part, minOverMax = 1. - S;

        if (minOverMax > 0.) {
            if (H < 1. / 6.) {   //  R>G>B
                H = 6. * (H - 0. / 6.);
                part = 1. + H * (1. / minOverMax - 1.);
                B = P / Math.sqrt(Pr / minOverMax / minOverMax + Pg * part * part + Pb);
                R = (B) / minOverMax;
                G = (B) + H * ((R) - (B));
            } else if (H < 2. / 6.) {   //  G>R>B
                H = 6. * (-H + 2. / 6.);
                part = 1. + H * (1. / minOverMax - 1.);
                B = P / Math.sqrt(Pg / minOverMax / minOverMax + Pr * part * part + Pb);
                G = (B) / minOverMax;
                R = (B) + H * ((G) - (B));
            } else if (H < 3. / 6.) {   //  G>B>R
                H = 6. * (H - 2. / 6.);
                part = 1. + H * (1. / minOverMax - 1.);
                R = P / Math.sqrt(Pg / minOverMax / minOverMax + Pb * part * part + Pr);
                G = (R) / minOverMax;
                B = (R) + H * ((G) - (R));
            } else if (H < 4. / 6.) {   //  B>G>R
                H = 6. * (-H + 4. / 6.);
                part = 1. + H * (1. / minOverMax - 1.);
                R = P / Math.sqrt(Pb / minOverMax / minOverMax + Pg * part * part + Pr);
                B = (R) / minOverMax;
                G = (R) + H * ((B) - (R));
            } else if (H < 5. / 6.) {   //  B>R>G
                H = 6. * (H - 4. / 6.);
                part = 1. + H * (1. / minOverMax - 1.);
                G = P / Math.sqrt(Pb / minOverMax / minOverMax + Pr * part * part + Pg);
                B = (G) / minOverMax;
                R = (G) + H * ((B) - (G));
            } else {   //  R>B>G
                H = 6. * (-H + 6. / 6.);
                part = 1. + H * (1. / minOverMax - 1.);
                G = P / Math.sqrt(Pr / minOverMax / minOverMax + Pb * part * part + Pg);
                R = (G) / minOverMax;
                B = (G) + H * ((R) - (G));
            }
        } else {
            if (H < 1. / 6.) {   //  R>G>B
                H = 6. * (H - 0. / 6.);
                R = Math.sqrt(P * P / (Pr + Pg * H * H));
                G = (R) * H;
                B = 0.;
            } else if (H < 2. / 6.) {   //  G>R>B
                H = 6. * (-H + 2. / 6.);
                G = Math.sqrt(P * P / (Pg + Pr * H * H));
                R = (G) * H;
                B = 0.;
            } else if (H < 3. / 6.) {   //  G>B>R
                H = 6. * (H - 2. / 6.);
                G = Math.sqrt(P * P / (Pg + Pb * H * H));
                B = (G) * H;
                R = 0.;
            } else if (H < 4. / 6.) {   //  B>G>R
                H = 6. * (-H + 4. / 6.);
                B = Math.sqrt(P * P / (Pb + Pg * H * H));
                G = (B) * H;
                R = 0.;
            } else if (H < 5. / 6.) {   //  B>R>G
                H = 6. * (H - 4. / 6.);
                B = Math.sqrt(P * P / (Pb + Pr * H * H));
                R = (B) * H;
                G = 0.;
            } else {   //  R>B>G
                H = 6. * (-H + 6. / 6.);
                R = Math.sqrt(P * P / (Pr + Pb * H * H));
                B = (R) * H;
                G = 0.;
            }
        }
        return new double[]{R, G, B};
    }
}
