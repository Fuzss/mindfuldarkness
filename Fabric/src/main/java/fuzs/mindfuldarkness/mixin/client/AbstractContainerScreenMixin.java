package fuzs.mindfuldarkness.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.mindfuldarkness.api.client.event.ContainerScreenEvents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
abstract class AbstractContainerScreenMixin extends Screen {

    protected AbstractContainerScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderBg(Lcom/mojang/blaze3d/vertex/PoseStack;FII)V", shift = At.Shift.AFTER))
    public void render$0(PoseStack poseStack, int mouseX, int mouseY, float partialTicks, CallbackInfo callback) {
        ContainerScreenEvents.BACKGROUND.invoker().onDrawBackground(AbstractContainerScreen.class.cast(this), poseStack, mouseX, mouseY);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderLabels(Lcom/mojang/blaze3d/vertex/PoseStack;II)V", shift = At.Shift.AFTER))
    public void render$1(PoseStack poseStack, int mouseX, int mouseY, float partialTicks, CallbackInfo callback) {
        ContainerScreenEvents.FOREGROUND.invoker().onDrawForeground(AbstractContainerScreen.class.cast(this), poseStack, mouseX, mouseY);
    }
}
