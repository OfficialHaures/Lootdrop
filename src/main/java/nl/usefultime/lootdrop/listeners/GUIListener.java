package nl.usefultime.lootdrop.listeners;

import nl.usefultime.lootdrop.Lootdrop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {
    private final Lootdrop plugin;

    public GUIListener(Lootdrop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        switch (title) {
            case "§6§lLootdrop Menu" -> {
                event.setCancelled(true);
                handleMainMenu(event);
            }
            case "§6§lConfigure Loot" -> {
                handleLootConfig(event);
            }
            case "§6§lSet Chances" -> {
                event.setCancelled(true);
                handleChanceSelection(event);
            }
            case "§6§lLoot Preview" -> {
                event.setCancelled(true);
                handleLootPreview(event);
            }
        }
    }

    private void handleMainMenu(InventoryClickEvent event) {
        if (!plugin.getPermissionManager().hasAdminPermission((Player) event.getWhoClicked())) return;

        switch (event.getSlot()) {
            case 11 -> openLootConfig((Player) event.getWhoClicked());
            case 13 -> spawnRandomChest((Player) event.getWhoClicked());
            case 15 -> openStatistics((Player) event.getWhoClicked());
        }
    }

    private void handleLootConfig(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == player.getInventory()) {
            ItemStack clicked = event.getCurrentItem();
            if (clicked != null && clicked.getType() != Material.AIR) {
                openChanceSelection(player, clicked.clone());
                event.setCancelled(true);
            }
        } else if (event.getClickedInventory() == event.getView().getTopInventory()) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked != null && clicked.getType() != Material.AIR) {
                if (event.isRightClick()) {
                    plugin.getLootManager().removeLootItem(clicked);
                    refreshLootConfig(player);
                } else {
                    openChanceSelection(player, clicked);
                }
            }
        }
    }

    private void handleChanceSelection(InventoryClickEvent event) {
        double chance = getChanceFromSlot(event.getSlot());
        if (chance > 0) {
            Player player = (Player) event.getWhoClicked();
            ItemStack selectedItem = plugin.getLootManager().getSelectedItem();
            plugin.getLootManager().addLootItem(selectedItem, chance);
            openLootConfig(player);
        }
    }

    private void handleLootPreview(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
            Player player = (Player) event.getWhoClicked();
            plugin.getLootChestManager().spawnLootChest(player.getLocation());
            player.closeInventory();
        }
    }

    private double getChanceFromSlot(int slot) {
        return switch (slot) {
            case 10 -> 0.01;
            case 12 -> 0.05;
            case 14 -> 0.10;
            case 16 -> 0.25;
            case 22 -> 0.50;
            default -> -1.0;
        };
    }

    private void openLootConfig(Player player) {
        plugin.getGuiManager().openLootConfig(player);
    }

    private void spawnRandomChest(Player player) {
        plugin.getLootChestManager().spawnRandomLootChest(player.getWorld());
        player.closeInventory();
    }

    private void openStatistics(Player player) {
        plugin.getGuiManager().openStatistics(player);
    }

    private void openChanceSelection(Player player, ItemStack item) {
        plugin.getGuiManager().openChanceSelection(player, item);
    }

    private void refreshLootConfig(Player player) {
        plugin.getGuiManager().refreshLootConfig(player);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().startsWith("§6§l")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("§6§lConfigure Loot")) {
            plugin.getLootManager().saveLootToConfig();
        }
    }
}