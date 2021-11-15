package me.eonexe.equinox.features.modules.render;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class PlayerChams
extends Module {
    public static PlayerChams INSTANCE;
    Setting<Sets> page = this.register(new Setting<Sets>("Page", Sets.OTHER));
    public final Setting<Boolean> chams = this.register(new Setting<Boolean>("Chams", Boolean.valueOf(false), v -> this.page.getValue() == Sets.OTHER));
    public final Setting<Boolean> wireframe = this.register(new Setting<Boolean>("Wireframe", Boolean.valueOf(false), v -> this.page.getValue() == Sets.OTHER));
    public final Setting<Float> scale = this.register(new Setting<Float>("Scale", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(1.1f), v -> this.page.getValue() == Sets.OTHER));
    public final Setting<Float> lineWidth = this.register(new Setting<Float>("Linewidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f), v -> this.page.getValue() == Sets.OTHER));
    public final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Sets.COLOR));
    public final Setting<Integer> visibleRed = this.register(new Setting<Integer>("Visible Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Sets.COLOR));
    public final Setting<Integer> visibleGreen = this.register(new Setting<Integer>("Visible Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Sets.COLOR));
    public final Setting<Integer> visibleBlue = this.register(new Setting<Integer>("Visible Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Sets.COLOR));
    public final Setting<Integer> invisibleRed = this.register(new Setting<Integer>("Invisible Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Sets.COLOR));
    public final Setting<Integer> invisibleGreen = this.register(new Setting<Integer>("Invisible Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Sets.COLOR));
    public final Setting<Integer> invisibleBlue = this.register(new Setting<Integer>("Invisible Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.page.getValue() == Sets.COLOR));

    public PlayerChams() {
        super("PlayerChams", "draws chams", Module.Category.RENDER, true, false, false);
        INSTANCE = this;
    }

    public final void onRenderModel(ModelBase base, Entity entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch, float scale) {
        if (entity instanceof EntityPlayer) {
            return;
        }
        GL11.glPushAttrib((int)1048575);
        GL11.glPolygonMode((int)1032, (int)6913);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        GL11.glColorMaterial((int)1032, (int)5634);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)this.lineWidth.getValue().floatValue());
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)((float)PlayerChams.INSTANCE.invisibleRed.getValue().intValue() / 255.0f), (double)((float)PlayerChams.INSTANCE.invisibleGreen.getValue().intValue() / 255.0f), (double)((float)PlayerChams.INSTANCE.invisibleBlue.getValue().intValue() / 255.0f), (double)((float)PlayerChams.INSTANCE.alpha.getValue().intValue() / 255.0f));
        base.render(entity, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glColor4d((double)((float)PlayerChams.INSTANCE.visibleRed.getValue().intValue() / 255.0f), (double)((float)PlayerChams.INSTANCE.visibleGreen.getValue().intValue() / 255.0f), (double)((float)PlayerChams.INSTANCE.visibleBlue.getValue().intValue() / 255.0f), (double)((float)PlayerChams.INSTANCE.alpha.getValue().intValue() / 255.0f));
        base.render(entity, limbSwing, limbSwingAmount, age, headYaw, headPitch, scale);
        GL11.glEnable((int)3042);
        GL11.glPopAttrib();
    }

    public static enum Sets {
        COLOR,
        OTHER;

    }
}