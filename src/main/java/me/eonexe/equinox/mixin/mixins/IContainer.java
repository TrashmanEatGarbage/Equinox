package me.eonexe.equinox.mixin.mixins;

import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Container.class})
public interface IContainer {
    @Accessor(value="transactionID")
    public void setTransactionID(short var1);
}