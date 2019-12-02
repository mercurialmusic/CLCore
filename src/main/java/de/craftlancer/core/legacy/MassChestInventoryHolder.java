package de.craftlancer.core.legacy;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Deprecated
public class MassChestInventoryHolder implements InventoryHolder {
    private MassChestInventory inventory;
    
    public MassChestInventoryHolder(MassChestInventory inventory) {
        this.inventory = inventory;
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
