package me.eonexe.equinox.features.modules.movement;

import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.event.events.MoveEvent;
import me.eonexe.equinox.event.events.PacketEvent;
import me.eonexe.equinox.event.events.UpdateWalkingPlayerEvent;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.MathUtil;
import me.eonexe.equinox.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public
class ElytraFly
        extends Module {
    private static ElytraFly INSTANCE = new ElytraFly();
    private final Timer timer = new Timer();
    public Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.FLY));
    public Setting<Integer> devMode = this.register(new Setting<Object>("Type", 2, 1, 3, v -> this.mode.getValue() == Mode.BYPASS || this.mode.getValue() == Mode.BETTER, "EventMode"));
    public Setting<Float> speed = this.register(new Setting<Object>("Speed", 1.0f, 0.0f, 10.0f, v -> this.mode.getValue() != Mode.FLY && this.mode.getValue() != Mode.BOOST && this.mode.getValue() != Mode.BETTER && this.mode.getValue() != Mode.PANCAKE, "The Speed."));
    public Setting<Float> vSpeed = this.register(new Setting<Object>("VSpeed", 0.3f, 0.0f, 10.0f, v -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.PANCAKE, "Vertical Speed"));
    public Setting<Float> hSpeed = this.register(new Setting<Object>("HSpeed", 1.0f, 0.0f, 10.0f, v -> this.mode.getValue() == Mode.BETTER || this.mode.getValue() == Mode.PANCAKE, "Horizontal Speed"));
    public Setting<Float> glide = this.register(new Setting<Object>("Glide", 1.0E-4f, 0.0f, 0.2f, v -> this.mode.getValue() == Mode.BETTER, "Glide Speed"));
    public Setting<Float> tooBeeSpeed = this.register(new Setting<Object>("TooBeeSpeed", 1.8000001f, 1.0f, 2.0f, v -> this.mode.getValue() == Mode.COCKED, "Speed for flight on 2b2t"));
    public Setting<Boolean> autoStart = this.register(new Setting<>("AutoStart", true));
    public Setting<Boolean> disableInLiquid = this.register(new Setting<>("NoLiquid", true));
    public Setting<Boolean> infiniteDura = this.register(new Setting<>("InfiniteDura", false));
    public Setting<Boolean> noKick = this.register(new Setting<Object>("NoKick", Boolean.FALSE, v -> this.mode.getValue() == Mode.PACKET));
    public Setting<Boolean> allowUp = this.register(new Setting<Object>("AllowUp", Boolean.TRUE, v -> this.mode.getValue() == Mode.BETTER));
    public Setting<Boolean> lockPitch = this.register(new Setting<>("LockPitch", false));
    private Double posX;
    private Double flyHeight;
    private Double posZ;

    public ElytraFly() {
        super("ElytraFly", "Makes Elytra  better.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static ElytraFly getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ElytraFly();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (this.mode.getValue() == Mode.BETTER && !this.autoStart.getValue() && this.devMode.getValue() == 1) {
            ElytraFly.mc.player.connection.sendPacket(new CPacketEntityAction(ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
        this.flyHeight = null;
        this.posX = null;
        this.posZ = null;
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 1 && ElytraFly.mc.player.isElytraFlying()) {
            ElytraFly.mc.player.motionX = 0.0;
            ElytraFly.mc.player.motionY = -1.0E-4;
            ElytraFly.mc.player.motionZ = 0.0;
            double forwardInput = ElytraFly.mc.player.movementInput.moveForward;
            double strafeInput = ElytraFly.mc.player.movementInput.moveStrafe;
            double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFly.mc.player.rotationYaw);
            double forward = result[0];
            double strafe = result[1];
            double yaw = result[2];
            if (forwardInput != 0.0 || strafeInput != 0.0) {
                final double cos = Math.cos(Math.toRadians(yaw + 90.0));
                final double sin = Math.sin(Math.toRadians(yaw + 90.0));
                ElytraFly.mc.player.motionX = forward * (double) this.speed.getValue() * cos + strafe * (double) this.speed.getValue() * sin;
                ElytraFly.mc.player.motionZ = forward * (double) this.speed.getValue() * sin - strafe * (double) this.speed.getValue() * cos;
            }
            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                ElytraFly.mc.player.motionY = -1.0;
            }
        }
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.COCKED) {
            event.getPacket();
            ElytraFly.mc.player.isElytraFlying();// empty if block
        }
        if (event.getPacket() instanceof CPacketPlayer && this.mode.getValue() == Mode.COCKEDBYPASS) {
            event.getPacket();
            ElytraFly.mc.player.isElytraFlying();// empty if block
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (this.mode.getValue() == Mode.PANCAKE) {
            ItemStack itemstack = ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack) && ElytraFly.mc.player.isElytraFlying()) {
                event.setY(ElytraFly.mc.gameSettings.keyBindJump.isKeyDown() ? (double) this.vSpeed.getValue() : (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown() ? (double) (-this.vSpeed.getValue()) : 0.0));
                ElytraFly.mc.player.addVelocity(0.0, ElytraFly.mc.gameSettings.keyBindJump.isKeyDown() ? (double) this.vSpeed.getValue() : (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown() ? (double) (-this.vSpeed.getValue()) : 0.0), 0.0);
                ElytraFly.mc.player.rotateElytraX = 0.0f;
                ElytraFly.mc.player.rotateElytraY = 0.0f;
                ElytraFly.mc.player.rotateElytraZ = 0.0f;
                ElytraFly.mc.player.moveVertical = ElytraFly.mc.gameSettings.keyBindJump.isKeyDown() ? this.vSpeed.getValue() : (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown() ? -this.vSpeed.getValue() : 0.0f);
                double forward = ElytraFly.mc.player.movementInput.moveForward;
                double strafe = ElytraFly.mc.player.movementInput.moveStrafe;
                float yaw = ElytraFly.mc.player.rotationYaw;
                if (forward == 0.0 && strafe == 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                } else {
                    if (forward != 0.0) {
                        if (strafe > 0.0) {
                            yaw += (float) (forward > 0.0 ? -45 : 45);
                        } else if (strafe < 0.0) {
                            yaw += (float) (forward > 0.0 ? 45 : -45);
                        }
                        strafe = 0.0;
                        if (forward > 0.0) {
                            forward = 1.0;
                        } else if (forward < 0.0) {
                            forward = -1.0;
                        }
                    }
                    double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                    double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                    event.setX(forward * (double) this.hSpeed.getValue() * cos + strafe * (double) this.hSpeed.getValue() * sin);
                    event.setZ(forward * (double) this.hSpeed.getValue() * sin - strafe * (double) this.hSpeed.getValue() * cos);
                }
            }
        } else if (event.getStage() == 0 && this.mode.getValue() == Mode.BYPASS && this.devMode.getValue() == 3) {
            if (ElytraFly.mc.player.isElytraFlying()) {
                event.setX(0.0);
                event.setY(-1.0E-4);
                event.setZ(0.0);
                double forwardInput = ElytraFly.mc.player.movementInput.moveForward;
                double strafeInput = ElytraFly.mc.player.movementInput.moveStrafe;
                double[] result = this.forwardStrafeYaw(forwardInput, strafeInput, ElytraFly.mc.player.rotationYaw);
                double forward = result[0];
                double strafe = result[1];
                double yaw = result[2];
                if (forwardInput != 0.0 || strafeInput != 0.0) {
                    final double cos = Math.cos(Math.toRadians(yaw + 90.0));
                    final double sin = Math.sin(Math.toRadians(yaw + 90.0));
                    event.setX(forward * (double) this.speed.getValue() * cos + strafe * (double) this.speed.getValue() * sin);
                    event.setY(forward * (double) this.speed.getValue() * sin - strafe * (double) this.speed.getValue() * cos);
                }
                if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.setY(-1.0);
                }
            }
        } else if (this.mode.getValue() == Mode.COCKED) {
            if (!ElytraFly.mc.player.isElytraFlying()) {
                return;
            }
            if (!ElytraFly.mc.player.movementInput.jump) {
                if (ElytraFly.mc.player.movementInput.sneak) {
                    ElytraFly.mc.player.motionY = -(this.tooBeeSpeed.getValue() / 2.0f);
                    event.setY(-(this.speed.getValue() / 2.0f));
                } else if (event.getY() != -1.01E-4) {
                    event.setY(-1.01E-4);
                    ElytraFly.mc.player.motionY = -1.01E-4;
                }
            } else {
                return;
            }
            this.setMoveSpeed(event, this.tooBeeSpeed.getValue());
        } else if (this.mode.getValue() == Mode.COCKEDBYPASS) {
            if (!ElytraFly.mc.player.isElytraFlying()) {
                return;
            }
            if (!ElytraFly.mc.player.movementInput.jump) {
                if (this.lockPitch.getValue()) {
                    ElytraFly.mc.player.rotationPitch = 4.0f;
                }
            } else {
                return;
            }
            if (Equinox.speedManager.getSpeedKpH() > 180.0) {
                return;
            }
            double yaw = Math.toRadians(ElytraFly.mc.player.rotationYaw);
            ElytraFly.mc.player.motionX -= (double) ElytraFly.mc.player.movementInput.moveForward * Math.sin(yaw) * 0.04;
            ElytraFly.mc.player.motionZ += (double) ElytraFly.mc.player.movementInput.moveForward * Math.cos(yaw) * 0.04;
        }
    }

    private void setMoveSpeed(MoveEvent event, double speed) {
        double forward = ElytraFly.mc.player.movementInput.moveForward;
        double strafe = ElytraFly.mc.player.movementInput.moveStrafe;
        float yaw = ElytraFly.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
            ElytraFly.mc.player.motionX = 0.0;
            ElytraFly.mc.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float) (forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float) (forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            double x = forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw));
            double z = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw));
            event.setX(x);
            event.setZ(z);
            ElytraFly.mc.player.motionX = x;
            ElytraFly.mc.player.motionZ = z;
        }
    }

    @Override
    public void onTick() {
        if (!ElytraFly.mc.player.isElytraFlying()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                if (ElytraFly.mc.player.isInWater()) {
                    Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketEntityAction(ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    return;
                }
                if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    ElytraFly.mc.player.motionY += 0.08;
                } else if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    ElytraFly.mc.player.motionY -= 0.04;
                }
                if (ElytraFly.mc.gameSettings.keyBindForward.isKeyDown()) {
                    float yaw = (float) Math.toRadians(ElytraFly.mc.player.rotationYaw);
                    ElytraFly.mc.player.motionX -= MathHelper.sin(yaw) * 0.05f;
                    ElytraFly.mc.player.motionZ += MathHelper.cos(yaw) * 0.05f;
                    break;
                }
                if (!ElytraFly.mc.gameSettings.keyBindBack.isKeyDown()) break;
                float yaw = (float) Math.toRadians(ElytraFly.mc.player.rotationYaw);
                ElytraFly.mc.player.motionX += MathHelper.sin(yaw) * 0.05f;
                ElytraFly.mc.player.motionZ -= MathHelper.cos(yaw) * 0.05f;
                break;
            }
            case FLY: {
                ElytraFly.mc.player.capabilities.isFlying = true;
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            return;
        }
        switch (event.getStage()) {
            case 0: {
                if (this.disableInLiquid.getValue() && (ElytraFly.mc.player.isInWater() || ElytraFly.mc.player.isInLava())) {
                    if (ElytraFly.mc.player.isElytraFlying()) {
                        Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketEntityAction(ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    }
                    return;
                }
                if (this.autoStart.getValue() && ElytraFly.mc.gameSettings.keyBindJump.isKeyDown() && !ElytraFly.mc.player.isElytraFlying() && ElytraFly.mc.player.motionY < 0.0 && this.timer.passedMs(250L)) {
                    Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketEntityAction(ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    this.timer.reset();
                }
                if (this.mode.getValue() == Mode.BETTER) {
                    double[] dir = MathUtil.directionSpeed(this.devMode.getValue() == 1 ? (double) this.speed.getValue() : (double) this.hSpeed.getValue());
                    switch (this.devMode.getValue()) {
                        case 1: {
                            ElytraFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                            ElytraFly.mc.player.jumpMovementFactor = this.speed.getValue();
                            if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFly.mc.player.motionY += this.speed.getValue();
                            }
                            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFly.mc.player.motionY -= this.speed.getValue();
                            }
                            if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFly.mc.player.motionX = dir[0];
                                ElytraFly.mc.player.motionZ = dir[1];
                                break;
                            }
                            ElytraFly.mc.player.motionX = 0.0;
                            ElytraFly.mc.player.motionZ = 0.0;
                            break;
                        }
                        case 2: {
                            if (ElytraFly.mc.player.isElytraFlying()) {
                                if (this.flyHeight == null) {
                                    this.flyHeight = ElytraFly.mc.player.posY;
                                }
                            } else {
                                this.flyHeight = null;
                                return;
                            }
                            if (this.noKick.getValue()) {
                                this.flyHeight = this.flyHeight - (double) this.glide.getValue();
                            }
                            this.posX = 0.0;
                            this.posZ = 0.0;
                            if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                this.posX = dir[0];
                                this.posZ = dir[1];
                            }
                            if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                                this.flyHeight = ElytraFly.mc.player.posY + (double) this.vSpeed.getValue();
                            }
                            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                this.flyHeight = ElytraFly.mc.player.posY - (double) this.vSpeed.getValue();
                            }
                            ElytraFly.mc.player.setPosition(ElytraFly.mc.player.posX + this.posX, this.flyHeight, ElytraFly.mc.player.posZ + this.posZ);
                            ElytraFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                            break;
                        }
                        case 3: {
                            if (ElytraFly.mc.player.isElytraFlying()) {
                                if (this.flyHeight == null || this.posX == null || this.posX == 0.0 || this.posZ == null || this.posZ == 0.0) {
                                    this.flyHeight = ElytraFly.mc.player.posY;
                                    this.posX = ElytraFly.mc.player.posX;
                                    this.posZ = ElytraFly.mc.player.posZ;
                                }
                            } else {
                                this.flyHeight = null;
                                this.posX = null;
                                this.posZ = null;
                                return;
                            }
                            if (this.noKick.getValue()) {
                                this.flyHeight = this.flyHeight - (double) this.glide.getValue();
                            }
                            if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                this.posX = this.posX + dir[0];
                                this.posZ = this.posZ + dir[1];
                            }
                            if (this.allowUp.getValue() && ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                                this.flyHeight = ElytraFly.mc.player.posY + (double) (this.vSpeed.getValue() / 10.0f);
                            }
                            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                this.flyHeight = ElytraFly.mc.player.posY - (double) (this.vSpeed.getValue() / 10.0f);
                            }
                            ElytraFly.mc.player.setPosition(this.posX, this.flyHeight, this.posZ);
                            ElytraFly.mc.player.setVelocity(0.0, 0.0, 0.0);
                        }
                    }
                }
                double rotationYaw = Math.toRadians(ElytraFly.mc.player.rotationYaw);
                if (ElytraFly.mc.player.isElytraFlying()) {
                    switch (this.mode.getValue()) {
                        case VANILLA: {
                            float speedScaled = this.speed.getValue() * 0.05f;
                            if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFly.mc.player.motionY += speedScaled;
                            }
                            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFly.mc.player.motionY -= speedScaled;
                            }
                            if (ElytraFly.mc.gameSettings.keyBindForward.isKeyDown()) {
                                ElytraFly.mc.player.motionX -= Math.sin(rotationYaw) * (double) speedScaled;
                                ElytraFly.mc.player.motionZ += Math.cos(rotationYaw) * (double) speedScaled;
                            }
                            if (!ElytraFly.mc.gameSettings.keyBindBack.isKeyDown()) break;
                            ElytraFly.mc.player.motionX += Math.sin(rotationYaw) * (double) speedScaled;
                            ElytraFly.mc.player.motionZ -= Math.cos(rotationYaw) * (double) speedScaled;
                            break;
                        }
                        case PACKET: {
                            this.freezePlayer(ElytraFly.mc.player);
                            this.runNoKick(ElytraFly.mc.player);
                            double[] directionSpeedPacket = MathUtil.directionSpeed(this.speed.getValue());
                            if (ElytraFly.mc.player.movementInput.jump) {
                                ElytraFly.mc.player.motionY = this.speed.getValue();
                            }
                            if (ElytraFly.mc.player.movementInput.sneak) {
                                ElytraFly.mc.player.motionY = -this.speed.getValue();
                            }
                            if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFly.mc.player.motionX = directionSpeedPacket[0];
                                ElytraFly.mc.player.motionZ = directionSpeedPacket[1];
                            }
                            Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketEntityAction(ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            mc.getConnection().sendPacket(new CPacketEntityAction(ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            break;
                        }
                        case BYPASS: {
                            if (this.devMode.getValue() != 3) break;
                            if (ElytraFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                                ElytraFly.mc.player.motionY = 0.02f;
                            }
                            if (ElytraFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                                ElytraFly.mc.player.motionY = -0.2f;
                            }
                            if (ElytraFly.mc.player.ticksExisted % 8 == 0 && ElytraFly.mc.player.posY <= 240.0) {
                                ElytraFly.mc.player.motionY = 0.02f;
                            }
                            ElytraFly.mc.player.capabilities.isFlying = true;
                            ElytraFly.mc.player.capabilities.setFlySpeed(0.025f);
                            double[] directionSpeedBypass = MathUtil.directionSpeed(0.52f);
                            if (ElytraFly.mc.player.movementInput.moveStrafe != 0.0f || ElytraFly.mc.player.movementInput.moveForward != 0.0f) {
                                ElytraFly.mc.player.motionX = directionSpeedBypass[0];
                                ElytraFly.mc.player.motionZ = directionSpeedBypass[1];
                                break;
                            }
                            ElytraFly.mc.player.motionX = 0.0;
                            ElytraFly.mc.player.motionZ = 0.0;
                        }
                    }
                }
                if (!this.infiniteDura.getValue()) break;
                ElytraFly.mc.player.connection.sendPacket(new CPacketEntityAction(ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                break;
            }
            case 1: {
                if (!this.infiniteDura.getValue()) break;
                ElytraFly.mc.player.connection.sendPacket(new CPacketEntityAction(ElytraFly.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }
    }

    private double[] forwardStrafeYaw(double forward, double strafe, double yaw) {
        double[] result = {forward, strafe, yaw};
        if ((forward != 0.0 || strafe != 0.0) && forward != 0.0) {
            if (strafe > 0.0) {
                result[2] = result[2] + (double) (forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                result[2] = result[2] + (double) (forward > 0.0 ? 45 : -45);
            }
            result[1] = 0.0;
            if (forward > 0.0) {
                result[0] = 1.0;
            } else if (forward < 0.0) {
                result[0] = -1.0;
            }
        }
        return result;
    }

    private void freezePlayer(EntityPlayer player) {
        player.motionX = 0.0;
        player.motionY = 0.0;
        player.motionZ = 0.0;
    }

    private void runNoKick(EntityPlayer player) {
        if (this.noKick.getValue() && !player.isElytraFlying() && player.ticksExisted % 4 == 0) {
            player.motionY = -0.04f;
        }
    }

    @Override
    public void onDisable() {
        if (ElytraFly.fullNullCheck() || ElytraFly.mc.player.capabilities.isCreativeMode) {
            return;
        }
        ElytraFly.mc.player.capabilities.isFlying = false;
    }

    public
    enum Mode {
        VANILLA,
        PACKET,
        BOOST,
        FLY,
        BYPASS,
        BETTER,
        PANCAKE,
        COCKED,
        COCKEDBYPASS

    }
}