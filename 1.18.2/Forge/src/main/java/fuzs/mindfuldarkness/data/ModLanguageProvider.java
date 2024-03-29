package fuzs.mindfuldarkness.data;

import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTranslations() {
        this.add("screen.debug.identifier", "Screen Identifier: %s");
        this.add("screen.debug.menuType", "Menu Type: %s");
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
}
