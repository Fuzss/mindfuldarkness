package fuzs.mindfuldarkness.neoforge;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.data.client.ModLanguageProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(MindfulDarkness.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MindfulDarknessNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(MindfulDarkness.MOD_ID, MindfulDarkness::new);
        DataProviderHelper.registerDataProviders(MindfulDarkness.MOD_ID, ModLanguageProvider::new);
    }
}
