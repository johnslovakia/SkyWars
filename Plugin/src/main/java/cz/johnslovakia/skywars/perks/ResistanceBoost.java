package cz.johnslovakia.skywars.perks;

import cz.johnslovakia.gameapi.events.GameStartEvent;
import cz.johnslovakia.gameapi.game.perk.Perk;
import cz.johnslovakia.gameapi.game.perk.PerkLevel;
import cz.johnslovakia.gameapi.game.perk.PerkType;
import cz.johnslovakia.gameapi.users.GamePlayer;
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

public class ResistanceBoost implements Perk {

    @Override
    public String getName() {
        return "Resistance Boost";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.IRON_CHESTPLATE);
    }

    @Override
    public PerkType getType() {
        return PerkType.SECONDS;
    }

    @Override
    public List<PerkLevel> getLevels() {
        PerkLevel level1 = new PerkLevel(1, Util.getPrice("perk.resistance_boost.level1", 400), 15);
        PerkLevel level2 = new PerkLevel(2, Util.getPrice("perk.resistance_boost.level2", 500), 20);
        PerkLevel level3 = new PerkLevel(3, Util.getPrice("perk.resistance_boost.level2", 725), 25);

        return List.of(level1, level2, level3);
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        return Set.of();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {

        for (GamePlayer gamePlayer : e.getGame().getPlayers()) {
            PlayerData data = gamePlayer.getPlayerData();

            if (data.hasPerk(this)) {
                PerkLevel level = data.getPerkLevel(this);

                PotionEffect effect = new PotionEffect(PotionEffectType.RESISTANCE, level.improvement() * 20, 1);
                gamePlayer.getOnlinePlayer().addPotionEffect(effect);
            }
        }
    }
}
