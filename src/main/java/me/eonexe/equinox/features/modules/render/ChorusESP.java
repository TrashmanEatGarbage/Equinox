package me.eonexe.equinox.features.modules.render;

import me.eonexe.equinox.features.modules.*;
import me.eonexe.equinox.features.setting.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.eonexe.equinox.event.events.*;
import net.minecraft.util.math.*;
import java.awt.*;
import me.eonexe.equinox.util.*;

public class ChorusESP extends Module
{
    private final Setting<Integer> time;
    private final Setting<Boolean> box;
    private final Setting<Boolean> outline;
    private final Setting<Integer> boxR;
    private final Setting<Integer> boxG;
    private final Setting<Integer> boxB;
    private final Setting<Integer> boxA;
    private final Setting<Float> lineWidth;
    private final Setting<Integer> outlineR;
    private final Setting<Integer> outlineG;
    private final Setting<Integer> outlineB;
    private final Setting<Integer> outlineA;
    private final TimerUtil timer;
    private double x;
    private double y;
    private double z;

    public ChorusESP() {
        super("ChorusESP", "Renders a chorus sound packet.", Module.Category.RENDER, true, false, false);
        this.time = (Setting<Integer>)this.register(new Setting("Duration", 500, 50, 3000));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", true));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true));
        this.boxR = (Setting<Integer>)this.register(new Setting("BoxR", 180, 0, 255, v -> this.box.getValue()));
        this.boxG = (Setting<Integer>)this.register(new Setting("BoxG", 0, 0, 255, v -> this.box.getValue()));
        this.boxB = (Setting<Integer>)this.register(new Setting("BoxB", 180, 0, 255, v -> this.box.getValue()));
        this.boxA = (Setting<Integer>)this.register(new Setting("BoxA", 180, 0, 255, v -> this.box.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 5.0f, v -> this.outline.getValue()));
        this.outlineR = (Setting<Integer>)this.register(new Setting("OutlineR", 255, 0, 255, v -> this.outline.getValue()));
        this.outlineG = (Setting<Integer>)this.register(new Setting("OutlineG", 0, 0, 255, v -> this.outline.getValue()));
        this.outlineB = (Setting<Integer>)this.register(new Setting("OutlineB", 255, 0, 255, v -> this.outline.getValue()));
        this.outlineA = (Setting<Integer>)this.register(new Setting("OutlineA", 255, 0, 255, v -> this.outline.getValue()));
        this.timer = new TimerUtil();
    }

    @SubscribeEvent
    public void onChorus(final ChorusEvent event) {
        this.x = event.getChorusX();
        this.y = event.getChorusY();
        this.z = event.getChorusZ();
        this.timer.reset();
    }

    public void onRender3D(final Render3DEvent render3DEvent) {
        if (this.timer.passedMs(this.time.getValue())) {
            return;
        }
        final AxisAlignedBB pos = RenderUtil.interpolateAxis(new AxisAlignedBB(this.x - 0.3, this.y, this.z - 0.3, this.x + 0.3, this.y + 1.8, this.z + 0.3));
        if (this.outline.getValue()) {
            RenderUtil.drawBlockOutline(pos, new Color(this.outlineR.getValue(), this.outlineG.getValue(), this.outlineB.getValue(), this.outlineA.getValue()), this.lineWidth.getValue());
        }
        if (this.box.getValue()) {
            RenderUtil.drawFilledBox(pos, ColorUtil.toRGBA(this.boxR.getValue(), this.boxG.getValue(), this.boxB.getValue(), this.boxA.getValue()));
        }
    }
}