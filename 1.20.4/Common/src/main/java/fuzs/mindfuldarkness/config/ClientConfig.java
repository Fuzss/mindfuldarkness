package fuzs.mindfuldarkness.config;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.handler.ColorChangedAssetsManager;
import fuzs.mindfuldarkness.client.packs.resources.ColorChangingResourceHandler;
import fuzs.mindfuldarkness.client.util.DarkeningAlgorithm;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;

public class ClientConfig implements ConfigCore {
    @Config(description = "Do not add the dark mode toggle buttons to the top of every menu.")
    public boolean hideInGameSwitcher = false;
    @Config(description = "Print menu type to game chat whenever a new menu screen is opened. Intended for finding menu types to be added to \"menu_blacklist\".")
    public boolean debugContainerTypes = false;
    @Config(description = "Print screen identifier to game chat or log file whenever a new screen is opened. Intended for finding screens to be added to \"font_color_blacklist\".")
    public boolean debugAllScreens = false;
    @Config(description = "Screens to add a dark mode toggle button to, so that toggling is possible outside of inventory menus.")
    public DaytimeButtonScreens darkModeToggleScreens = DaytimeButtonScreens.BOTH;
    @Config(name = "paths",
            description = {"Specifies gui paths and resources to darken.",
                    "End a path using '*' as a wildcard char to include every file (no nested directories) from that directory.",
                    "Begin with a namespace or skip namespace to apply to all namespaces.",
                    "Begin with '!' to exclude matches, list those exclusions after entries that would otherwise include them."}
    )
    List<String> pathsRaw = Lists.newArrayList("textures/gui/*",
            "minecraft:textures/atlas/gui.png",
            "!minecraft:textures/gui/options_background.png",
            "!minecraft:textures/gui/book.png",
            "textures/gui/container/*",
            "minecraft:textures/gui/container/creative_inventory/*",
            "trinkets:textures/gui/slots/*",
            "craftingtweaks:gui.png",
            "dankstorage:textures/container/gui/*",
            "ae2:textures/guis/*",
            "!modmenu:textures/gui/mod_configuration.png",
            "!dynamiccrosshair:textures/gui/crosshairs.png",
            "quark:textures/misc/backpack_gui.png",
            "quark:textures/misc/matrix_enchanting.png",
            "quark:textures/misc/shulker_widget.png"
    );
    @Config(name = "menu_blacklist",
            description = "Exclude certain menus from showing the dark mode switcher. Useful when the box intersects other screen elements."
    )
    List<String> menuBlacklistRaw = Lists.newArrayList();
    @Config(name = "font_color_blacklist",
            description = "Exclude certain screens from being affected by any text color alterations."
    )
    List<String> fontColorBlacklistRaw = Lists.newArrayList("chat_screen.title",
            "sign.edit",
            "hanging_sign.edit",
            "book.edit",
            "book.view"
    );

    public ModConfigSpec.BooleanValue darkTheme;
    public ModConfigSpec.DoubleValue textureDarkness;
    public ModConfigSpec.DoubleValue fontBrightness;
    public ModConfigSpec.EnumValue<DarkeningAlgorithm> darkeningAlgorithm;
    public List<String> paths;
    public ConfigDataSet<MenuType<?>> menuBlacklist;
    public Set<String> fontColorBlacklist;

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        this.darkTheme = builder.comment("Use a dark theme for the configuration screens.").define("dark_theme", false);
        this.textureDarkness = builder.comment("Percentage of original interface brightness to apply.").defineInRange("texture_darkness", 0.5, 0.0, 1.0);
        this.fontBrightness = builder.comment("The minimum brightness value of font while dark mode is enabled.").defineInRange("font_brightness", 0.75, 0.0, 1.0);
        this.darkeningAlgorithm = builder.comment("Algorithm selector for darkening interface pixels.").defineEnum("darkening_algorithm", DarkeningAlgorithm.GRAYSCALE_AND_HSP);
    }

    @Override
    public void afterConfigReload() {
        this.paths = this.pathsRaw.stream().filter(s -> {
            if (!s.matches(ColorChangingResourceHandler.VALID_MINDFUL_DARKNESS_PATH)) {
                MindfulDarkness.LOGGER.warn("'{}' is an invalid path and will be ignored", s);
                return false;
            } else {
                return true;
            }
        }).toList();
        this.menuBlacklist = ConfigDataSet.from(Registries.MENU, this.menuBlacklistRaw);
        this.fontColorBlacklist = ImmutableSet.copyOf(this.fontColorBlacklistRaw);
        ColorChangedAssetsManager.INSTANCE.recordedReset();
        ColorChangingResourceHandler.INSTANCE.clear();
    }
}
