package de.craftlancer.core.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.craftlancer.core.Utils;
import net.md_5.bungee.api.ChatColor;

public class PagedListGUIInventory {
    private List<PageItem> pageItems;
    private List<NavigationItem> navigationItems = new ArrayList<>();
    
    private List<GUIInventory> inventories;
    
    private final int nextPageSlot;
    private final int prevPageSlot;
    private final int infoSlot;
    
    // Play a sound when the gui is displayed?
    private boolean playSounds;
    // Use black glass to surround everything?
    private boolean useBorders;
    
    // amount of rows in the gui
    private final int rows;
    // amount of rows dedicated to page items
    private int rowsPerPage;
    // amount of slots per page dedicated to page items
    private int itemsPerPage;
    
    private String title;
    private final Plugin plugin;
    
    private ItemStack nextPageItem = Utils.buildItemStack(Material.ARROW, ChatColor.GOLD + "Forwards", Collections.emptyList());
    private ItemStack prevPageItem = Utils.buildItemStack(Material.ARROW, ChatColor.GOLD + "Backwards", Collections.emptyList());
    private ItemStack emptyItem = Utils.buildItemStack(Material.BLACK_STAINED_GLASS_PANE, "", Collections.emptyList());
    private ItemStack infoItem = emptyItem.clone();
    
    /**
     * Creates a traversable GUI with the ability to go back in forth between pages.
     * 
     * @param plugin the owning plugin
     * @param title the title of the inventory
     * @param useBorders whether to have a 1 block border around the list area
     * @param rows the number of rows in the inventory, between 1 (2 with borders) and 6 (inclusive)
     * @param pageItems list of items to display, can be modified later
     * @param playSounds whether to play sounds on certain actions
     */
    public PagedListGUIInventory(@Nonnull Plugin plugin, @Nullable String title, boolean useBorders, int rows, @Nonnull List<PageItem> pageItems,
            boolean playSounds) {
        this.plugin = plugin;
        this.title = title;
        this.useBorders = useBorders;
        if (rows <= (useBorders ? 2 : 1) || rows > 6)
            throw new IllegalArgumentException("Number of rows must be between " + (useBorders ? 2 : 1) + " and 6 (inclusive)");
        
        this.playSounds = playSounds;
        this.pageItems = pageItems;
        
        this.rows = rows;
        this.rowsPerPage = useBorders ? rows - 2 : rows - 1;
        this.itemsPerPage = useBorders ? rowsPerPage * 7 : rowsPerPage * 9;
        this.nextPageSlot = (rows * 9) - 4;
        this.infoSlot = (rows * 9) - 5;
        this.prevPageSlot = (rows * 9) - 6;
    }
    
    /**
     * Creates a traversable GUI with the ability to go back in forth between pages.
     * 
     * @param plugin the owning plugin
     * @param useBorders whether to have a 1 block border around the list area
     * @param rows the number of rows in the inventory, between 1 (2 with borders) and 6 (inclusive)
     * @param pageItems list of items to display, can be modified later
     * @param playSounds whether to play sounds on certain actions
     */
    public PagedListGUIInventory(Plugin plugin, boolean useBorders, int rows, List<PageItem> pageItems, boolean playSounds) {
        this(plugin, null, useBorders, rows, pageItems, playSounds);
    }
    
    /**
     * (Re)Builds the inventories
     */
    private void updateInventories() {
        inventories = new ArrayList<>();
        
        for (int page = 0; page <= pageItems.size() / itemsPerPage; page++) {
            int finalPage = page;
            GUIInventory inventory = title == null ? new GUIInventory(plugin, rows) : new GUIInventory(plugin, title, rows);
            inventory.fill(emptyItem);
            
            // Setting the forwards, backwards, and question mark items depending on the map size
            if (pageItems.size() <= (page + 1) * itemsPerPage)
                inventory.setItem(nextPageSlot, emptyItem);
            else {
                inventory.setItem(nextPageSlot, nextPageItem);
                inventory.setClickAction(nextPageSlot, player -> display(player, finalPage + 1));
            }
            
            if (page == 0)
                inventory.setItem(prevPageSlot, emptyItem);
            else {
                inventory.setItem(prevPageSlot, prevPageItem);
                inventory.setClickAction(prevPageSlot, player -> display(player, finalPage - 1));
            }
            
            inventory.setItem(infoSlot, getInfoItem());
            
            setPageItems(inventory, page);
            navigationItems.forEach(navigationItem -> {
                int localSlot = (rows * 9) + navigationItem.getSlot();
                inventory.setItem(localSlot, navigationItem.getItem());
                navigationItem.getClickActions().forEach((a, b) -> inventory.setClickAction(localSlot, b, a));
            });
            
            inventories.add(inventory);
        }
    }
    
