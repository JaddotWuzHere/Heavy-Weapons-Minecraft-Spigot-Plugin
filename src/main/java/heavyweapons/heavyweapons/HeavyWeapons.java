package heavyweapons.heavyweapons;

import heavyweapons.heavyweapons.handlers.PlayerDropItemHandler;
import heavyweapons.heavyweapons.handlers.PlayerHeldItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public final class HeavyWeapons extends JavaPlugin {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // Plugin startup logic
        config.options().copyDefaults(true);
        saveDefaultConfig();

        new PlayerHeldItemHandler(this);
        new PlayerDropItemHandler(this);

        Boolean loadSuccessful = true;
        Boolean materialIDCause = false;
        Boolean potionIDCause = false;

        List<String> validMaterialList = this.getConfig().getStringList("materials"); {
            List<String> valid = new ArrayList<>();
            for (String s : validMaterialList) {
                Material material = Material.getMaterial(s);
                if (material != null) {
                    valid.add(material.name());
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Invalid material ID: " + s);
                    loadSuccessful = false;
                    materialIDCause = true;
                }
            }
        }

        String configPotionEffect = this.getConfig().getString("potion_effect");
        PotionEffectType potionEffect = PotionEffectType.getByName(configPotionEffect);
        if (potionEffect == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Invalid potion effect type ID: " + configPotionEffect);
            loadSuccessful = false;
            potionIDCause = true;
        }

        if (loadSuccessful) Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "HeavyWeapons loaded successfully");
        else if (!loadSuccessful && materialIDCause) Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Failed to load HeavyWeapons config. Cause of error - Invalid Material ID(s)");
        else if (!loadSuccessful && potionIDCause) Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Failed to load HeavyWeapons config. Cause of error - Invalid Potion Effect ID");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("heavyweapons.reload.command")) {
                sender.sendMessage(ChatColor.RED + "Whoops! You don't have permission to use this command.");
                return true;
            }
        }
        this.reloadConfig();
        Boolean reloadSuccessful = true;
        Boolean materialIDCause = false;
        Boolean potionIDCause = false;

        List<String> validMaterialList = this.getConfig().getStringList("materials"); {
            List<String> valid = new ArrayList<>();
            for (String s : validMaterialList) {
                Material material = Material.getMaterial(s);
                if (material != null) {
                    valid.add(material.name());
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Invalid material ID: " + s);
                    reloadSuccessful = false;
                    materialIDCause = true;
                }
            }
        }

        String configPotionEffect = this.getConfig().getString("potion_effect");
        PotionEffectType potionEffect = PotionEffectType.getByName(configPotionEffect);
        if (potionEffect == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Invalid potion effect type ID: " + configPotionEffect);
            reloadSuccessful = false;
            potionIDCause = true;
        }

        if (reloadSuccessful) sender.sendMessage(ChatColor.GREEN + "HeavyWeapons reloaded successfully");
        else if (!reloadSuccessful && materialIDCause) sender.sendMessage(ChatColor.RED + "Failed to reload HeavyWeapons config. Cause of error - Invalid Material ID(s)");
        else if (!reloadSuccessful && potionIDCause) sender.sendMessage(ChatColor.RED + "Failed to reload HeavyWeapons config. Cause of error - Invalid Potion Effect ID");
        return true;
    }

    public FileConfiguration getConfigFile() {
        return config;
    }
}
