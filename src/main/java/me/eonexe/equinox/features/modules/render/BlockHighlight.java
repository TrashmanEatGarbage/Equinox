package me.eonexe.equinox.features.modules.render;

import me.eonexe.equinox.event.events.Render3DEvent;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.modules.client.ClickGui;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.ColorUtil;
import me.eonexe.equinox.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public class BlockHighlight
        extends Module {
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Integer> cAlpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));

    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            RenderUtil.drawBlockOutline(blockpos, ClickGui.getInstance().rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), false);
        }
    }
}

