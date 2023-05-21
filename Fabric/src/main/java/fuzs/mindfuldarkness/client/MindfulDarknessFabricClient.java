package fuzs.mindfuldarkness.client;

import fuzs.mindfuldarkness.api.client.event.ContainerScreenEvents;
import fuzs.mindfuldarkness.api.client.event.ExtraScreenEvents;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import fuzs.mindfuldarkness.client.handler.DaytimeMenuHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;

public class MindfulDarknessFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ContainerScreenEvents.BACKGROUND.register(DaytimeSwitcherHandler::onContainerScreen$Render$Background);
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            DaytimeSwitcherHandler.onScreenInit$Post(screen, client, scaledWidth, scaledHeight, abstractWidget -> {
                Screens.getButtons(screen).add(abstractWidget);
                return abstractWidget;
            });
            DaytimeMenuHandler.onScreenInit$Post(screen, client, scaledWidth, scaledHeight, abstractWidget -> {
                Screens.getButtons(screen).add(abstractWidget);
                return abstractWidget;
            });
        });
        ExtraScreenEvents.OPENING.register(DaytimeSwitcherHandler::onScreenOpen);
        ClientTickEvents.END_CLIENT_TICK.register(DaytimeSwitcherHandler::onClientTick$End);
    }
}
