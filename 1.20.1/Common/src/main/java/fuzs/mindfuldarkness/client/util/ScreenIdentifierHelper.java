package fuzs.mindfuldarkness.client.util;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ScreenIdentifierHelper {

    @Nullable
    public static String getScreenIdentifier(Screen screen) {
        Objects.requireNonNull(screen, "screen is null");
        Component title = screen.getTitle();
        if (title.getContents() instanceof LiteralContents literalContents) {
            return literalContents.text();
        } else if (title.getContents() instanceof TranslatableContents translatableContents) {
            return translatableContents.getKey();
        } else {
            return null;
        }
    }
}
