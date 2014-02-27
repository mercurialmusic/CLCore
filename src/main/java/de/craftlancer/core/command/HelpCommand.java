package de.craftlancer.core.command;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.core.CLPlugin;
import de.craftlancer.core.Utils;

public abstract class HelpCommand extends SubCommand
{
    private Map<String, SubCommand> commands;
    
    public HelpCommand(String permission, CLPlugin plugin, Map<String, SubCommand> map)
    {
        super(permission, plugin, true);
        commands = map;
    }
    
    @Override
    protected void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof Player && !sender.hasPermission(getPermission()))
            sender.sendMessage(cmd.getPermissionMessage());
        else if (args.length >= 2 && commands.containsKey(args[1]))
            commands.get(args[1]).help(sender);
        else
            help(sender);
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
