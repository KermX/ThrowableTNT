package me.kermx.throwabletnt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final ThrowableTNT plugin;
    public ReloadCommand(ThrowableTNT plugin){this.plugin = plugin;}
    @Override
    public  boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("throwabletnt")){
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")){
                plugin.reloadConfig();
                plugin.loadConfig();
                sender.sendMessage(ChatColor.GREEN + "ThrowableTNT configuration reloaded!");
                return true;
            }
        }
        sender.sendMessage(ChatColor.GREEN + "Incorrect Usage!");
        return false;
    }
}
