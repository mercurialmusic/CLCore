package de.craftlancer.core.items;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.Utils;
import de.craftlancer.core.command.SubCommand;

public class CustomItemDropCommand extends SubCommand {

    private CustomItemRegistry registry;
    
    public CustomItemDropCommand(CLCore plugin, CustomItemRegistry registry) {
        super("clcore.itemregistry.give", plugin, true);
        this.registry = registry;
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(!checkSender(sender))
            return "You can't run this command.";
        
        if(args.length < 6)
            return "You must specify a player and an item name.";
        
        World world = Bukkit.getWorld(args[1]);
        int x = Utils.parseIntegerOrDefault(args[2], Integer.MAX_VALUE);
        int y = Utils.parseIntegerOrDefault(args[3], Integer.MAX_VALUE);
        int z = Utils.parseIntegerOrDefault(args[4], Integer.MAX_VALUE);

        if(world == null || x == Integer.MAX_VALUE || y == Integer.MAX_VALUE || z == Integer.MAX_VALUE)
            return "Coordinates are invalid.";
        
        Location loc = new Location(world, x,y,z);
        
        Optional<ItemStack> item = registry.getItem(args[5]);
        int amount = args.length >= 7 ? Utils.parseIntegerOrDefault(args[6], 1) : 1;
        
        if(!item.isPresent())
            return "Item not found.";
        
        ItemStack localItem = item.get();
        localItem.setAmount(amount);
        world.dropItem(loc, localItem);
        return String.format("Item %s dropped at %s.", args[2], loc.toString());
    }
    
    @Override
    public void help(CommandSender sender) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 2)
            return Utils.getMatches(args[1], Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()));
        if(args.length == 6)
            return Utils.getMatches(args[5], registry.getKeys());
        
        return super.onTabComplete(sender, args);
    }
}
