package nl.usefultime.lootdrop.managers;

import nl.usefultime.lootdrop.Lootdrop;
import org.bukkit.entity.Player;

public class PermissionManager {
    private final Lootdrop plugin;

    public PermissionManager(Lootdrop plugin) {
        this.plugin = plugin;
    }

    public boolean hasAdminPermission(Player player) {
        return player.hasPermission("lootdrop.admin");
    }

    public boolean hasUserPermission(Player player) {
        return player.hasPermission("lootdrop.user");
    }

    public boolean canOpenChest(Player player) {
        return hasUserPermission(player) || hasAdminPermission(player);
    }

    public boolean canConfigureLoot(Player player) {
        return hasAdminPermission(player);
    }

    public boolean canSpawnChest(Player player) {
        return hasAdminPermission(player);
    }
}
