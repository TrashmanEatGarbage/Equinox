package me.eonexe.equinox.event.events;

import me.eonexe.equinox.event.EventStage;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PacketEvent
        extends EventStage {
    private final Packet<?> packet;
    public Time time;

    public PacketEvent(int stage, Packet<?> packet) {
        super(stage);
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) this.packet;
    }

    public PacketEvent.Time getTime() { return this.time;
    }


    @Cancelable
    public static class Send
            extends PacketEvent {
        public Send(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }

    @Cancelable
    public static class Receive
            extends PacketEvent {
        public Receive(int stage, Packet<?> packet) {
            super(stage, packet);
        }
    }
    public enum Time {
        Send,
        Receive
    }
}


