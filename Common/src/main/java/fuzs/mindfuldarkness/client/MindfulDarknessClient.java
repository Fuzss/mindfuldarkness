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
        ContainerScreenEvents.BACKGROUND.register(DaytimeSwitcherHandler::onDrawBackground);
        ScreenEvents.AFTER_INIT.register(DaytimeSwitcherHandler::onAfterInit);
        ScreenEvents.AFTER_INIT.register(EventPhase.AFTER, DaytimeMenuHandler::onAfterInit);
        ScreenOpeningCallback.EVENT.register(DaytimeSwitcherHandler::onScreenOpening);
        ClientTickEvents.END.register(DaytimeSwitcherHandler::onEndTick);
    }
}
