package fuzs.mindfuldarkness.client.core;

import fuzs.mindfuldarkness.client.packs.resources.ColorChangingResourceManager;
import fuzs.mindfuldarkness.client.packs.resources.FabricColorChangingResourceManager;
import fuzs.puzzleslib.core.ModLoaderEnvironment;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public CloseableResourceManager createColorChangingResourceManager(PackType type, CloseableResourceManager resourceManager) {
        if (ModLoaderEnvironment.INSTANCE.isModLoaded("quilt_loader")) {
            return new ColorChangingResourceManager(resourceManager);
        }
        return new FabricColorChangingResourceManager(type, resourceManager);
    }
}
