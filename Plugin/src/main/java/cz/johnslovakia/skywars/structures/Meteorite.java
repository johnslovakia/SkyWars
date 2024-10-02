package cz.johnslovakia.skywars.structures;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.gameapi.utils.RandomUtils;
import cz.johnslovakia.gameapi.utils.Sounds;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.events.ChestLootEvent;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Getter
public class Meteorite implements Listener {

    Game game;
    Location location;
    Chest chest;
    boolean opened;

    public Meteorite(Game game){
        this.game = game;
        //TODO: najit lokaci

        Bukkit.getPluginManager().registerEvents(this, SkyWars.getInstance());
    }

    private static final List<Material> type = Arrays.asList(Material.DIAMOND_ORE,
            Material.IRON_ORE,
            Material.IRON_ORE,
            Material.OBSIDIAN);


    public void spawn(){
        if (location == null){
            return;
        }
        location.getChunk().load();
        World world = location.getWorld();

        List<Block> change = new ArrayList<>();
        int radius = 5;

        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (!entity.getType().equals(EntityType.PLAYER)) {
                entity.remove();
            }
        }


        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    double distance = Math.sqrt(Math.pow(x - location.getBlockX(), 2) + Math.pow(y - location.getBlockY(), 2) + Math.pow(z - location.getBlockZ(), 2));
                    Location loc = new Location(world, x, y, z);

                    try{
                        if (!loc.getChunk().isLoaded()){
                            loc.getChunk().load();
                        }
                    }catch (Exception ignored){}

                    if (loc.getBlock().getType().equals(Material.AIR)){
                        continue;
                    }

                    if (distance <= radius) {
                        change.add(loc.getBlock());
                    }
                }
            }
        }

        new BukkitRunnable(){
            @Override
            public void run() {
                for (Block block : change){
                    double d = new Random().nextDouble();
                    if (d <= 0.09) {
                        Collections.shuffle(type);
                        block.setType(type.get(0));

                        if (block.getType().equals(Material.OBSIDIAN)){
                            block.getLocation().add(0, 1, 0).getBlock().setType(Material.FIRE);
                        }
                    }else if (d <= 0.135) {
                        block.setType(Material.AIR);
                        }
                    else {
                        block.setType(Material.AIR);
                    }
                }
            }
        }.runTaskTimer(SkyWars.getInstance(), 0L, 1L);


        for (int y = 0; y < 256; y++) {
            Block above = location.add(0, y, 0).getBlock();
            Block above2 = location.add(0, y + 1, 0).getBlock();

            if (above.getType() == Material.AIR && above2.getType() == Material.AIR) {
                above.setType(Material.CHEST);

                chest = (Chest) above.getLocation().getBlock().getState();


                List<Integer> slots = new ArrayList<>();
                for (int s = 0; s < chest.getBlockInventory().getSize(); s++){
                    slots.add(s);
                }

                List<ItemStack> list = new ArrayList<>(items);
                for (int i = 0; i < RandomUtils.randomInteger(3, 5); i++) {
                    Collections.shuffle(slots);
                    int chestSlot = slots.get(0);

                    Collections.shuffle(list);
                    ItemStack item = list.get(0);
                    chest.getBlockInventory().setItem(chestSlot, item);
                    slots.remove(0);

                    if (item.getType().toString().toLowerCase().contains("netherite_")){
                        list.remove(item);
                    }
                }
            }
        }

        game.getParticipants().forEach(gp -> MessageManager.get(gp, "chat.meteorit_spawn")
                .replace("%x%", "" + (int) location.getX())
                .replace("%z%", "" + (int) location.getZ())
                .send());
        game.getParticipants().forEach(gp -> gp.getOnlinePlayer().playSound(gp.getOnlinePlayer().getLocation(), Sounds.EXPLODE.bukkitSound(), 20.0F, 20.0F));
    }


    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        GamePlayer gp = PlayerManager.getGamePlayer(player);
        Game game = gp.getPlayerData().getGame();

        if (game == null || game.getState() != GameState.INGAME){
            return;
        }

        if (e.getInventory().getType() == InventoryType.CHEST && e.getInventory().getLocation() != null){
            Location location = e.getInventory().getLocation();
            Chest chest = (Chest) location.getBlock().getState();

            if (!chest.equals(getChest())){
                return;
            }
            if (opened){
                return;
            }

            XParticle.magicCircles(2, 2, 2, 2, ParticleDisplay.of(Particle.DRIPPING_OBSIDIAN_TEAR).face(location)).run();
            game.getParticipants().forEach(fgp -> MessageManager.get(fgp, "chat.meteorit_was_found")
                    .send());

            opened = true;
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        GamePlayer gp = PlayerManager.getGamePlayer(player);
        Game game = gp.getPlayerData().getGame();

        if (game == null || game.getState() != GameState.INGAME){
            return;
        }

        if (e.getInventory().getType() == InventoryType.CHEST && e.getInventory().getLocation() != null){
            Location location = e.getInventory().getLocation();
            Chest chest = (Chest) location.getBlock().getState();

            if (!chest.equals(getChest())){
                return;
            }

            if (chest.getInventory().getContents().length == 0){
                location.getBlock().setType(Material.AIR);
            }

            ChestLootEvent ev = new ChestLootEvent(gp, this);
            Bukkit.getPluginManager().callEvent(ev);
        }
    }


    private static final List<ItemStack> items = Arrays.asList(new ItemStack(Material.NETHERITE_SWORD),
            new ItemBuilder(XMaterial.NETHERITE_BOOTS.parseMaterial()).addEnchant(Enchantment.PROTECTION, RandomUtils.randomInteger(1, 2)).toItemStack(),
            new ItemStack(Material.NETHERITE_LEGGINGS),
            new ItemBuilder(XMaterial.TNT.parseMaterial(), RandomUtils.randomInteger(2, 4)).toItemStack(),
            new ItemBuilder(XMaterial.GOLDEN_APPLE.parseMaterial(), RandomUtils.randomInteger(1, 2)).toItemStack(),
            new ItemBuilder(XMaterial.COBWEB.parseMaterial(), RandomUtils.randomInteger(2, 4)).toItemStack(),
            new ItemBuilder(XMaterial.ENDER_PEARL.parseMaterial(), RandomUtils.randomInteger(1, 2)).toItemStack(),
            new ItemBuilder(XMaterial.ENCHANTED_BOOK.parseMaterial()).addStoragedEnchantment(Enchantment.PROTECTION, RandomUtils.randomInteger(2, 3)).toItemStack(),
            new ItemBuilder(XMaterial.ENCHANTED_BOOK.parseMaterial()).addStoragedEnchantment(Enchantment.SHARPNESS, 3).toItemStack(),
            new ItemBuilder(XMaterial.ENCHANTED_BOOK.parseMaterial()).addStoragedEnchantment(Enchantment.FIRE_ASPECT, RandomUtils.randomInteger(1, 2)).toItemStack(),
            new ItemBuilder(XMaterial.ENCHANTED_BOOK.parseMaterial()).addStoragedEnchantment(Enchantment.POWER, 2).toItemStack(),
            new ItemBuilder(XMaterial.STICK.parseMaterial(), RandomUtils.randomInteger(3, 5)).toItemStack(),
            new ItemBuilder(XMaterial.EXPERIENCE_BOTTLE.parseMaterial(), RandomUtils.randomInteger(12, 28)).toItemStack(),
            new ItemBuilder(XMaterial.ANVIL.parseMaterial()).toItemStack());

}
