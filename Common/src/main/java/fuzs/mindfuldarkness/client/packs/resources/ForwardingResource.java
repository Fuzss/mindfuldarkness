package fuzs.mindfuldarkness.client.packs.resources;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class ForwardingResource extends Resource {
    private final Resource resource;

    public ForwardingResource(Resource resource, IoSupplier<InputStream> streamSupplier) {
        super(resource.source(), streamSupplier, () -> ResourceMetadata.EMPTY);
        this.resource = resource;
    }

    @Override
    public PackResources source() {
        return this.resource.source();
    }

    @Override
    public String sourcePackId() {
        return this.resource.sourcePackId();
    }

    @Override
    public boolean isBuiltin() {
        return this.resource.isBuiltin();
    }

    @Override
    public final InputStream open() throws IOException {
        return super.open();
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
