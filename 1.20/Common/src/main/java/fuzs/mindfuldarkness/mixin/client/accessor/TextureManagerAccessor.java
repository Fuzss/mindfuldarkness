package fuzs.mindfuldarkness.mixin.client.accessor;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;
import java.util.Set;

@Mixin(TextureManager.class)
public interface TextureManagerAccessor {

    @Accessor("byPath")
    Map<ResourceLocation, AbstractTexture> mindfuldarkness$getByPath();

    @Accessor("tickableTextures")
    Set<Tickable> mindfuldarkness$getTickableTextures();

    @Invoker("safeClose")
    void mindfuldarkness$callSafeClose(ResourceLocation path, AbstractTexture texture);
}
