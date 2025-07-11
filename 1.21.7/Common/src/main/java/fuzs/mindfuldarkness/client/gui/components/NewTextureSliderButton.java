package fuzs.mindfuldarkness.client.gui.components;

import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public abstract class NewTextureSliderButton extends AbstractSliderButton {

    public NewTextureSliderButton(int x, int y, int width, int height, Component component, double d) {
        super(x, y, width, height, component, d);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                DaytimeSwitcherHandler.TEXTURE_LOCATION,
                this.getX() + 2,
                this.getY() + 2,
                101,
                226,
                (this.width - 4) / 2,
                this.height - 4,
                256,
                256,
                ARGB.white(this.alpha));
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                DaytimeSwitcherHandler.TEXTURE_LOCATION,
                this.getX() + this.width / 2,
                this.getY() + 2,
                101 + 146 - (this.width - 4) / 2,
                226,
                (this.width - 4) / 2,
                this.height - 4,
                256,
                256,
                ARGB.white(this.alpha));
        int textureY = (this.isHoveredOrFocused() ? 2 : 1) * 18;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                DaytimeSwitcherHandler.TEXTURE_LOCATION,
                this.getX() + (int) (this.value * (double) (this.width - 18)),
                this.getY(),
                176,
                57 + textureY,
                18,
                18,
                256,
                256,
                ARGB.white(this.alpha));
        int fontColor = this.active && this.isHoveredOrFocused() ? ChatFormatting.YELLOW.getColor() : 4210752;
        guiGraphics.drawCenteredString(minecraft.font,
                this.getMessage(),
                this.getX() + this.width / 2,
                this.getY() + (this.height - 8) / 2,
                fontColor | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean bl = keyCode == 263;
        if (bl || keyCode == 262) {
            float f = bl ? -1.0F : 1.0F;
            this.setValue(this.value + (double) (f / (float) (this.width - 18)));
        }

        return false;
    }

    private void setValueFromMouse(double mouseX) {
        this.setValue((mouseX - (double) (this.getX() + 9)) / (double) (this.width - 18));
    }

    private void setValue(double value) {
        double d = this.value;
        this.value = Mth.clamp(value, 0.0, 1.0);
        if (d != this.value) {
            this.applyValue();
        }

        this.updateMessage();
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.setValueFromMouse(mouseX);
        super.onDrag(mouseX, mouseY, dragX, dragY);
    }
}
