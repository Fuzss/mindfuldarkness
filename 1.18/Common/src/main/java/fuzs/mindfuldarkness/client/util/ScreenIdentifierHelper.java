package fuzs.mindfuldarkness.client.util;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ScreenIdentifierHelper {

    @Nullable
    public static String getScreenIdentifier(Screen screen) {
        Objects.requireNonNull(screen, "screen is null");
        Component title = screen.getTitle();
        if (title instanceof TextComponent literalContents) {
            return literalContents.getText();
        } else if (title instanceof TranslatableComponent translatableContents) {
            return translatableContents.getKey();
        } else {
            return null;
        }
    }
}
