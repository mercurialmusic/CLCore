package de.craftlancer.core.vault;

import net.milkbowl.vault.permission.Permission;

public class DefaultPermission extends Permission {
    
    @Override
    public String getName() {
        return "CLCore";
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public boolean hasSuperPermsCompat() {
        return false;
    }
    
    @Override
    public boolean playerHas(String world, String player, String permission) {
        return false;
    }
    
    @Override
    public boolean playerAdd(String world, String player, String permission) {
        return false;
    }
    
    @Override
    public boolean playerRemove(String world, String player, String permission) {
        return false;
    }
    
    @Override
    public boolean groupHas(String world, String group, String permission) {
        return false;
    }
    
    @Override
    public boolean groupAdd(String world, String group, String permission) {
        return false;
    }
    
    @Override
    public boolean groupRemove(String world, String group, String permission) {
        return false;
    }
    
    @Override
    public boolean playerInGroup(String world, String player, String group) {
        return false;
    }
    
    @Override
    public boolean playerAddGroup(String world, String player, String group) {
        return false;
    }
    
    @Override
    public boolean playerRemoveGroup(String world, String player, String group) {
        return false;
    }
    
    @Override
    public String[] getPlayerGroups(String world, String player) {
        return new String[0];
    }
    
    @Override
    public String getPrimaryGroup(String world, String player) {
        return null;
    }
    
    @Override
    public String[] getGroups() {
        return new String[0];
    }
    
    @Override
    public boolean hasGroupSupport() {
        return false;
    }
    
}
