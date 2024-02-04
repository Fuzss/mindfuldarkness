package fuzs.mindfuldarkness.mixin.client.accessor;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuAccessor {

    @Nullable
    @Accessor("menuType")
    MenuType<?> mindfuldarkness$getMenuType();
}
