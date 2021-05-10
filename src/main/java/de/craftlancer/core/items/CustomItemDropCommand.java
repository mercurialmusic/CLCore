package de.craftlancer.core.items;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
    
    private Optional<Location> getLocationByUUID(String str) {
        try {
            UUID uuid = UUID.fromString(str);
            Entity e = Bukkit.getEntity(uuid);
            
            return Optional.ofNullable(e).map(Entity::getLocation);
        }
        catch(IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private Optional<Location> getLocationBySelector(CommandSender sender, String selector) {
        try {
            List<Entity> e = Bukkit.selectEntities(sender, selector);
        
            if(e.isEmpty())
                return Optional.empty();
            else
                return Optional.ofNullable(e.get(0)).map(Entity::getLocation);
        }
        catch(Exception e) {
            // catch selectEntities exceptions, e.g. invalid inputs
            return Optional.empty();
        }
    }
    
    private Optional<Location> getLocationByPlayername(String name) {
        return Optional.ofNullable(Bukkit.getPlayerExact(name)).map(Player::getLocation);
    }
    
    private Optional<Location> getLocationByCoords(String[] args) {
        if(args.length < 4)
            return Optional.empty();
        
        World world = Bukkit.getWorld(args[0]);
        double x = Utils.parseDoubleOrDefault(args[1], Double.MAX_VALUE);
        double y = Utils.parseDoubleOrDefault(args[2], Double.MAX_VALUE);
        double z = Utils.parseDoubleOrDefault(args[3], Double.MAX_VALUE);

        if(world == null || x == Double.MAX_VALUE || y == Double.MAX_VALUE || z == Double.MAX_VALUE)
            return Optional.empty();
        
        return Optional.of(new Location(world, x, y, z));
    }
    
    private Optional<Location> parseLocation(CommandSender sender, String[] args) {
        Optional<Location> loc = getLocationByUUID(args[1]);
        
        if(loc.isPresent())
            return loc;
        
        loc = getLocationByPlayername(args[1]);
        if(loc.isPresent())
            return loc;

        loc = getLocationBySelector(sender, args[1]);
        if(loc.isPresent())
            return loc;
        
        loc = getLocationByCoords(Arrays.copyOfRange(args, 1, args.length));
        if(loc.isPresent())
            return loc;
        
        return Optional.empty();
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if(!checkSender(sender))
            return "You can't run this command.";
        
        if(args.length < 2)
            return "You must specify a location.";
        
        Optional<Location> loc = parseLocation(sender, args);
        
        if(!loc.isPresent())
            return "The given location is invalid.";
        
        boolean usedCoords = args.length >= 6;
        
        Optional<ItemStack> item = registry.getItem(args[usedCoords ? 5 : 2]);
        int amount = 1;
        
        if(usedCoords && args.length >= 7)
            amount = Utils.parseIntegerOrDefault(args[6], 1);
        else if(!usedCoords && args.length >= 4)
            amount = Utils.parseIntegerOrDefault(args[3], 1);
        
        if(!item.isPresent())
            return "Item not found.";
        
        ItemStack localItem = item.get();
        localItem.setAmount(amount);
        loc.get().getWorld().dropItem(loc.get(), localItem);
        return String.format("Item %s dropped at %s.", args[usedCoords ? 5 : 2], loc.get().toString());
    }
    
    @Override
    public void help(CommandSender sender) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return super.onTabComplete(sender, args);
    }
}
