package de.craftlancer.core.vault;

import java.util.Collections;
import java.util.List;

import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class DefaultEconomy implements Economy {
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public String getName() {
        return "CLCore";
    }
    
    @Override
    public boolean hasBankSupport() {
        return false;
    }
    
    @Override
    public int fractionalDigits() {
        return 0;
    }
    
    @Override
    public String format(double amount) {
        return "0";
    }
    
    @Override
    public String currencyNamePlural() {
        return "";
    }
    
    @Override
    public String currencyNameSingular() {
        return "";
    }
    
    @Override
    public boolean hasAccount(String playerName) {
        return false;
    }
    
    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return false;
    }
    
    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }
    
    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }
    
    @Override
    public double getBalance(String playerName) {
        return 0;
    }
    
    @Override
    public double getBalance(OfflinePlayer player) {
        return 0;
    }
    
    @Override
    public double getBalance(String playerName, String world) {
        return 0;
    }
    
    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return 0;
    }
    
    @Override
    public boolean has(String playerName, double amount) {
        return false;
    }
    
    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return false;
    }
    
    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }
    
    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return false;
    }
    
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Dummy economy, not implemented!");
    }
    
    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }
    
    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }
    
    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }
    
    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }
    
    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
