package de.craftlancer.core.newgui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MenuItem {
    
    private final Map<ClickType, BiConsumer<Player, InventoryAction>> clickActions = new HashMap<>();
    private final ItemStack item;
    private final MenuType menuType;
    
    public MenuItem(ItemStack item) {
        this(item, MenuType.ALL);
    }
    
    public MenuItem(ItemStack item, MenuType menuType) {
        this.item = item.clone();
        this.menuType = menuType;
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    public MenuType getMenuType() {
        return menuType;
    }
    
    public MenuItem addClickAction(BiConsumer<Player, InventoryAction> runnable, ClickType clickType) {
        clickActions.put(clickType, runnable);
        return this;
    }
    
    public MenuItem addClickAction(BiConsumer<Player, InventoryAction> runnable) {
        addClickAction(runnable, null);
        return this;
    }
    
    public MenuItem addClickAction(Consumer<Player> runnable, ClickType clickType) {
        addClickAction((p, a) -> runnable.accept(p), clickType);
        return this;
    }
    
    public MenuItem addClickAction(Consumer<Player> runnable) {
        addClickAction(runnable, null);
        return this;
    }
    
    public MenuItem addClickAction(Runnable runnable, ClickType clickType) {
        addClickAction(p -> runnable.run(), clickType);
        return this;
    }
    
    public MenuItem addClickAction(Runnable runnable) {
        addClickAction(runnable, null);
        return this;
    }
    
    public Map<ClickType, BiConsumer<Player, InventoryAction>> getClickActions() {
        return clickActions;
    }
}
