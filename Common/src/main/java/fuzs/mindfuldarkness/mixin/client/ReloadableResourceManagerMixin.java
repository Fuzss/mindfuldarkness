package fuzs.mindfuldarkness.mixin.client;

import fuzs.mindfuldarkness.server.packs.resources.ColorChangingResourceManager;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableResourceManager.class)
abstract class ReloadableResourceManagerMixin {
    private CloseableResourceManager resources;

    @Inject(method = "createReload", at = @At(value = "FIELD", target = "Lnet/minecraft/server/packs/resources/ReloadableResourceManager;resources:Lnet/minecraft/server/packs/resources/CloseableResourceManager;", ordinal = 2))
    public void createReload(Executor backgroundExecutor, Executor gameExecutor, CompletableFuture<Unit> waitingFor, List<PackResources> resourcePacks, CallbackInfoReturnable<ReloadInstance> callback) {
        this.resources = new ColorChangingResourceManager(this.resources);
    }
}
