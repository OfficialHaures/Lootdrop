package nl.usefultime.lootdrop.managers;

import nl.usefultime.lootdrop.Lootdrop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class LootManager {
    private final Lootdrop plugin;
    private final Map<ItemStack, Double> lootChances;
    private ItemStack selectedItem;


    public LootManager(Lootdrop plugin) {
        this.plugin = plugin;
        this.lootChances = new HashMap<>();
        loadLootFromConfig();
    }

    public void addLootItem(ItemStack item, double chance) {
        lootChances.put(item, chance);
        saveLootToConfig();
    }

    public void removeLootItem(ItemStack item) {
        lootChances.remove(item);
        saveLootToConfig();
    }

    public Map<ItemStack, Double> getLootChances() {
        return new HashMap<>(lootChances);
    }

    private void loadLootFromConfig() {
        FileConfiguration config = plugin.getConfig();
        if (config.contains("lootitems")) {
            for (String key : config.getConfigurationSection("lootitems").getKeys(false)) {
                ItemStack item = config.getItemStack("lootitems." + key + ".item");
                double chance = config.getDouble("lootitems." + key + ".chance");
                if (item != null) {
                    lootChances.put(item, chance);
                }
            }
        }
    }

    public void saveLootToConfig() {
        FileConfiguration config = plugin.getConfig();
        config.set("lootitems", null);

        int index = 0;
        for (Map.Entry<ItemStack, Double> entry : lootChances.entrySet()) {
            String path = "lootitems." + index;
            config.set(path + ".item", entry.getKey());
            config.set(path + ".chance", entry.getValue());
            index++;
        }

        plugin.saveConfig();
        plugin.reloadConfig();
    }

    public ItemStack getSelectedItem() {
        return selectedItem;
    }
}
