package de.craftlancer.core.command;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.craftlancer.core.Utils;

public abstract class HelpCommand extends SubCommand
{
    private Map<String, SubCommand> commands;
    
    public HelpCommand(String permission, Plugin plugin, Map<String, SubCommand> map)
    {
        super(permission, plugin, true);
        commands = map;
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof Player && !sender.hasPermission(getPermission()))
            return cmd.getPermissionMessage();
        
        if (args.length >= 2 && commands.containsKey(args[1]))
            commands.get(args[1]).help(sender);
        else
            help(sender);
        
        return null;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args)
    {
        switch (args.length)
        {
            case 2:
                return Utils.getMatches(args[1], commands.keySet());
            default:
                return null;
        }
    }
    
    @Override
    public abstract void help(CommandSender sender);
}
