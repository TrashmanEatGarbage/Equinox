package me.eonexe.equinox.features.modules.client;

import me.eonexe.equinox.features.modules.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;

public class Cape extends Module
{
    private final ResourceLocation capeTexture;
    public ModelPhyscisCapes cape;

    public Cape() {
        super("Cape", "Capes with superior physics.", Category.CLIENT, true, false, false);
        this.capeTexture = new ResourceLocation("textures/cape.png");
        this.cape = new ModelPhyscisCapes();
    }

    @SubscribeEvent
    public void onPlayerRender(final RenderPlayerEvent.Post event) {
        GlStateManager.pushMatrix();
        final float f11 = System.currentTimeMillis() / 1000.0f;
        final HashMap<ModelRenderer, Float> waveMap = new HashMap<ModelRenderer, Float>();
        float fuck = f11;
        for (final ModelRenderer renderer : this.cape.boxList) {
            waveMap.put(renderer, (float)Math.sin(fuck / 0.5) * 4.0f);
            ++fuck;
        }
        final double rotate = this.interpolate(event.getEntityPlayer().prevRenderYawOffset, event.getEntityPlayer().renderYawOffset, event.getPartialRenderTick());
        GlStateManager.translate(0.0f, 0.0f, 0.125f);
        final double d0 = event.getEntityPlayer().prevChasingPosX + (event.getEntityPlayer().chasingPosX - event.getEntityPlayer().prevChasingPosX) * event.getPartialRenderTick() - (event.getEntityPlayer().prevPosX + (event.getEntityPlayer().posX - event.getEntityPlayer().prevPosX) * event.getPartialRenderTick());
        final double d2 = event.getEntityPlayer().prevChasingPosY + (event.getEntityPlayer().chasingPosY - event.getEntityPlayer().prevChasingPosY) * event.getPartialRenderTick() - (event.getEntityPlayer().prevPosY + (event.getEntityPlayer().posY - event.getEntityPlayer().prevPosY) * event.getPartialRenderTick());
        final double d3 = event.getEntityPlayer().prevChasingPosZ + (event.getEntityPlayer().chasingPosZ - event.getEntityPlayer().prevChasingPosZ) * event.getPartialRenderTick() - (event.getEntityPlayer().prevPosZ + (event.getEntityPlayer().posZ - event.getEntityPlayer().prevPosZ) * event.getPartialRenderTick());
        final float f12 = event.getEntityPlayer().prevRenderYawOffset + (event.getEntityPlayer().renderYawOffset - event.getEntityPlayer().prevRenderYawOffset) * event.getPartialRenderTick();
        final double d4 = MathHelper.sin(f12 * 0.017453292f);
        final double d5 = -MathHelper.cos(f12 * 0.017453292f);
        float f13 = (float)d2 * 10.0f;
        f13 = MathHelper.clamp(f13, -6.0f, 32.0f);
        float f14 = (float)(d0 * d4 + d3 * d5) * 100.0f;
        final float f15 = (float)(d0 * d5 - d3 * d4) * 100.0f;
        if (f14 < 0.0f) {
            f14 = 0.0f;
        }
        final float f16 = event.getEntityPlayer().prevCameraYaw + (event.getEntityPlayer().cameraYaw - event.getEntityPlayer().prevCameraYaw) * event.getPartialRenderTick();
        f13 += MathHelper.sin((event.getEntityPlayer().prevDistanceWalkedModified + (event.getEntityPlayer().distanceWalkedModified - event.getEntityPlayer().prevDistanceWalkedModified) * event.getPartialRenderTick()) * 6.0f) * 32.0f * f16;
        if (event.getEntityPlayer().isSneaking()) {
            f13 += 25.0f;
        }
        GL11.glRotated(-rotate, 0.0, 1.0, 0.0);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GL11.glTranslated(0.0, -(event.getEntityPlayer().height - (event.getEntityPlayer().isSneaking() ? 0.25 : 0.0) - 0.38), 0.0);
        GlStateManager.rotate(6.0f + f14 / 2.0f + f13, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f15 / 2.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-f15 / 2.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        if (Cape.mc.player.moveForward != 0.0f || Cape.mc.player.moveStrafing != 0.0f) {
            for (final ModelRenderer renderer2 : this.cape.boxList) {
                renderer2.rotateAngleX = waveMap.get(renderer2);
            }
        }
        else {
            for (final ModelRenderer renderer2 : this.cape.boxList) {
                renderer2.rotateAngleX = 0.0f;
            }
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.capeTexture);
        this.cape.render(event.getEntity(), 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        Minecraft.getMinecraft().getTextureManager().deleteTexture(this.capeTexture);
        GlStateManager.popMatrix();
    }

    public float interpolate(final float yaw1, final float yaw2, final float percent) {
        float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (rotation < 0.0f) {
            rotation += 360.0f;
        }
        return rotation;
    }

    public static class ModelPhyscisCapes extends ModelBase
    {
        public ModelRenderer shape1;
        public ModelRenderer shape2;
        public ModelRenderer shape3;
        public ModelRenderer shape4;
        public ModelRenderer shape5;
        public ModelRenderer shape6;
        public ModelRenderer shape7;
        public ModelRenderer shape8;
        public ModelRenderer shape9;
        public ModelRenderer shape10;
        public ModelRenderer shape11;
        public ModelRenderer shape12;
        public ModelRenderer shape13;
        public ModelRenderer shape14;
        public ModelRenderer shape15;
        public ModelRenderer shape16;

        public ModelPhyscisCapes() {
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.shape9 = new ModelRenderer((ModelBase)this, 0, 8)).setRotationPoint(-5.0f, 8.0f, -1.0f);
            this.shape9.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape15 = new ModelRenderer((ModelBase)this, 0, 14)).setRotationPoint(-5.0f, 14.0f, -1.0f);
            this.shape15.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape3 = new ModelRenderer((ModelBase)this, 0, 2)).setRotationPoint(-5.0f, 2.0f, -1.0f);
            this.shape3.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape7 = new ModelRenderer((ModelBase)this, 0, 6)).setRotationPoint(-5.0f, 6.0f, -1.0f);
            this.shape7.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape1 = new ModelRenderer((ModelBase)this, 0, 0)).setRotationPoint(-5.0f, 0.0f, -1.0f);
            this.shape1.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape6 = new ModelRenderer((ModelBase)this, 0, 5)).setRotationPoint(-5.0f, 5.0f, -1.0f);
            this.shape6.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape14 = new ModelRenderer((ModelBase)this, 0, 13)).setRotationPoint(-5.0f, 13.0f, -1.0f);
            this.shape14.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape10 = new ModelRenderer((ModelBase)this, 0, 9)).setRotationPoint(-5.0f, 9.0f, -1.0f);
            this.shape10.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape13 = new ModelRenderer((ModelBase)this, 0, 12)).setRotationPoint(-5.0f, 12.0f, -1.0f);
            this.shape13.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape4 = new ModelRenderer((ModelBase)this, 0, 3)).setRotationPoint(-5.0f, 3.0f, -1.0f);
            this.shape4.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape8 = new ModelRenderer((ModelBase)this, 0, 7)).setRotationPoint(-5.0f, 7.0f, -1.0f);
            this.shape8.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape16 = new ModelRenderer((ModelBase)this, 0, 15)).setRotationPoint(-5.0f, 15.0f, -1.0f);
            this.shape16.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape12 = new ModelRenderer((ModelBase)this, 0, 11)).setRotationPoint(-5.0f, 11.0f, -1.0f);
            this.shape12.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape5 = new ModelRenderer((ModelBase)this, 0, 4)).setRotationPoint(-5.0f, 4.0f, -1.0f);
            this.shape5.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape11 = new ModelRenderer((ModelBase)this, 0, 10)).setRotationPoint(-5.0f, 10.0f, -1.0f);
            this.shape11.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            (this.shape2 = new ModelRenderer((ModelBase)this, 0, 1)).setRotationPoint(-5.0f, 1.0f, -1.0f);
            this.shape2.addBox(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.boxList.add(this.shape1);
            this.boxList.add(this.shape2);
            this.boxList.add(this.shape3);
            this.boxList.add(this.shape4);
            this.boxList.add(this.shape5);
            this.boxList.add(this.shape6);
            this.boxList.add(this.shape7);
            this.boxList.add(this.shape8);
            this.boxList.add(this.shape9);
            this.boxList.add(this.shape10);
            this.boxList.add(this.shape11);
            this.boxList.add(this.shape12);
            this.boxList.add(this.shape13);
            this.boxList.add(this.shape14);
            this.boxList.add(this.shape15);
            this.boxList.add(this.shape16);
        }

        public void render(final Entity entity, final float f, final float f1, final float f2, final float f3, final float f4, final float f5) {
            this.shape9.render(f5);
            this.shape15.render(f5);
            this.shape3.render(f5);
            this.shape7.render(f5);
            this.shape1.render(f5);
            this.shape6.render(f5);
            this.shape14.render(f5);
            this.shape10.render(f5);
            this.shape13.render(f5);
            this.shape4.render(f5);
            this.shape8.render(f5);
            this.shape16.render(f5);
            this.shape12.render(f5);
            this.shape5.render(f5);
            this.shape11.render(f5);
            this.shape2.render(f5);
        }

        public void setRotateAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
            modelRenderer.rotateAngleX = x;
            modelRenderer.rotateAngleY = y;
            modelRenderer.rotateAngleZ = z;
        }
    }
}