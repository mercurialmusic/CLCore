package de.craftlancer.core.util;

import org.bukkit.event.Cancellable;

public class AbstractCancellableEvent extends AbstractEvent implements Cancellable {
    
    private boolean isCancelled = false;
    
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }
    
    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}
