package me.eonexe.equinox;

import me.eonexe.equinox.manager.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import me.eonexe.equinox.loader.Loader;

@Mod(modid = Equinox.MODID, name = Equinox.MODNAME, version = Equinox.MODVER)
public class Equinox {
    public static final String MODID = "equinox";
    public static final String MODNAME = "Equinox";
    public static final String MODVER = "0.1.5";
    public static final Logger LOGGER = LogManager.getLogger(Equinox.MODNAME);
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static TimerManager timerManager;
    public static ReloadManager reloadManager;
    public static TotemPopManager totemPopManager;
    public static FileManager fileManager;
    public static CosmeticsManager cosmeticsManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static NotificationManager notificationManager;
    public static SafetyManager safetyManager;
    @Mod.Instance
    public static Equinox INSTANCE;
    private static boolean unloaded;

    static {
        unloaded = false;
    }

    public static void load() {
        LOGGER.info("\n\nLaunching Equinox\nEquinox owns me and all!\n-Made by Eonexe");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        textManager = new TextManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();
        eventManager = new EventManager();
        speedManager = new SpeedManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        notificationManager = new NotificationManager ( );
        serverManager = new ServerManager();
        fileManager = new FileManager();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        holeManager = new HoleManager();
        LOGGER.info("Managers loaded.");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();

    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nfuck off faggot");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        Equinox.onUnload();
        eventManager = null;
        friendManager = null;
        speedManager = null;
        holeManager = null;
        positionManager = null;
        rotationManager = null;
        configManager = null;
        commandManager = null;
        colorManager = null;
        serverManager = null;
        fileManager = null;
        potionManager = null;
        notificationManager = null;
        inventoryManager = null;
        moduleManager = null;
        textManager = null;
        LOGGER.info("Equinox unloaded!\n");
    }


    public static String getVersion() {
        return MODVER;
    }

    public static void reload() {
        Equinox.unload(false);
        Equinox.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(Equinox.configManager.config.replaceFirst("equinox/", ""));
            moduleManager.onUnloadPost();
            unloaded = true;
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Dont tell anyone but Aztrohh likes little kids    this is true -pancake");
        HWIDManager.hwidCheck();
        Loader.dothething();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Display.setTitle(Equinox.MODNAME + " v" + Equinox.MODVER);
        Equinox.load();
    }
}

