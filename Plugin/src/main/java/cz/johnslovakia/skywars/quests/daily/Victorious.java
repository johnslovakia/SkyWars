package cz.johnslovakia.skywars.quests.daily;

import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.economy.Economy;
import cz.johnslovakia.gameapi.events.GameEndEvent;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.quests.PlayerQuestData;
import cz.johnslovakia.gameapi.users.quests.Quest;
import cz.johnslovakia.gameapi.users.quests.QuestType;
import cz.johnslovakia.gameapi.utils.eTrigger.Mapper;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;

public class Victorious implements Quest, Listener {
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
                Economy.getEconomyByName("tokens"), 45);
    }

    @Override
    public int getCompletionGoal() {
        return 3;
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        Trigger<GameEndEvent> trigger = new Trigger<>(GameEndEvent.class,
                new Mapper.SingleMapper<>(event -> (GamePlayer) event.getWinner()),
                event -> event.getWinner() != null && ((GamePlayer)event.getWinner()).getPlayerData().getQuestsByStatus(PlayerQuestData.Status.IN_PROGRESS).contains(this),
                this::addProgress);

        return Set.of(trigger);
    }
}
