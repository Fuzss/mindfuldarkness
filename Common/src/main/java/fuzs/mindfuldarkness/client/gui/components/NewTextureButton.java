package fuzs.mindfuldarkness.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitchHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class NewTextureButton extends Button {

    public NewTextureButton(int i, int j, int k, int l, Component component, OnPress onPress) {
        super(i, j, k, l, component, onPress);
    }

    @Override
    protected int getYImage(boolean isHovered) {
        return !this.active || isHovered ? 2 : 1;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, DaytimeSwitchHandler.TEXTURE_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(poseStack, this.x, this.y, 0, 166 + i * 20, this.width / 2, this.height);
        this.blit(poseStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 166 + i * 20, this.width / 2, this.height);
        this.renderBg(poseStack, minecraft, mouseX, mouseY);
        final int j = this.active && this.isHoveredOrFocused() ? ChatFormatting.YELLOW.getColor() : 4210752;
        drawCenteredString(poseStack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24, false);
        if (this.isHoveredOrFocused()) {
            this.renderToolTip(poseStack, mouseX, mouseY);
        }
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
