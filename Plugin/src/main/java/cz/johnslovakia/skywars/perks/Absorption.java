package cz.johnslovakia.skywars.perks;

import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.game.perk.Perk;
import cz.johnslovakia.gameapi.game.perk.PerkLevel;
import cz.johnslovakia.gameapi.game.perk.PerkType;
import cz.johnslovakia.gameapi.users.PlayerData;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Set;

public class Absorption implements Perk {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.GOLDEN_APPLE);
    }

    @Override
    public PerkType getType() {
        return PerkType.SECONDS;
    }

    @Override
    public List<PerkLevel> getLevels() {
        PerkLevel level1 = new PerkLevel(1, Util.getPrice("perk.absorption.level1", 450), 4);
        PerkLevel level2 = new PerkLevel(2, Util.getPrice("perk.absorption.level2", 600), 6);
        PerkLevel level3 = new PerkLevel(3, Util.getPrice("perk.absorption.level3", 800), 8);
        PerkLevel level4 = new PerkLevel(4, Util.getPrice("perk.absorption.level4", 900), 10);

        return List.of(level1, level2, level3, level4);
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

                        PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, level.improvement() * 20, 1);
                        gamePlayer.getOnlinePlayer().addPotionEffect(effect);
                    }
                });

        return Set.of(trigger);
    }
}
