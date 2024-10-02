package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.events.GamePreparationEvent;
import cz.johnslovakia.gameapi.events.GameStartEvent;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.task.Task;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.chest.ChestManager;
import cz.johnslovakia.skywars.structures.MeteoriteTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PreparationStartListener implements Listener {

    @EventHandler
    public void onGamePreparation(GamePreparationEvent e) {
        ChestManager.getChestManager(e.getGame()).loadChests(e.getGame().getPlayingMap());
        ChestManager.getChestManager(e.getGame()).fillChests();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Game game = e.getGame();

        new Task(game, "TimePlaying", 120, SkyWars.getInstance());
        new Task(game, "ChestRefill", 120, SkyWars.getInstance());
        new Task(game, "Meteorit", 285, new MeteoriteTask(),SkyWars.getInstance());
    }
}
