package fuzs.mindfuldarkness.client.handler;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.util.HSPConversionUtil;
import fuzs.mindfuldarkness.client.util.RGBBrightnessUtil;
import fuzs.mindfuldarkness.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.OptionalInt;

public class FontColorHandler {
    private static boolean renderInDarkness;

    public static void onBeforeRender(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        String identifier = getScreenIdentifier(screen);
        if (identifier == null
                || !MindfulDarkness.CONFIG.get(ClientConfig.class).fontColorBlacklist.contains(identifier)) {
            renderInDarkness = true;
        }
    }

    @Nullable
    public static String getScreenIdentifier(Screen screen) {
        Objects.requireNonNull(screen, "screen is null");
        Component title = screen.getTitle();
        if (title.getContents() instanceof PlainTextContents.LiteralContents(String text)) {
            return text;
        } else if (title.getContents() instanceof TranslatableContents translatableContents) {
            return translatableContents.getKey();
        } else if (screen instanceof BookEditScreen) {
            return "book.edit.title";
        } else if (screen instanceof BookViewScreen) {
            return "book.view.title";
        } else {
            return null;
        }
    }

    public static void onAfterRender(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        renderInDarkness = false;
    }

    public static int adjustFontColor(int fontColor) {
        if (renderInDarkness) {
            if (MindfulDarkness.CONFIG.getHolder(ClientConfig.class).isAvailable() && MindfulDarkness.CONFIG.get(
                    ClientConfig.class).darkTheme.get()) {
                return tryAdjustColor(fontColor).orElse(fontColor);
            }
        }

        return fontColor;
    }

    private static OptionalInt tryAdjustColor(int color) {
        double[] hspColorArray = RGBBrightnessUtil.unpackRGBToHSP(color);
        double targetBrightness = MindfulDarkness.CONFIG.get(ClientConfig.class).fontBrightness.get();
        if (hspColorArray[2] < targetBrightness) {
            double[] rgbColorArray = HSPConversionUtil.HSPtoRGB(hspColorArray[0], hspColorArray[1], targetBrightness);
            int alpha = FastColor.ARGB32.alpha(color);
            color = RGBBrightnessUtil.packRGBColor(rgbColorArray);
            return OptionalInt.of(FastColor.ARGB32.color(alpha, color));
        } else {
            return OptionalInt.empty();
        }
    }
}
