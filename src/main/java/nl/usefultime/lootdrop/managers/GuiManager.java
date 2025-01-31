package nl.usefultime.lootdrop.managers;

import nl.usefultime.lootdrop.Lootdrop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuiManager {
    private final Lootdrop plugin;
    private ItemStack selectedItem;

    public GuiManager(Lootdrop plugin) {
        this.plugin = plugin;
    }

    public void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lLootdrop Menu");

        gui.setItem(11, createGuiItem(Material.CHEST, "§e§lConfigure Loot",
                "§7Click to configure loot items",
                "§7and their drop chances",
                "",
                "§7➥ Add/Remove items",
                "§7➥ Set drop chances"));

        gui.setItem(13, createGuiItem(Material.DIAMOND, "§b§lSpawn Lootchest",
                "§7Click to spawn a lootchest",
                "",
                "§7➥ Random location",
                "§7➥ Random loot based on chances"));

        gui.setItem(15, createGuiItem(Material.BOOK, "§a§lStatistics",
                "§7View lootdrop statistics",
                "",
                "§7➥ Total chests spawned",
                "§7➥ Items claimed",
                "§7➥ Active chests"));

        player.openInventory(gui);
    }

    public void openLootConfig(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lConfigure Loot");

        Map<ItemStack, Double> lootItems = plugin.getLootManager().getLootChances();
        int slot = 0;

        for (Map.Entry<ItemStack, Double> entry : lootItems.entrySet()) {
            if (entry.getKey() != null) {
                ItemStack item = entry.getKey().clone();
                ItemMeta meta = item.getItemMeta();
                meta.setLore(Arrays.asList(
                        "§7Drop chance: §e" + (entry.getValue() * 100) + "%",
                        "",
                        "§7Left-click to edit chance",
                        "§7Right-click to remove"
                ));
                item.setItemMeta(meta);
                gui.setItem(slot++, item);
            }
        }

        gui.setItem(53, createGuiItem(Material.BOOK, "§e§lHow to Add Items",
                "§7Drag & drop items from your",
                "§7inventory to add them as loot"));

        player.openInventory(gui);
    }

    public void openChanceSelection(Player player, ItemStack item) {
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lSet Chances");
        this.selectedItem = item;

        gui.setItem(10, createGuiItem(Material.RED_CONCRETE, "§c1% Chance", "§7Very rare item"));
        gui.setItem(12, createGuiItem(Material.ORANGE_CONCRETE, "§65% Chance", "§7Rare item"));
        gui.setItem(14, createGuiItem(Material.YELLOW_CONCRETE, "§e10% Chance", "§7Uncommon item"));
        gui.setItem(16, createGuiItem(Material.LIME_CONCRETE, "§a25% Chance", "§7Common item"));
        gui.setItem(22, createGuiItem(Material.GREEN_CONCRETE, "§250% Chance", "§7Very common item"));

        gui.setItem(4, item);
        player.openInventory(gui);
    }

    public void openStatistics(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lLootdrop Statistics");

        gui.setItem(11, createGuiItem(Material.CHEST, "§e§lTotal Chests",
                "§7Total chests spawned: §e" + plugin.getLootChestManager().getTotalChestsSpawned(),
                "§7Active chests: §e" + plugin.getLootChestManager().getActiveChestCount()));

        gui.setItem(13, createGuiItem(Material.DIAMOND, "§b§lItems Claimed",
                "§7Total items claimed: §e" + plugin.getLootChestManager().getTotalItemsClaimed(),
                "§7Rarest item found: §e" + plugin.getLootChestManager().getRarestItemFound()));

        gui.setItem(15, createGuiItem(Material.PLAYER_HEAD, "§a§lPlayer Stats",
                "§7Your chests found: §e" + plugin.getLootChestManager().getPlayerChestsFound(player),
                "§7Your items claimed: §e" + plugin.getLootChestManager().getPlayerItemsClaimed(player)));

        player.openInventory(gui);
    }

    public void refreshLootConfig(Player player) {
        openLootConfig(player);
    }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getSelectedItem() {
        return selectedItem;
    }

    public void saveConfig() {
        plugin.getLootManager().saveLootToConfig();
    }
}
