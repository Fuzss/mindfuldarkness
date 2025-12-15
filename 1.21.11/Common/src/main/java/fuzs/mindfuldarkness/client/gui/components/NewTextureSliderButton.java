package fuzs.mindfuldarkness.client.gui.components;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public abstract class NewTextureSliderButton extends AbstractSliderButton {

    public NewTextureSliderButton(int x, int y, int width, int height, Component component, double value) {
        super(x, y, width, height, CommonComponents.EMPTY, value);
    }

    @Override
    public Component getMessage() {
        return CommonComponents.EMPTY;
    }

    @Override
    public void setMessage(Component message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
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
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                DaytimeSwitcherHandler.TEXTURE_LOCATION,
                this.getX() + (int) (this.value * (double) (this.width - 18)),
                this.getY(),
                176,
                this.getTextureY(),
                18,
                18,
                256,
                256,
                ARGB.white(this.alpha));
        if (this.isHovered()) {
            guiGraphics.requestCursor(this.dragging ? CursorTypes.RESIZE_EW : CursorTypes.POINTING_HAND);
        }
    }

    private int getTextureY() {
        return 57 + (this.isHoveredOrFocused() ? 2 : 1) * 18;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isSelection()) {
            this.canChangeValue = !this.canChangeValue;
            return true;
        } else {
            if (this.canChangeValue) {
                boolean isLeft = event.isLeft();
                boolean isRight = event.isRight();
                if (isLeft || isRight) {
                    float directionValue = isLeft ? -1.0F : 1.0F;
                    this.setValue(this.value + directionValue / (this.width - 18));
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    protected void setValueFromMouse(MouseButtonEvent event) {
        this.setValue((event.x() - (double) (this.getX() + 9)) / (double) (this.width - 18));
    }
}
