package me.eonexe.equinox.features.modules.client;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.Util;
import net.minecraft.client.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;

public class GUIBlur extends Module
{
    final Minecraft mc;

    private final Setting<Boolean> ClickGuiOnly = this.register (new Setting<Boolean>("ClickguiOnly",true));

    public GUIBlur() {
        super("GUIBlur", "nigga", Category.CLIENT, true, false, false);
        this.mc = Minecraft.getMinecraft();
    }

    public void onDisable() {
        if (this.mc.world != null) {
            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    public void onUpdate() {
        if (this.mc.world != null) {
            if(ClickGuiOnly.getValue()){
                if (ClickGui.getInstance().isEnabled()) {
                    if (OpenGlHelper.shadersSupported && Util.mc.getRenderViewEntity() instanceof EntityPlayer) {
                        if (Util.mc.entityRenderer.getShaderGroup() != null) {
                            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                        }
                        try {
                            Util.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if (Util.mc.entityRenderer.getShaderGroup() != null && Util.mc.currentScreen == null) {
                        Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                    }
                }
                else if (Util.mc.entityRenderer.getShaderGroup() != null) {
                    Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            }else if (!ClickGuiOnly.getValue()){
                if(ClickGui.getInstance().isEnabled() || this.mc.currentScreen instanceof GuiContainer || this.mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiConfirmOpenLink || this.mc.currentScreen instanceof GuiEditSign || this.mc.currentScreen instanceof GuiGameOver || this.mc.currentScreen instanceof GuiOptions || this.mc.currentScreen instanceof GuiIngameMenu || this.mc.currentScreen instanceof GuiVideoSettings || this.mc.currentScreen instanceof GuiScreenOptionsSounds || this.mc.currentScreen instanceof GuiControls || this.mc.currentScreen instanceof GuiCustomizeSkin || this.mc.currentScreen instanceof GuiModList) {
                    if (ClickGui.getInstance().isEnabled()) {
                        if (OpenGlHelper.shadersSupported && Util.mc.getRenderViewEntity() instanceof EntityPlayer) {
                            if (Util.mc.entityRenderer.getShaderGroup() != null) {
                                Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                            }
                            try {
                                Util.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (Util.mc.entityRenderer.getShaderGroup() != null && Util.mc.currentScreen == null) {
                            Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                        }
                    } else if (Util.mc.entityRenderer.getShaderGroup() != null) {
                        Util.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                    }
                }
            }
        }

    }
}