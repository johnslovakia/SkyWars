package cz.johnslovakia.skywars.quests.weekly;

import cz.johnslovakia.gameapi.economy.Economy;
import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.users.quests.PlayerQuestData;
import cz.johnslovakia.gameapi.users.quests.Quest;
import cz.johnslovakia.gameapi.users.quests.QuestType;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import cz.johnslovakia.skywars.events.ChestLootEvent;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.Set;

public class MeteoriteDiscoverer implements Quest, Listener {

    @Override
    public QuestType getType() {
        return QuestType.WEEKLY;
    }

    @Override
    public String getName() {
        return "Meteorite Discoverer";
    }

    @Override
    public Map<Economy, Integer> getRewards() {
        return Map.of(Economy.getEconomyByName("coins"), 120,
                Economy.getEconomyByName("tokens"), 80);
    }

    @Override
    public int getCompletionGoal() {
        return 1;
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        Trigger<ChestLootEvent> trigger = new Trigger<>(ChestLootEvent.class,
                ChestLootEvent::getGamePlayer,
                event -> event.getGamePlayer() != null && event.getGamePlayer().getPlayerData().getQuestsByStatus(PlayerQuestData.Status.IN_PROGRESS).contains(this),
                this::addProgress);

        return Set.of(trigger);
    }
}
