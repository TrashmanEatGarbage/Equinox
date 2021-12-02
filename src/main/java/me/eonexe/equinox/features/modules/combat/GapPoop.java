package me.eonexe.equinox.features.modules.combat;


import me.eonexe.equinox.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import me.eonexe.equinox.event.events.GapEatEvent;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.util.NullUtils;
import me.eonexe.equinox.util.Timer;
import me.eonexe.equinox.util.InventoryUtils;


public class GapPoop
        extends Module {
    Timer switchBackTimer = new Timer();

    public Setting <mode> Mode = this.register ( new Setting < Object > ( "Mode" , mode.Packet ));
    public Setting <Integer> delay = this.register (new Setting<Object>("Delay",0 , 0 , 100));

    int oldSlot = -1;

    public GapPoop(){
        super("GapPoop", "gapPoop", Category.COMBAT,true,false,false);
        this.switchBackTimer.setDelay((delay.getValue()).longValue()); }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.oldSlot != -1 && this.switchBackTimer.isPassed()) {
            if (Mode.equals("Packet")) {
                InventoryUtils.switchToSlotGhost(this.oldSlot);
                this.switchBackTimer.resetDelay();
                this.switchBackTimer.setPaused(true);
            }
            if (Mode.equals("Normal")) {
                InventoryUtils.switchToSlot(this.oldSlot);
                this.switchBackTimer.resetDelay();
                this.switchBackTimer.setPaused(true);
            }
        }
    }

    @SubscribeEvent
    public void onEat(GapEatEvent event) {
        if (Mode.equals("Instant")) {
            InventoryUtils.switchToSlotGhost(GapPoop.mc.player.inventory.currentItem);
        }
        if (Mode.equals("Packet")) {
            this.oldSlot = GapPoop.mc.player.inventory.currentItem;
            InventoryUtils.switchToSlotGhost(this.getSlot());
            this.switchBackTimer.resetDelay();
            this.switchBackTimer.setPaused(false);
            if (this.delay.getValue().longValue() == 0L) {
                InventoryUtils.switchToSlotGhost(this.oldSlot);
                this.switchBackTimer.setPaused(true);
            }
        }
        if (this.Mode.getValue().equals("Normal")) {
            this.oldSlot = GapPoop.mc.player.inventory.currentItem;
            InventoryUtils.switchToSlot(this.getSlot());
            this.switchBackTimer.resetDelay();
            this.switchBackTimer.setPaused(false);
        }
    }

    int getSlot() {
        if (GapPoop.mc.player.inventory.currentItem == 9) {
            return GapPoop.mc.player.inventory.currentItem - 1;
        }
        return GapPoop.mc.player.inventory.currentItem + 1;
    }

    public enum  mode{
        Instant,
        Packet,
        Normal
    }
}
