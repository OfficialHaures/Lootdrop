package nl.usefultime.lootdrop.managers;

import nl.usefultime.lootdrop.Lootdrop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NotificationManager {
    private final Lootdrop plugin;

    public NotificationManager(Lootdrop plugin) {
        this.plugin = plugin;
    }

    public void broadcastLootChestSpawn(Location location) {
        String message = String.format(
            "§6§lLootdrop §8» §7A new lootchest has spawned at §eX: %d Y: %d Z: %d§7!",
            location.getBlockX(), location.getBlockY(), location.getBlockZ()
        );
        Bukkit.broadcastMessage(message);
    }

    public void sendLootClaimMessage(Player player) {
        player.sendMessage("§6§lLootdrop §8» §7You have claimed the loot!");
    }

    public void sendNoPermissionMessage(Player player) {
        player.sendMessage("§c§lLootdrop §8» §cYou don't have permission to do this!");
    }

    public void sendChestDespawnWarning(Location location) {
        String message = String.format(
            "§6§lLootdrop §8» §7The lootchest at §eX: %d Y: %d Z: %d §7will despawn soon!",
            location.getBlockX(), location.getBlockY(), location.getBlockZ()
        );
        Bukkit.broadcastMessage(message);
    }
}
