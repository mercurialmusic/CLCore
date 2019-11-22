package de.craftlancer.core.command;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
                return commands.keySet().stream().filter(a -> a.startsWith(args[1])).collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }
    
    @Override
    public abstract void help(CommandSender sender);
}
