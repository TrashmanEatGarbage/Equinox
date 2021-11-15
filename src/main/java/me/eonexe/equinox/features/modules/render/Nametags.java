package me.eonexe.equinox.features.modules.render;

import java.awt.Color;
import java.util.Objects;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.ColorHolder;
import me.eonexe.equinox.util.DamageUtil;
import me.eonexe.equinox.util.EntityUtil;
import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.event.events.Render3DEvent;
import me.eonexe.equinox.features.modules.Module;

public class Nametags
extends Module {
    private static Nametags INSTANCE = new Nametags();
    private final Setting<Boolean> rect = this.register(new Setting<Boolean>("Rectangle", true));
    private final Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", true));
    private final Setting<Boolean> reversed = this.register(new Setting<Object>("ArmorReversed", Boolean.valueOf(false), v -> this.armor.getValue()));
    private final Setting<Boolean> health = this.register(new Setting<Boolean>("Health", true));
    private final Setting<Boolean> ping = this.register(new Setting<Boolean>("Ping", true));
    private final Setting<Boolean> gamemode = this.register(new Setting<Boolean>("Gamemode", false));
    private final Setting<Boolean> entityID = this.register(new Setting<Boolean>("EntityID", false));
    private final Setting<Boolean> heldStackName = this.register(new Setting<Boolean>("StackName", true));
    private final Setting<Boolean> max = this.register(new Setting<Boolean>("Max", true));
    private final Setting<Boolean> maxText = this.register(new Setting<Object>("NoMaxText", Boolean.valueOf(false), v -> this.max.getValue()));
    private final Setting<Integer> Mred = this.register(new Setting<Object>("Max-Red", Integer.valueOf(178), Integer.valueOf(0), Integer.valueOf(255), v -> this.max.getValue()));
    private final Setting<Integer> Mgreen = this.register(new Setting<Object>("Max-Green", Integer.valueOf(52), Integer.valueOf(0), Integer.valueOf(255), v -> this.max.getValue()));
    private final Setting<Integer> Mblue = this.register(new Setting<Object>("Max-Blue", Integer.valueOf(57), Integer.valueOf(0), Integer.valueOf(255), v -> this.max.getValue()));
    private final Setting<Float> size = this.register(new Setting<Float>("Size", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    private final Setting<Boolean> scaleing = this.register(new Setting<Boolean>("Scale", false));
    private final Setting<Boolean> smartScale = this.register(new Setting<Object>("SmartScale", Boolean.valueOf(false), v -> this.scaleing.getValue()));
    private final Setting<Float> factor = this.register(new Setting<Object>("Factor", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), v -> this.scaleing.getValue()));
    private final Setting<Boolean> textcolor = this.register(new Setting<Boolean>("TextColor", true));
    private final Setting<Boolean> NCRainbow = this.register(new Setting<Object>("Text-Rainbow", Boolean.valueOf(false), v -> this.textcolor.getValue()));
    private final Setting<Integer> NCred = this.register(new Setting<Object>("Text-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.textcolor.getValue()));
    private final Setting<Integer> NCgreen = this.register(new Setting<Object>("Text-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.textcolor.getValue()));
    private final Setting<Integer> NCblue = this.register(new Setting<Object>("Text-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.textcolor.getValue()));
    private final Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    private final Setting<Boolean> ORainbow = this.register(new Setting<Object>("Outline-Rainbow", Boolean.valueOf(false), v -> this.outline.getValue()));
    private final Setting<Float> Owidth = this.register(new Setting<Object>("Outline-Width", Float.valueOf(1.3f), Float.valueOf(0.0f), Float.valueOf(5.0f), v -> this.outline.getValue()));
    private final Setting<Integer> Ored = this.register(new Setting<Object>("Outline-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> Ogreen = this.register(new Setting<Object>("Outline-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Integer> Oblue = this.register(new Setting<Object>("Outline-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue()));
    private final Setting<Boolean> friendcolor = this.register(new Setting<Boolean>("FriendColor", true));
    private final Setting<Boolean> FCRainbow = this.register(new Setting<Object>("Friend-Rainbow", Boolean.valueOf(false), v -> this.friendcolor.getValue()));
    private final Setting<Integer> FCred = this.register(new Setting<Object>("Friend-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.friendcolor.getValue()));
    private final Setting<Integer> FCgreen = this.register(new Setting<Object>("Friend-Green", Integer.valueOf(213), Integer.valueOf(0), Integer.valueOf(255), v -> this.friendcolor.getValue()));
    private final Setting<Integer> FCblue = this.register(new Setting<Object>("Friend-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.friendcolor.getValue()));
    private final Setting<Boolean> FORainbow = this.register(new Setting<Object>("FriendOutline-Rainbow", Boolean.valueOf(false), v -> this.outline.getValue() != false && this.friendcolor.getValue() != false));
    private final Setting<Integer> FOred = this.register(new Setting<Object>("FriendOutline-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.friendcolor.getValue() != false));
    private final Setting<Integer> FOgreen = this.register(new Setting<Object>("FriendOutline-Green", Integer.valueOf(213), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.friendcolor.getValue() != false));
    private final Setting<Integer> FOblue = this.register(new Setting<Object>("FriendOutline-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.friendcolor.getValue() != false));
    private final Setting<Boolean> sneakcolor = this.register(new Setting<Boolean>("Sneak", false));
    private final Setting<Boolean> sneak = this.register(new Setting<Object>("EnableSneak", Boolean.valueOf(true), v -> this.sneakcolor.getValue()));
    private final Setting<Boolean> SCRainbow = this.register(new Setting<Object>("Sneak-Rainbow", Boolean.valueOf(false), v -> this.sneakcolor.getValue()));
    private final Setting<Integer> SCred = this.register(new Setting<Object>("Sneak-Red", Integer.valueOf(245), Integer.valueOf(0), Integer.valueOf(255), v -> this.sneakcolor.getValue()));
    private final Setting<Integer> SCgreen = this.register(new Setting<Object>("Sneak-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.sneakcolor.getValue()));
    private final Setting<Integer> SCblue = this.register(new Setting<Object>("Sneak-Blue", Integer.valueOf(122), Integer.valueOf(0), Integer.valueOf(255), v -> this.sneakcolor.getValue()));
    private final Setting<Boolean> SORainbow = this.register(new Setting<Object>("SneakOutline-Rainbow", Boolean.valueOf(false), v -> this.outline.getValue() != false && this.sneakcolor.getValue() != false));
    private final Setting<Integer> SOred = this.register(new Setting<Object>("SneakOutline-Red", Integer.valueOf(245), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.sneakcolor.getValue() != false));
    private final Setting<Integer> SOgreen = this.register(new Setting<Object>("SneakOutline-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.sneakcolor.getValue() != false));
    private final Setting<Integer> SOblue = this.register(new Setting<Object>("SneakOutline-Blue", Integer.valueOf(122), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.sneakcolor.getValue() != false));
    private final Setting<Boolean> invisiblescolor = this.register(new Setting<Boolean>("InvisiblesColor", false));
    private final Setting<Boolean> invisibles = this.register(new Setting<Object>("EnableInvisibles", Boolean.valueOf(true), v -> this.invisiblescolor.getValue()));
    private final Setting<Boolean> ICRainbow = this.register(new Setting<Object>("Invisible-Rainbow", Boolean.valueOf(false), v -> this.invisiblescolor.getValue()));
    private final Setting<Integer> ICred = this.register(new Setting<Object>("Invisible-Red", Integer.valueOf(148), Integer.valueOf(0), Integer.valueOf(255), v -> this.invisiblescolor.getValue()));
    private final Setting<Integer> ICgreen = this.register(new Setting<Object>("Invisible-Green", Integer.valueOf(148), Integer.valueOf(0), Integer.valueOf(255), v -> this.invisiblescolor.getValue()));
    private final Setting<Integer> ICblue = this.register(new Setting<Object>("Invisible-Blue", Integer.valueOf(148), Integer.valueOf(0), Integer.valueOf(255), v -> this.invisiblescolor.getValue()));
    private final Setting<Boolean> IORainbow = this.register(new Setting<Object>("InvisibleOutline-Rainbow", Boolean.valueOf(false), v -> this.outline.getValue() != false && this.invisiblescolor.getValue() != false));
    private final Setting<Integer> IOred = this.register(new Setting<Object>("InvisibleOutline-Red", Integer.valueOf(148), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.invisiblescolor.getValue() != false));
    private final Setting<Integer> IOgreen = this.register(new Setting<Object>("InvisibleOutline-Green", Integer.valueOf(148), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.invisiblescolor.getValue() != false));
    private final Setting<Integer> IOblue = this.register(new Setting<Object>("InvisibleOutline-Blue", Integer.valueOf(148), Integer.valueOf(0), Integer.valueOf(255), v -> this.outline.getValue() != false && this.invisiblescolor.getValue() != false));

    public Nametags() {
        super("Nametags", "Renders info about the player on a NameTag", Module.Category.RENDER, false, false, false);
    }

    public static Nametags getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Nametags();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (EntityPlayer player : Nametags.mc.world.playerEntities) {
            if (player == null || player.equals((Object)Nametags.mc.player) || !player.isEntityAlive() || player.isInvisible() && !this.invisibles.getValue().booleanValue()) continue;
            double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosX;
            double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosY;
            double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosZ;
            this.renderNameTag(player, x, y, z, event.getPartialTicks());
        }
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y;
        tempY += player.isSneaking() ? 0.5 : 0.7;
        Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = this.getDisplayTag(player);
        double distance = camera.getDistance(x + Nametags.mc.getRenderManager().viewerPosX, y + Nametags.mc.getRenderManager().viewerPosY, z + Nametags.mc.getRenderManager().viewerPosZ);
        int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + (double)this.size.getValue().floatValue() * (distance * (double)this.factor.getValue().floatValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue().booleanValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue().booleanValue()) {
            scale = (double)this.size.getValue().floatValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)((float)x), (float)((float)tempY + 1.4f), (float)((float)z));
        GlStateManager.rotate((float)(-Nametags.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)Nametags.mc.getRenderManager().playerViewX, (float)(Nametags.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.rect.getValue().booleanValue()) {
            this.drawRect(-width - 2, -(Nametags.mc.fontRenderer.FONT_HEIGHT + 1), (float)width + 2.0f, 1.5f, 0x55000000);
        }
        if (this.outline.getValue().booleanValue()) {
            this.drawOutlineRect(-width - 2, -(Nametags.mc.fontRenderer.FONT_HEIGHT + 1), (float)width + 2.0f, 1.5f, this.getOutlineColor(player));
        }
        GlStateManager.disableBlend();
        ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect() && (renderMainHand.getItem() instanceof ItemTool || renderMainHand.getItem() instanceof ItemArmor)) {
            renderMainHand.stackSize = 1;
        }
        if (this.heldStackName.getValue().booleanValue() && !renderMainHand.isEmpty && renderMainHand.getItem() != Items.AIR) {
            String stackName = renderMainHand.getDisplayName();
            int stackNameWidth = this.renderer.getStringWidth(stackName) / 2;
            GL11.glPushMatrix();
            GL11.glScalef((float)0.75f, (float)0.75f, (float)0.0f);
            this.renderer.drawStringWithShadow(stackName, -stackNameWidth, -(this.getBiggestArmorTag(player) + 20.0f), -1);
            GL11.glScalef((float)1.5f, (float)1.5f, (float)1.0f);
            GL11.glPopMatrix();
        }
        if (this.armor.getValue().booleanValue()) {
            GlStateManager.pushMatrix();
            int xOffset = -6;
            int count = 0;
            for (ItemStack armourStack : player.inventory.armorInventory) {
                if (armourStack == null) continue;
                xOffset -= 8;
                if (armourStack.getItem() == Items.AIR) continue;
                ++count;
            }
            xOffset -= 8;
            ItemStack renderOffhand = player.getHeldItemOffhand().copy();
            if (renderOffhand.hasEffect() && (renderOffhand.getItem() instanceof ItemTool || renderOffhand.getItem() instanceof ItemArmor)) {
                renderOffhand.stackSize = 1;
            }
            this.renderItemStack(renderOffhand, xOffset, -26);
            xOffset += 16;
            if (this.reversed.getValue().booleanValue()) {
                for (int index = 0; index <= 3; ++index) {
                    ItemStack armourStack = (ItemStack)player.inventory.armorInventory.get(index);
                    if (armourStack == null || armourStack.getItem() == Items.AIR) continue;
                    ItemStack renderStack1 = armourStack.copy();
                    this.renderItemStack(armourStack, xOffset, -26);
                    xOffset += 16;
                }
            } else {
                for (int index = 3; index >= 0; --index) {
                    ItemStack armourStack = (ItemStack)player.inventory.armorInventory.get(index);
                    if (armourStack == null || armourStack.getItem() == Items.AIR) continue;
                    ItemStack renderStack1 = armourStack.copy();
                    this.renderItemStack(armourStack, xOffset, -26);
                    xOffset += 16;
                }
            }
            this.renderItemStack(renderMainHand, xOffset, -26);
            GlStateManager.popMatrix();
        }
        this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), this.getDisplayColor(player));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
        GlStateManager.popMatrix();
    }

    private int getDisplayColor(EntityPlayer player) {
        int displaycolor = ColorHolder.toHex(this.NCred.getValue(), this.NCgreen.getValue(), this.NCblue.getValue());
        if (Equinox.friendManager.isFriend(player)) {
            return ColorHolder.toHex(this.FCred.getValue(), this.FCgreen.getValue(), this.FCblue.getValue());
        }
        if (player.isInvisible() && this.invisibles.getValue().booleanValue()) {
            displaycolor = ColorHolder.toHex(this.ICred.getValue(), this.ICgreen.getValue(), this.ICblue.getValue());
        } else if (player.isSneaking() && this.sneak.getValue().booleanValue()) {
            displaycolor = ColorHolder.toHex(this.SCred.getValue(), this.SCgreen.getValue(), this.SCblue.getValue());
        }
        return displaycolor;
    }

    private int getOutlineColor(EntityPlayer player) {
        int outlinecolor = ColorHolder.toHex(this.Ored.getValue(), this.Ogreen.getValue(), this.Oblue.getValue());
        if (Equinox.friendManager.isFriend(player)) {
            outlinecolor = ColorHolder.toHex(this.FOred.getValue(), this.FOgreen.getValue(), this.FOblue.getValue());
        } else if (player.isInvisible() && this.invisibles.getValue().booleanValue()) {
            outlinecolor = ColorHolder.toHex(this.IOred.getValue(), this.IOgreen.getValue(), this.IOblue.getValue());
        } else if (player.isSneaking() && this.sneak.getValue().booleanValue()) {
            outlinecolor = ColorHolder.toHex(this.SOred.getValue(), this.SOgreen.getValue(), this.SOblue.getValue());
        }
        return outlinecolor;
    }

    private void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.clear((int)256);
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRenderer, stack, x, y);
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x, y);
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchantmentText(ItemStack stack, int x, int y) {
        NBTTagList enchants;
        int enchantmentY = y - 8;
        int yCount = y;
        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
            this.renderer.drawStringWithShadow("god", x * 2, enchantmentY, -3977919);
            enchantmentY -= 8;
        }
        if ((enchants = stack.getEnchantmentTagList()).tagCount() > 2 && this.max.getValue().booleanValue()) {
            if (this.maxText.getValue().booleanValue()) {
                this.renderer.drawStringWithShadow("", x * 2, enchantmentY, ColorHolder.toHex(this.Mred.getValue(), this.Mgreen.getValue(), this.Mblue.getValue()));
                enchantmentY -= 8;
            } else {
                this.renderer.drawStringWithShadow("max", x * 2, enchantmentY, ColorHolder.toHex(this.Mred.getValue(), this.Mgreen.getValue(), this.Mblue.getValue()));
                enchantmentY -= 8;
            }
        } else {
            for (int index = 0; index < enchants.tagCount(); ++index) {
                short id = enchants.getCompoundTagAt(index).getShort("id");
                short level = enchants.getCompoundTagAt(index).getShort("lvl");
                Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                if (enc == null) continue;
                String encName = enc.isCurse() ? TextFormatting.RED + enc.getTranslatedName((int)level).substring(11).substring(0, 1).toLowerCase() : enc.getTranslatedName((int)level).substring(0, 1).toLowerCase();
                encName = encName + level;
                this.renderer.drawStringWithShadow(encName, x * 2, enchantmentY, -1);
                enchantmentY -= 8;
            }
        }
        if (DamageUtil.hasDurability(stack)) {
            int percent = DamageUtil.getRoundedDamage(stack);
            String color = percent >= 60 ? "\u00a7a" : (percent >= 25 ? "\u00a7e" : "\u00a7c");
            this.renderer.drawStringWithShadow(color + percent + "%", x * 2, enchantmentY, -1);
        }
    }

    private float getBiggestArmorTag(EntityPlayer player) {
        ItemStack renderOffHand;
        Enchantment enc;
        int index;
        float enchantmentY = 0.0f;
        boolean arm = false;
        for (ItemStack stack : player.inventory.armorInventory) {
            float encY = 0.0f;
            if (stack != null) {
                NBTTagList enchants = stack.getEnchantmentTagList();
                for (index = 0; index < enchants.tagCount(); ++index) {
                    short id = enchants.getCompoundTagAt(index).getShort("id");
                    enc = Enchantment.getEnchantmentByID((int)id);
                    if (enc == null) continue;
                    encY += 8.0f;
                    arm = true;
                }
            }
            if (!(encY > enchantmentY)) continue;
            enchantmentY = encY;
        }
        ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect()) {
            float encY = 0.0f;
            NBTTagList enchants = renderMainHand.getEnchantmentTagList();
            for (int index2 = 0; index2 < enchants.tagCount(); ++index2) {
                short id = enchants.getCompoundTagAt(index2).getShort("id");
                Enchantment enc2 = Enchantment.getEnchantmentByID((int)id);
                if (enc2 == null) continue;
                encY += 8.0f;
                arm = true;
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        if ((renderOffHand = player.getHeldItemOffhand().copy()).hasEffect()) {
            float encY = 0.0f;
            NBTTagList enchants = renderOffHand.getEnchantmentTagList();
            for (index = 0; index < enchants.tagCount(); ++index) {
                short id = enchants.getCompoundTagAt(index).getShort("id");
                enc = Enchantment.getEnchantmentByID((int)id);
                if (enc == null) continue;
                encY += 8.0f;
                arm = true;
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        return (float)(arm ? 0 : 20) + enchantmentY;
    }

    private String getDisplayTag(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (name.contains(mc.getSession().getUsername())) {
            name = "You";
        }
        if (!this.health.getValue().booleanValue()) {
            return name;
        }
        float health = EntityUtil.getHealth((Entity)player);
        String color = health > 18.0f ? "\u00a7a" : (health > 16.0f ? "\u00a72" : (health > 12.0f ? "\u00a7e" : (health > 8.0f ? "\u00a7c" : (health > 5.0f ? "\u00a74" : "\u00a74"))));
        String pingStr = "";
        if (this.ping.getValue().booleanValue()) {
            try {
                int responseTime = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
                pingStr = pingStr + responseTime + "ms ";
            }
            catch (Exception responseTime) {
                // empty catch block
            }
        }
        String idString = "";
        if (this.entityID.getValue().booleanValue()) {
            idString = idString + "ID: " + player.getEntityId() + " ";
        }
        String gameModeStr = "";
        if (this.gamemode.getValue().booleanValue()) {
            gameModeStr = player.isCreative() ? gameModeStr + "[C] " : (player.isSpectator() || player.isInvisible() ? gameModeStr + "[I] " : gameModeStr + "[S] ");
        }
        name = Math.floor(health) == (double)health ? name + color + " " + (health > 0.0f ? Integer.valueOf((int)Math.floor(health)) : "dead") : name + color + " " + (health > 0.0f ? Integer.valueOf((int)health) : "dead");
        return " " + pingStr + idString + gameModeStr + name + " ";
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }

    public void drawOutlineRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth((float)this.Owidth.getValue().floatValue());
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth((float)this.Owidth.getValue().floatValue());
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public void onUpdate() {
        if (this.outline.getValue().equals(false)) {
            this.rect.setValue(true);
        } else if (this.rect.getValue().equals(false)) {
            this.outline.setValue(true);
        }
        if (this.ORainbow.getValue().booleanValue()) {
            this.OutlineRainbow();
        }
        if (this.NCRainbow.getValue().booleanValue()) {
            this.TextRainbow();
        }
        if (this.FCRainbow.getValue().booleanValue()) {
            this.FriendRainbow();
        }
        if (this.SCRainbow.getValue().booleanValue()) {
            this.SneakColorRainbow();
        }
        if (this.ICRainbow.getValue().booleanValue()) {
            this.InvisibleRainbow();
        }
        if (this.FORainbow.getValue().booleanValue()) {
            this.FriendOutlineRainbow();
        }
        if (this.IORainbow.getValue().booleanValue()) {
            this.InvisibleOutlineRainbow();
        }
        if (this.SORainbow.getValue().booleanValue()) {
            this.SneakOutlineRainbow();
        }
    }

    public void OutlineRainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.Ored.setValue(color_rgb_o >> 16 & 0xFF);
        this.Ogreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.Oblue.setValue(color_rgb_o & 0xFF);
    }

    public void TextRainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.NCred.setValue(color_rgb_o >> 16 & 0xFF);
        this.NCgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.NCblue.setValue(color_rgb_o & 0xFF);
    }

    public void FriendRainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.FCred.setValue(color_rgb_o >> 16 & 0xFF);
        this.FCgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.FCblue.setValue(color_rgb_o & 0xFF);
    }

    public void SneakColorRainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.SCred.setValue(color_rgb_o >> 16 & 0xFF);
        this.SCgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.SCblue.setValue(color_rgb_o & 0xFF);
    }

    public void InvisibleRainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.ICred.setValue(color_rgb_o >> 16 & 0xFF);
        this.ICgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.ICblue.setValue(color_rgb_o & 0xFF);
    }

    public void InvisibleOutlineRainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.IOred.setValue(color_rgb_o >> 16 & 0xFF);
        this.IOgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.IOblue.setValue(color_rgb_o & 0xFF);
    }

    public void FriendOutlineRainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.FOred.setValue(color_rgb_o >> 16 & 0xFF);
        this.FOgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.FOblue.setValue(color_rgb_o & 0xFF);
    }

    public void SneakOutlineRainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.SOred.setValue(color_rgb_o >> 16 & 0xFF);
        this.SOgreen.setValue(color_rgb_o >> 8 & 0xFF);
        this.SOblue.setValue(color_rgb_o & 0xFF);
    }
}