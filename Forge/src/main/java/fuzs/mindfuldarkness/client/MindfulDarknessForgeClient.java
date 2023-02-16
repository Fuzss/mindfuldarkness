package fuzs.mindfuldarkness.client;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitchHandler;
import fuzs.puzzleslib.client.core.ClientFactories;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = MindfulDarkness.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MindfulDarknessForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final ContainerScreenEvent.Render.Background evt) -> {
            DaytimeSwitchHandler.onContainerScreen$Render$Background(evt.getContainerScreen(), evt.getPoseStack(), evt.getMouseX(), evt.getMouseY());
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.Init.Post evt) -> {
            DaytimeSwitchHandler.onScreenInit$Post(evt.getScreen(), Minecraft.getInstance(), evt.getScreen().width, evt.getScreen().height, abstractWidget -> {
                evt.addListener(abstractWidget);
                return abstractWidget;
            });
        });
        MinecraftForge.EVENT_BUS.addListener((final ScreenEvent.Opening evt) -> {
            DaytimeSwitchHandler.onScreenOpen(evt.getCurrentScreen(), evt.getNewScreen()).ifPresent(newScreen -> {
                if (newScreen == evt.getCurrentScreen()) {
                    evt.setCanceled(true);
                } else {
                    evt.setNewScreen(newScreen);
                }
            });
        });
        MinecraftForge.EVENT_BUS.addListener((final TickEvent.ClientTickEvent evt) -> {
            if (evt.phase == TickEvent.Phase.END) DaytimeSwitchHandler.onClientTick$End(Minecraft.getInstance());
        });
    }
}
