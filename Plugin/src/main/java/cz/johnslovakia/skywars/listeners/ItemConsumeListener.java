package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemConsumeListener implements Listener {

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        GamePlayer gp = PlayerManager.getGamePlayer(player);

        if (gp.getPlayerData().getGame().getState() != GameState.INGAME){
            return;
        }

        if (e.getItem().getType().equals(Material.POTION)){
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (e.getItem().getAmount() != 1) {
                        e.getItem().setAmount(e.getItem().getAmount() - 1);
                    } else {
                        player.getInventory().remove(e.getItem());
                    }
                }
            }.runTaskLaterAsynchronously(SkyWars.getInstance(), 1L);
        }
    }
}
