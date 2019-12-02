package de.craftlancer.core.legacy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

@Deprecated
public class MassChestInventory implements Inventory, ConfigurationSerializable {
    private MassChestInventoryHolder holder = new MassChestInventoryHolder(this);
    private final List<Location> inventories = new ArrayList<>();
    
    public MassChestInventory(Inventory... inventories) {
        for (Inventory i : inventories)
            addInventory(i);
    }
    
    public MassChestInventory(Collection<Inventory> inventories) {
        for (Inventory i : inventories)
            addInventory(i);
    }
    
    public boolean addInventory(Chest c) {
        if (inventories.contains(c.getLocation()))
            return false;
        
        return inventories.add(c.getLocation());
    }
    
    public boolean addInventory(Inventory i) {
        if (i.getType() != InventoryType.CHEST)
            return false;
        
        if (i instanceof DoubleChestInventory) {
            boolean left = addInventory(((DoubleChestInventory) i).getLeftSide());
            boolean right = addInventory(((DoubleChestInventory) i).getRightSide());
            return left || right;
        }
        
        return addInventory((Chest) i.getHolder());
    }
    
    public boolean removeInventory(Location loc) {
        if (!inventories.contains(loc))
            return false;
        
        return inventories.remove(loc);
    }
    
    public boolean removeInventory(Inventory i) {
        if (i.getType() != InventoryType.CHEST)
            return false;
        
        if (i instanceof DoubleChestInventory) {
            boolean left = removeInventory(((DoubleChestInventory) i).getLeftSide());
            boolean right = removeInventory(((DoubleChestInventory) i).getRightSide());
            return left || right;
        }
        
        Chest chest = (Chest) i.getHolder();
        
        return removeInventory(chest.getLocation());
    }
    
    public Set<Inventory> getInventories() {
        Set<Inventory> i = new HashSet<>();
        
        for (Location loc : inventories) {
            Block b = loc.getBlock();
            if (b.getType() != Material.CHEST) {
                removeInventory(loc);
                continue;
            }
            
            Chest chest = (Chest) b.getState();
            i.add(chest.getBlockInventory());
        }
        
        return i;
    }
    
    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        Validate.noNullElements(items, "Item cannot be null");
        HashMap<Integer, ItemStack> leftOver = new HashMap<>();
        
        int index = 0;
        
        for (ItemStack item : items) {
            item = item.clone();
            for (Inventory inventory : getInventories()) {
                if (item.getAmount() == 0)
                    break;
                
                Map<Integer, ItemStack> map = inventory.addItem(item);
                if (map.containsKey(0))
                    item = map.get(0);
                else
                    item.setAmount(0);
            }
            
            if (item.getAmount() != 0)
                leftOver.put(index, item);
            
            index++;
        }
        
