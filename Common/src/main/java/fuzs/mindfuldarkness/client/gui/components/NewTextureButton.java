package fuzs.mindfuldarkness.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class NewTextureButton extends Button {

    public NewTextureButton(int x, int y, int width, int height, Component component, OnPress onPress) {
        super(x, y, width, height, component, onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float tickDelta) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, DaytimeSwitcherHandler.TEXTURE_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        blitNineSliced(poseStack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureY());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.active ? 16777215 : 10526880;
        drawCenteredString(poseStack, minecraft.font, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, k | Mth.ceil(this.alpha * 255.0F) << 24, false);
    }

    private int getTextureY() {
        int i = !this.active || this.isHoveredOrFocused() ? 2 : 1;
        return 166 + i * 20;
    }

    public static void drawCenteredString(PoseStack poseStack, Font font, Component text, int x, int y, int color, boolean dropShadow) {
        FormattedCharSequence formattedCharSequence = text.getVisualOrderText();
        if (dropShadow) {
            font.drawShadow(poseStack, formattedCharSequence, (float) (x - font.width(formattedCharSequence) / 2), (float)y, color);
        } else {
            font.draw(poseStack, formattedCharSequence, (float) (x - font.width(formattedCharSequence) / 2), (float)y, color);
        }
    }
}
