package fuzs.mindfuldarkness.config;

import com.google.common.collect.Lists;
import fuzs.mindfuldarkness.client.util.PixelDarkener;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public class ClientConfig implements ConfigCore {
    public ForgeConfigSpec.BooleanValue darkTheme;
    public ForgeConfigSpec.DoubleValue textureDarkness;
    public ForgeConfigSpec.DoubleValue fontBrightness;
    public ForgeConfigSpec.EnumValue<PixelDarkener> darkeningAlgorithm;
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
    public void addToBuilder(ForgeConfigSpec.Builder builder, ValueCallback callback) {
        this.darkTheme = builder.comment("Use a dark theme for the configuration screens.").define("dark_theme", false);
        this.textureDarkness = builder.comment("Percentage of original interface brightness to apply.").defineInRange("texture_darkness", 0.5, 0.0, 1.0);
        this.fontBrightness = builder.comment("The minimum brightness value of font while dark mode is enabled.").defineInRange("font_brightness", 0.75, 0.0, 1.0);
        this.darkeningAlgorithm = builder.comment("Algorithm selector for darkening interface pixels.").defineEnum("darkening_algorithm", PixelDarkener.GRAYSCALE_AND_HSP);
    }

    @Override
    public void afterConfigReload() {
        this.menuBlacklist = ConfigDataSet.from(Registries.MENU, this.menuBlacklistRaw);
    }

    public enum DaytimeButtonScreens {
        NONE(screen -> false),
        TITLE_SCREEN(screen -> screen instanceof TitleScreen, "modmenu.title", "fml.menu.mods", "menu.online", "menu.multiplayer", "menu.singleplayer"),
        PAUSE_SCREEN(screen -> screen instanceof PauseScreen, "modmenu.title", "fml.menu.mods", "menu.reportBugs", "menu.shareToLan", "menu.playerReporting", "menu.options", "gui.stats"),
        BOTH(screen -> screen instanceof TitleScreen || screen instanceof PauseScreen, "modmenu.title", "fml.menu.mods", "menu.online", "menu.multiplayer", "menu.singleplayer", "menu.reportBugs", "menu.shareToLan", "menu.playerReporting", "menu.options", "gui.stats");

        public final Predicate<Screen> filter;
        public final String[] buttonKeys;

        DaytimeButtonScreens(Predicate<Screen> filter, String... translationKeys) {
            this.filter = filter;
            this.buttonKeys = translationKeys;
        }
    }
}
