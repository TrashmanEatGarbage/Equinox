/*
 * Decompiled with CFR 0.151.
 */
package me.eonexe.equinox.event.events;


import me.eonexe.equinox.event.Event;

public class EventRender3D
extends Event {
    public float partialTicks;

    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public void setPartialTicks(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    public float getPartialTicks() {
        return this.partialTicks;
    }
}

