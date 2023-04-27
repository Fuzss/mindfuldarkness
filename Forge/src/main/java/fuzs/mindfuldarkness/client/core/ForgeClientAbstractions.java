package fuzs.mindfuldarkness.client.core;

import fuzs.mindfuldarkness.client.packs.resources.ColorChangingResourceManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;

public class ForgeClientAbstractions implements ClientAbstractions {

    @Override
    public CloseableResourceManager createColorChangingResourceManager(PackType type, CloseableResourceManager resourceManager) {
        return new ColorChangingResourceManager(resourceManager);
    }
}
