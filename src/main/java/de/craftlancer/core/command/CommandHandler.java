package de.craftlancer.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

// TODO implement usage of HelpMap and HelpTopic via HelpMapFactory
// TODO externalize common code between CommandHandler and SubCommandHandler
public abstract class CommandHandler implements TabExecutor {
    private Map<String, SubCommand> commands = new HashMap<>();
    private Plugin plugin;
    
    public CommandHandler(Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        args = parseArgumentStrings(args);
        
        String message;
        
        if (args.length == 0 || !commands.containsKey(args[0])) {
            if (commands.containsKey("help"))
                message = commands.get("help").execute(sender, cmd, label, args);
            else
                return false;
        }
        else
            message = commands.get(args[0]).execute(sender, cmd, label, args);
        
        if (message != null)
            sender.sendMessage(message);
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        switch (args.length) {
            case 0:
                return Collections.emptyList();
            case 1:
                return commands.keySet().stream().filter(a -> a.startsWith(args[0])).filter(a -> sender.hasPermission(commands.get(a).getPermission()))
                               .collect(Collectors.toList());
            default:
                if (!commands.containsKey(args[0]))
                    return Collections.emptyList();
                else
                    return commands.get(args[0]).onTabComplete(sender, args);
        }
    }
    
    public void registerSubCommand(String name, SubCommand command, String... alias) {
        Validate.notNull(command, "Command can't be null!");
        Validate.notEmpty(name, "Commandname can't be empty!");
        Validate.isTrue(!commands.containsKey(name), "Command " + name + " is already defined");
        for (String a : alias)
            Validate.isTrue(!commands.containsKey(a), "Command " + a + " is already defined");
        
        if (command instanceof SubCommandHandler)
            Validate.isTrue(((SubCommandHandler) command).getDepth() == 1, "The depth of a SubCommandHandler must be the depth of the previous Handler + 1!");
        
        commands.put(name, command);
        for (String s : alias)
            commands.put(s, command);
    }
    
    protected Plugin getPlugin() {
        return plugin;
    }
    
    protected Map<String, SubCommand> getCommands() {
        return commands;
    }
    
    public static String[] parseArgumentStrings(String[] args) {
        List<String> tmp = new ArrayList<>();
        
        // count " at the start and end of each string
        int[] open = new int[args.length];
        int[] close = new int[args.length];
        
        for (int i = 0; i < args.length; i++) {
            for (int j = 0; j < args[i].length() && args[i].charAt(j) == '\"'; j++)
                open[i]++;
            
            for (int j = args[i].length() - 1; j >= 0 && args[i].charAt(j) == '\"'; j--)
                close[i]++;
        }
        
        // iterate over the input strings with a string pointer
        int stringPtr = 0;
        while (stringPtr < args.length) {
            // if it doesn't start with a ", leave it as it is
            if (open[stringPtr] <= 0) {
                tmp.add(args[stringPtr]);
                stringPtr++;
                continue;
            }
            
            // otherwise count the difference between the opening/closing count of " for the string
            // and subsequent input strings until it is <= 0. Join the strings between stringPtr and j,
            // trim the starting and ending " and set the stringPtr to the value of j. 
            int count = 0;
            for (int j = stringPtr; j < args.length; j++) {
                count += open[j];
                count -= close[j];
                
                if (count <= 0) {
                    String joined = String.join(" ", Arrays.copyOfRange(args, stringPtr, j + 1));
                    tmp.add(joined.substring(1, joined.length() - 1));
                    stringPtr = j; // we dealt with those strings already
                    break;
                }
            }

            // If the count never reaches <= 0, leave the current input string as it is.
            if (count > 0)
                tmp.add(args[stringPtr]);
            
            stringPtr++;
        }
        
        return tmp.toArray(new String[tmp.size()]);
    }
}
