package cz.johnslovakia.skywars.perks;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.game.perk.Perk;
import cz.johnslovakia.gameapi.game.perk.PerkLevel;
import cz.johnslovakia.gameapi.game.perk.PerkType;
import cz.johnslovakia.gameapi.users.PlayerData;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class LuckyCharm implements Perk {

    @Override
    public String getName() {
        return "Lucky Charm";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.GOLDEN_APPLE);
    }

    @Override
    public PerkType getType() {
        return PerkType.CHANCE;
    }

    @Override
    public List<PerkLevel> getLevels() {
        PerkLevel level1 = new PerkLevel(1, Util.getPrice("perk.lucky_charm.level1", 500), 5);
        PerkLevel level2 = new PerkLevel(2, Util.getPrice("perk.lucky_charm.level2", 650), 10);
        PerkLevel level3 = new PerkLevel(3, Util.getPrice("perk.lucky_charm.level3", 900), 15);
        PerkLevel level4 = new PerkLevel(4, Util.getPrice("perk.lucky_charm.level4", 1200), 20);
        PerkLevel level5 = new PerkLevel(5, Util.getPrice("perk.lucky_charm.level5", 1600), 25);

        return List.of(level1, level2, level3, level4, level5);
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        Trigger<GamePlayerDeathEvent> trigger = new Trigger<>(GamePlayerDeathEvent.class,
                GamePlayerDeathEvent::getKiller,
                event -> event.getKiller() != null,
                gamePlayer -> {
                    Player player = gamePlayer.getOnlinePlayer();
                    PlayerData data = gamePlayer.getPlayerData();

                    if (data.hasPerk(this)){
                        PerkLevel level = data.getPerkLevel(this);

                        double randomValue = Math.random();

                        if (randomValue < (level.improvement() / 100.0)) {
                            player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE).toItemStack());
                        }
                    }
                });

        return Set.of(trigger);
    }
}
