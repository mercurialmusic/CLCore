package de.craftlancer.core.util;

import de.craftlancer.core.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.boss.BarColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Arrays;
import java.util.List;

public class MaterialUtil {
    
    public static boolean isBanner(Material material) {
        return Tag.BANNERS.isTagged(material);
    }
    
    public static boolean isConcrete(Material material) {
        switch (material) {
            case BLACK_CONCRETE:
            case CYAN_CONCRETE:
            case BLUE_CONCRETE:
            case RED_CONCRETE:
            case PINK_CONCRETE:
            case BROWN_CONCRETE:
            case GRAY_CONCRETE:
            case GREEN_CONCRETE:
            case LIGHT_BLUE_CONCRETE:
            case LIGHT_GRAY_CONCRETE:
            case LIME_CONCRETE:
            case MAGENTA_CONCRETE:
            case ORANGE_CONCRETE:
            case PURPLE_CONCRETE:
            case WHITE_CONCRETE:
            case YELLOW_CONCRETE:
                return true;
            default:
                return false;
        }
    }
    
    public static boolean isGlass(Material material) {
        switch (material) {
            case BLACK_STAINED_GLASS:
            case CYAN_STAINED_GLASS:
            case BLUE_STAINED_GLASS:
            case RED_STAINED_GLASS:
            case PINK_STAINED_GLASS:
            case BROWN_STAINED_GLASS:
            case GRAY_STAINED_GLASS:
            case GREEN_STAINED_GLASS:
            case LIGHT_BLUE_STAINED_GLASS:
            case LIGHT_GRAY_STAINED_GLASS:
            case LIME_STAINED_GLASS:
            case MAGENTA_STAINED_GLASS:
            case ORANGE_STAINED_GLASS:
            case PURPLE_STAINED_GLASS:
            case WHITE_STAINED_GLASS:
            case YELLOW_STAINED_GLASS:
                return true;
            default:
                return false;
        }
    }
    
    public static Color getBukkitColor(ChatColor color) {
        if (color == null)
            return Color.WHITE;
        switch (color) {
            case RED:
                return Color.fromRGB(255, 85, 85);
            case DARK_RED:
                return Color.fromRGB(170, 0, 0);
            case BLUE:
                return Color.BLUE;
            case BLACK:
                return Color.BLACK;
            case YELLOW:
                return Color.YELLOW;
            case GREEN:
                return Color.LIME;
            case GRAY:
                return Color.SILVER;
            case DARK_GREEN:
                return Color.GREEN;
            case AQUA:
                return Color.AQUA;
            case LIGHT_PURPLE:
                return Color.FUCHSIA;
            case DARK_PURPLE:
                return Color.PURPLE;
            case DARK_GRAY:
                return Color.GRAY;
            case DARK_BLUE:
                return Color.NAVY;
            case DARK_AQUA:
                return Color.fromRGB(0, 170, 170);
            case GOLD:
                return Color.ORANGE;
            default:
                return Color.WHITE;
        }
    }
    
    public static Material getGlassColor(ChatColor color) {
        if (color == null)
            return Material.WHITE_STAINED_GLASS;
        switch (color) {
            case AQUA:
                return Material.LIGHT_BLUE_STAINED_GLASS;
            case RED:
            case DARK_RED:
                return Material.RED_STAINED_GLASS;
            case BLUE:
            case DARK_BLUE:
                return Material.BLUE_STAINED_GLASS;
            case GOLD:
                return Material.ORANGE_STAINED_GLASS;
            case GRAY:
                return Material.LIGHT_GRAY_STAINED_GLASS;
            case BLACK:
                return Material.BLACK_STAINED_GLASS;
            case GREEN:
                return Material.LIME_STAINED_GLASS;
            case YELLOW:
                return Material.YELLOW_STAINED_GLASS;
            case DARK_AQUA:
                return Material.CYAN_STAINED_GLASS;
            case DARK_GRAY:
                return Material.GRAY_STAINED_GLASS;
            case DARK_GREEN:
                return Material.GREEN_STAINED_GLASS;
            case DARK_PURPLE:
                return Material.PURPLE_STAINED_GLASS;
            case LIGHT_PURPLE:
                return Material.MAGENTA_STAINED_GLASS;
            default:
                return Material.WHITE_STAINED_GLASS;
        }
    }
    
    public static void setSign(Location signLocation, String... lore) {
        setSign(signLocation, Arrays.asList(lore));
    }
    
    public static void setSign(Location signLocation, List<String> lore) {
        if (signLocation == null
                || !MaterialUtil.isSign(signLocation.getBlock().getType())
                || !Utils.isChunkLoaded(signLocation))
            return;
        Sign sign = (Sign) signLocation.getBlock().getState();
        
        for (int i = 0; i < Math.min(4, lore.size()); i++)
            sign.setLine(i, lore.get(i));
        
        sign.update();
    }
    
