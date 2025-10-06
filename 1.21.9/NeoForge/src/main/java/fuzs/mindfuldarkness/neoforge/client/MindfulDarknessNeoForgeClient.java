package fuzs.mindfuldarkness.neoforge.client;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.MindfulDarknessClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = MindfulDarkness.MOD_ID, dist = Dist.CLIENT)
public class MindfulDarknessNeoForgeClient {

    public MindfulDarknessNeoForgeClient() {
        ClientModConstructor.construct(MindfulDarkness.MOD_ID, MindfulDarknessClient::new);
    }
}
