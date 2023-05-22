package fuzs.mindfuldarkness.client.handler;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.gui.components.IconButton;
import fuzs.mindfuldarkness.config.ClientConfig;
import fuzs.puzzleslib.client.gui.screens.CommonScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.function.UnaryOperator;

public class DaytimeMenuHandler {

    public static void onScreenInit$Post(Screen screen, Minecraft minecraft, int width, int height, UnaryOperator<AbstractWidget> addWidget) {
        ClientConfig.DaytimeButtonScreens darkModeToggleScreens = MindfulDarkness.CONFIG.get(ClientConfig.class).darkModeToggleScreens;
        if (darkModeToggleScreens.filter.test(screen)) {
            List<Widget> renderables = CommonScreens.INSTANCE.getRenderableButtons(screen);
            IconButton iconButton = new IconButton(0, 0, 20, 20, 194, 57, 20, DaytimeSwitcherHandler.TEXTURE_LOCATION, 256, 256, button -> {
                DaytimeSwitcherHandler.activateDaytimeSwitch();
                applyTextureOffsets((IconButton) button);
            });
            if (ScreenElementPositioner.tryPositionElement(iconButton, renderables, darkModeToggleScreens.buttonKeys)) {
                addWidget.apply(iconButton);
                applyTextureOffsets(iconButton);
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
}
