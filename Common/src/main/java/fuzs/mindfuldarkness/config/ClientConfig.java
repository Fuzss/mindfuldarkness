package fuzs.mindfuldarkness.config;

import com.google.common.collect.Lists;
import fuzs.puzzleslib.config.ConfigCore;
import fuzs.puzzleslib.config.ValueCallback;
import fuzs.puzzleslib.config.annotation.Config;
import fuzs.puzzleslib.config.core.AbstractConfigBuilder;
import fuzs.puzzleslib.config.core.AbstractConfigValue;
import fuzs.puzzleslib.config.serialization.ConfigDataSet;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.List;

public class ClientConfig implements ConfigCore {
    public AbstractConfigValue<Boolean> darkTheme;
    @Config(description = "Specifies gui paths and resources to darken.")
    public List<String> paths = Lists.newArrayList("textures/gui/*.png", "!minecraft:textures/gui/icons.png", "!minecraft:textures/gui/options_background.png", "textures/gui/container/*.png");
    @Config(name = "menu_blacklist", description = "Exclude certain menus from showing the dark mode switcher. Useful when the box intersects other screen elements.")
    List<String> menuBlacklistRaw = Lists.newArrayList();
    @Config(description = "Print menu type to game chat whenever a new menu screen is opened. Intended for finding menu types to be added to \"menu_blacklist\".")
    public boolean debugContainerTypes = true;

    public ConfigDataSet<MenuType<?>> menuBlacklist;

    @Override
    public void addToBuilder(AbstractConfigBuilder builder, ValueCallback callback) {
        this.darkTheme = builder.comment("Use a dark theme for the configuration screens.").define("dark_theme", false);
        callback.accept(this.darkTheme, v -> {});
    }

    @Override
    public void afterConfigReload() {
        this.menuBlacklist = ConfigDataSet.of(Registry.MENU_REGISTRY, this.menuBlacklistRaw);
    }
}
