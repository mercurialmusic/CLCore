package de.craftlancer.core.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class BlockStructure implements ConfigurationSerializable, Iterable<Location> {
    private List<Location> blocks = new ArrayList<>();
    
    public BlockStructure() {
    }
    
    public BlockStructure(Location... blocks) {
        for (Location block : blocks)
            this.blocks.add(block);
    }
    
    public BlockStructure(Collection<Location> blocks) {
        this.blocks.addAll(blocks);
    }
    
    public void addBlock(Block block) {
        addBlock(block.getLocation());
    }
    
    public void addBlock(Location block) {
        blocks.add(block);
    }
    
    public boolean containsBlock(Block block) {
        return containsBlock(block.getLocation());
    }
    
    public boolean containsBlock(Location block) {
        return blocks.contains(block);
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("blocks", blocks);
        
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public static BlockStructure deserialize(Map<String, Object> map) {
        return new BlockStructure((List<Location>) map.get("blocks"));
    }
    
    @Override
    public Iterator<Location> iterator() {
        return blocks.iterator();
    }
    
    public List<Location> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }
}
