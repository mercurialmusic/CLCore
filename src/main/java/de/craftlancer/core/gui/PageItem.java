package de.craftlancer.core.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PageItem {
    
    private ItemStack item;
    private Map<ClickType, Consumer<Player>> clickActions = new HashMap<>();
    
    public PageItem(@Nonnull ItemStack item) {
        this.item = item;
    }
    
    @Nonnull
    public ItemStack getItem() {
        return item;
    }
    
    public void setItem(@Nonnull ItemStack item) {
        this.item = item;
    }
    
    @Nonnull
    public Map<ClickType, Consumer<Player>> getClickActions() {
        return clickActions;
    }
    
    public void setClickAction(@Nonnull Runnable action) {
        setClickAction(action, null);
    }
    
    public void setClickAction(@Nonnull Runnable action, @Nullable ClickType clickType) {
        setClickAction(a -> action.run(), clickType);
    }
    
    public void setClickAction(@Nonnull Consumer<Player> action) {
        setClickAction(action, null);
    }
    
    public void setClickAction(@Nonnull Consumer<Player> action, @Nullable ClickType clickType) {
        clickActions.put(clickType, action);
    }
    
    public void removeClickAction(@Nullable ClickType clickType) {
        clickActions.remove(clickType);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clickActions == null) ? 0 : clickActions.hashCode());
        result = prime * result + ((item == null) ? 0 : item.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof PageItem))
            return false;
        PageItem other = (PageItem) obj;
        if (clickActions == null) {
            if (other.clickActions != null)
                return false;
        }
        else if (!clickActions.equals(other.clickActions))
            return false;
        if (item == null) {
            if (other.item != null)
                return false;
        }
        else if (!item.equals(other.item))
            return false;
        return true;
    }
}
