package de.craftlancer.core;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;

public interface Area extends ConfigurationSerializable {
    public boolean isWithin(Location loc);
    
    public boolean isWithin(Entity entity);
    
    public boolean isWithin(Block block);
}
