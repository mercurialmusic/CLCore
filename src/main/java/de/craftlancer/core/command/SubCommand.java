package de.craftlancer.core.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.core.CLPlugin;

public abstract class SubCommand
{
    private String permission = "";
    protected CLPlugin plugin;
    private boolean console;
    
    public SubCommand(String permission, CLPlugin plugin, boolean console)
    {
        this.permission = permission;
        this.plugin = plugin;
        this.console = console;
    }
    
    public boolean checkSender(CommandSender sender)
    {
        if (!(sender instanceof Player) && isConsoleCommand())
            return true;
        
        if (sender.hasPermission(getPermission()))
            return true;
        
        return false;
    }
    
    public CLPlugin getPlugin()
    {
        return plugin;
    }
    
    public boolean isConsoleCommand()
    {
        return console;
    }
    
    public String getPermission()
    {
        return permission;
    }
    
    protected abstract void execute(CommandSender sender, Command cmd, String label, String[] args);
    
    protected List<String> onTabComplete(CommandSender sender, String[] args)
    {
        return null;
    }
    
    public abstract void help(CommandSender sender);
}
