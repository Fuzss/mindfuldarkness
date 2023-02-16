package fuzs.mindfuldarkness.client;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.api.client.event.ContainerScreenEvents;
import fuzs.mindfuldarkness.api.client.event.ExtraScreenEvents;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitchHandler;
import fuzs.puzzleslib.client.core.ClientFactories;
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
        ContainerScreenEvents.BACKGROUND.register(DaytimeSwitchHandler::onContainerScreen$Render$Background);
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            DaytimeSwitchHandler.onScreenInit$Post(screen, client, scaledWidth, scaledHeight, abstractWidget -> {
                Screens.getButtons(screen).add(abstractWidget);
                return abstractWidget;
            });
        });
        ExtraScreenEvents.OPENING.register(DaytimeSwitchHandler::onScreenOpen);
        ClientTickEvents.END_CLIENT_TICK.register(DaytimeSwitchHandler::onClientTick$End);
    }
}
