package fuzs.mindfuldarkness.config;

import com.google.common.collect.ImmutableSet;
import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.packs.resources.ColorChangingResourceHandler;
import fuzs.mindfuldarkness.client.util.DarkeningAlgorithm;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class ClientConfig implements ConfigCore {
    @Config(description = "Do not add the dark mode toggle buttons to the top of every menu.")
    public boolean hideInGameSwitcher = false;
    @Config(description = "Print menu type to game chat whenever a new menu screen is opened. Intended for finding menu types to be added to \"menu_blacklist\".")
    public boolean debugContainerTypes = false;
    @Config(description = "Print screen identifier to game chat or log file whenever a new screen is opened. Intended for finding screens to be added to \"font_color_blacklist\".")
    public boolean debugAllScreens = false;
    @Config(description = "Screens to add a dark mode toggle button to, so that toggling is possible outside of inventory menus.")
    public DaytimeButtonScreens darkModeToggleScreens = DaytimeButtonScreens.BOTH;
    @Config(
            name = "paths", description = {
            "Specifies gui paths and resources to darken.",
            "End a directory path using '/' to include every file without nested directories.",
            "End a directory path using '/*' to include every file including nested directories.",
            "Begin with a namespace or skip namespace to apply to all namespaces.",
            "Begin with '!' to exclude matches, list those exclusions after entries that would otherwise include them."
    }
    )
    List<String> pathsRaw = new ArrayList<>(Arrays.asList("minecraft:textures/gui/advancements/window.png",
            "textures/gui/",
            "!minecraft:textures/gui/book.png",
            "textures/gui/container/*",
            "textures/gui/sprites/advancements/*",
            "textures/gui/sprites/boss_bar/*",
            "textures/gui/sprites/container/*",
            "textures/gui/sprites/dialog/*",
            "textures/gui/sprites/gamemode_switcher/*",
            "textures/gui/sprites/hud/*",
            "textures/gui/sprites/popup/*",
            "textures/gui/sprites/recipe_book/*",
            "textures/gui/sprites/social_interactions/*",
            "textures/gui/sprites/toast/*",
            "textures/gui/sprites/tooltip/*",
            "textures/gui/sprites/transferable_list/*",
            "textures/gui/sprites/widget/*",
            "textures/gui/sprites/world_list/*",
            "trinkets:textures/gui/slots/*",
            "craftingtweaks:gui.png",
            "dankstorage:textures/container/gui/*",
            "ae2:textures/guis/*",
            "!modmenu:textures/gui/mod_configuration.png",
            "!dynamiccrosshair:textures/gui/crosshairs.png",
            "quark:textures/misc/backpack_gui.png",
            "quark:textures/misc/matrix_enchanting.png",
            "quark:textures/misc/shulker_widget.png"));
    @Config(
            name = "menu_blacklist",
            description = "Exclude certain menus from showing the dark mode switcher. Useful when the box intersects other screen elements."
    )
    List<String> menuBlacklistRaw = new ArrayList<>();
    @Config(
            name = "font_color_blacklist",
            description = "Exclude certain screens from being affected by any text color alterations."
    )
    List<String> fontColorBlacklistRaw = new ArrayList<>(Arrays.asList("chat_screen.title",
            "sign.edit",
            "hanging_sign.edit",
            "book.edit.title",
            "book.view.title"));

    public ModConfigSpec.BooleanValue darkTheme;
    public ModConfigSpec.DoubleValue textureDarkness;
    public ModConfigSpec.DoubleValue fontBrightness;
    public ModConfigSpec.EnumValue<DarkeningAlgorithm> darkeningAlgorithm;
    public List<String> paths;
    public ConfigDataSet<MenuType<?>> menuBlacklist;
    public Set<String> fontColorBlacklist;

    @Nullable
    private Boolean lastDarkTheme;
    @Nullable
    private Double lastTextureDarkness;
    @Nullable
    private DarkeningAlgorithm lastDarkeningAlgorithm;

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        this.darkTheme = builder.comment("Use a dark theme for the configuration screens.").define("dark_theme", false);
        this.textureDarkness = builder.comment("Percentage of original interface brightness to apply.")
                .defineInRange("texture_darkness", 0.5, 0.0, 1.0);
        this.fontBrightness = builder.comment("The minimum brightness value of font while dark mode is enabled.")
                .defineInRange("font_brightness", 0.75, 0.0, 1.0);
        this.darkeningAlgorithm = builder.comment("Algorithm selector for darkening interface pixels.")
                .defineEnum("darkening_algorithm", DarkeningAlgorithm.GRAYSCALE_AND_HSP);
    }

    @Override
    public void afterConfigReload() {
        this.paths = this.pathsRaw.stream().filter((String s) -> {
            if (!s.matches(ColorChangingResourceHandler.VALID_MINDFUL_DARKNESS_PATH)) {
                MindfulDarkness.LOGGER.warn("'{}' is an invalid path and will be ignored", s);
                return false;
            } else {
                return true;
            }
        }).map((String s) -> {
            // some special handling to support the sprite atlas
            // just adding paths without this prefix works as well though
            return s.replaceFirst("textures/gui/sprites/", "");
        }).toList();
        this.menuBlacklist = ConfigDataSet.from(Registries.MENU, this.menuBlacklistRaw);
        this.fontColorBlacklist = ImmutableSet.copyOf(this.fontColorBlacklistRaw);
        ColorChangingResourceHandler.INSTANCE.clear();
        if (!Objects.equals(this.darkTheme.get(), this.lastDarkTheme) || !Objects.equals(this.textureDarkness.get(),
                this.lastTextureDarkness) || !Objects.equals(this.darkeningAlgorithm.get(),
                this.lastDarkeningAlgorithm)) {
            // only reload when the dark theme is already enabled or was just enabled
            if (this.lastDarkTheme != null && (this.darkTheme.get() || this.lastDarkTheme)) {
                Minecraft.getInstance().reloadResourcePacks();
            }
            this.lastDarkTheme = this.darkTheme.get();
            this.lastTextureDarkness = this.textureDarkness.get();
            this.lastDarkeningAlgorithm = this.darkeningAlgorithm.get();
        }
    }
}
