package fuzs.mindfuldarkness.client.handler;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.gui.components.IconButton;
import fuzs.mindfuldarkness.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.function.Consumer;

public class DaytimeMenuHandler {

    public static void onScreenInit$Post(Minecraft minecraft, Screen screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, Consumer<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget) {
        ClientConfig.DaytimeButtonScreens darkModeToggleScreens = MindfulDarkness.CONFIG.get(ClientConfig.class).darkModeToggleScreens;
        if (darkModeToggleScreens.filter.test(screen)) {
            IconButton iconButton = new IconButton(0, 0, 20, 20, 194, 57, 20, DaytimeSwitcherHandler.TEXTURE_LOCATION, 256, 256, button -> {
                DaytimeSwitcherHandler.activateDaytimeSwitch();
                applyTextureOffsets((IconButton) button);
            });
            // TODO remove in 1.19.4 where Mojang fixed this again, not having any container widgets in the screen widgets list anymore
            widgets = widgets.stream().mapMulti((AbstractWidget widget, Consumer<AbstractWidget> consumer) -> {
                if (widget instanceof ContainerEventHandler containerEventHandler) {
                    for (GuiEventListener eventListener : containerEventHandler.children()) {
                        if (eventListener instanceof AbstractWidget abstractWidget) {
                            consumer.accept(abstractWidget);
                        }
                    }
                } else {
                    consumer.accept(widget);
                }
            }).toList();
            if (ScreenElementPositioner.tryPositionElement(iconButton, widgets, darkModeToggleScreens.buttonKeys)) {
                addWidget.accept(iconButton);
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