    public static void setBanner(Location bannerLocation, ItemStack banner) {
        if (banner == null
                || bannerLocation == null
                || !MaterialUtil.isBanner(bannerLocation.getBlock().getType())
                || !bannerLocation.getWorld().isChunkLoaded(bannerLocation.getBlockX() >> 4, bannerLocation.getBlockZ() >> 4))
            return;
        
        
        if (bannerLocation.getBlock().getType().toString().contains("_WALL_BANNER")) {
            Directional directional = (Directional) bannerLocation.getBlock().getBlockData();
            BlockFace face = directional.getFacing();
            
            bannerLocation.getBlock().setType(Material.getMaterial(banner.getType().toString().replace("_BANNER", "_WALL_BANNER")));
            //To apply direction
            
            //Set face to what it was before.
            Directional newFace = (Directional) bannerLocation.getBlock().getBlockData();
            newFace.setFacing(face);
            bannerLocation.getBlock().setBlockData(newFace);
        } else {
            Rotatable rotatable = (Rotatable) bannerLocation.getBlock().getBlockData();
            BlockFace face = rotatable.getRotation();
            
            bannerLocation.getBlock().setType(banner.getType());
            
            Rotatable newRotatable = (Rotatable) bannerLocation.getBlock().getBlockData();
            newRotatable.setRotation(face);
            bannerLocation.getBlock().setBlockData(newRotatable);
        }
        
        //If the banner meta of the clan banner is null, don't set the patterns.
        
        //Set directional to previous face
        
        BannerMeta clanBanner = (BannerMeta) banner.getItemMeta();
        Banner bannerData = (Banner) bannerLocation.getBlock().getState();
        
        if (clanBanner != null) {
            bannerData.setPatterns(clanBanner.getPatterns());
            bannerData.update();
        }
    }
    
    public static BarColor getBarColor(ChatColor color) {
        if (color == null)
            return BarColor.WHITE;
        switch (color) {
            case RED:
            case DARK_RED:
                return BarColor.RED;
            case DARK_PURPLE:
            case LIGHT_PURPLE:
                return BarColor.PURPLE;
            case GOLD:
            case YELLOW:
                return BarColor.YELLOW;
            case BLUE:
            case DARK_BLUE:
            case AQUA:
            case DARK_AQUA:
                return BarColor.BLUE;
            case GREEN:
            case DARK_GREEN:
                return BarColor.GREEN;
            default:
                return BarColor.WHITE;
        }
    }
    
    public static Material getWoolColor(ChatColor color) {
        switch (color) {
            case AQUA:
                return Material.LIGHT_BLUE_WOOL;
            case RED:
            case DARK_RED:
                return Material.RED_WOOL;
            case BLUE:
            case DARK_BLUE:
                return Material.BLUE_WOOL;
            case GOLD:
                return Material.ORANGE_WOOL;
            case GRAY:
                return Material.LIGHT_GRAY_WOOL;
            case BLACK:
                return Material.BLACK_WOOL;
            case GREEN:
                return Material.LIME_WOOL;
            case YELLOW:
                return Material.YELLOW_WOOL;
            case DARK_AQUA:
                return Material.CYAN_WOOL;
            case DARK_GRAY:
                return Material.GRAY_WOOL;
            case DARK_GREEN:
                return Material.GREEN_WOOL;
            case DARK_PURPLE:
                return Material.PURPLE_WOOL;
            case LIGHT_PURPLE:
                return Material.MAGENTA_WOOL;
            default:
                return Material.WHITE_WOOL;
        }
    }
    
    public static Material getConcreteColor(ChatColor color) {
        if (color == null)
            return Material.WHITE_CONCRETE;
        switch (color) {
            case AQUA:
                return Material.LIGHT_BLUE_CONCRETE;
            case RED:
            case DARK_RED:
                return Material.RED_CONCRETE;
            case BLUE:
            case DARK_BLUE:
                return Material.BLUE_CONCRETE;
            case GOLD:
                return Material.ORANGE_CONCRETE;
            case GRAY:
                return Material.LIGHT_GRAY_CONCRETE;
            case BLACK:
                return Material.BLACK_CONCRETE;
            case GREEN:
                return Material.LIME_CONCRETE;
            case YELLOW:
                return Material.YELLOW_CONCRETE;
            case DARK_AQUA:
                return Material.CYAN_CONCRETE;
            case DARK_GRAY:
                return Material.GRAY_CONCRETE;
            case DARK_GREEN:
                return Material.GREEN_CONCRETE;
            case DARK_PURPLE:
                return Material.PURPLE_CONCRETE;
            case LIGHT_PURPLE:
                return Material.MAGENTA_CONCRETE;
            default:
                return Material.WHITE_CONCRETE;
        }
    }
    
    public static Material replaceWallMaterial(Material material) {
        return Material.valueOf(material.name().replace("_WALL", "").replace("WALL_", ""));
    }
    
    public static boolean isSign(Material material) {
        return Tag.SIGNS.isTagged(material);
    }
    
    public static boolean isHead(Material material) {
        switch (material) {
            case PLAYER_HEAD:
            case CREEPER_HEAD:
            case CREEPER_WALL_HEAD:
            case DRAGON_HEAD:
            case DRAGON_WALL_HEAD:
            case PISTON_HEAD:
            case PLAYER_WALL_HEAD:
            case ZOMBIE_HEAD:
            case ZOMBIE_WALL_HEAD:
                return true;
            default:
                return false;
        }
    }
}
