package fuzs.mindfuldarkness.client;

import fuzs.mindfuldarkness.client.handler.DaytimeMenuHandler;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.ContainerScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenOpeningCallback;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;

public class MindfulDarknessClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ContainerScreenEvents.BACKGROUND.register(DaytimeSwitcherHandler::onContainerScreen$Render$Background);
        ScreenEvents.AFTER_INIT.register(DaytimeSwitcherHandler::onScreenInit$Post);
        ScreenEvents.AFTER_INIT.register(EventPhase.AFTER, DaytimeMenuHandler::onScreenInit$Post);
        ScreenOpeningCallback.EVENT.register(DaytimeSwitcherHandler::onScreenOpen);
        ClientTickEvents.END.register(DaytimeSwitcherHandler::onClientTick$End);
    }
}
