package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.game.GameManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class JoinQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (GameManager.getGames().isEmpty()){
            e.getPlayer().sendMessage("");
            e.getPlayer().sendMessage("§cThe plugin is not set up! Instructions: type /skywars howToSetup");
            e.getPlayer().sendMessage("");
            e.getPlayer().getInventory().clear();
        }
    }
}
