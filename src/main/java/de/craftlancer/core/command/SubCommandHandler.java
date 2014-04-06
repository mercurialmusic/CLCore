package de.craftlancer.core.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.craftlancer.core.Utils;

public abstract class SubCommandHandler extends SubCommand
{
    private Map<String, SubCommand> commands = new HashMap<String, SubCommand>();
    
    public SubCommandHandler(String permission, Plugin plugin, boolean console)
    {
        super(permission, plugin, console);
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length <= 1 || !commands.containsKey(args[1]))
            if (commands.containsKey("help"))
                return commands.get("help").execute(sender, cmd, label, args);
            else
            {
                help(sender);
                return null;
            }
        else
            return commands.get(args[1]).execute(sender, cmd, label, args);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args)
    {
        switch (args.length)
        {
            case 1:
                return null;
            case 2:
                List<String> l = Utils.getMatches(args[1], commands.keySet());
                for (String str : l)
                    if (!sender.hasPermission(commands.get(str).getPermission()))
                        l.remove(l);
                return l;
            default:
                if (!commands.containsKey(args[1]))
                    return null;
                else
                    return commands.get(args[1]).onTabComplete(sender, args);
        }
    }
    
    @Override
    public abstract void help(CommandSender sender);
    
    public void registerSubCommand(String name, SubCommand command)
    {
        Validate.notNull(command, "SubCommand can't be null!");
        Validate.notEmpty(name, "SubCommandname can't be empty!");
        commands.put(name, command);
    }
    
    protected Map<String, SubCommand> getCommands()
    {
        return commands;
    }
}
