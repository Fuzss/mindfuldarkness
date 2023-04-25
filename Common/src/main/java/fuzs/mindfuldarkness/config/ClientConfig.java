package fuzs.mindfuldarkness.config;

import com.google.common.collect.Lists;
import fuzs.mindfuldarkness.client.util.PixelDarkener;
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
    public AbstractConfigValue<Double> textureDarkness;
    public AbstractConfigValue<Double> fontBrightness;
    public AbstractConfigValue<PixelDarkener> darkeningAlgorithm;
    @Config(description = "Specifies gui paths and resources to darken. Use '*' as wildcard char. Directory boundaries will not be crossed. Begin with a namespace or skip namespace to apply to all namespaces. Begin with '!' to exclude matches.")
    public List<String> paths = Lists.newArrayList("textures/gui/*.png", "!minecraft:textures/gui/icons.png", "!minecraft:textures/gui/options_background.png", "textures/gui/container/*.png", "textures/gui/container/creative_inventory/*.png");
    @Config(description = "Do not add the dark mode toggle buttons to the top of every menu.")
    public boolean hideIngameSwitcher = false;
    @Config(name = "menu_blacklist", description = "Exclude certain menus from showing the dark mode switcher. Useful when the box intersects other screen elements.")
    List<String> menuBlacklistRaw = Lists.newArrayList();
    @Config(description = "Print menu type to game chat whenever a new menu screen is opened. Intended for finding menu types to be added to \"menu_blacklist\".")
    public boolean debugContainerTypes = false;

    public ConfigDataSet<MenuType<?>> menuBlacklist;

    @Override
    public void addToBuilder(AbstractConfigBuilder builder, ValueCallback callback) {
        this.darkTheme = builder.comment("Use a dark theme for the configuration screens.").define("dark_theme", false);
        callback.accept(this.darkTheme, v -> {});
        this.textureDarkness = builder.comment("Percentage of original interface brightness to apply.").defineInRange("texture_darkness", 0.5, 0.0, 1.0);
        callback.accept(this.textureDarkness, v -> {});
        this.fontBrightness = builder.comment("The minimum brightness value of font while dark mode is enabled.").defineInRange("font_brightness", 0.75, 0.0, 1.0);
        callback.accept(this.fontBrightness, v -> {});
        this.darkeningAlgorithm = builder.comment("Algorithm selector for darkening interface pixels.").defineEnum("darkening_algorithm", PixelDarkener.GRAYSCALE_AND_HSP);
        callback.accept(this.fontBrightness, v -> {});
    }

    @Override
    public void afterConfigReload() {
        this.menuBlacklist = ConfigDataSet.of(Registry.MENU_REGISTRY, this.menuBlacklistRaw);
    }
}
