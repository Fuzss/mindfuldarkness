package fuzs.mindfuldarkness.config;

import com.google.common.base.Predicates;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

import java.util.function.Predicate;

public enum DaytimeButtonScreens {
    NONE(Predicates.alwaysFalse()),
    TITLE_SCREEN((Screen screen) -> screen instanceof TitleScreen,
            "modmenu.title",
            "fml.menu.mods",
            "menu.online",
            "menu.multiplayer",
            "menu.singleplayer"),
    PAUSE_SCREEN((Screen screen) -> screen instanceof PauseScreen,
            "modmenu.title",
            "fml.menu.mods",
            "menu.reportBugs",
            "menu.shareToLan",
            "menu.playerReporting",
            "menu.options",
            "gui.stats"),
    BOTH((Screen screen) -> screen instanceof TitleScreen || screen instanceof PauseScreen,
            "modmenu.title",
            "fml.menu.mods",
            "menu.online",
            "menu.multiplayer",
            "menu.singleplayer",
            "menu.reportBugs",
            "menu.shareToLan",
            "menu.playerReporting",
            "menu.options",
            "gui.stats");

    public final Predicate<Screen> filter;
    public final String[] buttonKeys;

    DaytimeButtonScreens(Predicate<Screen> filter, String... translationKeys) {
        this.filter = filter;
        this.buttonKeys = translationKeys;
    }
}
