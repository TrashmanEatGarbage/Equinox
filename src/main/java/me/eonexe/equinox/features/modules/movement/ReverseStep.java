package me.eonexe.equinox.features.modules.movement;

import me.eonexe.equinox.event.events.MoveEvent;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.NullUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ReverseStep
        extends Module {
    private final Setting<Integer> force = this.register(new Setting<Integer>("Force", 5, 3, 20));
    //Value<String> mode = new ValueBuilder().withDescriptor("Mode").withValue("Normal").withModes("Normal", "Bypass", "MoveEventCancel").register(this);

    public ReverseStep() {
        super("ReverseStep", "yes", Module.Category.MOVEMENT, true, false, false);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isInWater() || ReverseStep.mc.player.isOnLadder()) {
            return;
        }
        if (/*(this.mode.getValue().equals("Normal") || this.mode.getValue().equals("MoveEventCancel")) && */ReverseStep.mc.player.onGround) {
            ReverseStep.mc.player.motionY -= this.force.getValue().doubleValue();
        }
    }

    @SubscribeEvent
    public void moveEvent(MoveEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isInWater() || ReverseStep.mc.player.isOnLadder()) {
            return;
        }
        /*if (this.mode.getValue().equals("MoveEventCancel") && ReverseStep2.mc.player.onGround) {
            event.y = 0.0;
        }
        if (this.mode.getValue().equals("Bypass") && ReverseStep2.mc.player.onGround && event.y < 0.1) {
            event.y = -this.force.getValue().doubleValue();
            event.moved = true;
        }

         */
    }

    //@Override
    //public String getHudInfo() {
        //return this.mode.getValue();
    //}
}

