package fuzs.mindfuldarkness;

import fuzs.puzzleslib.core.CommonFactories;
import net.fabricmc.api.ModInitializer;

public class MindfulDarknessFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(MindfulDarkness.MOD_ID).accept(new MindfulDarkness());
    }
}
