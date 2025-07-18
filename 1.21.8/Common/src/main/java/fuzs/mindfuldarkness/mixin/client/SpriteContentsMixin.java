package fuzs.mindfuldarkness.mixin.client;

import com.mojang.blaze3d.platform.NativeImage;
import fuzs.mindfuldarkness.client.packs.resources.ColorChangingResourceHandler;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteContents.class)
abstract class SpriteContentsMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(ResourceLocation name, FrameSize frameSize, NativeImage originalImage, ResourceMetadata metadata, CallbackInfo callback) {
        ColorChangingResourceHandler.INSTANCE.processResource(name, originalImage);
    }
}
