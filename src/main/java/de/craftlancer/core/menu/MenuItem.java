package de.craftlancer.core.menu;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MenuItem {
    
    private final Map<ClickType, Consumer<MenuClick>> clickActions = new HashMap<>();
    private final ItemStack item;
    
    public MenuItem(ItemStack item) {
        this.item = item.clone();
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    public MenuItem addClickAction(Consumer<MenuClick> runnable, ClickType clickType) {
        clickActions.put(clickType, runnable);
        return this;
    }
    
    public MenuItem addClickAction(Consumer<MenuClick> runnable) {
        addClickAction(runnable, null);
        return this;
    }
    
    public MenuItem addClickAction(Runnable runnable, ClickType clickType) {
        addClickAction(click -> runnable.run(), clickType);
        return this;
    }
    
    public MenuItem addClickAction(Runnable runnable) {
        addClickAction(runnable, null);
        return this;
    }
    
    public Map<ClickType, Consumer<MenuClick>> getClickActions() {
        return clickActions;
    }
    
    @Override
    public MenuItem clone() {
        return new MenuItem(item);
    }
}
