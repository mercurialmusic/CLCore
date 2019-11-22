package de.craftlancer.core;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

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
