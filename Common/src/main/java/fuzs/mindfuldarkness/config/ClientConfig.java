package fuzs.mindfuldarkness.config;

import com.google.common.collect.Lists;
import fuzs.mindfuldarkness.client.util.PixelDarkener;
import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.ValueCallback;
import fuzs.puzzleslib.config.annotation.Config;
import fuzs.puzzleslib.config.core.AbstractConfigBuilder;
import fuzs.puzzleslib.config.core.AbstractConfigValue;
import fuzs.puzzleslib.config.serialization.ConfigDataSet;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.List;
import java.util.function.Predicate;

public class ClientConfig implements ConfigCore {
    public AbstractConfigValue<Boolean> darkTheme;
    public AbstractConfigValue<Double> textureDarkness;
    public AbstractConfigValue<Double> fontBrightness;
    public AbstractConfigValue<PixelDarkener> darkeningAlgorithm;
    @Config(description = "Specifies gui paths and resources to darken. Use '*' as wildcard char. Directory boundaries will not be crossed. Begin with a namespace or skip namespace to apply to all namespaces. Begin with '!' to exclude matches.")
    public List<String> paths = Lists.newArrayList("textures/gui/*.png", "!minecraft:textures/gui/icons.png", "!minecraft:textures/gui/options_background.png", "textures/gui/container/*.png", "minecraft:textures/gui/container/creative_inventory/*.png", "trinkets:textures/gui/slots/*.png");
    @Config(description = "Do not add the dark mode toggle buttons to the top of every menu.")
    public boolean hideInGameSwitcher = false;
    @Config(name = "menu_blacklist", description = "Exclude certain menus from showing the dark mode switcher. Useful when the box intersects other screen elements.")
    List<String> menuBlacklistRaw = Lists.newArrayList();
    @Config(description = "Print menu type to game chat whenever a new menu screen is opened. Intended for finding menu types to be added to \"menu_blacklist\".")
    public boolean debugContainerTypes = false;
    @Config(description = "Screens to add a dark mode toggle button to, so that toggling is possible outside of inventory menus.")
    public DaytimeButtonScreens darkModeToggleScreens = DaytimeButtonScreens.BOTH;

    public ConfigDataSet<MenuType<?>> menuBlacklist;

    @Override
    public void addToBuilder(AbstractConfigBuilder builder, ValueCallback callback) {
        this.darkTheme = builder.comment("Use a dark theme for the configuration screens.").define("dark_theme", false);
        this.textureDarkness = builder.comment("Percentage of original interface brightness to apply.").defineInRange("texture_darkness", 0.5, 0.0, 1.0);
        this.fontBrightness = builder.comment("The minimum brightness value of font while dark mode is enabled.").defineInRange("font_brightness", 0.75, 0.0, 1.0);
        this.darkeningAlgorithm = builder.comment("Algorithm selector for darkening interface pixels.").defineEnum("darkening_algorithm", PixelDarkener.GRAYSCALE_AND_HSP);
    }

    @Override
    public void afterConfigReload() {
        this.menuBlacklist = ConfigDataSet.of(Registry.MENU_REGISTRY, this.menuBlacklistRaw);
    }

    public enum DaytimeButtonScreens {
        NONE(screen -> false),
        TITLE_SCREEN(screen -> screen instanceof TitleScreen, "menu.online", "menu.multiplayer", "menu.singleplayer"),
        PAUSE_SCREEN(screen -> screen instanceof PauseScreen, "menu.reportBugs", "menu.shareToLan", "menu.playerReporting"),
        BOTH(screen -> screen instanceof TitleScreen || screen instanceof PauseScreen, "menu.online", "menu.multiplayer", "menu.singleplayer", "menu.reportBugs", "menu.shareToLan", "menu.playerReporting");

        public final Predicate<Screen> filter;
        public final String[] buttonKeys;

        DaytimeButtonScreens(Predicate<Screen> filter, String... buttonKeys) {
            this.filter = filter;
            this.buttonKeys = buttonKeys;
        }
    }
}
