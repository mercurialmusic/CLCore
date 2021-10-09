package de.craftlancer.core.structure;

import de.craftlancer.clapi.clcore.structure.AbstractBlockStructure;
import de.craftlancer.core.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockStructure implements ConfigurationSerializable, AbstractBlockStructure {
    private Set<Location> blocks = new HashSet<>();
    private BoundingBox boundingBox;
    
    public BlockStructure() {
    }
    
    public BlockStructure(Location... blocks) {
        for (Location block : blocks)
            this.blocks.add(block);
        
        recalculateBoundingBox();
    }
    
    public BlockStructure(Collection<Location> blocks) {
        this.blocks.addAll(blocks);
        recalculateBoundingBox();
    }
    
    private void recalculateBoundingBox() {
        this.boundingBox = Utils.calculateBoundingBoxLocation(blocks).expand(0.5D);
    }
    
    @Override
    public void addBlock(Block block) {
        addBlock(block.getLocation());
    }
    
    @Override
    public void addBlock(Location block) {
        blocks.add(block);
        recalculateBoundingBox();
    }
    
    @Override
    public boolean containsBlock(Block block) {
        return blocks.contains(block.getLocation());
    }
    
    @Override
    public boolean containsBlock(Location block) {
        if (block == null)
            return false;
        
        Location loc = block.clone();
        loc.setX(loc.getBlockX());
        loc.setY(loc.getBlockY());
        loc.setZ(loc.getBlockZ());
        return blocks.contains(loc);
    }
    
    @Override
    public boolean containsBoundingBox(BoundingBox box) {
        return this.boundingBox.overlaps(box);
    }
    
    @Override
    public boolean containsAnyBlock(Collection<Block> otherBlocks) {
        return otherBlocks.stream().anyMatch(this::containsBlock);
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("blocks", new ArrayList<>(blocks));
        
        return map;
    }
    
    public static BlockStructure deserialize(Map<String, Object> map) {
        return new BlockStructure((List<Location>) map.get("blocks"));
    }
    
    @Override
    public Iterator<Location> iterator() {
        return blocks.iterator();
    }
    
    @Override
    public Set<Location> getBlocks() {
        return Collections.unmodifiableSet(blocks);
    }
}
