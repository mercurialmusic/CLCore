package de.craftlancer.core;

import org.bukkit.scheduler.BukkitRunnable;

public class LambdaRunnable extends BukkitRunnable {
    private Runnable function;
    
    public LambdaRunnable(Runnable function) {
        this.function = function;
    }
    
    @Override
    public void run() {
        function.run();
    }
}