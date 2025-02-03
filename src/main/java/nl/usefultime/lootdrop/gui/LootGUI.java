package nl.usefultime.lootdrop.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LootGUI extends AbstractGUI {
    private Map<ItemStack, Double> lootChances;

    public LootGUI(Map<ItemStack, Double> lootChances) {
        super("§6§lLootdrop Configuration", 54);
        this.lootChances = lootChances;
    }

    @Override
    protected void initialize() {
        ItemStack helpItem = createGuiItem(Material.BOOK, "§e§lHow to Configure",
            "§7• Drag & drop items to add them",
            "§7• Click items to set chances",
            "§7• Right-click to remove items",
            "",
            "§7Current items are shown below");
        inventory.setItem(4, helpItem);

        loadItems();
    }

    public void loadItems() {
        int slot = 9;
        for (Map.Entry<ItemStack, Double> entry : lootChances.entrySet()) {
            ItemStack item = entry.getKey().clone();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            lore.add("");
            lore.add("§7Chance: §e" + (entry.getValue() * 100) + "%");
            lore.add("§7Click to modify chance");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(slot++, item);
        }
    }
}