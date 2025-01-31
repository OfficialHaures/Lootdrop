package nl.usefultime.lootdrop.managers;

import nl.usefultime.lootdrop.Lootdrop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class LootChestManager {
    private final Lootdrop plugin;
    private final Random random;
    private final Map<Location, Long> activeChests;
    private int totalChestsSpawned = 0;
    private int totalItemsClaimed = 0;
    private Map<UUID, Integer> playerChestsFound = new HashMap<>();
    private Map<UUID, Integer> playerItemsClaimed = new HashMap<>();
    private ItemStack rarestItemFound = null;
    private final long DESPAWN_TIME = 300000; // 5 minutes in millisecondsss

    public LootChestManager(Lootdrop plugin) {
        this.plugin = plugin;
        this.random = new Random();
        this.activeChests = new HashMap<>();
    }

    public void spawnLootChest(Location location) {
        Map<ItemStack, Double> lootTable = plugin.getLootManager().getLootChances();
        if (lootTable.isEmpty()) {
            return;
        }

        location.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) location.getBlock().getState();
        Random random = new Random();

        List<ItemStack> items = new ArrayList<>(lootTable.keySet());
        int firstSlot = random.nextInt(27);
        chest.getInventory().setItem(firstSlot, items.get(random.nextInt(items.size())).clone());

        // Add 2-4 additional random items based on chances
        int additionalItems = random.nextInt(3) + 2;
        for (int i = 0; i < additionalItems; i++) {
            for (Map.Entry<ItemStack, Double> entry : lootTable.entrySet()) {
                if (random.nextDouble() <= entry.getValue()) {
                    int slot;
                    do {
                        slot = random.nextInt(27);
                    } while (chest.getInventory().getItem(slot) != null);
                    chest.getInventory().setItem(slot, entry.getKey().clone());
                    break;
                }
            }
        }

        activeChests.put(location, System.currentTimeMillis());
    }

    private void startDespawnTimer(Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (activeChests.containsKey(location)) {
                    long spawnTime = activeChests.get(location);
                    long currentTime = System.currentTimeMillis();

                    if (currentTime - spawnTime >= DESPAWN_TIME) {
                        location.getBlock().setType(Material.AIR);
                        activeChests.remove(location);
                        plugin.getNotificationManager().broadcastLootChestSpawn(location);
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public boolean isLootChest(Location location) {
        return activeChests.containsKey(location);
    }

    public void removeLootChest(Location location) {
        activeChests.remove(location);
        location.getBlock().setType(Material.AIR);
    }


    public int getTotalChestsSpawned() {
        return totalChestsSpawned;
    }

    public int getActiveChestCount() {
        return activeChests.size();
    }

    public int getTotalItemsClaimed() {
        return totalItemsClaimed;
    }

    public String getRarestItemFound() {
        return rarestItemFound != null ? rarestItemFound.getType().toString() : "None";
    }

    public int getPlayerChestsFound(Player player) {
        return playerChestsFound.getOrDefault(player.getUniqueId(), 0);
    }

    public int getPlayerItemsClaimed(Player player) {
        return playerItemsClaimed.getOrDefault(player.getUniqueId(), 0);
    }

    public void spawnRandomLootChest(World world) {
        Map<ItemStack, Double> lootTable = plugin.getLootManager().getLootChances();
        List<ItemStack> validItems = lootTable.keySet().stream()
                .filter(item -> item != null)
                .collect(Collectors.toList());

        if (validItems.isEmpty()) {
            Bukkit.broadcastMessage("§6§lLootdrop §8» §7No loot items configured!");
            return;
        }

        Random random = new Random();
        int x = random.nextInt(2000) - 1000;
        int z = random.nextInt(2000) - 1000;
        int y = world.getHighestBlockYAt(x, z);

        Location location = new Location(world, x, y, z);
        location.getBlock().setType(Material.STONE);
        Location chestLoc = location.add(0, 1, 0);
        chestLoc.getBlock().setType(Material.CHEST);

        Chest chest = (Chest) chestLoc.getBlock().getState();

        ItemStack guaranteedItem = validItems.get(random.nextInt(validItems.size())).clone();
        chest.getInventory().setItem(random.nextInt(27), guaranteedItem);

        chest.update();
        activeChests.put(chestLoc, System.currentTimeMillis());

        Bukkit.broadcastMessage("§6§lLootdrop §8» §7Chest spawned at: " + x + ", " + (y+1) + ", " + z);
    }
}