package de.craftlancer.core;

import java.util.function.BooleanSupplier;

import org.bukkit.scheduler.BukkitRunnable;

public class CancelableRunnable extends BukkitRunnable {
    private BooleanSupplier function;
    
    public CancelableRunnable(BooleanSupplier function) {
        this.function = function;
    }
    
    @Override
    public void run() {
        if(function.getAsBoolean())
            cancel();
    }
}