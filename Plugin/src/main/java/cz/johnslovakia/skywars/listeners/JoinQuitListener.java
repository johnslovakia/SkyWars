package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.game.GameManager;
import cz.johnslovakia.gameapi.events.GameJoinEvent;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class JoinQuitListener implements Listener {

    /*@EventHandler
    public void onGameQuit(GameQuitEvent e) {
        if (e.getGame().getState() == GameState.INGAME){
            if (e.getGame().getAlivePlayers().size() < 2) {
                SkyWars.getInstance().winPlayer(e.getGame(), e.getGame().getAlivePlayers().get(0));
            }
        }
    }*/

    @EventHandler
    public void onGameJoin(GameJoinEvent e) {
        Player player = e.getGamePlayer().getOnlinePlayer();

        //GameAPI.getMinigame().getMinigameTable().createMySQLUser(player.getUniqueId(), player.getName());

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        GamePlayer gamePlayer = PlayerManager.getGamePlayer(player);

        if (GameManager.getGames().isEmpty()){
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage("§cThe plugin is not set up! Instructions: type /skywars howToSetup");
            e.getPlayer().sendMessage("");
            e.getPlayer().getInventory().clear();
        }
    }
}
