package fuzs.mindfuldarkness.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class NewTextureButton extends Button {

    public NewTextureButton(int x, int y, int width, int height, Component component, OnPress onPress) {
        super(x, y, width, height, component, onPress);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float tickDelta) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        int i = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, DaytimeSwitcherHandler.TEXTURE_LOCATION);
        this.blit(poseStack, this.x, this.y, 0, 166 + i * 20, this.width / 2, this.height);
        this.blit(poseStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 166 + i * 20, this.width / 2, this.height);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.active && this.isHoveredOrFocused() ? ChatFormatting.YELLOW.getColor() : 4210752;
        drawCenteredString(poseStack, minecraft.font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, k | Mth.ceil(this.alpha * 255.0F) << 24, false);
    }

    private int getTextureY() {
        int i = !this.active || this.isHoveredOrFocused() ? 2 : 1;
        return 166 + i * 20;
    }

    public static void drawCenteredString(PoseStack poseStack, Font font, Component text, int x, int y, int color, boolean dropShadow) {
        FormattedCharSequence formattedCharSequence = text.getVisualOrderText();
        if (dropShadow) {
            font.drawShadow(poseStack, formattedCharSequence, (x - font.width(formattedCharSequence) / 2), y, color);
        } else {
            font.draw(poseStack, formattedCharSequence, (x - font.width(formattedCharSequence) / 2), y, color);
        }
    }
}
