package fuzs.mindfuldarkness.client.packs.resources;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public record ForwardingResource(Resource resource, InputStream inputStream) implements Resource {

    @Override
    public ResourceLocation getLocation() {
        return this.resource.getLocation();
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override
    public boolean hasMetadata() {
        return this.resource.hasMetadata();
    }

    @Nullable
    @Override
    public <T> T getMetadata(MetadataSectionSerializer<T> serializer) {
        return this.resource.getMetadata(serializer);
    }

    @Override
    public String getSourceName() {
        return this.resource.getSourceName();
    }

    @Override
    public void close() throws IOException {
        this.resource.close();
    }
}
