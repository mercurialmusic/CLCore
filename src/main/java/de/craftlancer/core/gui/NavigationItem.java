package de.craftlancer.core.gui;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

public class NavigationItem extends PageItem {
    
    private int slot;
    
    /**
     * @param slot the slot you want it to be at, to use it with variable rows, use -1 for the last slot, or -5 for the
     *        middle slot of the last row, etc.
     */
    public NavigationItem(@Nonnull ItemStack item, int slot) {
        super(item);
        
        if (slot == -4 || slot == -5 || slot == -6 || slot < -9 || slot == 0 || slot > 9)
            throw new IllegalArgumentException(
                    "Given slot must be in between -9 and 9 inclusive, not including -4, -5, -6, and 0 (the spots of navigation items)");
        
        this.slot = slot;
    }
    
    public int getSlot() {
        return slot;
    }
    
    public void setSlot(int slot) {
        if (slot == -4 || slot == -5 || slot == -6 || slot < -9 || slot == 0 || slot > 9)
            throw new IllegalArgumentException(
                    "Given slot must be in between -9 and 9 inclusive, not including -4, -5, -6, and 0 (the spots of navigation items)");
        
        this.slot = slot;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + slot;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof NavigationItem))
            return false;
        NavigationItem other = (NavigationItem) obj;
        if (slot != other.slot)
            return false;
        return true;
    }
    
}
