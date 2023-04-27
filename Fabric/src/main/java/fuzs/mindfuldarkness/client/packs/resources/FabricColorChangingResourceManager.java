package fuzs.mindfuldarkness.client.packs.resources;

import fuzs.mindfuldarkness.client.packs.resources.ColorChangingResourceManager;
import net.fabricmc.fabric.impl.resource.loader.FabricLifecycledResourceManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;

// we need to additionally implement net.fabricmc.fabric.impl.resource.loader.FabricLifecycledResourceManager which is required for Fabric Api's whole net.fabricmc.fabric.api.resource.ResourceManagerHelper class to work
// the interface is unfortunately internal, so this might break at any point, but this seems to be the best workaround
@SuppressWarnings("UnstableApiUsage")
public class FabricColorChangingResourceManager extends ColorChangingResourceManager implements FabricLifecycledResourceManager {
    private final PackType type;

    public FabricColorChangingResourceManager(PackType type, CloseableResourceManager resourceManager) {
        super(resourceManager);
        this.type = type;
    }

    @Override
    public PackType fabric_getResourceType() {
        return this.type;
    }
}
