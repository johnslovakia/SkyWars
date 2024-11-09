package cz.johnslovakia.skywars.chest;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.game.Game;

import cz.johnslovakia.gameapi.game.map.GameMap;
import cz.johnslovakia.gameapi.game.map.MapLocation;
import cz.johnslovakia.gameapi.utils.ConfigAPI;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.gameapi.utils.Logger;
import cz.johnslovakia.gameapi.utils.StringUtils;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.items.HealingSoup;
import cz.johnslovakia.skywars.items.RunItem;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.block.Chest;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import static java.util.Map.Entry.comparingByValue;
import static java.util.Comparator.comparingInt;

import java.util.*;
import java.util.stream.Collectors;


//TODO: rewrite
@Getter
public class ChestManager {

    private static List<ChestManager> chestManagers = new ArrayList<>();
    private Game game;

    private Map<String, List<Chest>> islandChests = new HashMap<>();
    private Set<Chest> rareChests = new HashSet<>();
    private Set<Chest> opened = new HashSet<>();

    @Getter
    private List<ChestItem> normalItems = new ArrayList<>();
    @Getter
    private List<ChestItem> rareItems = new ArrayList<>();


    public ChestManager(Game game){
        this.game = game;
        loadChestItems();
        chestManagers.add(this);
    }


    public void loadChests(GameMap map){
        ConfigAPI mConfig = DataHandler.getMapsConfig();

        if (mConfig.getConfig().getConfigurationSection("maps." + map.getName() + ".islands") != null) {
            for (String island : mConfig.getConfig().getConfigurationSection("maps." + map.getName() + ".islands").getKeys(false)) {
                if (mConfig.getConfig().get("maps." + map.getName() + ".islands." + island + ".chest1") == null
                        || mConfig.getConfig().get("maps." + map.getName() + ".islands." + island + ".chest2") == null
                        || mConfig.getConfig().get("maps." + map.getName() + ".islands." + island + ".chest3") == null){
                    Logger.log("Island " + island +" has improperly set chests!", Logger.LogType.ERROR);
                }
                List<MapLocation> chests = Arrays.asList(mConfig.getMapLocation(island + ".chest1", "maps." + map.getName() + ".islands." + island + ".chest1"),
                        mConfig.getMapLocation(island + ".chest1", "maps." + map.getName() + ".islands." + island + ".chest2"),
                        mConfig.getMapLocation(island + ".chest1", "maps." + map.getName() + ".islands." + island + ".chest3"));

                for (MapLocation location : chests) {
                    Block block = location.getLocationForMap(map).getBlock();

                    BlockState state = block.getState();
                    if (state instanceof Chest) {
                        Chest chest = (Chest) block.getState();

                        if (islandChests.get(island) == null) {
                            islandChests.put(island, List.of(chest));
                        } else {
                            if (!islandChests.get(island).contains(chest)) {
                                List<Chest> list = new ArrayList<>(islandChests.get(island));
                                list.add(chest);
                                islandChests.put(island, list);
                            }
                        }
                    }
                }
            }
        }


        if (mConfig.getConfig().getConfigurationSection("maps." + map.getName() + ".rareChests") != null) {
            for (String key : mConfig.getConfig().getConfigurationSection("maps." + map.getName() + ".rareChests").getKeys(false)) {
                if (mConfig.getConfig().getString("maps." + map.getName() + ".rareChests." + key + ".type") != null) {
                    if (mConfig.getLocation("maps." + map.getName() + ".rareChests." + key + ".location") != null) {
                        if (mConfig.getConfig().getString("maps." + map.getName() + ".rareChests." + key + ".type").equalsIgnoreCase("rare")) {
                            MapLocation location = mConfig.getMapLocation(key, "maps." + map.getName() + ".rareChests." + key + ".location");

                            Block block = location.getLocationForMap(map).getBlock();
                            BlockState state = block.getState();
                            if (state instanceof Chest) {
                                Chest chest = (Chest) block.getState();
                                rareChests.add(chest);
                            }
                        }
                    }
                }
            }
        }
    }

