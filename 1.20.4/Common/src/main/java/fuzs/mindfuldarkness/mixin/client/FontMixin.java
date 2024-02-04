package fuzs.mindfuldarkness.mixin.client;

import fuzs.mindfuldarkness.client.handler.FontColorHandler;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(Font.class)
abstract class FontMixin {

    @ModifyVariable(method = "adjustColor", at = @At("HEAD"))
    private static int adjustColor(int color) {
        return FontColorHandler.adjustFontColor(color);
    }
}
