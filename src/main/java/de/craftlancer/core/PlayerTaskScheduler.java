package de.craftlancer.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerTaskScheduler {
    
    private long tickID;
    private Map<UUID, PlayerTask> tasks = new HashMap<>();
    
    public PlayerTaskScheduler(CLCore plugin) {
        new LambdaRunnable(() -> tickID++).runTaskTimer(plugin, 0, 1);
    }
    
    /**
     * Schedules a runnable to be run for duration ticks and will delay runnables after it by duration amount.
     *
     * @param duration the amount of ticks this runnable will last and will delay the next runnable from starting
     */
    public void schedule(Player player, Runnable runnable, int duration) {
        PlayerTask task = tasks.getOrDefault(player.getUniqueId(), new PlayerTask(player.getUniqueId()));
        task.queue(runnable, duration);
        tasks.put(player.getUniqueId(), task);
    }
    
    
    private class PlayerTask {
        
        private long nextStart;
        private UUID owner;
        
        public PlayerTask(UUID owner) {
            this.owner = owner;
            this.nextStart = tickID;
        }
        
        public void queue(Runnable runnable, int duration) {
            new LambdaRunnable(() -> {
                if (Bukkit.getPlayer(owner) != null)
                    runnable.run();
            }).runTaskLater(CLCore.getInstance(), (nextStart <= tickID ? 0 : nextStart - tickID));
            nextStart = duration + (Math.max(nextStart, tickID));
        }
    }
}
