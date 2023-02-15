package fuzs.mindfuldarkness;

import fuzs.mindfuldarkness.data.ModLanguageProvider;
import fuzs.puzzleslib.core.CommonFactories;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(MindfulDarkness.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MindfulDarknessForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CommonFactories.INSTANCE.modConstructor(MindfulDarkness.MOD_ID).accept(new MindfulDarkness());
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
//        generator.addProvider(true, new ModRecipeProvider(generator));
        generator.addProvider(true, new ModLanguageProvider(generator, MindfulDarkness.MOD_ID));
//        generator.addProvider(true, new ModLootTableProvider(generator, MindfulDarkness.MOD_ID));
//        generator.addProvider(true, new ModItemModelProvider(generator, MindfulDarkness.MOD_ID, existingFileHelper));
    }
}
