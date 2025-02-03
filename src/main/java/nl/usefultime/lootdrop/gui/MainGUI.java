package nl.usefultime.lootdrop.gui;

import nl.usefultime.lootdrop.Lootdrop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MainGUI {
    private final Inventory inventory;
    private final Lootdrop plugin;

    public MainGUI(Lootdrop plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, 27, "§6§lLootdrop Menu");
        setupGUI();
    }

    private void setupGUI() {
        ItemStack configLoot = createGuiItem(Material.CHEST, "§e§lConfigure Loot",
                "§7Click to configure loot items",
                "§7and their drop chances",
                "",
                "§7➥ Add/Remove items",
                "§7➥ Set drop chances");
        inventory.setItem(11, configLoot);

        ItemStack spawnChest = createGuiItem(Material.DIAMOND, "§b§lSpawn Lootchest",
                "§7Click to spawn a lootchest",
                "",
                "§7➥ Random location",
                "§7➥ Random loot based on chances");
        inventory.setItem(13, spawnChest);

        ItemStack stats = createGuiItem(Material.BOOK, "§a§lStatistics",
                "§7View lootdrop statistics",
                "",
                "§7➥ Total chests spawned",
                "§7➥ Items claimed",
                "§7➥ Active chests");
        inventory.setItem(15, stats);
    }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public Inventory getInventory() {
        return inventory;
    }
}