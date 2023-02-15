package fuzs.mindfuldarkness.data;

import fuzs.essentialpotions.EssentialPotions;
import fuzs.essentialpotions.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator dataGenerator, String modId) {
        super(dataGenerator, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("debug.menu.opening", "Menu Type: %s");
    }

    public void add(CreativeModeTab tab, String name) {
        this.add(((TranslatableContents) tab.getDisplayName().getContents()).getKey(), name);
    }

    public void add(Potion potion, String name) {
        String potionName = potion.getName("");
        this.add("item.minecraft.tipped_arrow.effect." + potionName, "Arrow of " + name);
        this.add("item.minecraft.potion.effect." + potionName, "Potion of " + name);
        this.add("item.minecraft.splash_potion.effect." + potionName, "Splash Potion of " + name);
        this.add("item.minecraft.lingering_potion.effect." + potionName, "Lingering Potion of " + name);
    }

    public void add(SoundEvent soundEvent, String name) {
        this.add("subtitles." + soundEvent.getLocation().getPath(), name);
    }
}
