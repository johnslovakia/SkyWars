package cz.johnslovakia.skywars.quests.daily;

import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.economy.Economy;
import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.users.quests.PlayerQuestData;
import cz.johnslovakia.gameapi.users.quests.Quest;
import cz.johnslovakia.gameapi.users.quests.QuestType;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;
import java.util.Set;

public class Bloodthirsty implements Quest, Listener {

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
        return Map.of(Economy.getEconomyByName("coins"), 50,
                Economy.getEconomyByName("tokens"), 35);
    }

    @Override
    public int getCompletionGoal() {
        return 15;
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        Trigger<GamePlayerDeathEvent> trigger = new Trigger<>(GamePlayerDeathEvent.class,
                GamePlayerDeathEvent::getKiller,
                event -> event.getKiller() != null && event.getKiller().getPlayerData().getQuestsByStatus(PlayerQuestData.Status.IN_PROGRESS).contains(this),
                this::addProgress);

        return Set.of(trigger);
    }
}
