package fuzs.mindfuldarkness.client;

import fuzs.mindfuldarkness.client.handler.DaytimeMenuHandler;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import fuzs.mindfuldarkness.client.handler.FontColorHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.*;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

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
        ScreenMouseEvents.afterMouseClick(AbstractContainerScreen.class).register(DaytimeSwitcherHandler::onAfterMouseClick);
        ScreenEvents.beforeRender(Screen.class).register(FontColorHandler::onBeforeRender);
        ScreenEvents.afterRender(Screen.class).register(FontColorHandler::onAfterRender);
    }
}
