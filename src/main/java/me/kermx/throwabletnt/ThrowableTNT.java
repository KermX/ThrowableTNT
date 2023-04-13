package me.kermx.throwabletnt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Objects;

public final class ThrowableTNT extends JavaPlugin implements Listener {

    private double TNT_VELOCITY;
    private int TNT_COST;
    private int DURABILITY_COST;

    @Override
    public void onEnable() {
        //load config
        saveDefaultConfig();
        reloadConfig();
        TNT_VELOCITY = getConfig().getDouble("TNT_Velocity");
        TNT_COST = getConfig().getInt("TNT_Cost");
        DURABILITY_COST = getConfig().getInt("Durability_Cost");
        getServer().getPluginManager().registerEvents(this,this);
        //register command
        Objects.requireNonNull(getCommand("throwabletnt")).setExecutor(new ReloadCommand(this));
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + ">>" + ChatColor.GREEN + " ThrowableTNT enabled successfully");
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        if (event.getAction() == Action.LEFT_CLICK_AIR && mainHandItem.getType() == Material.FLINT_AND_STEEL && offHandItem.getType() == Material.TNT){
            Vector direction = player.getLocation().getDirection();
            //spawn tnt with velocity
            TNTPrimed primedTnt = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
            primedTnt.setVelocity(direction.multiply(TNT_VELOCITY));
            //remove tnt
            offHandItem.setAmount(offHandItem.getAmount() - TNT_COST);
            //damage flint and steal
            if (mainHandItem.getItemMeta() instanceof Damageable) {
                Damageable mainHandDamage = (Damageable) mainHandItem.getItemMeta();
                if (mainHandDamage.getDamage() >= mainHandItem.getType().getMaxDurability()) {
                    // Flint and steel is at 0 durability, remove it from the player's inventory
                    player.getInventory().setItemInMainHand(null);
                } else {
                    int newDamage = mainHandDamage.getDamage() + DURABILITY_COST;
                    mainHandDamage.setDamage(newDamage);
                    mainHandItem.setItemMeta((ItemMeta) mainHandDamage);
                }
            }
        }
    }

    public void loadConfig(){
        reloadConfig();
        TNT_VELOCITY = getConfig().getDouble("TNT_Velocity");
        TNT_COST = getConfig().getInt("TNT_Cost");
        DURABILITY_COST = getConfig().getInt("Durability_Cost");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + ">>" + ChatColor.RED + " ThrowableTNT disabled");
    }
}
