package fuzs.mindfuldarkness.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class NewTextureButton extends Button {

    public NewTextureButton(int x, int y, int width, int height, Component component, OnPress onPress) {
        super(x, y, width, height, component, onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        GuiGraphicsHelper.blitNineSliced(guiGraphics, DaytimeSwitcherHandler.TEXTURE_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureY());
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int fontColor = this.active && this.isHoveredOrFocused() ? ChatFormatting.YELLOW.getColor() : 0x404040;
        drawCenteredString(guiGraphics, minecraft.font, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, fontColor | Mth.ceil(this.alpha * 255.0F) << 24, false);
    }

    private int getTextureY() {
        int offset = !this.active || this.isHoveredOrFocused() ? 2 : 1;
        return 166 + offset * 20;
    }

    public static void drawCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color, boolean dropShadow) {
        FormattedCharSequence formattedCharSequence = text.getVisualOrderText();
        if (dropShadow) {
            guiGraphics.drawString(font, formattedCharSequence, (x - font.width(formattedCharSequence) / 2), y, color);
        } else {
            guiGraphics.drawString(font, formattedCharSequence, (x - font.width(formattedCharSequence) / 2), y, color, false);
        }
    }
}
