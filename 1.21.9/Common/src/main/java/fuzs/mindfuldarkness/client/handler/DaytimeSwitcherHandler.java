package fuzs.mindfuldarkness.client.handler;

import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.gui.screens.PixelConfigScreen;
import fuzs.mindfuldarkness.config.ClientConfig;
import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class DaytimeSwitcherHandler {
    public static final ResourceLocation TEXTURE_LOCATION = MindfulDarkness.id("textures/gui/daytime_switcher.png");
    public static final String KEY_DEBUG_IDENTIFIER = "screen.debug.identifier";
    public static final String KEY_DEBUG_MENU_TYPE = "screen.debug.menuType";

    private static AbstractWidget[] buttons;

    public static void onEndTick(Minecraft minecraft) {
        setHorizontalButtonPosition(minecraft.screen);
    }

    public static void onAfterMouseClick(Screen screen, double mouseX, double mouseY, int button) {
        setHorizontalButtonPosition(screen);
    }

    private static void setHorizontalButtonPosition(Screen screen) {
        if (buttons == null) return;
        if (screen instanceof AbstractContainerScreen<?> containerScreen && screen instanceof RecipeUpdateListener) {
            int leftPos = containerScreen.leftPos;
            int imageWidth = containerScreen.imageWidth;
            buttons[0].setX(leftPos + imageWidth - 3 - 21);
            buttons[1].setX(leftPos + imageWidth - 3 - 40);
            buttons[2].setX(leftPos + imageWidth - 3 - 68);
            buttons[3].setX(leftPos + imageWidth - 3 - 95);
        }
    }

    public static EventResultHolder<@Nullable Screen> onScreenOpening(@Nullable Screen oldScreen, @Nullable Screen newScreen) {
        if (newScreen == null) {
            buttons = null;
        } else if (!(oldScreen instanceof PixelConfigScreen)) {
            // don't get minecraft from screen, it is still null
            Minecraft minecraft = Minecraft.getInstance();

            if (MindfulDarkness.CONFIG.get(ClientConfig.class).debugAllScreens) {
                String identifier = FontColorHandler.getScreenIdentifier(newScreen);

                if (identifier != null) {
                    Component message = Component.translatable(KEY_DEBUG_IDENTIFIER,
                            ComponentUtils.wrapInSquareBrackets(Component.literal(identifier)));

                    // we don't need both as chat messages are logged automatically
                    if (minecraft.level != null) {
                        minecraft.gui.getChat().addMessage(message);
                    } else {
                        MindfulDarkness.LOGGER.info(message.getString());
                    }
                }
            }

            if (newScreen instanceof AbstractContainerScreen<?> containerScreen && MindfulDarkness.CONFIG.get(
                    ClientConfig.class).debugContainerTypes) {
                // don't use vanilla getter as it throws an UnsupportedOperationException for the player inventory
                MenuType<?> menuType = containerScreen.getMenu().menuType;

                if (menuType != null) {
                    Component component = Component.literal(BuiltInRegistries.MENU.getKey(menuType).toString());
                    Component message = Component.translatable(KEY_DEBUG_MENU_TYPE,
                            ComponentUtils.wrapInSquareBrackets(component));
                    minecraft.gui.getChat().addMessage(message);
                }
            }
        }

        return EventResultHolder.pass();
    }

    public static void onDrawBackground(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (supportsDaytimeSwitcher(screen)) {
            drawThemeBackground(guiGraphics, screen.leftPos, screen.topPos, screen.imageWidth);
        }
    }

    public static void drawThemeBackground(GuiGraphics guiGraphics, int leftPos, int topPos, int imageWidth) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                TEXTURE_LOCATION,
                leftPos + imageWidth - 3 - 101,
                topPos - 24,
                0,
                226,
                101,
                24,
                256,
                256);
    }

    private static boolean supportsDaytimeSwitcher(AbstractContainerScreen<?> containerScreen) {
        if (MindfulDarkness.CONFIG.get(ClientConfig.class).hideInGameSwitcher) return false;
        if (containerScreen.height >= containerScreen.imageHeight + 2 * 24) {
            if (containerScreen instanceof CreativeModeInventoryScreen) return false;
            MenuType<?> menuType = containerScreen.getMenu().menuType;
            return menuType == null || !MindfulDarkness.CONFIG.get(ClientConfig.class).menuBlacklist.contains(menuType);
        }
        return false;
    }

    public static void onAfterInit(Minecraft minecraft, AbstractContainerScreen<?> screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, UnaryOperator<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget) {
        if (supportsDaytimeSwitcher(screen)) {
            buttons = makeButtons(minecraft, screen, screen.leftPos, screen.topPos, screen.imageWidth);
            for (AbstractWidget button : buttons) {
                addWidget.apply(button);
            }
        }
    }

    public static AbstractWidget[] makeButtons(Minecraft minecraft, Screen screen, int leftPos, int topPos, int imageWidth) {
        AbstractWidget[] abstractWidgets = new AbstractWidget[4];
        abstractWidgets[0] = new SpritelessImageButton(leftPos + imageWidth - 3 - 21,
                topPos - 18,
                15,
                15,
                224,
                0,
                TEXTURE_LOCATION,
                button -> {
                    screen.onClose();
                });
        abstractWidgets[1] = new SpritelessImageButton(leftPos + imageWidth - 3 - 40,
                topPos - 18,
                15,
                15,
                239,
                0,
                TEXTURE_LOCATION,
                button -> {
                    if (screen instanceof PixelConfigScreen pixelConfigScreen) {
                        pixelConfigScreen.closeToLastScreen();
                    } else {
                        minecraft.setScreen(new PixelConfigScreen(screen));
                    }
                });
        abstractWidgets[2] = new SpritelessImageButton(leftPos + imageWidth - 3 - 68,
                topPos - 20,
                24,
                19,
                200,
                0,
                TEXTURE_LOCATION,
                button -> {
                    toggleThemeButtons(abstractWidgets[3], abstractWidgets[2], true);
                });
        abstractWidgets[3] = new SpritelessImageButton(leftPos + imageWidth - 3 - 95,
                topPos - 20,
                24,
                19,
                176,
                0,
                TEXTURE_LOCATION,
                button -> {
                    toggleThemeButtons(abstractWidgets[3], abstractWidgets[2], true);
                });
        toggleThemeButtons(abstractWidgets[3], abstractWidgets[2], false);
        return abstractWidgets;
    }

    private static void toggleThemeButtons(AbstractWidget lightThemeWidget, AbstractWidget darkThemeWidget, boolean toggleSetting) {
        if (toggleSetting) activateDaytimeSwitch();
        boolean darkTheme = MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get();
        lightThemeWidget.active = darkTheme;
        darkThemeWidget.active = !darkTheme;
    }

    public static void activateDaytimeSwitch() {
        ModConfigSpec.BooleanValue configValue = MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme;
        configValue.set(!configValue.get());
        configValue.save();
    }
}
