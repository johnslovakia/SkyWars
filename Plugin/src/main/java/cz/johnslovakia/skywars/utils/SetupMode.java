package cz.johnslovakia.skywars.utils;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import cz.johnslovakia.gameapi.worldManagement.WorldManager;
import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.BukkitSerialization;
import cz.johnslovakia.gameapi.utils.inventoryBuilder.InventoryManager;
import cz.johnslovakia.gameapi.utils.inventoryBuilder.Item;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.gameapi.utils.ConfigAPI;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import me.zort.containr.Component;
import me.zort.containr.GUI;
import me.zort.containr.internal.util.Items;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetupMode implements Listener {

    private Player admin;
    private int act = 1;
    private InventoryManager currentInventory;

    private String currentMap = null;
    private ConfigAPI mapConfig = DataHandler.getMapsConfig();

    public SetupMode(Player admin){
        this.admin = admin;
        admin.setGameMode(GameMode.CREATIVE);

        GamePlayer gamePlayer = PlayerManager.getGamePlayer(admin);

        if (gamePlayer.getPlayerData().getGame() != null){
            gamePlayer.getPlayerData().getGame().quitPlayer(admin);
            /*for (Game game : GameManager.getGames()){
                game.setState(GameState.SETUP);
            }*/
        }

        admin.sendMessage("§f");
        MessageManager.get(admin, "chat.setup_mode_on")
                .send();
        admin.sendMessage("§f");

        giveSelectMapItems();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, SkyWars.getInstance());
    }


    public void openMapMenu() throws IOException {

    }

    public void setMapIcon(){
        if (currentInventory != null) {
            currentInventory.unloadInventory(admin);
        }

        MessageManager.get(admin, "chat.how_to_set_map_icon")
                .send();

        InventoryManager inventoryManager = new InventoryManager("Set Map Icon");
        inventoryManager.setFill(XMaterial.RED_STAINED_GLASS_PANE.parseItem());

        Item confirm = new Item(new ItemStack(Material.EMERALD), 8, "setup_item.confirm", e -> {
            PlayerInventory inventory = admin.getInventory();
            ItemStack item = inventory.getItem(0);

            if (item == null || item.getType() == Material.AIR){
                MessageManager.get(admin, "chat.no_icon")
                        .send();
                XSound.UI_BUTTON_CLICK.play(admin);
            }else{
                mapConfig.getConfig().set("maps." + currentMap + ".icon", BukkitSerialization.itemStackToBase64(item));
                mapConfig.saveConfig();
                MessageManager.get(admin, "chat.successfully.icon_set")
                        .send();
                XSound.ENTITY_PLAYER_LEVELUP.play(admin);
                try {
                    giveSetupItems();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        inventoryManager.registerItem(confirm);
        inventoryManager.give(admin);
        currentInventory = inventoryManager;

        admin.getInventory().setItem(0, new ItemStack(Material.AIR));
    }



    public void giveSelectMapItems(){
        if (currentInventory != null) {
            currentInventory.unloadInventory(admin);
        }

        admin.getInventory().clear();

        InventoryManager inventoryManager = new InventoryManager("Select Map");

        Item selector = new Item(new ItemBuilder(Material.PAPER).setName("§aMap Selector").toItemStack(),
                0, "setup_item.map_selector", e -> openMapSelectorInventory());
        Item cancel = new Item(new ItemBuilder(Material.BARRIER).setName("§4Cancel").toItemStack(),
                8, "setup_item.cancel", e -> admin.getInventory().clear());

        inventoryManager.registerItem(selector, cancel);
        inventoryManager.give(admin);

        currentInventory = inventoryManager;
    }

    public void openMapSelectorInventory(){
        if (getMaps().isEmpty()){
            MessageManager.get(admin, "chat.error.no_map")
                    .send();
            return;
        }

        //int inv_size = ((getMaps().size() + 9) / 9 * 9);
        GUI inventory = Component.gui()
                .title("Map Selector")
                .rows(3)
                .prepare((gui, player) -> {

                    int i = 0;
                    for (String m : getMaps()) {
                        String base64 = mapConfig.getConfig().getString("maps." + currentMap + ".icon");

                        try {
                            gui.setElement(i, Component.element()
                                    .click(info -> {
                                        currentMap = m;
                                        load();
                                        try {
                                            giveSetupItems();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .item(new ItemBuilder((base64 != null && BukkitSerialization.itemStackFromBase64(base64) != null ? BukkitSerialization.itemStackFromBase64(base64) : new ItemStack(Material.PAPER)))
                                            .setName("§a" + m).addLoreLine("").addLoreLine(MessageManager.get(player, "setup_inventory.click_to_select").getTranslated()).toItemStack())
                                    .build());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        i++;
                    }
                })
                .build();

        inventory.open(admin);
    }


    List<Block> island_chests = new ArrayList<>();

    public void giveSetupIslandItems(){
        //ConfigAPI config = DataHandler.getMainConfig();
        ConfigAPI config = DataHandler.getMapsConfig();

        //island = config.getConfig().getInt("maps." + currentMap + ".islandChests_set") + 1;
        int island = (config.getConfig().getConfigurationSection("maps." + currentMap + ".islands") == null ? 1 : config.getConfig().getConfigurationSection("maps." + currentMap + ".islands").getKeys(false).size() + 1);

        if (currentInventory != null) {
            currentInventory.unloadInventory(admin);
        }

        admin.getInventory().clear();

        InventoryManager inventoryManager = new InventoryManager("Select Map");


        Item spawnpoint = new Item(XMaterial.CARROT_ON_A_STICK.parseItem(),
                2, "setup_item.set_spawnpoint", e -> {
            config.setLocation("maps." + currentMap + ".islands.island-" + island + ".spawnpoint", admin.getLocation(), true);
            config.saveConfig();
            MessageManager.get(admin, "chat.successfully.spawnpoint")
                    .replace("%island%", "" + island)
                    .send();
        });
        Item chest1 = new Item(XMaterial.CHEST.parseItem(),
                3, "setup_item.set_chest1", e -> {
            Block block = e.getClickedBlock();

            if (block == null || block.getType() != Material.CHEST){
                MessageManager.get(admin, "chat.error.island_chest")
                        .send();
                return;
            }
            if (island_chests.contains(block)){
                MessageManager.get(admin, "chat.error.island_chest.already_set")
                        .send();
                return;
            }

            config.setLocation("maps." + currentMap + ".islands..island-" + island + ".chest1", block.getLocation(), true);
            config.saveConfig();
            MessageManager.get(admin, "chat.successfully.island_chest")
                    .replace("%island%", "" + island)
                    .send();
        });
        Item chest2 = new Item(XMaterial.CHEST.parseItem(),
                4, "setup_item.set_chest2", e -> {
            Block block = e.getClickedBlock();

            if (block == null || block.getType() != Material.CHEST){
                MessageManager.get(admin, "chat.error.island_chest")
                        .send();
                return;
            }
            if (island_chests.contains(block)){
                MessageManager.get(admin, "chat.error.island_chest.already_set")
                        .send();
                return;
            }

            config.setLocation("maps." + currentMap + ".islands..island-" + island + ".chest2", block.getLocation(), true);
            config.saveConfig();
            MessageManager.get(admin, "chat.successfully.island_chest")
                    .replace("%island%", "" + island)
                    .send();
        });
        Item chest3 = new Item(XMaterial.CHEST.parseItem(),
                5, "setup_item.set_chest3", e -> {
            Block block = e.getClickedBlock();

            if (block == null || block.getType() != Material.CHEST){
                MessageManager.get(admin, "chat.error.island_chest")
                        .send();
                return;
            }
            if (island_chests.contains(block)){
                MessageManager.get(admin, "chat.error.island_chest.already_set")
                        .send();
                return;
            }

            config.setLocation("maps." + currentMap + ".islands..island-" + island + ".chest3", block.getLocation(), true);
            config.saveConfig();
            MessageManager.get(admin, "chat.successfully.island_chest")
                    .replace("%island%", "" + island)
                    .send();
        });


        Item next_island = new Item(new ItemBuilder(XMaterial.ARROW.parseMaterial()).toItemStack(),
                7, "setup_item.next_island", e -> {
            giveSetupIslandItems();
        });

        Item back = new Item(new ItemBuilder(XMaterial.ARROW.parseMaterial()).toItemStack(),
                8, "setup_item.back", e -> {
            try {
                giveSetupItems();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        inventoryManager.registerItem(spawnpoint, chest1, chest2, chest3, back, next_island);
        inventoryManager.give(admin);

        ItemBuilder current_island = new ItemBuilder(XMaterial.OAK_SIGN.parseMaterial());
        current_island.setName("§fIsland: §a" + island);
        admin.getInventory().setItem(0, current_island.toItemStack());

        currentInventory = inventoryManager;
    }

    public void giveSetupItems() throws IOException {
        if (currentInventory != null) {
            currentInventory.unloadInventory(admin);
        }

        admin.getInventory().clear();

        InventoryManager inventoryManager = new InventoryManager("Select Map");

        String base64 = mapConfig.getConfig().getString("maps." + currentMap + ".icon");
        ItemBuilder map = new ItemBuilder((base64 != null && BukkitSerialization.itemStackFromBase64(base64) != null ? BukkitSerialization.itemStackFromBase64(base64) : new ItemStack(Material.PAPER)));

        Item selector = new Item(map.toItemStack(),
                0, "setup_item.set_icon", e -> setMapIcon());
        Item islandSetup = new Item(new ItemBuilder(XMaterial.GRASS_BLOCK.parseMaterial()).toItemStack(),
                2, "setup_item.island_setup", e -> giveSetupIslandItems());
        Item rareChest = new Item(new ItemBuilder(XMaterial.ENDER_CHEST.parseMaterial()).toItemStack(),
                3, "setup_item.rare_chest", e -> {
            if (e.getClickedBlock() == null || e.getClickedBlock().getType() != Material.CHEST){
                MessageManager.get(admin, "chat.error.rare_chest")
                        .send();
                return;
            }
            Block block = e.getClickedBlock();
            String uuid = UUID.randomUUID().toString();

            mapConfig.getConfig().set("maps." + currentMap + ".rareChests." + uuid + ".type", "Rare");
            mapConfig.setLocation("maps." + currentMap + ".rareChests." + uuid + ".location", block.getLocation(), false);
            mapConfig.saveConfig();
            MessageManager.get(admin, "chat.successfully.rare_chest")
                    .send();
        });
        Item spectatorSpawn = new Item(new ItemBuilder(XMaterial.FEATHER.parseMaterial()).toItemStack(),
                4, "setup_item.spectator_spawn", e -> {
            mapConfig.setLocation("maps." + currentMap + ".spectatorSpawn", admin.getLocation(), true);
            mapConfig.saveConfig();
            MessageManager.get(admin, "chat.successfully.spectator_spawn")
                    .send();
        });
        Item pos1 = new Item(new ItemBuilder(XMaterial.STICK.parseMaterial()).toItemStack(),
                5, "setup_item.pos1", e -> {
            mapConfig.setLocation("maps." + currentMap + ".pos1", admin.getLocation());
            mapConfig.saveConfig();
            MessageManager.get(admin, "chat.successfully.pos1")
                    .send();
        });
        Item pos2 = new Item(new ItemBuilder(XMaterial.STICK.parseMaterial()).toItemStack(),
                6, "setup_item.pos2", e -> {
            mapConfig.setLocation("maps." + currentMap + ".pos2", admin.getLocation());
            mapConfig.saveConfig();
            MessageManager.get(admin, "chat.successfully.pos2")
                    .send();
        });

        Item finish = new Item(new ItemBuilder(XMaterial.EMERALD_BLOCK.parseMaterial()).toItemStack(),
                8, "setup_item.finish", e -> {
            if (currentMap != null) {
                final World world = Bukkit.getWorld(currentMap);

                if (world != null) {
                    Bukkit.getServer().unloadWorld(world, false);
                }
            }
            currentMap = null;
            giveSelectMapItems();
            MessageManager.get(admin, "chat.successfully.finish")
                    .send();
        });

        inventoryManager.registerItem(selector, islandSetup, rareChest, spectatorSpawn, pos1, pos2, finish);
        inventoryManager.give(admin);

        currentInventory = inventoryManager;
    }


    public void load() {
        WorldManager.loadWorld(currentMap, admin);
    }

    /*@EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)){
            return;
        }

        if ((Player) e.getWhoClicked() != player){
            return;
        }

        Inventory inv = e.getInventory();
        ItemStack item = e.getCurrentItem();

        e.setCancelled(true);

        if (e.getView().getTitle().equalsIgnoreCase("Map Selector")) {
            e.setCancelled(true);

            if (item == null) {
                return;
            }
            if (!item.hasItemMeta()) {
                return;
            }


            String map = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if (map != null) {
                currentMap = map;
                player.sendMessage("§7Selected map: §a" + map + "§7.");

                load();
                setupItems();
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!(e.getPlayer() == this.player)){
            return;
        }

        ConfigAPI config = DataHandler.getMapsConfig();

        Action action = e.getAction();
        Block block = e.getClickedBlock();
        ItemStack item = e.getItem();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)
                || action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)){

            if (!item.hasItemMeta()){
                return;
            }
            e.setCancelled(true);

            String W = player.getLocation().getWorld().getName();
            Location loc = player.getLocation();
            Location bLoc = (block != null && block.getLocation() != null  ? block.getLocation() : null);

            if (W.contains("_active") && W.endsWith("_active")) {
                W = W.substring(0, W.length() - 7);
                if (Bukkit.getWorld(W) != null) {
                    loc.setWorld(Bukkit.getWorld(W));
                    bLoc.setWorld(Bukkit.getWorld(W));
                }
            }


            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aMap selector")){
                this.selectMapInventory();
            }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§eIsland chests")){
                this.islandChests();
                act = 3;
            }else if (item.getItemMeta().getDisplayName().contains("§6Set Island chest")){
                if (e.getClickedBlock() == null){
                    player.sendMessage("§cNo Clicked Block!");
                }

                String s = ChatColor.stripColor(item.getItemMeta().getLore().get(1));

                //String uuid = UUID.randomUUID().toString();
                config.getConfig().set("maps." + currentMap + ".islandChests." + "island-" + chests + ".chest-" + s + ".type", "Normal");
                config.setLocation("maps." + currentMap + ".islandChests." + "island-" + chests + ".chest-" + s + ".location", bLoc, false);
                config.getConfig().set("maps." + currentMap + ".islandChests_set", chests);
                config.saveConfig();
                player.sendMessage("§7Chest (Normal) is saved.");
            }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aSet pos1")) {
                config.setLocation("maps." + currentMap + ".pos1", loc);
                config.saveConfig();
                player.sendMessage("§aPos1 §7for map §a" + currentMap + " §7is saved.");
            }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aSet pos2")){
                config.setLocation("maps." + currentMap + ".pos2", loc);
                config.saveConfig();
                player.sendMessage("§aPos2 §7for map §a" + currentMap + " §7is saved.");
            }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§cSet Throwable TNT")){
                config.setLocation("maps." + currentMap + ".throwableTNT", loc);
                config.saveConfig();
                player.sendMessage("§aThrowable TNT §7for map §a" + currentMap + " §7is isaved.");
            }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§bSet spectator spawn")) {
                config.setLocation("maps." + currentMap + ".spectatorSpawn", loc, true);
                config.saveConfig();
                player.sendMessage("§Spectator spawn §7for map §a" + currentMap + " §7is saved.");
            }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§7Set spawn point")){
                String uuid = UUID.randomUUID().toString();
                config.setLocation("maps." + currentMap + ".spawnPoints." + uuid, loc, true);
                config.saveConfig();
                player.sendMessage("§7Spawn point §7is saved.");
            }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§6Set Chest (Rare)")){
                String uuid = UUID.randomUUID().toString();

                Chest chest = (Chest) bLoc.getBlock().getState();
                BlockFace blockFace = chest.getBlock().getFace(block);

                config.getConfig().set("maps." + currentMap + ".rareChests." + uuid + ".type", "Rare");
                config.setLocation("maps." + currentMap + ".rareChests." + uuid + ".location", bLoc, false);
                config.saveConfig();
                player.sendMessage("§7Chest (Rare) is saved.");
            }else if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aNext island")){
                config.saveConfig();
                this.islandChests();
                player.sendMessage("§fIsland: §a" + chests);
            }else if (item.getItemMeta().getDisplayName().equals("§4Cancel")){
                config.saveConfig();
                player.sendMessage("§7Map §a" + currentMap + " §7is saved.");
                player.getInventory().clear();
                if (contents != null) {
                    player.getInventory().setContents(contents);
                }
                if (currentMap != null) {
                    final World world = Bukkit.getWorld(currentMap);

                    if (world != null) {
                        Bukkit.getServer().unloadWorld(world, false);
                    }
                }
                player = null;
            }else if (item.getItemMeta().getDisplayName().equals("§cGo Back")){
                config.saveConfig();
                if (act == 3){
                    act = 2;
                    setupItems();
                }else if (act == 2){
                    player.sendMessage("§7Map §a" + currentMap + " §7is saved.");
                    selectMapItems();
                    if (currentMap != null) {
                        final World world = Bukkit.getWorld(currentMap);

                        if (world != null) {
                            Bukkit.getServer().unloadWorld(world, false);

                        }
                    }
                }
            }
        }
    }*/

    public List<String> getMaps() {
        List<String> maps = new ArrayList<>();
        ConfigAPI mConfig = DataHandler.getMapsConfig();
        for (String key : mConfig.getConfig().getConfigurationSection("maps").getKeys(false)) {
            maps.add(key);
        }
        return maps;
    }
}
