package fuzs.mindfuldarkness.mixin.client;

import fuzs.mindfuldarkness.MindfulDarkness;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class TextureManagerMixin {

    @Inject(method = "register", at = @At("HEAD"))
    public void register(ResourceLocation path, AbstractTexture texture, CallbackInfo callback) {
        if (path.toString().contains("atlas/")) MindfulDarkness.LOGGER.info("", new RuntimeException());
    }
}
