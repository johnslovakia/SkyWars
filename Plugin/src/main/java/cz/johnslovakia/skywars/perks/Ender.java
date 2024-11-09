package cz.johnslovakia.skywars.perks;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.game.perk.Perk;
import cz.johnslovakia.gameapi.game.perk.PerkLevel;
import cz.johnslovakia.gameapi.game.perk.PerkType;
import cz.johnslovakia.gameapi.users.PlayerData;
import cz.johnslovakia.gameapi.utils.eTrigger.Mapper;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class Ender implements Perk {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ENDER_PEARL);
    }

    @Override
    public PerkType getType() {
        return PerkType.CHANCE;
    }

    @Override
    public List<PerkLevel> getLevels() {
        PerkLevel level1 = new PerkLevel(1, Util.getPrice("perk.ender.level1", 800), 2);
        PerkLevel level2 = new PerkLevel(2, Util.getPrice("perk.ender.level2", 1200), 4);
        PerkLevel level3 = new PerkLevel(3, Util.getPrice("perk.ender.level3", 1600), 6);
        PerkLevel level4 = new PerkLevel(4, Util.getPrice("perk.ender.level4", 2000), 8);

        return List.of(level1, level2, level3, level4);
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        Trigger<GamePlayerDeathEvent> trigger = new Trigger<>(GamePlayerDeathEvent.class,
                new Mapper.SingleMapper<>(GamePlayerDeathEvent::getKiller),
                event -> event.getKiller() != null,
                gamePlayer -> {
                    Player player = gamePlayer.getOnlinePlayer();
                    PlayerData data = gamePlayer.getPlayerData();

                    if (data.hasPerk(this)){
                        PerkLevel level = data.getPerkLevel(this);

                        double randomValue = Math.random();

                        if (randomValue < (level.improvement() / 100.0)) {
                            player.getInventory().addItem(XMaterial.ENDER_PEARL.parseItem());
                        }
                    }
                });

        return Set.of(trigger);
    }
}
