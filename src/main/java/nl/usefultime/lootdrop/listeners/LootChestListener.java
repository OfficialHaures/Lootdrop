package nl.usefultime.lootdrop.listeners;

import nl.usefultime.lootdrop.Lootdrop;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class LootChestListener implements Listener {
    @EventHandler
    public void onChestInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.CHEST) return;

        Block block = event.getClickedBlock();
        if (!Lootdrop.getInstance().getLootChestManager().isLootChest(block.getLocation())) return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        Chest chest = (Chest) block.getState();

        player.sendMessage("§6§lLootdrop §8» §7Opening chest...");

        for (ItemStack item : chest.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                player.getWorld().dropItem(player.getLocation(), item.clone());
                player.sendMessage("§6§lLootdrop §8» §7You received §e" + item.getAmount() + "x " + item.getType().toString().toLowerCase().replace("_", " "));
            }
        }

        chest.getInventory().clear();
        block.setType(Material.AIR);
        Lootdrop.getInstance().getLootChestManager().removeLootChest(block.getLocation());
    }

    private String formatItemName(String name) {
        return Arrays.stream(name.toLowerCase().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.CHEST) return;
        if (Lootdrop.getInstance().getLootChestManager().isLootChest(event.getBlock().getLocation())) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            Lootdrop.getInstance().getNotificationManager().sendNoPermissionMessage(player);
        }
    }
}