    private Chest getChest(Map<Chest, List<ChestItem>> chestsItems){

        if (chestsItems.isEmpty()){
            return null;
        }
        List<Chest> chest = new ArrayList<>();
        if (!chestsItems.isEmpty()) {
            chestsItems.entrySet()
                    .stream()
                    .sorted(comparingByValue(comparingInt(List::size)))
                    .limit(1)
                    .forEach(e -> {
                        chest.add(e.getKey());
                    });
        }
        return chest.get(0);
    }

    public <K,V extends Collection> Map<K,V> sortMap(Map<K,V> map){
        return map.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void fillChests() {
        for (String island : getIslandChests().keySet()) {
            List<Chest> chests = getIslandChests().get(island);
            if (chests.isEmpty()){
                SkyWars.getInstance().getLogger().warning("Fill Chests Error: Island:" + island);
                return;
            }
            List<ChestItem> items = new ArrayList<>();

            Map<Chest, List<ChestItem>> chests_items = new HashMap<>();
            for (Chest chest : chests) {
                List<ChestItem> l = new ArrayList();
                chests_items.put(chest, l);
            }

            List<ChestItem> mustBeInserted = new ArrayList<>();

            for (ChestItem item : getNormalItems()) {
                if (item.mustBeInserted()) {
                    mustBeInserted.add(item);
                }
            }

            List<ChestItem> armorItemsClone = new ArrayList<>(armorItems);

            for (int i = 0; i <= 2; i++){
                Collections.shuffle(armorItemsClone);

                if (armorItemsClone.get(0) == null){
                    break;
                }

                ChestItem chestItem = armorItemsClone.get(0);
                String armorType = chestItem.getItem().getType().toString().split("_")[1].toLowerCase();
                mustBeInserted.add(chestItem);
                armorItemsClone.removeIf(item -> item.getItem().getType().toString().toLowerCase().contains(armorType));
            }


            Collections.shuffle(armsItems);
            mustBeInserted.add(armsItems.get(0));

            for (ChestItem item : mustBeInserted) {
                Chest ch = getChest(chests_items);
                if (chests_items.get(ch) == null) {
                    chests_items.put(ch, Arrays.asList(item));
                } else {
                    if (!chests_items.get(ch).contains(item)) {
                        List<ChestItem> list = new ArrayList<>(chests_items.get(ch));
                        list.add(item);
                        chests_items.put(ch, list);
                    }
                }
                items.add(item);
            }

            //for (ChestItem item : getNormalItems()) {
            Map<ChestItem, Map<Integer, Integer>> chances = new HashMap<>();
            int i = 0;
            for (ChestItem chestItem : getNormalItems()) {
                if (!chestItem.mustBeInserted()) {
                    int chance = chestItem.getChance();
                    int c1 = i + 1;
                    int c2 = (i + 1) + chance;
                    Map<Integer, Integer> iMap = new HashMap<>();
                    iMap.put(c1, c2);
                    chances.put(chestItem, iMap);

                    i = c2;
                }
            }

            int toFill = 13; //Util.getRandom(15, 16); //13
            for (int x = 0; x < toFill; x++) {
                List<ChestItem> list2 = new ArrayList<>(getNormalItems());//.stream().filter(it -> it.getInsterted(island) >= it.getLimitInIsland()).toList();
                list2.removeIf(it -> (it.getLimitInIsland() != -1 && it.getInsterted(island) >= it.getLimitInIsland()));
                chances.keySet().removeIf(it -> (it.getLimitInIsland() != -1 && it.getInsterted(island) >= it.getLimitInIsland()));

                if (addUpTheChances(list2) != 0) {
                    int chance = new Random().nextInt(addUpTheChances(list2));

                    for (ChestItem chestItem : chances.keySet()) {
                        if (!chestItem.mustBeInserted()) {
                            int c1 = 0;
                            for (Integer c1i : chances.get(chestItem).keySet()) {
                                c1 = c1i;
                            }
                            int c2 = 0;
                            for (Integer c2i : chances.get(chestItem).values()) {
                                c2 = c2i;
                            }

                            if (chance >= c1 && chance <= c2) {
                                Chest ch = getChest(chests_items);
                                if (chests_items.get(ch) == null) {
                                    chests_items.put(ch, List.of(chestItem));

                                    chestItem.insterted(island);
                                    items.add(chestItem);
                                } else {
                                    if (!chests_items.get(ch).contains(chestItem)) {
                                        List<ChestItem> list = new ArrayList<>(chests_items.get(ch));
                                        list.add(chestItem);

                                        chestItem.insterted(island);
                                        items.add(chestItem);

                                        chests_items.put(ch, list);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (Chest chest : chests) {
                List<Integer> slots = new ArrayList<>();
                for (int s = 0; s < chest.getBlockInventory().getSize(); s++){
                    slots.add(s);
                }
                Collections.shuffle(slots);

                if (chests_items.get(chest) != null) {
                    if (!chests_items.get(chest).isEmpty()) {
                        for (ChestItem item : chests_items.get(chest)) {
                            int chestSlot = slots.get(0);

                            if (item != null) {
                                item.setItem(chest.getBlockInventory(), chestSlot);
                                slots.remove(0);
                                Collections.shuffle(slots);
                            }
                        }
                    }
                }
            }
        }



        for (Chest chest : getRareChests()) {

            if (!chest.getBlockInventory().getViewers().isEmpty()) {
                for (int i2 = chest.getBlockInventory().getViewers().size() - 1; i2 >= 0; --i2) {
                    chest.getBlockInventory().getViewers().get(i2).closeInventory();
                }
            }

            List<Integer> slots = new ArrayList<>();
            for (int s = 0; s < chest.getBlockInventory().getSize(); s++){
                slots.add(s);
            }
            Collections.shuffle(slots);

            List<ChestItem> items2 = new ArrayList<>();
            int toFill = Util.getRandom(3, 5);
            for (int x = 0; x < toFill; x++) {
                Map<ChestItem, Map<Integer, Integer>> chances2 = new HashMap<>();

                int i2 = 0;
                for (ChestItem item : getRareItems()) {
                    if (item.getLimitInChest() != -1 && item.getInstertedInChests(chest) >= item.getLimitInChest()){
                        continue;
                    }

                    int chance = item.getChance();
                    int c1 = i2 + 1;
                    int c2 = i2 + 1 + chance;
                    Map<Integer, Integer> iMap = new HashMap<>();
                    iMap.put(c1, c2);
                    chances2.put(item, iMap);

                    i2 = c2;
                }

                ChestItem item = null;
                if (addUpTheChances(ChestType.RARE) != 0) {
                    int chance = new Random().nextInt(addUpTheChances(ChestType.RARE));

                    for (ChestItem chestItem : chances2.keySet()) {
                        int c1 = 0;
                        for (Integer c1i : chances2.get(chestItem).keySet()) {
                            c1 = c1i;
                        }
                        int c2 = 0;
                        for (Integer c2i : chances2.get(chestItem).values()) {
                            c2 = c2i;
                        }

                        if (chance >= c1 && chance <= c2) {
                            item = chestItem;
                        }
                    }
                }

                int chestSlot = slots.get(0);

                if (item != null) {
                    if (!items2.contains(item)) {
                        items2.add(item);
                        item.setItem(chest.getBlockInventory(), chestSlot);
                        item.instertedInChests(chest);
                        slots.remove(0);
                        Collections.shuffle(slots);
                    }
                }
            }
        }
    }

    public void cleanChests(){
        for (String chestString : getIslandChests().keySet()){
            for (Chest chest : getIslandChests().get(chestString)) {
                chest.getBlockInventory().clear();
            }
        }

        for (Chest chest : getRareChests()){
            chest.getBlockInventory().clear();
        }
    }

    public List<ChestItem> armorItems = new ArrayList<>();
    public List<ChestItem> armsItems = new ArrayList<>();

    public void loadChestItems(){
        ItemStack potionStack = new ItemStack(Material.POTION, 1, (byte) 16385);
        PotionMeta potionMeta = (PotionMeta) potionStack.getItemMeta();
        potionMeta.clearCustomEffects();
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*20 , 0), true);
        potionStack.setItemMeta(potionMeta);

        ItemStack poisonArrow = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta poisonArrowMeta = (PotionMeta) poisonArrow.getItemMeta();
        poisonArrowMeta.setBasePotionType(PotionType.POISON);
        poisonArrow.setItemMeta(poisonArrowMeta);

        ItemStack book = new ItemBuilder(Material.ENCHANTED_BOOK, 1).setName("Enchanted Book").toItemStack();
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        meta.addStoredEnchant(Enchantment.SHARPNESS, 1, true);
        book.setItemMeta(meta);

        armorItems.add(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).toItemStack()));
        armorItems.add(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(XMaterial.IRON_LEGGINGS.parseMaterial()).toItemStack()));
        armorItems.add(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(XMaterial.IRON_BOOTS.parseMaterial()).toItemStack()));
        armorItems.add(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(XMaterial.CHAINMAIL_LEGGINGS.parseMaterial()).toItemStack()));
        armorItems.add(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(XMaterial.CHAINMAIL_BOOTS.parseMaterial()).toItemStack()));

        armsItems.add(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(Material.STONE_SWORD).addEnchant(Enchantment.SHARPNESS, 1).toItemStack()));
        armsItems.add(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(Material.IRON_SWORD).toItemStack()));
        armsItems.add(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(Material.STONE_AXE).toItemStack()));

        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(XMaterial.OAK_PLANKS.parseItem()), true).setItemRandomAmount(20, 32));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(Material.STONE_PICKAXE).addEnchant(Enchantment.EFFICIENCY, 3).toItemStack(), 95).setLimitInIsland(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, book, 10).setLimitInIsland(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, HealingSoup.getHealingSoupItem().getFinalItemStack(), 8).setLimitInIsland(2));
        addChestItem(new ChestItem(this, ChestType.NORMAL, RunItem.getRunItem().getFinalItemStack(), 6).setLimitInIsland(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.ARROW), 10).setItemRandomAmount(2, 5));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(XMaterial.SNOWBALL.parseItem()), 15).setItemRandomAmount(4, 7));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.GOLDEN_APPLE), 5));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(XMaterial.FIRE_CHARGE.parseMaterial()), 8).setItemRandomAmount(1, 2).setLimitInIsland(2));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.DIAMOND, 2), 8).setLimitInIsland(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(XMaterial.EXPERIENCE_BOTTLE.parseMaterial()), 10).setItemRandomAmount(8,16));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.LAVA_BUCKET), 6));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.LADDER, 12), 8));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(XMaterial.ENCHANTING_TABLE.parseMaterial()), 10).setLimitInIsland(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.WATER_BUCKET), 25).setLimitInIsland(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.BOW), 14).setLimitInIsland(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.FISHING_ROD), 10).setLimitInIsland(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.TNT), 8).setItemRandomAmount(1, 2).setLimitInIsland(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemStack(Material.COBWEB), 5).setItemRandomAmount(1, 2).setLimitInIsland(1));

        addChestItem(new ChestItem(this, ChestType.RARE, HealingSoup.getHealingSoupItem().getFinalItemStack(), 10).setLimit(4));
        addChestItem(new ChestItem(this, ChestType.RARE, RunItem.getRunItem().getFinalItemStack(), 15).setLimit(5));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.EFFICIENCY, 2).toItemStack(), 8).setLimitInChest(1));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemBuilder(Material.IRON_AXE).toItemStack(), 10).setLimitInChest(1));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.FIRE_ASPECT, 1).toItemStack(), 12).setLimitInChest(1));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemBuilder(Material.BOW).addEnchant(Enchantment.POWER, 2).toItemStack(), 8).setLimitInChest(1));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(XMaterial.OAK_PLANKS.parseMaterial()), 16).setItemRandomAmount(20, 27));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(Material.ARROW), 12).setItemRandomAmount(4, 7));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(XMaterial.SNOWBALL.parseMaterial()), 14).setItemRandomAmount(6, 9));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(XMaterial.FIRE_CHARGE.parseMaterial()), 10).setItemRandomAmount(1, 2));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(XMaterial.EXPERIENCE_BOTTLE.parseMaterial()), 7).setItemRandomAmount(8, 16));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION, 2).toItemStack(), 14).setLimitInChest(1));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemBuilder(Material.DIAMOND_BOOTS).toItemStack(), 16).setLimitInChest(1));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemBuilder(Material.DIAMOND_LEGGINGS).toItemStack(), 12).setLimitInChest(1));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(Material.ENDER_PEARL), 12).setLimit(15).setItemRandomAmount(1, 2));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(Material.GOLDEN_APPLE), 15).setItemRandomAmount(1, 2));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(Material.TNT), 10).setItemRandomAmount(1, 2));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(Material.COBWEB), 6).setItemRandomAmount(1, 2));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(Material.LAVA_BUCKET), 4).setLimitInChest(1));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(Material.WATER_BUCKET), 8));
        addChestItem(new ChestItem(this, ChestType.RARE, new ItemStack(Material.ANVIL), 5).setLimitInChest(1));
        addChestItem(new ChestItem(this, ChestType.NORMAL, new ItemBuilder(poisonArrow, 2).toItemStack(), 4));
    }


    public void refill(){
        //holograms.forEach(h -> h.delete());

        for (ChestItem item : getNormalItems()){
            item.getInstertedInChests().clear();
            item.getInsterted().clear();
            item.setGived(0);
        }

        for (ChestItem item : getRareItems()){
            item.getInstertedInChests().clear();
            item.getInsterted().clear();
            item.setGived(0);
        }

        cleanChests();
        fillChests();
        hologramSpawned.replaceAll((c, v) -> false);
        refilled++;
    }

    //private static List<Hologram> holograms = new ArrayList<>();
    private Map<Chest, Boolean> hologramSpawned = new HashMap<>();
    private int refilled = 0;

    private void spawnHolograms(Chest chest){
        //ArmorStand firstArmorStand;
        //ArmorStand supportArmorStand;

        /*Location location = chest.getBlock().getLocation();

        for (GamePlayer gamePlayer : GameManager.getGame().getPlayers()) {
            Player player = gamePlayer.getOnlinePlayer();
            Hologram hologram = new Hologram("Chest " + location + refilled, location.add(0.5D, -0.5D, 0.5D));

            DHAPI.addHologramLine(hologram, "§c00:00");
            DHAPI.addHologramLine(hologram, MessageManager.get(player, "holograms.empty").getTranslated());

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (chest.getBlock() == null){
                        hologram.delete();
                        this.cancel();
                        return;
                    }

                    DHAPI.setHologramLine(hologram, 0, "§a" + GameUtil.getDurationString((Task.getTasks().get("ChestRefill") != null ? Task.getTasks().get("ChestRefill").getCounter() : 0)));
                }
            }.runTaskTimer(SkyWars.getInstance(), 0, 20L);

            holograms.add(hologram);
            hologramSpawned.put(chest, true);


            /*List<ItemStack> items = new ArrayList<>();
            for (ItemStack item : chest.getBlockInventory().getContents()) {
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }
                items.add(item);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (items.size() == 0) {
                        hologram.setLine("line-2", Messages.translate(player, "holograms.empty"));
                    } else {
                        hologram.setLine("line-2", Messages.translate(player, "holograms.items").replace("{ITEMS}", "" + items.size()));
                    }
                    hologram.showPlayer(player);
                }
            }.runTaskTimer(SkyWars.getInstance(), 0, 20L);*


        }

        /*supportArmorStand = (ArmorStand) location.getWorld().spawn(location.add(0.5D, 0.32D, 0.5D), ArmorStand.class);
        supportArmorStand.setVisible(false);
        supportArmorStand.setGravity(false);
        supportArmorStand.setCustomNameVisible(true);
        new BukkitRunnable(){
            @Override
            public void run() {
                supportArmorStand.setCustomName("§a" + GameUtil.getDurationString((Task.getTasks().get("ChestRefill") != null ? Task.getTasks().get("ChestRefill").getCounter() : 0)));
            }
        }.runTaskTimer(SkyWars.getInstance(), 0, 20L);

        firstArmorStand = (ArmorStand) location.getWorld().spawn(supportArmorStand.getLocation().subtract(0.0D, 0.5D, 0.0D), ArmorStand.class);
        firstArmorStand.setVisible(false);
        firstArmorStand.setGravity(false);
        firstArmorStand.setCustomNameVisible(true);

        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : chest.getBlockInventory().getContents()){
            if (item == null || item.getType() == Material.AIR){
                continue;
            }
            items.add(item);
        }

        new BukkitRunnable(){
            @Override
            public void run() {
                if (items.size() == 0){
                    firstArmorStand.setCustomName("§cEmpty!");
                }else {
                    firstArmorStand.setCustomName("§6" + items.size() + " items");
                }
            }
        }.runTaskTimer(SkyWars.getInstance(), 0, 20L);

        armorStands.add(firstArmorStand);
        armorStands.add(supportArmorStand);*/
    }

    public void addOpened(Chest chest){
        if (getOpened().contains(chest)){
            return;
        }
        getOpened().add(chest);
        hologramSpawned.put(chest, false);
    }

    public void closeChest(Chest chest){
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : chest.getBlockInventory().getContents()){
            if (item == null || item.getType() == Material.AIR){
                continue;
            }
            items.add(item);
        }

        //Zkontrolovat jestli to tu patří...
        //Util.chestLookOpen(chest.getLocation(), true);

        if (items.isEmpty()) {
            if (hologramSpawned.isEmpty()
                    || hologramSpawned.get(chest) == null
                    || !hologramSpawned.get(chest)) {
                spawnHolograms(chest);
            }
        }
    }

    public void removeOpened(Chest chest){
        if (!getOpened().contains(chest)){
            return;
        }
        getOpened().remove(chest);
    }

    public void addChestItem(ChestItem chestItem){
        if (chestItem.getChestType().equals(ChestType.NORMAL)){
            if (!normalItems.contains(chestItem)) {
                normalItems.add(chestItem);
            }
        }else if (chestItem.getChestType().equals(ChestType.RARE)){
            if (!rareItems.contains(chestItem)) {
                rareItems.add(chestItem);
            }
        }
    }

    public int addUpTheChances(ChestType chestType){
        int integer = 0;
        for (ChestItem item : (chestType.equals(ChestType.NORMAL) ? normalItems : rareItems)){
            integer = integer + item.getChance();
        }
        return integer;
    }

    public int addUpTheChances(List<ChestItem> itemList){
        int integer = 0;
        for (ChestItem item : itemList){
            integer = integer + item.getChance();
        }
        return integer;
    }





    public static ChestManager getChestManager(Game game){
        for (ChestManager chestManager : chestManagers){
            if (chestManager.getGame().equals(game)){
                return chestManager;
            }
        }
        return null;
    }
}