        return leftOver;
    }
    
    /*
     * Taken from CraftInventory.java
     */
    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material) {
        HashMap<Integer, ItemStack> slots = new HashMap<>();
        
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material)
                slots.put(i, item);
        }
        return slots;
    }
    
    /*
     * Taken from CraftInventory.java
     */
    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<>();
        if (item != null) {
            ItemStack[] inventory = getStorageContents();
            for (int i = 0; i < inventory.length; i++)
                if (item.equals(inventory[i]))
                    slots.put(i, inventory[i]);
        }
        return slots;
    }
    
    @Override
    public void clear() {
        for (Inventory inventory : getInventories())
            inventory.clear();
    }
    
    @Override
    public void clear(int slot) {
        setItem(slot, null);
    }
    
    @Override
    public boolean contains(Material material) {
        for (Inventory inventory : getInventories())
            if (inventory.contains(material))
                return true;
        
        return false;
    }
    
    @Override
    public boolean contains(ItemStack item) {
        for (Inventory inventory : getInventories())
            if (inventory.contains(item))
                return true;
        
        return false;
    }
        
    @Override
    public boolean contains(Material material, int amount) {
        for (Inventory inventory : getInventories())
            if (inventory.contains(material, amount))
                return true;
        
        return false;
    }
    
    @Override
    public boolean contains(ItemStack item, int amount) {
        for (Inventory inventory : getInventories())
            if (inventory.contains(item, amount))
                return true;
        
        return false;
    }
    
    /*
     * Taken from CraftInventory.java
     */
    @Override
    public boolean containsAtLeast(ItemStack item, int amount) {
        if (item == null)
            return false;
        
        if (amount <= 0)
            return true;
        
        for (ItemStack i : getStorageContents())
            if (item.isSimilar(i) && (amount -= i.getAmount()) <= 0)
                return true;
        
        return false;
    }
    
    /*
     * Taken from CraftInventory.java
     */
    @Override
    public int first(Material material) {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material)
                return i;
            
        }
        return -1;
    }

    /*
     * Taken from CraftInventory.java
     */
    @Override
    public int first(ItemStack arg) {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.equals(arg))
                return i;
        }
        return -1;
    }
    
    /*
     * Taken from CraftInventory.java
     */
    @Override
    public int firstEmpty() {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++)
            if (inventory[i] == null)
                return i;
        
        return -1;
    }
    
    @Override
    public ItemStack[] getContents() {
        ItemStack[] array = new ItemStack[getSize()];
        
        int index = 0;
        
        for (Inventory i : getInventories())
            for (ItemStack item : i.getContents()) {
                array[index] = item;
                index++;
            }
        
        return array;
    }
    
    @Override
    public InventoryHolder getHolder() {
        return holder;
    }
    
    @Override
    public ItemStack getItem(int slot) {
        for (Inventory inventory : getInventories())
            if (inventory.getSize() > slot)
                return inventory.getItem(slot);
            else
                slot -= inventory.getSize();
        
        return null;
    }
    
    @Override
    public int getMaxStackSize() {
        return 64;
    }
    
    @Override
    public int getSize() {
        int size = 0;
        for (Inventory i : getInventories())
            size += i.getSize();
        
        return size;
    }
    
    
    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }
    
    @Override
    public List<HumanEntity> getViewers() {
        List<HumanEntity> viewer = new ArrayList<>();
        
        for (Inventory inventory : getInventories())
            viewer.addAll(inventory.getViewers());
        
        return viewer;
    }
    
    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }
    
    @Override
    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0)
            index += getSize() + 1;
        
        return new InventoryIterator(this, index);
    }
    
    @Override
    public void remove(Material material) {
        for (Inventory i : getInventories())
            i.remove(material);
    }
    
    @Override
    public void remove(ItemStack item) {
        for (Inventory i : getInventories())
            i.remove(item);
    }
    
    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        Validate.noNullElements(items, "Item cannot be null");
        HashMap<Integer, ItemStack> leftOver = new HashMap<>();
        
        int index = 0;
        
        for (ItemStack item : items) {
            for (Inventory inventory : getInventories()) {
                if (item.getAmount() == 0)
                    break;
                
                Map<Integer, ItemStack> map = inventory.removeItem(item);
                if (map.containsKey(0))
                    item = map.get(0);
                else
                    item.setAmount(0);
            }
            
            if (item.getAmount() != 0)
                leftOver.put(index, item);
            
            index++;
        }
        
        return leftOver;
    }
    
    @Override
    public void setContents(ItemStack[] items) {
        if (getSize() < items.length)
            throw new IllegalArgumentException("Invalid inventory size; expected " + getSize() + " or less");
        
        int remaining = items.length;
        ItemStack[] partArray;
        
        for (Inventory inventory : getInventories()) {
            if (remaining <= 0)
                break;
            
            partArray = new ItemStack[inventory.getSize()];
            System.arraycopy(items, getSize() - remaining, partArray, 0, Math.min(inventory.getSize(), remaining));
            inventory.setContents(partArray);
            
            remaining -= inventory.getSize();
        }
    }
    
    @Override
    public void setItem(int slot, ItemStack item) {
        for (Inventory inventory : getInventories())
            if (inventory.getSize() > slot)
                inventory.setItem(slot, item);
            else
                slot -= inventory.getSize();
    }
    
    @Override
    public void setMaxStackSize(int arg0) {
        throw new UnsupportedOperationException("We do not permit changing the MaxStackSize yet.");
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("inventories", inventories);
        
        return map;
    }
    
    public static MassChestInventory deserialize(Map<String, Object> map) {
        @SuppressWarnings("unchecked")
        List<Location> locs = (List<Location>) map.get("inventories");
        
        MassChestInventory inv = new MassChestInventory();
        
        for (Location loc : locs) {
            if (loc.getBlock().getType() != Material.CHEST)
                continue;
            
            inv.addInventory((Chest) loc.getBlock().getState());
        }
        
        return inv;
    }

    @Override
    public ItemStack[] getStorageContents() {
        return getContents();
    }

    @Override
    public void setStorageContents(ItemStack[] items)  {
        setContents(items);
    }

    @Override
    public Location getLocation() {
        return null;
    }
}

/*
 * Taken from InventoryIterator.java
 */
class InventoryIterator implements ListIterator<ItemStack> {
    private final Inventory inventory;
    private int nextIndex;
    private Boolean lastDirection; // true = forward, false = backward, null = haven't moved yet
    
    InventoryIterator(Inventory craftInventory) {
        this.inventory = craftInventory;
        this.nextIndex = 0;
    }
    
    InventoryIterator(Inventory craftInventory, int index) {
        this.inventory = craftInventory;
        this.nextIndex = index;
    }
    
    @Override
    public boolean hasNext() {
        return nextIndex < inventory.getSize();
    }
    
    @Override
    public ItemStack next() {
        lastDirection = true;
        return inventory.getItem(nextIndex++);
    }
    
    @Override
    public int nextIndex() {
        return nextIndex;
    }
    
    @Override
    public boolean hasPrevious() {
        return nextIndex > 0;
    }
    
    @Override
    public ItemStack previous() {
        lastDirection = false;
        return inventory.getItem(--nextIndex);
    }
    
    @Override
    public int previousIndex() {
        return nextIndex - 1;
    }
    
    @Override
    public void set(ItemStack item) {
        if (lastDirection == null) {
            throw new IllegalStateException("No current item!");
        }
        int i = lastDirection ? nextIndex - 1 : nextIndex;
        inventory.setItem(i, item);
    }
    
    @Override
    public void add(ItemStack item) {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't change the size of an inventory!");
    }
}
