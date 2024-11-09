package cz.johnslovakia.skywars.quests.weekly;

import cz.johnslovakia.gameapi.economy.Economy;
import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.users.quests.PlayerQuestData;
import cz.johnslovakia.gameapi.users.quests.Quest;
import cz.johnslovakia.gameapi.users.quests.QuestType;
import cz.johnslovakia.gameapi.utils.eTrigger.Mapper;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;

public class AlwaysFirst implements Quest, Listener {

    @Override
    public QuestType getType() {
        return QuestType.WEEKLY;
    }

    @Override
    public String getName() {
        return "Always First";
    }

    @Override
    public Map<Economy, Integer> getRewards() {
        return Map.of(Economy.getEconomyByName("coins"), 120,
                Economy.getEconomyByName("tokens"), 80);
    }

    @Override
    public int getCompletionGoal() {
        return 12;
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        Trigger<GamePlayerDeathEvent> trigger = new Trigger<>(GamePlayerDeathEvent.class,
                new Mapper.SingleMapper<>(GamePlayerDeathEvent::getKiller),
                event -> event.getKiller() != null && event.isFirstGameKill() && event.getKiller().getPlayerData().getQuestsByStatus(PlayerQuestData.Status.IN_PROGRESS).contains(this),
                this::addProgress);

        return Set.of(trigger);
    }
}
