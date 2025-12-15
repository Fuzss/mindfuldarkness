package fuzs.mindfuldarkness.client.handler;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TickableTexture;
import net.minecraft.resources.Identifier;

import java.util.Set;

public class ColorChangedAssetsManager {
    public static final ColorChangedAssetsManager INSTANCE = new ColorChangedAssetsManager();

    private final Set<Identifier> colorChangingTextures = Sets.newHashSet();

    public void add(Identifier path) {
        this.colorChangingTextures.add(path);
    }

    @SuppressWarnings("ConstantValue")
    public void reset() {
        if (!RenderSystem.isOnRenderThread()) {
            return;
        }

        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        if (textureManager != null) {
            for (Identifier path : this.colorChangingTextures) {
                AbstractTexture abstractTexture = textureManager.byPath.remove(path);
                if (abstractTexture != null) {
                    textureManager.safeClose(path, abstractTexture);
                    if (abstractTexture instanceof TickableTexture) {
                        textureManager.tickableTextures.remove(abstractTexture);
                    }
                }
            }

            this.colorChangingTextures.clear();
        }
    }
}
