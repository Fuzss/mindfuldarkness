package fuzs.mindfuldarkness.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.mindfuldarkness.client.handler.FontColorHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(targets = "net.minecraft.client.gui.Font$PreparedTextBuilder")
abstract class Font$PreparedTextBuilderMixin {

    @ModifyReturnValue(method = "getTextColor", at = @At("RETURN"))
    private int getTextColor(int textColor) {
        return FontColorHandler.adjustFontColor(textColor);
    }
}
