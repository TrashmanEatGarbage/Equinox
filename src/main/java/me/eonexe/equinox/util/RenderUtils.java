package me.eonexe.equinox.util;

import java.awt.Color;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public class RenderUtils
        implements IMinecraft {
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder builder = tessellator.getBuffer();

    public static float getInterpolatedLinWid(float distance, float line, float lineFactor) {
        return line * lineFactor / distance;
    }

    public static void renderBB(int glMode, AxisAlignedBB bb, Color bottom, Color top) {
        GL11.glShadeModel((int)7425);
        bb = RenderUtils.updateToCamera(bb);
        RenderUtils.prepare();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        builder = tessellator.getBuffer();
        builder.begin(glMode, DefaultVertexFormats.POSITION_COLOR);
        RenderUtils.buildBBBuffer(builder, bb, bottom, top);
        tessellator.draw();
        RenderUtils.release();
        GL11.glShadeModel((int)7424);
    }

    public static void renderBBFog(AxisAlignedBB bb, Color main, Color center) {
        GL11.glShadeModel((int)7425);
        bb = RenderUtils.updateToCamera(bb);
        RenderUtils.prepare();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        builder = tessellator.getBuffer();
        builder.begin(4, DefaultVertexFormats.POSITION_COLOR);
        RenderUtils.buildBBBufferFog(builder, bb, main, center);
        tessellator.draw();
        RenderUtils.release();
        GL11.glShadeModel((int)7424);
    }

    public static void buildBBBuffer(BufferBuilder builder, AxisAlignedBB bb, Color bottom, Color top) {
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.maxZ, top);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.maxZ, top);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, bottom);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.minZ, top);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.maxY, bb.maxZ, top);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.maxY, bb.maxZ, top);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.maxY, bb.minZ, top);
    }

    public static void buildBBBufferFog(BufferBuilder builder, AxisAlignedBB bb, Color main, Color center) {
        double centerX = (bb.maxX - bb.minX) / 2.0;
        double centerY = (bb.maxY - bb.minY) / 2.0;
        double centerZ = (bb.maxZ - bb.minZ) / 2.0;
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, main);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, main);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, main);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, main);
        RenderUtils.addBuilderVertex(builder, bb.minX + centerX, bb.maxY, bb.minZ + centerZ, center);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, main);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.minZ, main);
        RenderUtils.addBuilderVertex(builder, bb.minX + centerX, bb.maxY, bb.minZ + centerZ, center);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, main);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, main);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, main);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, main);
        RenderUtils.addBuilderVertex(builder, bb.maxX, bb.minY, bb.maxZ, main);
        RenderUtils.addBuilderVertex(builder, bb.minX + centerX, bb.maxY, bb.minZ + centerZ, center);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, main);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.maxZ, main);
        RenderUtils.addBuilderVertex(builder, bb.minX + centerX, bb.maxY, bb.minZ + centerZ, center);
        RenderUtils.addBuilderVertex(builder, bb.minX, bb.minY, bb.minZ, main);
    }

    public static void addBuilderVertex(BufferBuilder bufferBuilder, double x, double y, double z, Color color) {
        bufferBuilder.pos(x, y, z).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f).endVertex();
    }

    public static void prepare() {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
    }

    public static void release() {
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glEnable((int)3553);
        GL11.glPolygonMode((int)1032, (int)6914);
    }

    public static AxisAlignedBB updateToCamera(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - RenderUtils.mc.getRenderManager().viewerPosX, bb.minY - RenderUtils.mc.getRenderManager().viewerPosY, bb.minZ - RenderUtils.mc.getRenderManager().viewerPosZ, bb.maxX - RenderUtils.mc.getRenderManager().viewerPosX, bb.maxY - RenderUtils.mc.getRenderManager().viewerPosY, bb.maxZ - RenderUtils.mc.getRenderManager().viewerPosZ);
    }

    public static Vec3d updateToCamera(Vec3d vec) {
        return new Vec3d(vec.x - RenderUtils.mc.getRenderManager().viewerPosX, vec.y - RenderUtils.mc.getRenderManager().viewerPosY, vec.z - RenderUtils.mc.getRenderManager().viewerPosZ);
    }

    public static void renderEntity(EntityLivingBase entity, ModelBase modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (mc.getRenderManager() == null) {
            return;
        }
        if (modelBase instanceof ModelPlayer) {
            ModelPlayer modelPlayer = (ModelPlayer)modelBase;
            modelPlayer.bipedHeadwear.showModel = false;
            modelPlayer.bipedBodyWear.showModel = false;
            modelPlayer.bipedLeftLegwear.showModel = false;
            modelPlayer.bipedRightLegwear.showModel = false;
            modelPlayer.bipedLeftArmwear.showModel = false;
            modelPlayer.bipedRightArmwear.showModel = false;
        }
        float partialTicks = mc.getRenderPartialTicks();
        double x = entity.posX - RenderUtils.mc.getRenderManager().viewerPosX;
        double y = entity.posY - RenderUtils.mc.getRenderManager().viewerPosY;
        double z = entity.posZ - RenderUtils.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        RenderUtils.renderLivingAt(x, y, z);
        RenderUtils.prepareRotations(entity);
        float f4 = RenderUtils.prepareScale(entity, scale);
        float yaw = RenderUtils.handleRotationFloat(entity, partialTicks);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, 0.0f, yaw, entity.rotationPitch, f4, (Entity)entity);
        modelBase.render((Entity)entity, limbSwing, limbSwingAmount, 0.0f, yaw, entity.rotationPitch, f4);
        GlStateManager.popMatrix();
    }

    public static void prepareTranslate(EntityLivingBase entityIn, double x, double y, double z) {
        RenderUtils.renderLivingAt(x - RenderUtils.mc.getRenderManager().viewerPosX, y - RenderUtils.mc.getRenderManager().viewerPosY, z - RenderUtils.mc.getRenderManager().viewerPosZ);
    }

    public static void renderLivingAt(double x, double y, double z) {
        GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
    }

    public static float prepareScale(EntityLivingBase entity, float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale((float)-1.0f, (float)-1.0f, (float)1.0f);
        double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale((double)((double)scale + widthX), (double)(scale * entity.height), (double)((double)scale + widthZ));
        float f = 0.0625f;
        GlStateManager.translate((float)0.0f, (float)-1.501f, (float)0.0f);
        return f;
    }

    public static void prepareRotations(EntityLivingBase entityLivingBase) {
        GlStateManager.rotate((float)(180.0f - entityLivingBase.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
    }

    public static float roundAngle(float f) {
        while (f >= 360.0f) {
            f -= 360.0f;
        }
        return f;
    }

    public static float handleRotationFloat(EntityLivingBase livingBase, float partialTicks) {
        return livingBase.rotationYawHead;
    }

    public static void applyRotations(EntityLivingBase entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        GlStateManager.rotate((float)(180.0f - rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            if ((f = MathHelper.sqrt((float)f)) > 1.0f) {
                f = 1.0f;
            }
            GlStateManager.rotate((float)f, (float)0.0f, (float)0.0f, (float)1.0f);
        } else {
            String s = TextFormatting.getTextWithoutFormattingCodes((String)entityLiving.getName());
            if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer)entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate((float)0.0f, (float)(entityLiving.height + 0.1f), (float)0.0f);
                GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
        }
    }
}

