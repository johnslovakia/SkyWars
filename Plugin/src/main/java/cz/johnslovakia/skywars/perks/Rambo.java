package cz.johnslovakia.skywars.perks;

import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.game.perk.Perk;
import cz.johnslovakia.gameapi.game.perk.PerkLevel;
import cz.johnslovakia.gameapi.game.perk.PerkType;
import cz.johnslovakia.gameapi.users.PlayerData;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

public class Rambo implements Perk {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.POTION, 1, (byte) 8201);
    }

    @Override
    public PerkType getType() {
        return PerkType.SECONDS;
    }

    @Override
    public List<PerkLevel> getLevels() {
        PerkLevel level1 = new PerkLevel(1, Util.getPrice("perk.rambo.level1", 800), 1);
        PerkLevel level2 = new PerkLevel(2, Util.getPrice("perk.rambo.level2", 1500), 2);
        PerkLevel level3 = new PerkLevel(3, Util.getPrice("perk.rambo.level3", 2000), 3);

        return List.of(level1, level2, level3);
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        Trigger<GamePlayerDeathEvent> trigger = new Trigger<>(GamePlayerDeathEvent.class,
                GamePlayerDeathEvent::getKiller,
                event -> event.getKiller() != null,
                gamePlayer -> {
                    PlayerData data = gamePlayer.getPlayerData();

                    if (data.hasPerk(this)){
                        PerkLevel level = data.getPerkLevel(this);

                        PotionEffect effect = new PotionEffect(PotionEffectType.STRENGTH, level.improvement() * 20, 1);
                        gamePlayer.getOnlinePlayer().addPotionEffect(effect);
                    }
                });

        return Set.of(trigger);
    }
}
