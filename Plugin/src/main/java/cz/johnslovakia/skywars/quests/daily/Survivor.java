package cz.johnslovakia.skywars.quests.daily;

import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.economy.Economy;
import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.events.GameStartEvent;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.quests.PlayerQuestData;
import cz.johnslovakia.gameapi.users.quests.Quest;
import cz.johnslovakia.gameapi.users.quests.QuestType;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;

public class Survivor implements Quest, Listener {

    public Survivor(){
        Bukkit.getPluginManager().registerEvents(this, GameAPI.getInstance());
    }


    @Override
    public QuestType getType() {
        return QuestType.DAILY;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Map<Economy, Integer> getRewards() {
        return Map.of(Economy.getEconomyByName("coins"), 60,
                Economy.getEconomyByName("tokens"), 40);
    }

    @Override
    public int getCompletionGoal() {
        return 3;
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        return Set.of();
    }



    int playersAtStart;

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        playersAtStart = e.getGame().getPlayers().size();
    }

    @EventHandler
    public void onGamePlayerDeath(GamePlayerDeathEvent e) {
        if ((e.getGame().getPlayers().size() / 2) <= playersAtStart){
            for (GamePlayer gamePlayer : e.getGame().getPlayers().stream().filter(gamePlayer -> gamePlayer.getPlayerData().getQuestsByStatus(PlayerQuestData.Status
                    .IN_PROGRESS).contains(this)).toList()){
                addProgress(gamePlayer);
            }
        }
    }
}
