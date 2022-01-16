package de.craftlancer.core.util;

import java.util.Arrays;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
    private InventoryUtils() {
    }
    
    /**
     * Checks if an inventory contains ItemStacks with at least the amount given.
     * 
     * @param inv the {@link Inventory} to check
     * @param itemStacks the {@link ItemStack}s to match against
     * @return true when there are enough items in the inventory, false otherwise
     */
    public static boolean containsAtLeast(@Nonnull Inventory inv, @Nonnull ItemStack... itemStacks) {
        ItemStack[] leftover = deepCopyItemStacks(itemStacks);
        
        for (ItemStack item : inv.getStorageContents()) {
            if (item == null)
                continue;
            
            int remainingAmount = item.getAmount();
            
            for (int j = 0; j < leftover.length; j++) {
                ItemStack tmpItem = leftover[j];
                
                if (!item.isSimilar(tmpItem))
                    continue;
                
                if (remainingAmount >= tmpItem.getAmount()) {
                    remainingAmount -= tmpItem.getAmount();
                    leftover[j] = null;
                }
                else {
                    tmpItem.setAmount(tmpItem.getAmount() - remainingAmount);
                    remainingAmount = 0;
                }
            }
        }
        
        return Arrays.stream(leftover).allMatch(Objects::isNull);
    }
    
    /**
     * Checks if an inventory contains at least a given amount of items with a specific material.
     * 
     * @param inv the {@link Inventory} to check
     * @param material {@link Material} to match against
     * @param amount the amount
     * @return true when there are enough items in the inventory, false otherwise
     */
    public static boolean containsAtLeast(@Nonnull Inventory inv, @Nullable Material material, int amount) {
        for (ItemStack item : inv) {
            if (item.getType() == material) {
                amount -= item.getAmount();
                if (amount <= 0)
                    return true;
            }
        }
        
        return false;
    }
    
    /**
     * Removes up to the given amount of items with the given material.
     * 
     * @param inv the inventory to remove from
     * @param material the material to remove
     * @param amount the amount to remove
     * @return the amount that couldn't be removed
     */
    public static int remove(@Nonnull Inventory inv, @Nullable Material material, int amount) {
        if(amount <= 0)
            return 0;
        for (ItemStack item : inv) {
            if (item.getType() != material)
                continue;
            
            if (item.getAmount() < amount) {
                amount -= item.getAmount();
                item.setAmount(0);
                item.setType(Material.AIR);
            }
            else {
                item.setAmount(item.getAmount() - amount);
                return 0;
            }
        }
        
        return amount;
    }
    
    @Nonnull
    public static ItemStack[] deepCopyItemStacks(@Nonnull ItemStack[] array) {
        ItemStack[] newArray = new ItemStack[array.length];
        
        for (int i = 0; i < array.length; i++)
            newArray[i] = array[i] == null ? null : array[i].clone();
        
        return newArray;
    }
}
