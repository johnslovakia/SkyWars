package cz.johnslovakia.skywars.perks;

import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.game.perk.Perk;
import cz.johnslovakia.gameapi.game.perk.PerkLevel;
import cz.johnslovakia.gameapi.game.perk.PerkType;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerData;
import cz.johnslovakia.gameapi.utils.Sounds;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class VoidScavenger implements Perk {

    @Override
    public String getName() {
        return "Void scavenger";
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
        PerkLevel level1 = new PerkLevel(1, Util.getPrice("perk.void_scavenger.level1", 425), 4);
        PerkLevel level2 = new PerkLevel(2, Util.getPrice("perk.void_scavenger.level2", 500), 8);
        PerkLevel level3 = new PerkLevel(3, Util.getPrice("perk.void_scavenger.level3", 600), 12);
        PerkLevel level4 = new PerkLevel(4, Util.getPrice("perk.void_scavenger.level4", 800), 16);
        PerkLevel level5 = new PerkLevel(5, Util.getPrice("perk.void_scavenger.level5", 925), 20);

        return List.of(level1, level2, level3, level4, level5);
    }

    @Override
    public Set<Trigger<?>> getTriggers() {
        return Set.of();
    }

    @EventHandler
    public void onkillerDeath(GamePlayerDeathEvent e) {
        GamePlayer killer = e.getKiller();
        GamePlayer dead = e.getPlayer();

        if (e.getKiller() != null && e.getKiller() != dead){
            if (!e.getDmgCause().equals(EntityDamageEvent.DamageCause.VOID)){
                return;
            }
            PlayerData data = killer.getPlayerData();

            if (data.hasPerk(this)){
                PerkLevel level = data.getPerkLevel(this);


                double randomValue = Math.random();

                if (randomValue < (level.improvement() / 100.0)) {
                    ItemStack[] items = dead.getOnlinePlayer().getInventory().getContents();
                    if (items.length == 0){
                        return;
                    }

                    Random random = new Random();
                    int randomIndex = random.nextInt(items.length);

                    ItemStack item = items[randomIndex];
                    if (item == null){
                        return;
                    }

                    killer.getOnlinePlayer().getInventory().addItem(item);
                    killer.getOnlinePlayer().playSound(killer.getOnlinePlayer().getLocation(), Sounds.LEVEL_UP.bukkitSound(), 20.0F, 20.0F);
                }
            }
        }
    }
}
