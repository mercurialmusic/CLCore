package de.craftlancer.core.command.newcmd;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import de.craftlancer.core.Utils;
import de.craftlancer.core.command.CommandUtils;

/*
 * Custom Command API, to replace the old one
 * 
 * - check permissions automatically, instead of manually
 * - generate argument checks and tab complete automatically
 * - alias support
 * - sub-command support
 * - escaped String support
 * 
 * 
 */
/*
 * onCommand
 * -> create arguments array (handle escaped strings)
 * -> find applicable sub-command (first args matches name or alias)
 * -> check permissions / sender
 * -> check arguments based on @CommandArgument annotation
 * -> call sub-command
 * 
 * onTabComplete
 * -> create arguments array (handle escaped strings)
 * -> find applicable sub-command (first args matches name or alias)
 * -> check permissions / sender
 * -> find last argument, get tab completion list based on @CommandArgument annotation
 * 
 * 
 * onSubCommand
 * -> find applicable sub-command (first args matches name or alias)
 * -> check permissions / sender
 * -> check arguments based on @CommandArgument annotation
 * -> call sub-command
 * 
 */
public abstract class NewCommandHandler implements TabExecutor {
    private static final String DEFAULT_COMMAND = "help";
    
    private Plugin plugin;
    private Map<String, CLCommand> subCommands = new HashMap<>();
    
    public NewCommandHandler(Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            handleCommand(sender, CommandUtils.parseArgumentStrings(args));
        }
        catch (CommandHandlerException e) {
            plugin.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        try {
            return handleTabComplete(sender, CommandUtils.parseArgumentStrings(args));
        }
        catch (CommandHandlerException e) {
            plugin.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
        return Collections.emptyList();
    }
    
    public void handleCommand(CommandSender sender, String[] args) throws CommandHandlerException {
        CLCommand command = args.length != 0 ? subCommands.get(args[0]) : subCommands.getOrDefault(DEFAULT_COMMAND, null);
        
        if (command != null)
            command.handleCommand(sender, Arrays.copyOfRange(args, 1, args.length));
    }
    
    public List<String> handleTabComplete(CommandSender sender, String[] args) throws CommandHandlerException {
        switch (args.length) {
            case 0:
                return Collections.emptyList();
            case 1:
                return Utils.getMatches(args[0], subCommands.keySet()).stream()
                            .filter(a -> sender.hasPermission(subCommands.get(a).getPermission()))
                            .collect(Collectors.toList());
            default:
                if (!subCommands.containsKey(args[0]))
                    return Collections.emptyList();
                else
                    return subCommands.get(args[0]).handleTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }
    
    public void registerSubCommand(String name, CLCommand command, String... alias) {
        Validate.notNull(command, "Command can't be null!");
        Validate.notEmpty(name, "Commandname can't be empty!");
        Validate.isTrue(!subCommands.containsKey(name), "Command " + name + " is already defined");
        for (String a : alias)
            Validate.isTrue(!subCommands.containsKey(a), "Command " + a + " is already defined");
        
        subCommands.put(name, command);
        for (String s : alias)
            subCommands.put(s, command);
    }
}
