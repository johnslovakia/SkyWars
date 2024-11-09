package cz.johnslovakia.skywars.structures;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.game.map.GameMap;
import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.gameapi.utils.RandomUtils;
import cz.johnslovakia.gameapi.utils.Sounds;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.events.ChestLootEvent;
import cz.johnslovakia.skywars.items.*;
import cz.johnslovakia.skywars.utils.Util;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.EnderChest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.*;

@Getter
public class Meteorite implements Listener {

    public static List<Meteorite> meteorites = new ArrayList<>();

    public static Meteorite getMeteorite(Game game){
        for (Meteorite meteorite : meteorites){
            if (meteorite.getGame().equals(game)){
                return meteorite;
            }
        }
        return null;
    }

    Game game;
    Location location;
    Chest chest;
    boolean opened;

    public Meteorite(Game game){
        this.game = game;
        this.location = findRandomSafeMeteoriteLocation();
        meteorites.add(this);
        registerItems();

        Bukkit.getPluginManager().registerEvents(this, SkyWars.getInstance());
    }

    public Location getHighestBlock(Location checkLocation){
        for (int y = 256; y > 0; y--){
            Location loc = new Location(checkLocation.getWorld(), checkLocation.getX(), y, checkLocation.getZ());
            Block block = loc.getBlock();
            if (block.getType().equals(XMaterial.AIR.parseMaterial())) {
                continue;
            }
            if (!loc.getChunk().isLoaded()){
                loc.getChunk().load();
            }

            if (!(block.getType() == Material.AIR ||
                    block.getType().toString().toUpperCase().contains("LOG") ||
                    block.getType().toString().toUpperCase().contains("WOOD") ||
                    block.getType().toString().toUpperCase().contains("LEAVES"))) {
                return block.getLocation();
            }
        }
        return checkLocation;
    }

    public Location findRandomSafeMeteoriteLocation() {
        GameMap map = game.getPlayingMap();
        Location center = map.getMainArea().getCenter();
        int radius = 40;
        Random random = new Random();

        while (true) {
            int dx = random.nextInt((2 * radius) + 1) - radius;
            int dz = random.nextInt((2 * radius) + 1) - radius;

            Location checkLocation = center.clone().add(dx, 0, dz);
            Location highestBlock = getHighestBlock(checkLocation);//checkLocation.getWorld().getHighestBlockAt(checkLocation).getLocation();

            if (isSafe3x3Area(highestBlock)) {
                return highestBlock;
            }
        }
    }

    private boolean isSafe3x3Area(Location startLocation) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block upperLayer = startLocation.clone().add(x, 0, z).getBlock();
                Block lowerLayer = startLocation.clone().add(x, -1, z).getBlock();

