package de.craftlancer.core.vault;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class DefaultChat extends Chat {
    
    public DefaultChat(Permission perms) {
        super(perms);
    }

    @Override
    public String getName() {
        return "CLCore";
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public String getPlayerPrefix(String world, String player) {
        return "";
    }
    
    @Override
    public void setPlayerPrefix(String world, String player, String prefix) {
        // no op
    }
    
    @Override
    public String getPlayerSuffix(String world, String player) {
        return "";
    }
    
    @Override
    public void setPlayerSuffix(String world, String player, String suffix) {
        // no op
    }
    
    @Override
    public String getGroupPrefix(String world, String group) {
        return "";
    }
    
    @Override
    public void setGroupPrefix(String world, String group, String prefix) {
        // no op
    }
    
    @Override
    public String getGroupSuffix(String world, String group) {
        return "";
    }
    
    @Override
    public void setGroupSuffix(String world, String group, String suffix) {
        // no op
    }
    
    @Override
    public int getPlayerInfoInteger(String world, String player, String node, int defaultValue) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public void setPlayerInfoInteger(String world, String player, String node, int value) {
        // no op
    }
    
    @Override
    public int getGroupInfoInteger(String world, String group, String node, int defaultValue) {
        return 0;
    }
    
    @Override
    public void setGroupInfoInteger(String world, String group, String node, int value) {
        // no op
    }
    
    @Override
    public double getPlayerInfoDouble(String world, String player, String node, double defaultValue) {
        return 0;
    }
    
    @Override
    public void setPlayerInfoDouble(String world, String player, String node, double value) {
        // no op
    }
    
    @Override
    public double getGroupInfoDouble(String world, String group, String node, double defaultValue) {
        return 0;
    }
    
    @Override
    public void setGroupInfoDouble(String world, String group, String node, double value) {
        // no op
    }
    
    @Override
    public boolean getPlayerInfoBoolean(String world, String player, String node, boolean defaultValue) {
        return false;
    }
    
    @Override
    public void setPlayerInfoBoolean(String world, String player, String node, boolean value) {
        // no op
    }
    
    @Override
    public boolean getGroupInfoBoolean(String world, String group, String node, boolean defaultValue) {
        return false;
    }
    
    @Override
    public void setGroupInfoBoolean(String world, String group, String node, boolean value) {
        // no op
    }
    
    @Override
    public String getPlayerInfoString(String world, String player, String node, String defaultValue) {
        return "";
    }
    
    @Override
    public void setPlayerInfoString(String world, String player, String node, String value) {
        // no op
    }
    
    @Override
    public String getGroupInfoString(String world, String group, String node, String defaultValue) {
        return "";
    }
    
    @Override
    public void setGroupInfoString(String world, String group, String node, String value) {
        // no op
    }
    
}
