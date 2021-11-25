package me.eonexe.equinox.event.events;

import me.eonexe.equinox.event.Event;
import net.minecraft.item.ItemStack;

public class GapEatEvent extends Event {
    public ItemStack stack;

    public GapEatEvent(ItemStack itemStack) {
        this.stack = itemStack;
    }
}
