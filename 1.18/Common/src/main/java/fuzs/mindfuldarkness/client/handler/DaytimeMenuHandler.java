package fuzs.mindfuldarkness.client.handler;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.gui.components.IconButton;
import fuzs.mindfuldarkness.config.ClientConfig;
import fuzs.puzzleslib.api.client.screen.v2.ScreenElementPositioner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.function.Consumer;

public class DaytimeMenuHandler {

    public static void onAfterInit(Minecraft minecraft, Screen screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, Consumer<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget) {
        ClientConfig.DaytimeButtonScreens darkModeToggleScreens = MindfulDarkness.CONFIG.get(ClientConfig.class).darkModeToggleScreens;
        if (darkModeToggleScreens.filter.test(screen)) {
            IconButton iconButton = new IconButton(0, 0, 20, 20, 194, 57, 20, DaytimeSwitcherHandler.TEXTURE_LOCATION, 256, 256, button -> {
                DaytimeSwitcherHandler.activateDaytimeSwitch();
                applyTextureOffsets((IconButton) button);
            });
            if (ScreenElementPositioner.tryPositionElement(iconButton, widgets, darkModeToggleScreens.buttonKeys)) {
                addWidget.accept(iconButton);
                applyTextureOffsets(iconButton);
            }
        }
    }

    private static void applyTextureOffsets(IconButton iconButton) {
        if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
            iconButton.xTexStart = 194;
        } else {
            iconButton.xTexStart = 214;
        }
    }
}
