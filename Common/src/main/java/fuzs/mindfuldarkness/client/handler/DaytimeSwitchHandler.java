package fuzs.mindfuldarkness.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.gui.screens.PixelConfigScreen;
import fuzs.mindfuldarkness.config.ClientConfig;
import fuzs.mindfuldarkness.mixin.client.accessor.AbstractContainerMenuAccessor;
import fuzs.puzzleslib.client.gui.screens.CommonScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.UnaryOperator;

public class DaytimeSwitchHandler {
    public static final ResourceLocation TEXTURE_LOCATION = MindfulDarkness.id("textures/gui/daytime_switcher.png");

    public static Optional<Screen> onScreenOpen(@Nullable Screen oldScreen, @Nullable Screen newScreen) {
        if (newScreen instanceof AbstractContainerScreen<?> containerScreen && MindfulDarkness.CONFIG.get(ClientConfig.class).debugContainerTypes) {
            // don't use vanilla getter as it throws an UnsupportedOperationException for the player inventory
            MenuType<?> type = ((AbstractContainerMenuAccessor) containerScreen.getMenu()).mindfuldarkness$getMenuType();
            String id = null;
            if (type != null) {
                id = Registry.MENU.getKey(type).toString();
            } else {
                if (newScreen instanceof InventoryScreen) {
                    id = "minecraft:inventory";
                } else if (newScreen instanceof CreativeModeInventoryScreen) {
                    id = "minecraft:creative_inventory";
                }
            }
            if (id != null) {
                Minecraft.getInstance().gui.getChat().addMessage(Component.translatable("debug.menu.opening", ComponentUtils.wrapInSquareBrackets(Component.literal(id))));
            }
        }
        return Optional.empty();
    }

    public static void onContainerScreen$Render$Background(AbstractContainerScreen<?> screen, PoseStack poseStack, int mouseX, int mouseY) {
        if (supportsDaytimeSwitcher(screen)) {
            drawThemeBg(poseStack, CommonScreens.INSTANCE.getLeftPos(screen), CommonScreens.INSTANCE.getTopPos(screen), CommonScreens.INSTANCE.getImageWidth(screen));
        }
    }

    public static void drawThemeBg(PoseStack poseStack, int leftPos, int topPos, int imageWidth) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        GuiComponent.blit(poseStack, leftPos + imageWidth - 3 - 101, topPos - 24, 0, 226, 101, 24, 256, 256);
    }

    private static boolean supportsDaytimeSwitcher(AbstractContainerScreen<?> containerScreen) {
        if (MindfulDarkness.CONFIG.get(ClientConfig.class).hideIngameSwitcher) return false;
        if (containerScreen.height >= CommonScreens.INSTANCE.getImageHeight(containerScreen) + 2 * 24) {
            if (containerScreen instanceof CreativeModeInventoryScreen) return false;
            MenuType<?> type = ((AbstractContainerMenuAccessor) containerScreen.getMenu()).mindfuldarkness$getMenuType();
            return type == null || !MindfulDarkness.CONFIG.get(ClientConfig.class).menuBlacklist.contains(type);
        }
        return false;
    }

    public static void onScreenInit$Post(Screen screen, Minecraft minecraft, int width, int height, UnaryOperator<AbstractWidget> addWidget) {
        if (screen instanceof AbstractContainerScreen<?> containerScreen && supportsDaytimeSwitcher(containerScreen)) {
            makeButtons(minecraft, screen, CommonScreens.INSTANCE.getLeftPos(containerScreen), CommonScreens.INSTANCE.getTopPos(containerScreen), CommonScreens.INSTANCE.getImageWidth(containerScreen), addWidget);
        }
    }

    public static AbstractWidget[] makeButtons(Minecraft minecraft, Screen screen, int leftPos, int topPos, int imageWidth, UnaryOperator<AbstractWidget> addWidget) {
        AbstractWidget[] abstractWidgets = new AbstractWidget[4];
        abstractWidgets[0] = addWidget.apply(new ImageButton(leftPos + imageWidth - 3 - 21, topPos - 18, 15, 15, 224, 0, TEXTURE_LOCATION, button -> {
            screen.onClose();
        }));
        abstractWidgets[1] = addWidget.apply(new ImageButton(leftPos + imageWidth - 3 - 95, topPos - 20, 24, 19, 176, 0, TEXTURE_LOCATION, button -> {
            toggleThemeButtons(abstractWidgets[1], abstractWidgets[2], true);
        }));
        abstractWidgets[2] = addWidget.apply(new ImageButton(leftPos + imageWidth - 3 - 68, topPos - 20, 24, 19, 200, 0, TEXTURE_LOCATION, button -> {
            toggleThemeButtons(abstractWidgets[1], abstractWidgets[2], true);
        }));
        abstractWidgets[3] = addWidget.apply(new ImageButton(leftPos + imageWidth - 3 - 40, topPos - 18, 15, 15, 239, 0, TEXTURE_LOCATION, button -> {
            if (screen instanceof PixelConfigScreen pixelConfigScreen) {
                pixelConfigScreen.closeToLastScreen();
            } else {
                minecraft.setScreen(new PixelConfigScreen(screen));
            }
        }));
        toggleThemeButtons(abstractWidgets[1], abstractWidgets[2], false);
        return abstractWidgets;
    }

    private static void toggleThemeButtons(AbstractWidget lightThemeWidget, AbstractWidget darkThemeWidget, boolean toggleSetting) {
        boolean darkTheme = MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get();
        if (toggleSetting) {
            darkTheme = !darkTheme;
            MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.set(darkTheme);
            ColorChangedResourcesHandler.INSTANCE.recordedReset();
        }
        lightThemeWidget.active = darkTheme;
        darkThemeWidget.active = !darkTheme;
    }
}
