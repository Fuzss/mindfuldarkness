package fuzs.mindfuldarkness.api.client.core;

import fuzs.mindfuldarkness.api.client.packs.resources.FabricColorChangingResourceManager;
import fuzs.mindfuldarkness.client.core.ClientAbstractions;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public CloseableResourceManager createColorChangingResourceManager(PackType type, CloseableResourceManager resourceManager) {
        return new FabricColorChangingResourceManager(type, resourceManager);
    }
}
