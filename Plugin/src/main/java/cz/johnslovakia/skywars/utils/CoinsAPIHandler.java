package cz.johnslovakia.skywars.utils;


import cz.johnslovakia.gameapi.economy.Economy;
import cz.johnslovakia.gameapi.economy.EconomyInterface;
import cz.johnslovakia.gameapi.users.GamePlayer;
import de.NeonnBukkit.CoinsAPI.API.CoinsAPI;

public class CoinsAPIHandler {

    public static void hook(Economy mainEconomy){
        mainEconomy.setEconomyInterface(new EconomyInterface() {
            @Override
            public void deposit(GamePlayer gamePlayer, int i) {
                CoinsAPI.addCoinsDB(gamePlayer.getOnlinePlayer().getUniqueId().toString(), i);
            }

            @Override
            public void withdraw(GamePlayer gamePlayer, int i) {
                CoinsAPI.removeCoinsDB(gamePlayer.getOnlinePlayer().getUniqueId().toString(), i);
            }

            @Override
            public void setBalance(GamePlayer gamePlayer, int i) {
                CoinsAPI.setCoinsDB(gamePlayer.getOnlinePlayer().getUniqueId().toString(), i);
            }

            @Override
            public int getBalance(GamePlayer gamePlayer) {
                return CoinsAPI.getCoinsDB(gamePlayer.getOnlinePlayer().getUniqueId().toString());
            }
        });
    }
}
