package de.craftlancer.core.legacy;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils
{
    public static int freeSlots(Inventory inv)
    {
        int i = 0;
        
        for (ItemStack item : inv)
            if (item == null)
                i++;
        
        return i;
    }
}