    /**
     * Sets a page.
     * 
     * @param inventory the inventory to modify
     * @param page the page to set
     */
    private void setPageItems(GUIInventory inventory, int page) {
        int slot = getFirstSlot();
        // Looping through all the items to set the item in inventory and apply runnables
        List<PageItem> localItems = pageItems.stream().filter(a -> a.getItem().getType() != Material.AIR).skip((long) (page) * itemsPerPage).limit(itemsPerPage)
                                             .collect(Collectors.toList());
        
        for (PageItem pageItem : localItems) {
            int finalSlot = slot;
            inventory.setItem(finalSlot, pageItem.getItem());
            pageItem.getClickActions().forEach((a, b) -> inventory.setClickAction(finalSlot, b, a));
            
            if (slot % 9 == (useBorders ? 7 : 9)) {
                if (slot == getLastSlot())
                    break;
                else
                    slot += 3;
            }
            else
                slot++;
        }
    }
    
    private int getFirstSlot() {
        return useBorders ? 10 : 0;
    }
    
    private int getLastSlot() {
        return useBorders ? (rows * 9) - 11 : rowsPerPage * 9;
    }
    
    /**
     * Updates the inventories by recreating them.
     */
    public void reload() {
        updateInventories();
    }
    
    /**
     * Opens page 0 for given player
     * 
     * @param player the player to open the inventory for
     */
    public void display(@Nonnull Player player) {
        display(player, 0);
    }
    
    /**
     * Opens a given page for a given player
     * 
     * @param player
     * @param page
     */
    public void display(@Nonnull Player player, int page) {
        if (inventories == null)
            updateInventories();
        
        if (page < 0 || inventories.size() < page)
            throw new IllegalArgumentException("Given page is out of bounds.");
        
        if (playSounds)
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2F);
        player.openInventory(inventories.get(page).getInventory());
    }
    
    /**
     * Set the forwards arrow item
     * Reloads the inventory
     *
     * @param forwardsArrowItem the item to be used as the forwards arrow button
     */
    public void setNextPageItem(@Nonnull ItemStack forwardsArrowItem) {
        this.nextPageItem = forwardsArrowItem;
        reload();
    }
    
    /**
     * Sets the backwards arrow item
     * Reloads the inventory
     *
     * @param backwardsArrowItem the item to be used as the backwards arrow button
     */
    public void setPreviousPageItem(@Nonnull ItemStack backwardsArrowItem) {
        this.prevPageItem = backwardsArrowItem;
        reload();
    }
    
    /**
     * Sets the inventory title
     * Reloads the inventory
     * 
     * @param title the new title
     */
    public void setTitle(@Nullable String title) {
        this.title = title;
        reload();
    }
    
    /**
     * Get a <b>mutable</b> list of page items.
     * 
     * @return a list of all page items
     */
    public List<PageItem> getPageItems() {
        return pageItems;
    }
    
    /**
     * Sets the items to be put into the menu
     * Reloads the inventory
     */
    public void setPageItems(@Nonnull List<PageItem> pageItems) {
        this.pageItems = pageItems;
        reload();
    }
    
    /**
     * Adds a page item
     * Reloads the inventory if changes occurred
     */
    public void addPageItem(@Nonnull PageItem pageItem) {
        if (pageItems.add(pageItem))
            reload();
    }
    
    /**
     * Removes a page item
     * Reloads the inventory if changes occurred
     */
    public void removePageItem(@Nonnull PageItem pageItem) {
        if (pageItems.remove(pageItem))
            reload();
    }
    
    /**
     * Add a navigation item
     * Reloads the inventory if changes occurred
     * 
     * @param navigationItem
     */
    public void addNavigationItem(@Nonnull NavigationItem navigationItem) {
        if (navigationItem.getSlot() > 0 && !useBorders)
            throw new IllegalArgumentException("Positive slot numbers can only be used with navigation items when useBorders is true!");
        
        if (navigationItems.add(navigationItem))
            reload();
    }
    
    /**
     * Removes a navigation item
     * Reloads the inventory if changes occurred
     * 
     * @param navigationItem
     */
    public void removeNavigationItem(@Nonnull NavigationItem navigationItem) {
        if (navigationItems.remove(navigationItem))
            reload();
    }
    
    @Nonnull
    public List<NavigationItem> getNavigationItems() {
        return navigationItems;
    }
    
    public int getRows() {
        return rows;
    }
    
    /**
     * Get the info item
     */
    @Nonnull
    public ItemStack getInfoItem() {
        return infoItem;
    }
    
    /**
     * Sets the info item
     * Reloads the inventory
     * 
     * @param infoItem
     */
    public void setInfoItem(@Nonnull ItemStack infoItem) {
        this.infoItem = infoItem;
        reload();
    }
}
