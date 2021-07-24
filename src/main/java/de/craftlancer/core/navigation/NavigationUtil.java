package de.craftlancer.core.navigation;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Allows players to set a location to have a compass emoji be sent to guide them.
 */
public class NavigationUtil {
    
    public static String getUnicode(Player player, Location destination) {
        
        if (destination == null)
            return CompassUnicode.ZERO.toString();
        
        if (!player.getWorld().equals(destination.getWorld()))
            return CompassUnicode.ZERO.toString();
        
        double xDiff = destination.getX() - player.getLocation().getX();
        double zDiff = destination.getZ() - player.getLocation().getZ();
        
        double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
        if (zDiff < 0.0)
            newYaw = newYaw + Math.abs(180 - newYaw) * 2;
        newYaw = (newYaw - 90);
        
        double newAngle = player.getEyeLocation().getYaw() - newYaw;
        while (newAngle < 0)
            newAngle += 360;
        while (newAngle > 360)
            newAngle -= 360;
        
        int num = getNumberCompass(newAngle);
        
        return CompassUnicode.values()[num].toString();
    }
    
    private static int getNumberCompass(double angle) {
        //0 <= num <= 31
        int num = Math.min(31, (int) ((angle) / 11.25));
        
        return num < 17 ? 16 - num : 32 - (num - 16);
    }
}
