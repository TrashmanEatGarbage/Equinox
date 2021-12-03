package me.eonexe.equinox.features.modules.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;

import me.eonexe.equinox.event.events.PacketEvent;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.modules.misc.FakePlayer2;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.ColorUtils;
import me.eonexe.equinox.util.NullUtils;
import me.eonexe.equinox.util.RenderUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PopChams
        extends Module {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private final Setting<Integer> fadeStart = this.register(new Setting<Integer>("Fade Start", 100, 0,4000));
    private final Setting<Integer> fadeTime = this.register(new Setting<Integer>("Fade Time", 500, 0, 2000));
    private final Setting<Boolean> self = this.register(new Setting<Boolean>("Self", true));
    private final Setting<Integer> fillRed = this.register(new Setting<Integer>("Red Fill", 15, 0, 255));
    private final Setting<Integer> fillGreen = this.register(new Setting<Integer>("Green Fill", 100, 0, 255));
    private final Setting<Integer> fillBlue = this.register(new Setting<Integer>("Blue Fill", 255, 0, 255));
    private final Setting<Integer> fillAlpha = this.register(new Setting<Integer>("Alpha Fill", 100, 0, 255));
    private final Setting<Integer> lineRed = this.register(new Setting<Integer>("Red Line", 15, 0, 255));
    private final Setting<Integer> lineGreen = this.register(new Setting<Integer>("Green Line", 100, 0, 255));
    private final Setting<Integer> lineBlue = this.register(new Setting<Integer>("Blue Line", 255, 0, 255));
    private final Setting<Integer> lineAlpha = this.register(new Setting<Integer>("Alpha Line", 255, 0, 255));
    private final Setting<Boolean> glint = this.register(new Setting<Boolean>("Glint", false));
    private final Setting<Integer> glintRed = this.register(new Setting<Integer>("Red Glint", 15, 0, 255));
    private final Setting<Integer> glintGreen = this.register(new Setting<Integer>("Green Glint", 100, 0, 255));
    private final Setting<Integer> glintBlue = this.register(new Setting<Integer>("Blue Glint", 255, 0, 255));
    private final Setting<Integer> glintAlpha = this.register(new Setting<Integer>("Alpha Glint", 100, 0, 255));
    EntityOtherPlayerMP player = null;
    ModelPlayer playerModel = null;
    long startTime = 0L;

    public PopChams() {
        super("PopChams", "", Module.Category.RENDER, true, false, false);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
        SPacketEntityStatus packet;
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 35 && packet.getEntity((World)PopChams.mc.world) != null && (this.self.getValue().booleanValue() || packet.getEntity((World)PopChams.mc.world).getEntityId() != PopChams.mc.player.getEntityId())) {
            GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
            this.player = new EntityOtherPlayerMP((World) FakePlayer2.mc.world, profile);
            this.player.copyLocationAndAnglesFrom(packet.getEntity((World)PopChams.mc.world));
            this.playerModel = new ModelPlayer(0.0f, false);
            this.startTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        GL11.glLineWidth((float)1.0f);
        int lineA = this.lineAlpha.getValue();
        int fillA = this.fillAlpha.getValue();
        int glintA = this.glintAlpha.getValue();
        if (System.currentTimeMillis() - this.startTime > this.fadeStart.getValue().longValue()) {
            long time = System.currentTimeMillis() - this.startTime - this.fadeStart.getValue().longValue();
            double normal = this.normalize(time, 0.0, this.fadeTime.getValue().doubleValue());
            normal = MathHelper.clamp((double)normal, (double)0.0, (double)1.0);
            normal = -normal + 1.0;
            lineA = (int)(normal * (double)lineA);
            fillA = (int)(normal * (double)fillA);
            glintA = (int)(normal * (double)glintA);
        }
        Color lineColor = new Color(this.lineRed.getValue(), this.lineGreen.getValue(), this.lineBlue.getValue(), lineA);
        Color fillColor = new Color(this.fillRed.getValue(), this.fillGreen.getValue(), this.fillBlue.getValue(), fillA);
        Color finalGlintColor = new Color(this.glintRed.getValue(), this.glintGreen.getValue(), this.glintBlue.getValue(), glintA);
        if (this.player != null && this.playerModel != null) {
            RenderUtils.prepare();
            ColorUtils.glColor(fillColor);
            GL11.glPolygonMode((int)1032, (int)6914);
            RenderUtils.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            if (this.glint.getValue().booleanValue()) {
                PopChams.mc.getRenderManager().renderEngine.bindTexture(RES_ITEM_GLINT);
                GL11.glTexCoord3d((double)1.0, (double)1.0, (double)1.0);
                GL11.glEnable((int)3553);
                GL11.glBlendFunc((int)768, (int)771);
                ColorUtils.glColor(finalGlintColor);
                RenderUtils.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
                GL11.glBlendFunc((int)770, (int)771);
            }
            ColorUtils.glColor(lineColor);
            GL11.glPolygonMode((int)1032, (int)6913);
            RenderUtils.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            GL11.glPolygonMode((int)1032, (int)6914);
            RenderUtils.release();
        }
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }



}