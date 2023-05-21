package fuzs.mindfuldarkness.mixin.client;

import fuzs.mindfuldarkness.client.packs.resources.ColorChangingResourceHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(MultiPackResourceManager.class)
abstract class MultiPackResourceManagerMixin {
    @Nullable
    @Unique
    private ColorChangingResourceHandler mindfuldarkness$colorChangingHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(PackType packType, List<PackResources> list, CallbackInfo callback) {
        if (packType == PackType.CLIENT_RESOURCES) {
            this.mindfuldarkness$colorChangingHandler = new ColorChangingResourceHandler();
        }
    }

    @Inject(method = "getResource", at = @At("RETURN"), cancellable = true)
    public void getResource(ResourceLocation resourceLocation, CallbackInfoReturnable<Optional<Resource>> callback) {
        if (this.mindfuldarkness$colorChangingHandler != null) {
            Optional<Resource> resource = this.mindfuldarkness$colorChangingHandler.getResource(resourceLocation, callback.getReturnValue());
            if (resource.isPresent()) callback.setReturnValue(resource);
        }
    }
}
