package fuzs.mindfuldarkness.data;

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
        this.add("screen.daytime_switcher.algorithm.linear", "Linear");
        this.add("screen.daytime_switcher.algorithm.grayscale_and_linear", "Grayscale And Linear");
        this.add("screen.daytime_switcher.algorithm.hsp", "HSP");
        this.add("screen.daytime_switcher.algorithm.grayscale_and_hsp", "Grayscale And HSP");
        this.add("screen.daytime_switcher.algorithm.hsl", "HSL");
        this.add("screen.daytime_switcher.algorithm.grayscale_and_hsl", "Grayscale And HSL");
        this.add("screen.daytime_switcher.algorithm.hsv", "HSV");
        this.add("screen.daytime_switcher.algorithm.grayscale_and_hsv", "Grayscale And HSV");
        this.add("screen.daytime_switcher.algorithm", "Algorithm");
        this.add("screen.daytime_switcher.interface_darkness", "Interface Darkness");
        this.add("screen.daytime_switcher.front_brightness", "Font Brightness");
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
