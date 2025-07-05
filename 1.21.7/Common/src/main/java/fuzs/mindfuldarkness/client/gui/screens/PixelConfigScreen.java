package fuzs.mindfuldarkness.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.mindfuldarkness.MindfulDarkness;
import fuzs.mindfuldarkness.client.gui.components.NewTextureButton;
import fuzs.mindfuldarkness.client.gui.components.NewTextureSliderButton;
import fuzs.mindfuldarkness.client.handler.ColorChangedAssetsManager;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import fuzs.mindfuldarkness.client.util.DarkeningAlgorithm;
import fuzs.mindfuldarkness.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;

public class PixelConfigScreen extends Screen {
    public static final Component ALGORITHM_COMPONENT = Component.translatable("screen.daytime_switcher.algorithm");
    public static final Component INTERFACE_DARKNESS_COMPONENT = Component.translatable("screen.daytime_switcher.interface_darkness");
    public static final Component FONT_BRIGHTNESS_COMPONENT = Component.translatable("screen.daytime_switcher.front_brightness");

    private final Screen lastScreen;
    protected int imageWidth = 176;
    protected int imageHeight = 166;
    protected int leftPos;
    protected int topPos;

    public PixelConfigScreen(Screen lastScreen) {
        super(Component.empty());
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        AbstractWidget[] buttons = DaytimeSwitcherHandler.makeButtons(this.minecraft, this, this.leftPos, this.topPos, this.imageWidth);
        for (AbstractWidget button : buttons) {
            this.addRenderableWidget(button);
        }
        ClientConfig clientConfig = MindfulDarkness.CONFIG.get(ClientConfig.class);
        this.addRenderableWidget(new NewTextureButton(this.leftPos + 13, this.topPos + 32, 150, 20, clientConfig.darkeningAlgorithm.get().getComponent(), button -> {
            ModConfigSpec.EnumValue<DarkeningAlgorithm> configValue = clientConfig.darkeningAlgorithm;
            int length = DarkeningAlgorithm.values().length;
            DarkeningAlgorithm darkeningAlgorithm = DarkeningAlgorithm.values()[((configValue.get().ordinal() + (hasShiftDown() ? -1 : 1)) % length + length) % length];
            configValue.set(darkeningAlgorithm);
            button.setMessage(darkeningAlgorithm.getComponent());
            if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
                ColorChangedAssetsManager.INSTANCE.recordedReset();
            }
        }));
        this.addRenderableWidget(new NewTextureSliderButton(this.leftPos + 13, this.topPos + 81, 150, 18, Component.empty(), clientConfig.textureDarkness.get()) {

            @Override
            protected void updateMessage() {
                this.setTooltip(Tooltip.create(Component.literal(String.format("%.0f%%", this.value * 100.0))));
            }

            @Override
            protected void applyValue() {
                clientConfig.textureDarkness.set(this.value);
                if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
                    ColorChangedAssetsManager.INSTANCE.recordedReset();
                }
            }
        }).setTooltip(Tooltip.create(Component.literal(String.format("%.0f%%", clientConfig.textureDarkness.get() * 100.0))));
        this.addRenderableWidget(new NewTextureSliderButton(this.leftPos + 13, this.topPos + 129, 150, 18, Component.empty(), clientConfig.fontBrightness.get()) {

            @Override
            protected void updateMessage() {
                this.setTooltip(Tooltip.create(Component.literal(String.format("%.0f%%", this.value * 100.0))));
            }

            @Override
            protected void applyValue() {
                clientConfig.fontBrightness.set(this.value);
            }
        }).setTooltip(Tooltip.create(Component.literal(String.format("%.0f%%", clientConfig.fontBrightness.get() * 100.0))));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(DaytimeSwitcherHandler.TEXTURE_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        DaytimeSwitcherHandler.drawThemeBackground(guiGraphics, this.leftPos, this.topPos, this.imageWidth);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        NewTextureButton.drawCenteredString(guiGraphics, this.font, ALGORITHM_COMPONENT, this.width / 2, this.topPos + 19, 4210752, false);
        NewTextureButton.drawCenteredString(guiGraphics, this.font, INTERFACE_DARKNESS_COMPONENT, this.width / 2, this.topPos + 67, 4210752, false);
        NewTextureButton.drawCenteredString(guiGraphics, this.font,
                FONT_BRIGHTNESS_COMPONENT, this.width / 2, this.topPos + 115, 4210752, false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }  else if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            this.onClose();
            return true;
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        this.lastScreen.onClose();
    }

    public void closeToLastScreen() {
        this.minecraft.setScreen(this.lastScreen);
    }
}
