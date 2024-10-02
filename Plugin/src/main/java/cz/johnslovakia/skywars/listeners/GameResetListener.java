package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.events.GameResetEvent;
import cz.johnslovakia.skywars.chest.ChestManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameResetListener implements Listener {


    @EventHandler
    public void onGameReset(GameResetEvent e) {
        new ChestManager(e.getNewGame());
    }
}
