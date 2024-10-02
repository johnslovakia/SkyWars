package cz.johnslovakia.skywars.quests.weekly;

import cz.johnslovakia.gameapi.economy.Economy;
import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.events.PlayerScoreEvent;
import cz.johnslovakia.gameapi.users.quests.PlayerQuestData;
import cz.johnslovakia.gameapi.users.quests.Quest;
import cz.johnslovakia.gameapi.users.quests.QuestType;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;

public class TopKiller implements Quest, Listener {

    @Override
    public QuestType getType() {
        return QuestType.WEEKLY;
    }

    @Override
    public String getName() {
        return "Top Killer";
    }

    @Override
    public Map<Economy, Integer> getRewards() {
        return Map.of(Economy.getEconomyByName("coins"), 120,
                Economy.getEconomyByName("tokens"), 80);
    }

    @Override
    public int getCompletionGoal() {
        return 3;
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        Trigger<PlayerScoreEvent> trigger = new Trigger<>(PlayerScoreEvent.class,
                PlayerScoreEvent::getGamePlayer,
                event -> event.getGamePlayer() != null && event.getScore().getName().equalsIgnoreCase("1stKiller") && event.getGamePlayer().getPlayerData().getQuestsByStatus(PlayerQuestData.Status.IN_PROGRESS).contains(this),
                this::addProgress);

        return Set.of(trigger);
    }
}
