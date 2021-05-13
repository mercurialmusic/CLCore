package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ResourcePackToggleCommand extends SubCommand {
    
    private ResourcePackManager manager;
    
    public ResourcePackToggleCommand(CLCore plugin, ResourcePackManager manager) {
        super("", plugin, false);
        
        this.manager = manager;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkSender(sender))
            return "§cYou do not have access to this command.";
        
        Player player = (Player) sender;
        
        ResourcePackConfiguration configuration = manager.getConfigurations().getOrDefault(player.getUniqueId(), new ResourcePackConfiguration(player.getUniqueId()));
        
        if (!manager.getConfigurations().containsKey(player.getUniqueId()))
            manager.getConfigurations().put(player.getUniqueId(), configuration);
        
        configuration.setEnabled(!configuration.isEnabled());
        
        return "§aSuccessfully toggled resource pack to be " + (configuration.isEnabled() ? "enabled" : "disabled") + " when logging in.";
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
