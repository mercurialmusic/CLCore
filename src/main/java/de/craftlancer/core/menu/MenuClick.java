package de.craftlancer.core.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class MenuClick {
    private final Player player;
    private final String menuKey;
    private final InventoryAction clickAction;
    private ItemStack cursor;
    private final MenuItem item;
    private final int slot;
    private final Inventory inventory;
    
    public MenuClick(Player player, InventoryAction action, @Nullable String menuKey,
                     MenuItem item, ItemStack cursor, int slot, Inventory inventory) {
        this.player = player;
        this.clickAction = action;
        this.menuKey = menuKey == null ? "default" : menuKey;
        this.item = item.clone();
        this.cursor = cursor;
        this.slot = slot;
        this.inventory = inventory;
    }
    
    public int getSlot() {
        return slot;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public InventoryAction getClickAction() {
        return clickAction;
    }
    
    /**
     * @return "default" by default, or key if present.
     */
    public String getMenuKey() {
        return menuKey;
    }
    
    /**
     * @return The item in the player's cursor at the time of the click
     */
    public ItemStack getCursor() {
        return cursor;
    }
    
    /**
     * Changes the item in the player's cursor
     */
    public void setCursor(ItemStack cursor) {
        this.cursor = cursor;
    }
    
    /**
     * Replaces the item in the inventory at the given slot
     */
    public void replaceItem(ItemStack item) {
        inventory.setItem(slot, item.clone());
    }
    
    /**
     * @return The item in the slot at the time of click
     */
    public MenuItem getItem() {
        return item;
    }
}
