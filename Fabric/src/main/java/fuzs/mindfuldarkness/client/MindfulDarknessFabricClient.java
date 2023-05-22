package fuzs.mindfuldarkness.client;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.ResourceLocation;

public class MindfulDarknessFabricClient implements ClientModInitializer {
    public static final ResourceLocation AFTER_PHASE = MindfulDarkness.id("after");

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(MindfulDarkness.MOD_ID, MindfulDarknessClient::new);
    }
}
