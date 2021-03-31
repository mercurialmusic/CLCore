package de.craftlancer.core.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class MenuClick {
    private Player player;
    private String menuKey;
    private InventoryAction clickAction;
    private ItemStack cursor;
    private MenuItem item;
    
    public MenuClick(Player player, InventoryAction action, @Nullable String menuKey, MenuItem item, ItemStack cursor) {
        this.player = player;
        this.clickAction = action;
        this.menuKey = menuKey == null ? "default" : menuKey;
        this.item = item.clone();
        this.cursor = cursor;
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
     * @return The item in the slot at the time of click
     */
    public MenuItem getItem() {
        return item;
    }
}
