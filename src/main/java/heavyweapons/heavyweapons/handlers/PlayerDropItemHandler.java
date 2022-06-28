package heavyweapons.heavyweapons.handlers;

import heavyweapons.heavyweapons.HeavyWeapons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class PlayerDropItemHandler implements Listener {

    private final HeavyWeapons plugin;

    public PlayerDropItemHandler(HeavyWeapons plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    // Removes effect if player drops item
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        List<String> configMaterial = plugin.getConfig().getStringList("materials");
        String potionEffect = plugin.getConfig().getString("potion_effect");

        if (player.hasPermission("heavyweapons.strong")) return;

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, () -> {
            ItemStack mainhandItem = playerInventory.getItemInMainHand();
            ItemStack offhandItem = playerInventory.getItemInOffHand();

            // Removes effect
            if (mainhandItem.getType() == Material.AIR || offhandItem.getType() == Material.AIR) {
                player.removePotionEffect(PotionEffectType.getByName(potionEffect));
            } else if (!configMaterial.contains(mainhandItem.getType().name()) && !configMaterial.contains((mainhandItem.getType().name()))) {
                player.removePotionEffect(PotionEffectType.getByName(potionEffect));
            }
        }, 1L);
    }
}
