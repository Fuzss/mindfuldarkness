package fuzs.mindfuldarkness.client.gui.components;

import fuzs.mindfuldarkness.client.gui.screens.PixelConfigScreen;
import fuzs.mindfuldarkness.client.handler.DaytimeSwitcherHandler;
import fuzs.puzzleslib.api.client.gui.v2.GuiGraphicsHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;

public class NewTextureButton extends Button {

    public NewTextureButton(int x, int y, int width, int height, Component component, OnPress onPress) {
        super(x, y, width, height, component, onPress, DEFAULT_NARRATION);
        this.setMessage(component);
    }

    @Override
    public Component getMessage() {
        return this.active && this.isHoveredOrFocused() ? this.message : this.inactiveMessage;
    }

    @Override
    public void setMessage(Component message) {
        this.message = ComponentUtils.mergeStyles(message, Style.EMPTY.withColor(ChatFormatting.YELLOW));
        this.inactiveMessage = ComponentUtils.mergeStyles(message, Style.EMPTY.withColor(0x404040));
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        Minecraft minecraft = Minecraft.getInstance();
        GuiGraphicsHelper.blitNineSliced(guiGraphics,
                RenderPipelines.GUI_TEXTURED,
                DaytimeSwitcherHandler.TEXTURE_LOCATION,
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                20,
                4,
                20,
                4,
                200,
                20,
                0,
                this.getTextureY(),
                ARGB.white(this.alpha));
        PixelConfigScreen.drawCenteredString(guiGraphics,
                minecraft.font,
                this.getMessage(),
                this.getX() + this.width / 2,
                this.getY() + (this.height - 8) / 2,
                ARGB.white(this.alpha),
                false);
    }

    private int getTextureY() {
        return 166 + (!this.active || this.isHoveredOrFocused() ? 2 : 1) * 20;
    }
}
