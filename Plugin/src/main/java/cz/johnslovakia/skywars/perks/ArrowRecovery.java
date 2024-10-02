package cz.johnslovakia.skywars.perks;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.game.perk.Perk;
import cz.johnslovakia.gameapi.game.perk.PerkLevel;
import cz.johnslovakia.gameapi.game.perk.PerkType;
import cz.johnslovakia.gameapi.users.PlayerData;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class ArrowRecovery implements Perk {

    @Override
    public String getName() {
        return "Arrow Recovery";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ARROW);
    }

    @Override
    public PerkType getType() {
        return PerkType.CHANCE;
    }

    @Override
    public List<PerkLevel> getLevels() {
        PerkLevel level1 = new PerkLevel(1, Util.getPrice("perk.arrow_recovery.level1", 500), 5);
        PerkLevel level2 = new PerkLevel(2, Util.getPrice("perk.arrow_recovery.level2", 650), 10);
        PerkLevel level3 = new PerkLevel(3, Util.getPrice("perk.arrow_recovery.level3", 800), 15);
        PerkLevel level4 = new PerkLevel(4, Util.getPrice("perk.arrow_recovery.level4", 950), 20);
        PerkLevel level5 = new PerkLevel(5, Util.getPrice("perk.arrow_recovery.level5", 1100), 25);

        return List.of(level1, level2, level3, level4, level5);
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        Trigger<ProjectileLaunchEvent> trigger = new Trigger<>(ProjectileLaunchEvent.class,
                event -> PlayerManager.getGamePlayer((Player) event.getEntity().getShooter()),
                event -> event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player,
                gamePlayer -> {
                    Player player = gamePlayer.getOnlinePlayer();
                    PlayerData data = gamePlayer.getPlayerData();

                    if (data.hasPerk(this)){
                        PerkLevel level = data.getPerkLevel(this);

                        double randomValue = Math.random();

                        if (randomValue < (level.improvement() / 100.0)) {
                            player.getInventory().addItem(XMaterial.ARROW.parseItem());
                        }
                    }
                });

        return Set.of(trigger);
    }
}
