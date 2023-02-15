package fuzs.mindfuldarkness.server.packs.resources;

import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

class ColorChangingResource extends Resource {
    private final Resource resource;

    public ColorChangingResource(Resource resource, IoSupplier<InputStream> streamSupplier) {
        super(resource.sourcePackId(), streamSupplier, null);
        this.resource = resource;
    }

    @Override
    public String sourcePackId() {
        return this.resource.sourcePackId();
    }

    @Override
    public BufferedReader openAsReader() throws IOException {
        return this.resource.openAsReader();
    }

    @Override
    public ResourceMetadata metadata() throws IOException {
        return this.resource.metadata();
    }
}
