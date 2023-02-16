package fuzs.mindfuldarkness.client.handler;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;
import fuzs.mindfuldarkness.mixin.client.accessor.TextureManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;

public class ColorChangedResourcesHandler {
    public static final ColorChangedResourcesHandler INSTANCE = new ColorChangedResourcesHandler();

    private final Set<ResourceLocation> colorChangingTextures = Sets.newHashSet();

    public void add(ResourceLocation path) {
        this.colorChangingTextures.add(path);
    }

    private void reset() {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        Map<ResourceLocation, AbstractTexture> byPath = ((TextureManagerAccessor) textureManager).mindfuldarkness$getByPath();
        Set<Tickable> tickableTextures = ((TextureManagerAccessor) textureManager).mindfuldarkness$getTickableTextures();
        for (ResourceLocation path : this.colorChangingTextures) {
            AbstractTexture abstractTexture = byPath.remove(path);
            if (abstractTexture != null) {
                ((TextureManagerAccessor) textureManager).mindfuldarkness$callSafeClose(path, abstractTexture);
                if (abstractTexture instanceof Tickable) {
                    tickableTextures.remove(abstractTexture);
                }
            }
        }
        this.colorChangingTextures.clear();
    }

    public void recordedReset() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(this::reset);
        } else {
            this.reset();
        }
    }
}
