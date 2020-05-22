package de.craftlancer.core.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class BlockStructure implements ConfigurationSerializable, Iterable<Location> {
    private Set<Location> blocks = new HashSet<>();
    
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
    
    public boolean containsAnyBlock(Collection<Block> otherBlocks) {
        return otherBlocks.stream().anyMatch(this::containsBlock);
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("blocks", new ArrayList<Location>(blocks));
        
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
    
    public Set<Location> getBlocks() {
        return Collections.unmodifiableSet(blocks);
    }
}
