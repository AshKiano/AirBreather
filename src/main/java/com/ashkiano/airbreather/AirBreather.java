package com.ashkiano.airbreather;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class AirBreather extends JavaPlugin {

    private String maskLore;
    private String maskName;
    private String permission;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        maskLore = config.getString("mask-lore", "This is an Air Mask");
        maskName = config.getString("mask-name", "Air Mask");
        permission = config.getString("mask-command-permission", "airbreather.giveairmask");

        Metrics metrics = new Metrics(this, 19433);

        getLogger().info("Thank you for using the AirBreather plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");

        getCommand("giveairmask").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission(permission)) {
                        ItemStack mask = new ItemStack(Material.LEATHER_HELMET);
                        LeatherArmorMeta meta = (LeatherArmorMeta) mask.getItemMeta();
                        meta.setColor(Color.BLUE);
                        meta.setDisplayName(maskName);
                        meta.setLore(Arrays.asList(maskLore));
                        mask.setItemMeta(meta);
                        player.getInventory().addItem(mask);
                        player.sendMessage("You received a " + maskName + "!");
                        return true;
                    } else {
                        player.sendMessage("You don't have permission to use this command.");
                        return true;
                    }
                }
                return false;
            }
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    ItemStack helmet = player.getInventory().getHelmet();
                    if (helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasLore() && helmet.getItemMeta().getLore().contains(maskLore)) {
                        if (player.isInWater() && player.getInventory().contains(Material.GLASS_BOTTLE)) {
                            player.setRemainingAir(player.getMaximumAir());
                            player.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1));
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0L, 100L);
    }
}
