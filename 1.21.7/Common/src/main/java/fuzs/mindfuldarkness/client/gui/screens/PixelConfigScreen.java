package fuzs.mindfuldarkness.client.gui.screens;

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
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;

public class PixelConfigScreen extends Screen {
    public static final Component ALGORITHM_COMPONENT = Component.translatable("screen.daytime_switcher.algorithm");
    public static final Component INTERFACE_DARKNESS_COMPONENT = Component.translatable(
            "screen.daytime_switcher.interface_darkness");
    public static final Component FONT_BRIGHTNESS_COMPONENT = Component.translatable(
            "screen.daytime_switcher.front_brightness");

    private final Screen lastScreen;
    protected int imageWidth = 176;
    protected int imageHeight = 166;
    protected int leftPos;
    protected int topPos;

    public PixelConfigScreen(Screen lastScreen) {
        super(CommonComponents.EMPTY);
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        AbstractWidget[] buttons = DaytimeSwitcherHandler.makeButtons(this.minecraft,
                this,
                this.leftPos,
                this.topPos,
                this.imageWidth);
        for (AbstractWidget button : buttons) {
            this.addRenderableWidget(button);
        }
        ClientConfig clientConfig = MindfulDarkness.CONFIG.get(ClientConfig.class);
        this.addRenderableWidget(new NewTextureButton(this.leftPos + 13,
                this.topPos + 32,
                150,
                20,
                clientConfig.darkeningAlgorithm.get().getComponent(),
                button -> {
                    ModConfigSpec.EnumValue<DarkeningAlgorithm> configValue = clientConfig.darkeningAlgorithm;
                    int length = DarkeningAlgorithm.values().length;
                    DarkeningAlgorithm darkeningAlgorithm = DarkeningAlgorithm.values()[
                            ((configValue.get().ordinal() + (hasShiftDown() ? -1 : 1)) % length + length) % length];
                    configValue.set(darkeningAlgorithm);
                    configValue.save();
                    button.setMessage(darkeningAlgorithm.getComponent());
                    if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
                        ColorChangedAssetsManager.INSTANCE.reset();
                    }
                }));
        this.addRenderableWidget(new NewTextureSliderButton(this.leftPos + 13,
                        this.topPos + 81,
                        150,
                        18,
                        CommonComponents.EMPTY,
                        clientConfig.textureDarkness.get()) {

                    @Override
                    protected void updateMessage() {
                        this.setTooltip(Tooltip.create(Component.literal(String.format("%.0f%%", this.value * 100.0))));
                    }

                    @Override
                    protected void applyValue() {
                        clientConfig.textureDarkness.set(this.value);
                        clientConfig.textureDarkness.save();
                        if (MindfulDarkness.CONFIG.get(ClientConfig.class).darkTheme.get()) {
                            ColorChangedAssetsManager.INSTANCE.reset();
                        }
                    }
                })
                .setTooltip(Tooltip.create(Component.literal(String.format("%.0f%%",
                        clientConfig.textureDarkness.get() * 100.0))));
        this.addRenderableWidget(new NewTextureSliderButton(this.leftPos + 13,
                        this.topPos + 129,
                        150,
                        18,
                        CommonComponents.EMPTY,
                        clientConfig.fontBrightness.get()) {

                    @Override
                    protected void updateMessage() {
                        this.setTooltip(Tooltip.create(Component.literal(String.format("%.0f%%", this.value * 100.0))));
                    }

                    @Override
                    protected void applyValue() {
                        clientConfig.fontBrightness.set(this.value);
                        clientConfig.fontBrightness.save();
                    }
                })
                .setTooltip(Tooltip.create(Component.literal(String.format("%.0f%%",
                        clientConfig.fontBrightness.get() * 100.0))));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                DaytimeSwitcherHandler.TEXTURE_LOCATION,
                this.leftPos,
                this.topPos,
                0.0F,
                0.0F,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
        DaytimeSwitcherHandler.drawThemeBackground(guiGraphics, this.leftPos, this.topPos, this.imageWidth);
    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        NewTextureButton.drawCenteredString(guiGraphics,
                this.font,
                ALGORITHM_COMPONENT,
                this.width / 2,
                this.topPos + 19,
                0xFF404040,
                false);
        NewTextureButton.drawCenteredString(guiGraphics,
                this.font,
                INTERFACE_DARKNESS_COMPONENT,
                this.width / 2,
                this.topPos + 67,
                0xFF404040,
                false);
        NewTextureButton.drawCenteredString(guiGraphics,
                this.font,
                FONT_BRIGHTNESS_COMPONENT,
                this.width / 2,
                this.topPos + 115,
                0xFF404040,
                false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
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
