package fuzs.mindfuldarkness.fabric;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class MindfulDarknessFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(MindfulDarkness.MOD_ID, MindfulDarkness::new);
    }
}
