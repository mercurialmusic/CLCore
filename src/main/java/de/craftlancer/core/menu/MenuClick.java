package de.craftlancer.core.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;

import javax.annotation.Nullable;

public class MenuClick {
    private Player player;
    private String menuKey;
    private InventoryAction clickAction;
    
    public MenuClick(Player player, InventoryAction action, @Nullable String menuKey) {
        this.player = player;
        this.clickAction = action;
        this.menuKey = menuKey;
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
        return menuKey == null ? "default" : menuKey;
    }
}
