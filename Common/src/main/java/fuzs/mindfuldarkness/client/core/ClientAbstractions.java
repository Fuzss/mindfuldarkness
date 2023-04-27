package fuzs.mindfuldarkness.client.core;

import fuzs.puzzleslib.util.PuzzlesUtil;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = PuzzlesUtil.loadServiceProvider(ClientAbstractions.class);

    CloseableResourceManager createColorChangingResourceManager(PackType type, CloseableResourceManager resourceManager);
}
