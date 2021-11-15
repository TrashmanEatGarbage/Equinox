package me.eonexe.equinox.features.modules.combat;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;

import me.eonexe.equinox.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class HoleFill extends Module {
    public enum SwitchMode {
        Normal,
        Ghost,
        Require

    }
    public Setting<SwitchMode> switchMode = this.register(new Setting<SwitchMode>("Switch", SwitchMode.Normal));
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(10.0f)));
    public Setting<Float> wallRange = this.register(new Setting<Float>("Wall Range", Float.valueOf(3.0f),  Float.valueOf(1.0f),  Float.valueOf(10.0f)));
    public Setting<Integer> delay = this.register(new Setting<Integer>("Delay",  1, 0, 1000));
    public Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Blocks Per Tick", 1, 1, 10));
    public Setting<Boolean> disableAfter = this.register(new Setting<Boolean>("Disable", true));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    public Setting<Boolean> doubles = this.register(new Setting<Boolean>("Doubles", true));
    public Setting<Boolean> smart = this.register(new Setting<Boolean>("Smart",false));
    public Setting<Float> smartTargetRange = this.register(new Setting<Float>("Target Range", Float.valueOf(5.0f),  Float.valueOf(1.0f),  Float.valueOf(10.0f)));
    public Setting<Float> smartBlockRange = this.register(new Setting<Float>("Smart Block Range", Float.valueOf(1.0f),  Float.valueOf(0.3f),  Float.valueOf(4.0f)));
    public Setting<Boolean> noSelfFill = this.register(new Setting<Boolean>("No Self Fill", false));
    public Setting<Integer> selfDist = this.register(new Setting<Integer>("Self Dist", 1, 0, 3));
    Kami5Timer timeSystem;
    List<HoleUtils.Hole> holes;
    BlockPos render;
    Entity target;


    public HoleFill() { super("Hole Fill", "Kami5 Hole Fill", Category.COMBAT, true, false, false);

        this.timeSystem = new Kami5Timer();

        this.holes = new ArrayList<>();

        this.render = null;
    }




    public void onEnable() {
        super.onEnable();
        this.timeSystem.resetDelay();
    }


    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck())
            return;
        this.target = (Entity)TargetUtils.getTarget(((Number)this.smartTargetRange.getValue()).doubleValue());
        this.timeSystem.setDelay(((Number)this.delay.getValue()).longValue());
        int blocksPlaced = 0;
        if (this.timeSystem.isPassed()) {
            getHoles();
            if (this.holes == null || this.holes.size() == 0) {
                if (((Boolean)this.disableAfter.getValue()).booleanValue()) {
                    setEnabled(false);
                }
                return;
            }
            if (switchMode.equals("Require") &&
                    mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() != Item.getItemFromBlock(Blocks.OBSIDIAN))
                return;
            int oldSlot = mc.player.inventory.currentItem;

            int blockSlot = InventoryUtils.getHotbarItemSlot(Item.getItemFromBlock(Blocks.OBSIDIAN));
            if (blockSlot == -1) {
                return;
            }


            boolean switched = false;

            for (HoleUtils.Hole hole : this.holes) {
                if (!switched) {
                    doSwitch(blockSlot);
                    switched = true;
                }
                doRotate(hole.pos1);
                if (hole.doubleHole) {
                    BlockUtils.placeBlock(hole.pos1, true);
                    BlockUtils.placeBlock(hole.pos2, true);
                } else {
                    BlockUtils.placeBlock(hole.pos1, true);
                }

                this.render = hole.pos1;

                blocksPlaced++;
                if (blocksPlaced >= ((Number)this.blocksPerTick.getValue()).intValue()) {
                    break;
                }
            }
            if (switchMode.equals("Ghost") &&
                    switched) {
                doSwitch(oldSlot);
            }

            this.timeSystem.resetDelay();
        } else {
            if (Kami5RotationUtil.INSTANCE.rotating) {
                Kami5RotationUtil.INSTANCE.resetRotations();
            }
            Kami5RotationUtil.INSTANCE.rotating = false;
        }
    }

    public void getHoles() {
        loadHoles();
    }

    public void loadHoles() {
        this.holes = (List<HoleUtils.Hole>)HoleUtils.getHoles(((Number)this.range.getValue()).doubleValue(), mc.player.getPosition(), ((Boolean)this.doubles.getValue()).booleanValue()).stream().filter(hole -> { boolean isAllowedHole = true; AxisAlignedBB bb = hole.doubleHole ? new AxisAlignedBB(hole.pos1.getX(), hole.pos1.getY(), hole.pos1.getZ(), (hole.pos2.getX() + 1), (hole.pos2.getY() + 1), (hole.pos2.getZ() + 1)) : new AxisAlignedBB(hole.pos1); for (Entity e : mc.world.getEntitiesWithinAABB(Entity.class, bb)) isAllowedHole = false;  return isAllowedHole; }).filter(hole -> { boolean isAllowedSmart = false; if (((Boolean)this.smart.getValue()).booleanValue()) { if (this.target != null && this.target.getDistance(hole.pos1.getX() + 0.5D, (hole.pos1.getY() + 1), hole.pos1.getZ() + 0.5D) < ((Number)this.smartBlockRange.getValue()).doubleValue()) isAllowedSmart = true;  } else { isAllowedSmart = true; }  return isAllowedSmart; }).filter(hole -> { BlockPos pos = hole.pos1.add(0, 1, 0); boolean raytrace = (mc.world.rayTraceBlocks(BlockUtils.getEyesPos(), new Vec3d((Vec3i)pos)) != null); return (!raytrace || mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= ((Number)this.wallRange.getValue()).doubleValue()); }).collect(Collectors.toList());
    }

    public void doSwitch(Item i) {
        if (switchMode.equals("Normal")) {
            InventoryUtils.switchToSlot(i);
        }
        if (switchMode.equals("Ghost")) {
            InventoryUtils.switchToSlotGhost(i);
        }
    }

    public void doSwitch(int i) {
        if (switchMode.equals("Normal")) {
            InventoryUtils.switchToSlot(i);
        }
        if (switchMode.equals("Ghost")) {
            InventoryUtils.switchToSlotGhost(i);
        }
    }

    public void doRotate(BlockPos pos) {
        if (((Boolean)this.rotate.getValue()).booleanValue()) {
            if (!Kami5RotationUtil.INSTANCE.rotating) {
                Kami5RotationUtil.INSTANCE.rotating = true;
            }
            Kami5RotationUtil.INSTANCE.rotate(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
        }
    }







    public void onDisable() {
        super.onDisable();
    }

}
