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
    
    /**
     * The code that will be executed when the sub command is called
     * 
     * @param sender the sender of the command
     * @param cmd the root command
     * @param label the command's label
     * @param args the arguments provided to the command, they are already processed to support input with space chars
     * @return a String, that will be send to the player, used for error messages. No message will be shown, when this is null
     */
    protected abstract String execute(CommandSender sender, Command cmd, String label, String[] args);
    
    protected List<String> onTabComplete(CommandSender sender, String[] args)
    {
        return null;
    }
    
    public abstract void help(CommandSender sender);
}
