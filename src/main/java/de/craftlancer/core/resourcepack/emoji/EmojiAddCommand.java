package de.craftlancer.core.resourcepack.emoji;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.SubCommand;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class EmojiAddCommand extends SubCommand {
    
    private EmojiFontRegistry registry;
    
    public EmojiAddCommand(CLCore plugin, EmojiFontRegistry registry) {
        super("clcore.admin", plugin, true);
        
        this.registry = registry;
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkSender(sender)) {
            MessageUtil.sendMessage(getPlugin(), sender, MessageLevel.INFO, "You do not have permission to that command.");
            return null;
        }
        
        if (args.length < 3) {
            MessageUtil.sendMessage(getPlugin(), sender, MessageLevel.INFO, "You must enter a name and a unicode.");
            return null;
        }
        
        String name = args[1];
        String unicode = args[2];
        
        if (registry.addEmoji(name, unicode))
            MessageUtil.sendMessage(getPlugin(), sender, MessageLevel.SUCCESS, "Successfully added emoji.");
        else
            MessageUtil.sendMessage(getPlugin(), sender, MessageLevel.INFO, "An emoji with this name already exists.");
        
        return null;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2)
            return Collections.singletonList("<name>");
        
        if (args.length == 3)
            return Collections.singletonList("<unicode");
        
        return Collections.emptyList();
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
