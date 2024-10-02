package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.events.GameEndEvent;
import cz.johnslovakia.gameapi.users.GamePlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameEndListener implements Listener {

    @EventHandler
    public void onGameEnd(GameEndEvent e) {

        /*for (GamePlayer player : e.getGame().getRanking().keySet()){
            Integer ranking = e.getGame().getRanking().get(player);
            if (ranking == 1){
                player.getScoreByName("1stKiller").increaseScore();
            }else if (ranking == 2){
                player.getScoreByName("2ndKiller").increaseScore();
            }else if (ranking == 3){
                player.getScoreByName("3rdKiller").increaseScore();
            }
        }*/ //TODO: TOP3 players
    }
}