                if (upperLayer.getType() == Material.AIR || lowerLayer.getType() == Material.AIR
                        || upperLayer.getType().toString().toUpperCase().contains("LOG") || lowerLayer.getType().toString().toUpperCase().contains("LOG")
                        || upperLayer.getType().toString().toUpperCase().contains("WOOD") || lowerLayer.getType().toString().toUpperCase().contains("WOOD")
                        || upperLayer.getType().toString().toUpperCase().contains("LEAVES") || lowerLayer.getType().toString().toUpperCase().contains("LEAVES")) {
                    return false;
                }
            }
        }
        return true;
    }

    private static final List<Material> type = Arrays.asList(Material.DIAMOND_ORE,
            Material.IRON_ORE,
            Material.IRON_ORE,
            Material.OBSIDIAN,
            Material.OBSIDIAN);


    public void spawn(){
        if (location == null){
            return;
        }
        location.getChunk().load();
        World world = location.getWorld();

        List<Block> change = new ArrayList<>();
        int radius = 3;

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

                    if ((x == location.getBlockX() - radius || x == location.getBlockX() + radius
                        || z == location.getBlockZ() - radius || z == location.getBlockZ() + radius) && Math.random() < 0.3){
                        change.add(loc.clone().add(Util.getRandom(1, 2), Util.getRandom(0, 1), Util.getRandom(1, 2)).getBlock());
                    }
                }
            }
        }

        for (Block block : change){
            double d = new Random().nextDouble();
            if (d <= 0.10) {
                Collections.shuffle(type);
                block.setType(type.get(0));

                if (block.getType().equals(Material.OBSIDIAN)){
                    block.getLocation().add(0, 1, 0).getBlock().setType(Material.FIRE);
                }else{
                    if (Math.random() < 0.95){
                        block.getLocation().add(0, 1, 0).getBlock().setType(Material.FIRE);
                    }
                }
            }else if (d <= 0.190) {
                block.setType(Material.AIR);
            }else{
                block.setType(Math.random() < 0.85 ?
                        (block.getRelative(BlockFace.UP).getType().equals(Material.AIR) && block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType().equals(Material.AIR) ? (Math.random() < 0.75 ? Material.STONE : Material.STONE_SLAB) : Material.STONE) : (Math.random() < 0.85 ? Material.BLACKSTONE : Material.BLACK_GLAZED_TERRACOTTA));
            }
        }


        for (int y = 0; y < 256; y++) {
            Block above = location.clone().add(0, y, 0).getBlock();
            Block above2 = location.clone().add(0, y + 1, 0).getBlock();

            if (above.getType() == Material.AIR && above2.getType() == Material.AIR) {

                above.getLocation().subtract(0, 1, 0).getBlock().setType(Material.CHISELED_POLISHED_BLACKSTONE);
                above.setType(Material.CHEST);


                chest = (Chest) above.getLocation().getBlock().getState();

                List<Integer> slots = new ArrayList<>();
                for (int s = 0; s < chest.getBlockInventory().getSize(); s++){
                    slots.add(s);
                }

                List<ItemStack> list = new ArrayList<>(items);
                for (int i = 0; i < RandomUtils.randomInteger(3, 4); i++) {
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

                Collections.shuffle(special_items);
                chest.getBlockInventory().setItem(slots.get(0), special_items.get(0));


                break;
            }
        }

        game.getParticipants().forEach(gp -> MessageManager.get(gp, "chat.meteorit_spawn")
                .replace("%x%", "" + (int) location.getX())
                .replace("%z%", "" + (int) location.getZ())
                .send());
        game.getParticipants().forEach(gp -> gp.getOnlinePlayer().playSound(gp.getOnlinePlayer().getLocation(), Sounds.EXPLODE.bukkitSound(), 20.0F, 20.0F));
    }


    @EventHandler
    public void onBlockFade(BlockFadeEvent e) {
        if (e.getBlock().getType() == Material.FIRE) {
            double distance = e.getBlock().getLocation().distance(location);

            if (distance <= 3) {
                e.setCancelled(true);
            }
        }
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

            XParticle.magicCircles(2, 2, 2, 1, ParticleDisplay.of(Particle.LANDING_OBSIDIAN_TEAR).withLocation(location.clone().add(0.5, 0, 0.5))).run();

            Firework fw = (Firework) location.getWorld().spawnEntity(location.clone().add(0.5, 0, 0.5), EntityType.FIREWORK_ROCKET);
            FireworkMeta fwm = fw.getFireworkMeta();
            fwm.setPower(1);
            fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).build());
            fw.setFireworkMeta(fwm);
            fw.detonate();

            game.getParticipants().forEach(fgp -> MessageManager.get(fgp, "chat.meteorit_was_found")
                    .send());

            ChestLootEvent ev = new ChestLootEvent(gp, this);
            Bukkit.getPluginManager().callEvent(ev);
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

            if (chest.getBlockInventory().getContents().length == 0){
                location.getBlock().setType(Material.AIR);
            }
        }
    }


    private static final List<ItemStack> items = new ArrayList<>();
    private static final List<ItemStack> special_items = new ArrayList<>();

    public void registerItems(){
        items.addAll(Arrays.asList(new ItemStack(Material.NETHERITE_SWORD),
                        new ItemBuilder(XMaterial.NETHERITE_BOOTS.parseMaterial()).addEnchant(Enchantment.PROTECTION, RandomUtils.randomInteger(1, 2)).toItemStack(),
                        new ItemStack(Material.NETHERITE_LEGGINGS),
                        new ItemBuilder(XMaterial.TNT.parseMaterial(), RandomUtils.randomInteger(2, 4)).toItemStack(),
                        new ItemBuilder(XMaterial.COBWEB.parseMaterial(), RandomUtils.randomInteger(2, 3)).toItemStack(),
                        new ItemBuilder(XMaterial.GOLDEN_APPLE.parseMaterial(), RandomUtils.randomInteger(1, 2)).toItemStack(),
                        new ItemBuilder(XMaterial.COBWEB.parseMaterial(), RandomUtils.randomInteger(2, 4)).toItemStack(),
                        new ItemBuilder(XMaterial.ENDER_PEARL.parseMaterial(), RandomUtils.randomInteger(1, 2)).toItemStack(),
                        new ItemBuilder(XMaterial.ENCHANTED_BOOK.parseMaterial()).addStoragedEnchantment(Enchantment.PROTECTION, RandomUtils.randomInteger(2, 3)).toItemStack(),
                        new ItemBuilder(XMaterial.ENCHANTED_BOOK.parseMaterial()).addStoragedEnchantment(Enchantment.SHARPNESS, 2).toItemStack(),
                        new ItemBuilder(XMaterial.ENCHANTED_BOOK.parseMaterial()).addStoragedEnchantment(Enchantment.FIRE_ASPECT, RandomUtils.randomInteger(1, 2)).toItemStack(),
                        new ItemBuilder(XMaterial.ENCHANTED_BOOK.parseMaterial()).addStoragedEnchantment(Enchantment.POWER, 2).toItemStack(),
                        new ItemBuilder(XMaterial.STICK.parseMaterial(), RandomUtils.randomInteger(3, 5)).toItemStack(),
                        new ItemBuilder(XMaterial.EXPERIENCE_BOTTLE.parseMaterial(), RandomUtils.randomInteger(12, 28)).toItemStack(),
                        new ItemBuilder(XMaterial.ANVIL.parseMaterial()).toItemStack()));

        ItemStack harmingPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) harmingPotion.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setBasePotionType(PotionType.HARMING);
            harmingPotion.setItemMeta(potionMeta);
        }
        items.add(harmingPotion);

        ItemStack poisonArrow = new ItemStack(Material.TIPPED_ARROW, Util.getRandom(2, 4));
        PotionMeta poisonArrowMeta = (PotionMeta) poisonArrow.getItemMeta();
        poisonArrowMeta.setBasePotionType(PotionType.POISON);
        poisonArrow.setItemMeta(poisonArrowMeta);
        items.add(poisonArrow);

        special_items.add(new ItemBuilder(SparkOfLevitationItem.getSparkOfLevitationItem().getFinalItemStack(), Util.getRandom(1, 2)).toItemStack());
        special_items.add(new ItemBuilder(SparkOfInvisibilityItem.getSparkOfInvisibilityItem().getFinalItemStack(), Util.getRandom(1, 2)).toItemStack());
        special_items.add(new ItemBuilder(ToxicGrenadeItem.getToxicGrenadeItem().getFinalItemStack(), Util.getRandom(1, 2)).toItemStack());
        special_items.add(NetherShield.getNetherShieldItem().getFinalItemStack());
        special_items.add(GhostPearl.getGhostPearlItem().getFinalItemStack());
    }


}
