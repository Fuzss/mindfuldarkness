package fuzs.mindfuldarkness.client.handler;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.gui.components.IconButton;
import fuzs.mindfuldarkness.config.ClientConfig;
import fuzs.puzzleslib.client.gui.screens.CommonScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class DaytimeMenuHandler {

    public static void onScreenInit$Post(Screen screen, Minecraft minecraft, int width, int height, UnaryOperator<AbstractWidget> addWidget) {
        ClientConfig.DaytimeButtonScreens darkModeToggleScreens = MindfulDarkness.CONFIG.get(ClientConfig.class).darkModeToggleScreens;
        if (darkModeToggleScreens.filter.test(screen)) {
            List<Widget> renderables = CommonScreens.INSTANCE.getRenderableButtons(screen);
            for (String buttonKey : darkModeToggleScreens.buttonKeys) {
                // TODO add a check for already present buttons in the spot we want to add ours for 1.19.4 with the new rectangles from LayoutElement
                Optional<Button> optional = findButton(renderables, buttonKey);
                if (optional.isPresent()) {
                    int buttonX = optional.get().x + optional.get().getWidth() + 4;
                    int buttonY = optional.get().y;
                    IconButton iconButton = (IconButton) addWidget.apply(new IconButton(buttonX, buttonY, 20, 20, 194, 57, 20, DaytimeSwitcherHandler.TEXTURE_LOCATION, 256, 256, button -> {
                        DaytimeSwitcherHandler.activateDaytimeSwitch();
                        applyTextureOffsets((IconButton) button);
                    }));
                    applyTextureOffsets(iconButton);
                    break;
                }
            }
        }
    }

    private static void applyTextureOffsets(IconButton iconButton) {
        if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
            iconButton.setTexture(194, 57);
        } else {
            iconButton.setTexture(214, 57);
        }
    }

    private static Optional<Button> findButton(List<Widget> renderables, String translationKey) {
        for (Widget widget : renderables) {
            if (widget instanceof Button button && matchesTranslationKey(button, translationKey)) {
                return Optional.of(button);
            }
        }
        return Optional.empty();
    }

    private static boolean matchesTranslationKey(Button button, String translationKey) {
        return button.getMessage().getContents() instanceof TranslatableContents contents && contents.getKey().equals(translationKey);
    }
}
