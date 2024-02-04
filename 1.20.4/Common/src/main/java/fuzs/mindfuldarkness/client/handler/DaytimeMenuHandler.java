package fuzs.mindfuldarkness.client.handler;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.gui.components.SpritelessImageButton;
import fuzs.mindfuldarkness.config.ClientConfig;
import fuzs.mindfuldarkness.config.DaytimeButtonScreens;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenEvents;
import fuzs.puzzleslib.api.client.gui.v2.components.ScreenElementPositioner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class DaytimeMenuHandler {

    public static void onAfterInit(Minecraft minecraft, Screen screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, ScreenEvents.ConsumingOperator<AbstractWidget> addWidget, ScreenEvents.ConsumingOperator<AbstractWidget> removeWidget) {
        DaytimeButtonScreens darkModeToggleScreens = MindfulDarkness.CONFIG.get(ClientConfig.class).darkModeToggleScreens;
        if (darkModeToggleScreens.filter.test(screen)) {
            SpritelessImageButton iconButton = new SpritelessImageButton(0,
                    0,
                    20,
                    20,
                    194,
                    57,
                    DaytimeSwitcherHandler.TEXTURE_LOCATION,
                    button -> {
                        DaytimeSwitcherHandler.activateDaytimeSwitch();
                        applyTextureOffsets((SpritelessImageButton) button);
                    }
            ).setDrawBackground();
            if (ScreenElementPositioner.tryPositionElement(iconButton, widgets, darkModeToggleScreens.buttonKeys)) {
                addWidget.apply(iconButton);
                applyTextureOffsets(iconButton);
            }
        }
    }

    private static void applyTextureOffsets(SpritelessImageButton iconButton) {
        if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
            iconButton.setTextureCoordinates(194, 57);
        } else {
            iconButton.setTextureCoordinates(214, 57);
        }
    }
}
