package de.craftlancer.core.menu;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class MenuItem {
    
    private final Map<ClickType, Consumer<MenuClick>> clickActions = new HashMap<>();
    private ItemStack item;
    private final List<MenuItemFlag> flags;
    private Predicate<ItemStack> itemFilter;
    private Predicate<MenuClick> pickupFilter;
    private Consumer<MenuClick> placeHandler;
    private Consumer<MenuClick> pickupHandler;
    
    private MenuItem(@Nullable ItemStack item, Map<ClickType, Consumer<MenuClick>> clickActions, List<MenuItemFlag> flags) {
        this.item = item;
        this.clickActions.putAll(clickActions);
        this.flags = new ArrayList<>(flags);
    }
    
    public MenuItem(@Nullable ItemStack item, MenuItemFlag... flags) {
        this.item = item == null ? new ItemStack(Material.AIR) : item.clone();
        this.flags = Arrays.asList(flags);
    }
    
    public MenuItem(@Nullable ItemStack item) {
        this.item = item == null ? new ItemStack(Material.AIR) : item.clone();
        this.flags = Collections.emptyList();
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    protected void setItem(ItemStack item) {
        this.item = item.clone();
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
    
    public MenuItem withPlaceHandler(Consumer<MenuClick> placeHandler) {
        this.placeHandler = placeHandler;
        
        return this;
    }
    
    public MenuItem withPickupHandler(Consumer<MenuClick> pickupHandler) {
        this.pickupHandler = pickupHandler;
        
        return this;
    }
    
    public MenuItem withItemFilter(Predicate<ItemStack> itemFilter) {
        this.itemFilter = itemFilter;
        
        return this;
    }
    
    public MenuItem withPickupFilter(Predicate<MenuClick> pickupFilter) {
        this.pickupFilter = pickupFilter;
        
        return this;
    }
    
    public Map<ClickType, Consumer<MenuClick>> getClickActions() {
        return clickActions;
    }
    
    public boolean canPickup() {
        return flags.contains(MenuItemFlag.PICKUP);
    }
    
    public boolean canPlace() {
        return flags.contains(MenuItemFlag.PLACE);
    }
    
    public Predicate<ItemStack> getItemFilter() {
        return itemFilter;
    }
    
    public Predicate<MenuClick> getPickupFilter() {
        return pickupFilter;
    }
    
    public Consumer<MenuClick> getPickupHandler() {
        return pickupHandler;
    }
    
    public Consumer<MenuClick> getPlaceHandler() {
        return placeHandler;
    }
    
    public boolean dropOnClose() {
        return flags.contains(MenuItemFlag.DROP_ON_CLOSE);
    }
    
    @Override
    public MenuItem clone() {
        return new MenuItem(item);
    }
    
    public MenuItem deepClone() {
        return deepClone(item -> item);
    }
    
    public MenuItem deepClone(@Nullable ItemStack replacement) {
        return deepClone(item -> replacement == null ? new ItemStack(Material.AIR) : replacement);
    }
    
    public MenuItem deepClone(Function<ItemStack, ItemStack> itemRemap) {
        MenuItem clone = new MenuItem(itemRemap.apply(item.clone()).clone(), clickActions, flags);
        
        clone.withPickupFilter(pickupFilter);
        clone.withItemFilter(itemFilter);
        clone.withPickupHandler(pickupHandler);
        clone.withPlaceHandler(placeHandler);
        
        return clone;
    }
}
