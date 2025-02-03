package nl.usefultime.lootdrop;

import nl.usefultime.lootdrop.commands.LootdropCommand;
import nl.usefultime.lootdrop.listeners.GUIListener;
import nl.usefultime.lootdrop.listeners.LootChestListener;
import nl.usefultime.lootdrop.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lootdrop extends JavaPlugin {
    private static Lootdrop instance;
    private LootManager lootManager;
    private NotificationManager notificationManager;
    private PermissionManager permissionManager;
    private LootChestManager lootChestManager;
    private GuiManager guiManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        this.notificationManager = new NotificationManager(this);
        this.permissionManager = new PermissionManager(this);
        this.lootManager = new LootManager(this);
        this.lootChestManager = new LootChestManager(this);
        this.guiManager = new GuiManager(this);

        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getServer().getPluginManager().registerEvents(new LootChestListener(), this);

        getCommand("lootdrop").setExecutor(new LootdropCommand());
    }

    public static Lootdrop getInstance() { return instance; }
    public LootManager getLootManager() { return lootManager; }
    public NotificationManager getNotificationManager() { return notificationManager; }
    public PermissionManager getPermissionManager() { return permissionManager; }
    public LootChestManager getLootChestManager() { return lootChestManager; }
    public GuiManager getGuiManager() { return guiManager; }
}
