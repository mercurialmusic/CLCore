package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.Utils;
import de.craftlancer.core.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResourcePackSetForceResourcePackCommand extends SubCommand {
    
    private ResourcePackManager manager;
    
    public ResourcePackSetForceResourcePackCommand(CLCore plugin, ResourcePackManager manager) {
        super("clcore.admin", plugin, true);
        
        this.manager = manager;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2)
            return Utils.getMatches(args[1], Arrays.asList("true", "false"));
        
        return Collections.emptyList();
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkSender(sender))
            return "§cYou do not have access to this command.";
        
        if (args.length < 2)
            return "§eYou must enter a url!";
        
        boolean bool = Boolean.parseBoolean(args[1]);
        
        manager.setForceResourcePack(bool);
        
        return "§aSuccessfully " + (bool ? "enabled" : "disabled") + " using the server resource pack.";
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
