package heavyweapons.heavyweapons.handlers;

import heavyweapons.heavyweapons.HeavyWeapons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class PlayerHeldItemHandler implements Listener {

    private final HeavyWeapons plugin;

    public PlayerHeldItemHandler(HeavyWeapons plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    // Equipping or unequipping items via offhand keybind
    @EventHandler
    public void onPlayerHoldItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack mainhandItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        List<String> configMaterial = plugin.getConfig().getStringList("materials");
        String potionEffect = plugin.getConfig().getString("potion_effect");

        if (player.hasPermission("heavyweapons.strong")) return;

        // Removes effect
        if (mainhandItem == null || offhandItem == null) {
            player.removePotionEffect(PotionEffectType.getByName(potionEffect));
        } else if (!configMaterial.contains(mainhandItem.getType().name()) && !configMaterial.contains(offhandItem.getType().name())) {
            player.removePotionEffect(PotionEffectType.getByName(potionEffect));
        }
        // Adds effect
        else if ((configMaterial.contains(mainhandItem.getType().name()) && offhandItem.getType() != Material.AIR) || (mainhandItem.getType() != Material.AIR && configMaterial.contains(offhandItem.getType().name()))) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionEffect), 12096000, 0, true));
        }
    }

    // Swap items keybind bug fix
    @EventHandler
    public void onPlayerSwapItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack mainhandItem = player.getInventory().getItemInMainHand();
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        List<String> configMaterial = plugin.getConfig().getStringList("materials");
        String potionEffect = plugin.getConfig().getString("potion_effect");

        if (player.hasPermission("heavyweapons.strong")) return;

        // Removes effect
        if (mainhandItem.getType() == Material.AIR || offhandItem.getType() == Material.AIR) {
            player.removePotionEffect(PotionEffectType.getByName(potionEffect));
        } else if (!configMaterial.contains(mainhandItem.getType().name()) && !configMaterial.contains(offhandItem.getType().name())) {
            player.removePotionEffect(PotionEffectType.getByName(potionEffect));
        }
        // Adds effect
        else if ((configMaterial.contains(mainhandItem.getType().name()) && offhandItem.getType() != Material.AIR) || (mainhandItem.getType() != Material.AIR && configMaterial.contains(offhandItem.getType().name()))) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionEffect), 12096000, 0, true));
        }
    }

    // Close inventory checks if player switched items while in inventory
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        ItemStack mainhandItem = player.getInventory().getItemInMainHand();
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        List<String> configMaterial = plugin.getConfig().getStringList("materials");
        String potionEffect = plugin.getConfig().getString("potion_effect");

        if (player.hasPermission("heavyweapons.strong")) return;

        // Removes effect
        if (mainhandItem.getType() == Material.AIR || offhandItem.getType() == Material.AIR) {
            player.removePotionEffect(PotionEffectType.getByName(potionEffect));
        } else if (!configMaterial.contains(mainhandItem.getType().name()) && !configMaterial.contains(offhandItem.getType().name())) {
            player.removePotionEffect(PotionEffectType.getByName(potionEffect));
        }
        // Adds effect
        else if ((configMaterial.contains(mainhandItem.getType().name()) && offhandItem.getType() != Material.AIR) || (mainhandItem.getType() != Material.AIR && configMaterial.contains(offhandItem.getType().name()))) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionEffect), 12096000, 0, true));
        }
    }

    // Check items if player picks up item from ground
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        Player player = (Player) event.getEntity();
        PlayerInventory playerInventory = player.getInventory();
        List<String> configMaterial = plugin.getConfig().getStringList("materials");
        String potionEffect = plugin.getConfig().getString("potion_effect");

        if (player.hasPermission("heavyweapons.strong")) return;

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, () -> {
            ItemStack mainhandItem = playerInventory.getItemInMainHand();
            ItemStack offhandItem = playerInventory.getItemInOffHand();

            // Adds effect
            if ((configMaterial.contains(mainhandItem.getType().name()) && offhandItem.getType() != Material.AIR) || (mainhandItem.getType() != Material.AIR && configMaterial.contains(offhandItem.getType().name()))) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionEffect), 12096000, 0, true));
            }
        }, 1L);
    }
}