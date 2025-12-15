package fuzs.mindfuldarkness.client.handler;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.util.HSPConversionUtil;
import fuzs.mindfuldarkness.client.util.RGBBrightnessUtil;
import fuzs.mindfuldarkness.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class FontColorHandler {
    private static boolean renderInDarkness;

    public static void onBeforeRender(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        String id = getScreenId(screen);
        if (id == null || !MindfulDarkness.CONFIG.get(ClientConfig.class).fontColorBlacklist.contains(id)) {
            renderInDarkness = true;
        }
    }

    @Nullable
    public static String getScreenId(Screen screen) {
        Objects.requireNonNull(screen, "screen is null");
        Component title = screen.getTitle();
        if (title.getContents() instanceof PlainTextContents.LiteralContents(String text)) {
            return text;
        } else if (title.getContents() instanceof TranslatableContents translatableContents) {
            return translatableContents.getKey();
        } else {
            return null;
        }
    }

    public static void onAfterRender(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        renderInDarkness = false;
    }

    public static int processTextColor(int textColor) {
        if (renderInDarkness && MindfulDarkness.CONFIG.getHolder(ClientConfig.class).isAvailable()
                && MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
            return increaseTextColorBrightness(textColor);
        } else {
            return textColor;
        }
    }

    private static int increaseTextColorBrightness(int color) {
        double[] hspColorArray = RGBBrightnessUtil.unpackRGBToHSP(color);
        double targetBrightness = MindfulDarkness.CONFIG.get(ClientConfig.class).fontBrightness.get();
        if (hspColorArray[2] < targetBrightness) {
            double[] rgbColorArray = HSPConversionUtil.HSPtoRGB(hspColorArray[0], hspColorArray[1], targetBrightness);
            int alpha = ARGB.alpha(color);
            color = RGBBrightnessUtil.packRGBColor(rgbColorArray);
            return ARGB.color(alpha, color);
        } else {
            return color;
        }
    }
}
