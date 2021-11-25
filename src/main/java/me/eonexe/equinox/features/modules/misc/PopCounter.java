package me.eonexe.equinox.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.eonexe.equinox.Equinox;
import me.eonexe.equinox.features.command.Command;
import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.features.modules.client.HUD;
import me.eonexe.equinox.features.setting.Setting;
import me.eonexe.equinox.util.TextUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class PopCounter
        extends Module {
    public static HashMap<String, Integer> TotemPopContainer = new HashMap();
    private static PopCounter INSTANCE = new PopCounter();
    public Setting<TextUtil.Color> nameColor = register(new Setting("NameColor", TextUtil.Color.WHITE));
    public Setting<Boolean> face = this.register(new Setting<Boolean>(">:^)", true));
    public static String custom = " >:^)";


    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static PopCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopCounter();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        TotemPopContainer.clear();
    }

    public String death1(EntityPlayer player) {
        int l_Count = TotemPopContainer.get(player.getName());
        TotemPopContainer.remove(player.getName());
        if(!face.getValue().booleanValue()){
            if (l_Count == 1) {
                return TextUtil.coloredString(""+ ChatFormatting.BOLD + player.getName() ,nameColor.getValue()) + ChatFormatting.WHITE + " died after popping " + ChatFormatting.DARK_RED + l_Count + " Totem!";
            } else {
                return TextUtil.coloredString(""+ ChatFormatting.BOLD + player.getName() ,nameColor.getValue()) + ChatFormatting.WHITE + " died after popping " + ChatFormatting.DARK_RED + l_Count + " Totems!";
            }
        }else{
            if (l_Count == 1) {
                return TextUtil.coloredString(""+ ChatFormatting.BOLD + player.getName() ,nameColor.getValue()) + ChatFormatting.WHITE + " died after popping " + ChatFormatting.DARK_RED + l_Count + " Totem!" + custom;
            } else {
                return TextUtil.coloredString(""+ ChatFormatting.BOLD + player.getName() ,nameColor.getValue()) + ChatFormatting.WHITE + " died after popping " + ChatFormatting.DARK_RED + l_Count + " Totems!" + custom;
            }
        }

    }

    public void onDeath(EntityPlayer player) {
        if (PopCounter.fullNullCheck()) {
            return;
        }
        if (getInstance().isDisabled())
            return;
        if (PopCounter.mc.player.equals(player)) {
            return;
        }
        if (TotemPopContainer.containsKey(player.getName())) {
            Command.sendClientMessage(death1(player), player.getEntityId());
        }
        //this.resetPops(player);
    }

    public String pop(EntityPlayer player) {
        int l_Count = 1;
        if (TotemPopContainer.containsKey(player.getName())) {
            l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.put(player.getName(), ++l_Count);
        } else {
            TotemPopContainer.put(player.getName(), l_Count);
        }
        if(!face.getValue().booleanValue()){
            if (l_Count == 1) {
                return TextUtil.coloredString(""+ ChatFormatting.BOLD + player.getName() ,nameColor.getValue()) + ChatFormatting.WHITE + " popped " + ChatFormatting.DARK_RED + l_Count + " Totem";
            } else {
                return TextUtil.coloredString(""+ ChatFormatting.BOLD + player.getName() ,nameColor.getValue()) + ChatFormatting.WHITE + " popped " + ChatFormatting.DARK_RED + l_Count + " Totems";

            }
        }else{
            if (l_Count == 1) {
                return TextUtil.coloredString(""+ ChatFormatting.BOLD + player.getName() ,nameColor.getValue()) + ChatFormatting.WHITE + " popped " + ChatFormatting.DARK_RED + l_Count + " Totem" + custom;
            } else {
                return TextUtil.coloredString(""+ ChatFormatting.BOLD + player.getName() ,nameColor.getValue()) + ChatFormatting.WHITE + " popped " + ChatFormatting.DARK_RED + l_Count + " Totems" + custom;

            }
        }

    }

    public void resetPops(final EntityPlayer player) {
        this.setTotemPops(player, 0);
    }

    public void setTotemPops(final EntityPlayer player, final int amount) {
        this.TotemPopContainer.put(player.getName(), amount);
    }

    public void onTotemPop(EntityPlayer player) {
        if (PopCounter.fullNullCheck()) {
            return;
        }
        if (getInstance().isDisabled())
            return;
        if (PopCounter.mc.player.equals(player)) {
            return;
        }

        Command.sendClientMessage(pop(player), player.getEntityId());
    }
}