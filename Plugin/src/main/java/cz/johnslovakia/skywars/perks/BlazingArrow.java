package cz.johnslovakia.skywars.perks;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.game.perk.Perk;
import cz.johnslovakia.gameapi.game.perk.PerkLevel;
import cz.johnslovakia.gameapi.game.perk.PerkType;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerData;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class BlazingArrow implements Perk, Listener {

    @Override
    public String getName() {
        return "Blazing arrow";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    public PerkType getType() {
        return PerkType.CHANCE;
    }

    @Override
    public List<PerkLevel> getLevels() {
        PerkLevel level1 = new PerkLevel(1, Util.getPrice("perk.blazing_arrow.level1", 500), 5);
        PerkLevel level2 = new PerkLevel(2, Util.getPrice("perk.blazing_arrow.level2", 650), 10);
        PerkLevel level3 = new PerkLevel(3, Util.getPrice("perk.blazing_arrow.level3", 800), 15);
        PerkLevel level4 = new PerkLevel(4, Util.getPrice("perk.blazing_arrow.level4", 950), 20);
        PerkLevel level5 = new PerkLevel(5, Util.getPrice("perk.blazing_arrow.level5", 1100), 25);

        return List.of(level1, level2, level3, level4, level5);
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        return Set.of();
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getShooter() instanceof Player && projectile.getType().equals(EntityType.ARROW)) {
            Player shooter = (Player) projectile.getShooter();
            GamePlayer gamePlayer = PlayerManager.getGamePlayer(shooter);
            PlayerData data = gamePlayer.getPlayerData();

            if (data.hasPerk(this)){
                PerkLevel level = data.getPerkLevel(this);

                double randomValue = Math.random();

                if (randomValue < (level.improvement() / 100.0)) {
                    event.getEntity().setFireTicks(Integer.MAX_VALUE);
                }
            }
        }
    }
}
