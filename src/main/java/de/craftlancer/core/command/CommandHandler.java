package de.craftlancer.core.command;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// TODO implement usage of HelpMap and HelpTopic via HelpMapFactory
// TODO externalize common code between CommandHandler and SubCommandHandler
public abstract class CommandHandler implements TabExecutor {
    private Map<String, SubCommand> commands = new HashMap<>();
    private SubCommand noArgCommand;
    private Plugin plugin;
    private BaseComponent prefix;
    
    public CommandHandler(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public CommandHandler(Plugin plugin, BaseComponent prefix) {
        this.plugin = plugin;
        this.prefix = prefix;
    }
    
    public void setNoArgCommand(SubCommand noArgCommand) {
        this.noArgCommand = noArgCommand;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        args = CommandUtils.parseArgumentStrings(args);
        
        String message;
        
        if (args.length == 0 || !commands.containsKey(args[0])) {
            if (noArgCommand != null)
                message = noArgCommand.execute(sender, cmd, label, args);
            else if (commands.containsKey("help"))
                message = commands.get("help").execute(sender, cmd, label, args);
            else
                return false;
        } else
            message = commands.get(args[0]).execute(sender, cmd, label, args);
        
        if (message != null)
            if (prefix == null)
                sender.sendMessage(message);
            else
                sender.spigot().sendMessage(new ComponentBuilder(prefix)
                        .append(" ", ComponentBuilder.FormatRetention.NONE)
                        .append(message, ComponentBuilder.FormatRetention.NONE).create());
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        switch (args.length) {
            case 0:
                return Collections.emptyList();
            case 1:
                return commands.keySet().stream().filter(a -> a.startsWith(args[0])).filter(a -> commands.get(a).getPermission().isEmpty() || sender.hasPermission(commands.get(a).getPermission()))
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
}
