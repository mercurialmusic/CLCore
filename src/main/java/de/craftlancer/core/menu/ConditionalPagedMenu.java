package de.craftlancer.core.menu;

import de.craftlancer.core.util.Tuple;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ConditionalPagedMenu extends AbstractPagedMenu<ConditionalMenu> {
    
    private List<ConditionalMenu> inventories;
    private List<Tuple<String, String>> menus;
    
    /**
     * Creates a traversable GUI with the ability to go back in forth between pages.
     *
     * @param plugin     the owning plugin
     * @param useBorders whether to have a 1 block border around the list area
     * @param rows       the number of rows in the inventory, between 1 (2 with borders) and 6 (inclusive)
     * @param pageItems  list of items to display, can be modified later
     * @param playSounds whether to play sounds on certain actions
     * @param menus      Tuples should be inventory key -> inventory title. Inventory title is nullable.
     */
    public ConditionalPagedMenu(@Nonnull Plugin plugin, int rows, @Nonnull List<MenuItem> pageItems, boolean useBorders,
                                boolean playSounds, List<Tuple<String, String>> menus) {
        super(plugin, rows, pageItems, useBorders, playSounds);
        
        this.menus = menus;
    }
    
    @Override
    protected void updateInventories() {
        inventories = new ArrayList<>();
        
        for (int page = 0; page <= getPageItems().size() / getItemsPerPage(); page++) {
            int finalPage = page;
            
            ConditionalMenu inventory = new ConditionalMenu(getPlugin(), getRows(), menus);
            
            // Setting the forwards, backwards, and question mark items depending on the map size
            if (getPageItems().size() > (page + 1) * getItemsPerPage())
                inventory.set(getNextPageSlot(), getNextPageItem().clone().addClickAction(click -> display(click.getPlayer(), finalPage + 1, click.getMenuKey())));
            
            if (page > 0)
                inventory.set(getPrevPageSlot(), getPrevPageItem().clone().addClickAction(c -> display(c.getPlayer(), finalPage - 1, c.getMenuKey())));
            
            inventory.set(getInfoSlot(), getInfoItem());
            
            for (Menu menu : inventory.getMenus().values())
                fillPageItems(menu, page);
            
            getToolbarItems().forEach((slot, menuItem) -> inventory.set((getRows() - 1) * 9 + slot, menuItem));
            
            if (getInventoryUpdateHandler() != null)
                getInventoryUpdateHandler().accept(inventory);
            
            inventories.add(inventory);
        }
    }
    
    /**
     * Opens page 0 for given player
     *
     * @param player the player to open the inventory for
     */
    public void display(@Nonnull Player player, String key) {
        display(player, 0, key);
    }
    
    /**
     * Opens a given page for a given player
     */
    public void display(@Nonnull Player player, int page, String key) {
        if (inventories == null)
            updateInventories();
        
        if (page < 0 || inventories.size() < page)
            throw new IllegalArgumentException("Given page is out of bounds.");
        
        if (isPlaySounds())
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2F);
        player.openInventory(inventories.get(page).getMenu(key).getInventory());
    }
}
