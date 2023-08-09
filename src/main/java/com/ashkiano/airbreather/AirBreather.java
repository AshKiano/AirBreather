package com.ashkiano.airbreather;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class AirBreather extends JavaPlugin {

    private static final String MASK_LORE = "This is an Air Mask";

    @Override
    public void onEnable() {

        Metrics metrics = new Metrics(this, 19433);

        this.getCommand("giveairmask").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    ItemStack mask = new ItemStack(Material.LEATHER_HELMET);
                    ItemMeta meta = mask.getItemMeta();
                    meta.setLore(Arrays.asList(MASK_LORE));
                    mask.setItemMeta(meta);
                    player.getInventory().addItem(mask);
                    player.sendMessage("You received an Air Mask!");
                    return true;
                }
                return false;
            }
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    ItemStack helmet = player.getInventory().getHelmet();
                    if (helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().hasLore() && helmet.getItemMeta().getLore().contains(MASK_LORE)) {
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
