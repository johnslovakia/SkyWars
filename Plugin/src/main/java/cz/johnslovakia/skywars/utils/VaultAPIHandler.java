package cz.johnslovakia.skywars.utils;


import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.economy.Economy;
import cz.johnslovakia.gameapi.economy.EconomyInterface;
import cz.johnslovakia.gameapi.users.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAPIHandler {

    public static void hook(Economy mainEconomy){
        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp != null) {
            net.milkbowl.vault.economy.Economy vaultEconomy = rsp.getProvider();
            mainEconomy.setEconomyInterface(new EconomyInterface() {
                @Override
                public void deposit(GamePlayer gamePlayer, int i) {
                    vaultEconomy.depositPlayer(gamePlayer.getOnlinePlayer(), i);
                }

                @Override
                public void withdraw(GamePlayer gamePlayer, int i) {
                    vaultEconomy.withdrawPlayer(gamePlayer.getOnlinePlayer(), i);
                }

                @Override
                public void setBalance(GamePlayer gamePlayer, int i) {

                }

                @Override
                public int getBalance(GamePlayer gamePlayer) {
                    return (int) GameAPI.getInstance().getVaultEconomy().getBalance(gamePlayer.getOnlinePlayer());
                }
            });
        }
    }
}
