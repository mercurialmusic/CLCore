package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ResourcePackSetDelayCommand extends SubCommand {
    
    private ResourcePackManager manager;
    
    public ResourcePackSetDelayCommand(CLCore plugin, ResourcePackManager manager) {
        super("", plugin, false);
        
        this.manager = manager;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2)
            return Collections.singletonList("<secondsOfDelay>");
        
        return Collections.emptyList();
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkSender(sender))
            return "§cYou do not have access to this command.";
        
        Player player = (Player) sender;
        
        if (args.length < 2)
            return "§eYou must enter an amount of seconds to delay the resource pack upon login.";
        
        int seconds;
        try {
            seconds = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return "§eYou must enter a valid integer, no decimals or letters.";
        }
        
        ResourcePackConfiguration configuration = manager.getConfigurations().getOrDefault(player.getUniqueId(), new ResourcePackConfiguration(player.getUniqueId()));
        
        if (!manager.getConfigurations().containsKey(player.getUniqueId()))
            manager.getConfigurations().put(player.getUniqueId(), configuration);
        
        configuration.setDelay((long) seconds * 20);
        
        return "§aSuccessfully set delay to " + seconds + " seconds.";
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
