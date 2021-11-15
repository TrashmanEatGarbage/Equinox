package me.eonexe.equinox.features.modules.movement;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class ReverseStepStrict
        extends Module {
    private static ReverseStepStrict INSTANCE = new ReverseStepStrict();
    private final Setting<Boolean> twoBlocks = this.register(new Setting<Boolean>("2Blocks", Boolean.FALSE));

    public ReverseStepStrict() {
        super("ReverseStepStrict", "ReverseStep.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static ReverseStepStrict getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReverseStepStrict();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (ReverseStepStrict.fullNullCheck()) {
            return;
        }
        IBlockState touchingState = ReverseStepStrict.mc.world.getBlockState(new BlockPos(ReverseStepStrict.mc.player.posX, ReverseStepStrict.mc.player.posY, ReverseStepStrict.mc.player.posZ).down(2));
        IBlockState touchingState2 = ReverseStepStrict.mc.world.getBlockState(new BlockPos(ReverseStepStrict.mc.player.posX, ReverseStepStrict.mc.player.posY, ReverseStepStrict.mc.player.posZ).down(3));
        if (ReverseStepStrict.mc.player.isInLava() || ReverseStepStrict.mc.player.isInWater()) {
            return;
        }
        if (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN) {
            if (ReverseStepStrict.mc.player.onGround) {
                ReverseStepStrict.mc.player.motionY -= 1.0;
            }
        } else if ((this.twoBlocks.getValue().booleanValue() && touchingState2.getBlock() == Blocks.BEDROCK || this.twoBlocks.getValue().booleanValue() && touchingState2.getBlock() == Blocks.OBSIDIAN) && ReverseStepStrict.mc.player.onGround) {
            ReverseStepStrict.mc.player.motionY -= 1.0;
        }
    }
}

