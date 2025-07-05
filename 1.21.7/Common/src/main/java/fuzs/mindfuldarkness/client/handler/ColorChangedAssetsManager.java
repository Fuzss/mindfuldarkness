package fuzs.mindfuldarkness.client.handler;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class ColorChangedAssetsManager {
    public static final ColorChangedAssetsManager INSTANCE = new ColorChangedAssetsManager();

    private final Set<ResourceLocation> colorChangingTextures = Sets.newHashSet();

    public void add(ResourceLocation path) {
        this.colorChangingTextures.add(path);
    }

    @SuppressWarnings("ConstantValue")
    private void reset() {
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        if (textureManager != null) {
            for (ResourceLocation path : this.colorChangingTextures) {
                AbstractTexture abstractTexture = textureManager.byPath.remove(path);
                if (abstractTexture != null) {
                    textureManager.safeClose(path, abstractTexture);
                    if (abstractTexture instanceof Tickable) {
                        textureManager.tickableTextures.remove(abstractTexture);
                    }
                }
            }
            this.colorChangingTextures.clear();
        }
    }

    public void recordedReset() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(this::reset);
        } else {
            this.reset();
        }
    }
}
