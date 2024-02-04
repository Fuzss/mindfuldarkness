package fuzs.mindfuldarkness.mixin.client;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.util.ScreenIdentifierHelper;
import fuzs.mindfuldarkness.config.ClientConfig;
import fuzs.mindfuldarkness.client.util.HSPConversionUtil;
import fuzs.mindfuldarkness.client.util.RGBBrightnessUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(Font.class)
abstract class FontMixin {

    @ModifyVariable(method = "adjustColor", at = @At("HEAD"))
    private static int adjustColor(int color) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen != null) {
            if (MindfulDarkness.CONFIG.getHolder(ClientConfig.class).isAvailable() && MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
                String identifier = ScreenIdentifierHelper.getScreenIdentifier(screen);
                if (identifier == null || !MindfulDarkness.CONFIG.get(ClientConfig.class).fontColorBlacklist.contains(identifier)) {
                    double[] hspColorArray = RGBBrightnessUtil.unpackRGBToHSP(color);
                    double targetBrightness = MindfulDarkness.CONFIG.get(ClientConfig.class).fontBrightness.get();
                    if (hspColorArray[2] < targetBrightness) {
                        double[] rgbColorArray = HSPConversionUtil.HSPtoRGB(hspColorArray[0], hspColorArray[1], targetBrightness);
                        return RGBBrightnessUtil.packRGBColor(rgbColorArray);
                    }
                }
            }
        }
        return color;
    }
}
