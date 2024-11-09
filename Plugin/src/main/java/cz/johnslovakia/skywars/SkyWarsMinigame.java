package cz.johnslovakia.skywars;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.Minigame;
import cz.johnslovakia.gameapi.MinigameSettings;
import cz.johnslovakia.gameapi.datastorage.MinigameTable;
import cz.johnslovakia.gameapi.economy.Economy;
import cz.johnslovakia.gameapi.events.GameEndEvent;
import cz.johnslovakia.gameapi.events.GamePlayerDeathEvent;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.game.GameManager;
import cz.johnslovakia.gameapi.game.kit.KitManager;
import cz.johnslovakia.gameapi.game.map.*;
import cz.johnslovakia.gameapi.game.perk.PerkManager;
import cz.johnslovakia.gameapi.guis.KitInventory;
import cz.johnslovakia.gameapi.guis.PerksInventory;
import cz.johnslovakia.gameapi.guis.QuestInventory;
import cz.johnslovakia.gameapi.guis.VotingInventory;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.users.PlayerScore;
import cz.johnslovakia.gameapi.users.quests.QuestManager;
import cz.johnslovakia.gameapi.users.stats.StatsManager;
import cz.johnslovakia.gameapi.utils.*;
import cz.johnslovakia.gameapi.utils.eTrigger.Mapper;
import cz.johnslovakia.gameapi.utils.eTrigger.Trigger;
import cz.johnslovakia.gameapi.utils.inventoryBuilder.InventoryManager;
import cz.johnslovakia.gameapi.utils.inventoryBuilder.Item;
import cz.johnslovakia.gameapi.worldManagement.WorldManager;
import cz.johnslovakia.skywars.chest.ChestManager;
import cz.johnslovakia.skywars.events.ChestLootEvent;
import cz.johnslovakia.skywars.kits.*;
import cz.johnslovakia.skywars.perks.*;
import cz.johnslovakia.skywars.quests.daily.*;
import cz.johnslovakia.skywars.quests.weekly.AddictedToBlood;
import cz.johnslovakia.skywars.quests.weekly.AlwaysFirst;
import cz.johnslovakia.skywars.quests.weekly.ThisGameIsSoEasy;
import cz.johnslovakia.skywars.utils.CoinsAPIHandler;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.VaultAPIHandler;
import me.zort.sqllib.SQLConnectionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkyWarsMinigame implements Minigame {

    //TODO: dělat v interface to je asi uplně na hovno vzhledem k tomu, že se v get metodách vytváří vždy nová instance... přepsat
    Economy coins, souls, tokens;
    Database db;

    public Economy getCoins(){
        boolean coinsAPINB = false, vault = false;

        if (Bukkit.getPluginManager().getPlugin("CoinsAPINB") != null) {
            coinsAPINB = true;
        }else if (Bukkit.getPluginManager().getPlugin("Vault") != null && DataHandler.getMainConfig().getConfig().getBoolean("useVault")) {
            vault = true;
        }

        if (coins == null) {
            coins = new Economy("coins", ChatColor.GOLD, 1, !(coinsAPINB || vault), true);
        }

        if (coinsAPINB){
            CoinsAPIHandler.hook(coins);
        }else if (vault){
            VaultAPIHandler.hook(coins);
        }

        return coins;
    }

    public Economy getSouls(){
        if (souls == null){
            souls = new Economy("souls", ChatColor.AQUA, 2, true, false);
        }

        return souls;
    }

    public Economy getTokens(){
        if (tokens == null){
            tokens = new Economy("tokens", ChatColor.DARK_GREEN, 3, true, false);;
        }

        return tokens;
    }

    public Database getDB(){
        if (db == null){
            FileConfiguration config = DataHandler.getMainConfig().getConfig();

            if (config.getString("mysql.host") == null
                    || config.getString("mysql.database") == null
                    || config.getString("mysql.username") == null
                    || config.getString("mysql.password") == null){
                Logger.log("You don't have the database set up in the config.yml!", Logger.LogType.ERROR);
                return null;
            }

            String host = config.getString("mysql.host");
            int port = (config.getString("mysql.port") != null ? config.getInt("mysql.port") : 3306);
            String database = config.getString("mysql.database");
            String username = config.getString("mysql.username");
            String password = config.getString("mysql.password");

            db = new Database("jdbc:mysql://{host}:{port}/{database}?autoReconnect=true&allowMultiQueries=true&useSSL={usessl}", host, database, username, password, port, false);
        }
        return db;
    }
    
    
    
    @Override
    public Plugin getPlugin() {
        return SkyWars.getInstance();
    }

    @Override
    public String getMinigameName() {
        return "SkyWars";
    }

    @Override
    public String getDescriptionTranslateKey() {
        return "minigame.description";
    }

    @Override
    public List<InputStreamWithName> getLanguageFiles() {
        return List.of(new InputStreamWithName(SkyWars.getInstance().getResource("languages/english.yml"), "english.yml"));
    }

    @Override
    public MinigameSettings getSettings() {
        MinigameSettings settings = new MinigameSettings();
        settings.setGameTime(DataHandler.getMainConfig().getConfig().getInt("game_time") != 0 ? DataHandler.getMainConfig().getConfig().getInt("game_time") : 480);
        settings.setMaxPlayers(DataHandler.getMainConfig().getConfig().getInt("max_players") != 0 ? DataHandler.getMainConfig().getConfig().getInt("max_players") : 12);
        settings.setMinPlayers(DataHandler.getMainConfig().getConfig().getInt("min_players") != 0 ? DataHandler.getMainConfig().getConfig().getInt("min_players") : 6);
        settings.setStartingTime(DataHandler.getMainConfig().getConfig().getInt("starting_time") != 0 ? DataHandler.getMainConfig().getConfig().getInt("starting_time") : 60);
        settings.setReducedPlayers(DataHandler.getMainConfig().getConfig().getInt("players_needed_to_reduce_starting_time") != 0 ? DataHandler.getMainConfig().getConfig().getInt("players_needed_to_reduce_starting_time") : 6);
        settings.setReducedTime(DataHandler.getMainConfig().getConfig().getInt("reduced_time") != 0 ? DataHandler.getMainConfig().getConfig().getInt("reduced_time") : 20);
        settings.setUseTeams(false);
        settings.setPreparationTime(5);
        settings.setWorkbenchCommand(true);
        settings.setDisplayHealthBar(true);
        settings.setAutoBestGameJoin(true);
        settings.setRespawning(false);
        settings.setTeleportPlayersAfterEnd(false);

        return settings;
    }

    @Override
    public List<Economy> getEconomies() {

        return List.of(getCoins(), getSouls(), getTokens());
    }

    @Override
    public Database getDatabase() {
        return getDB();
    }

    @Override
    public MinigameTable getMinigameTable() {
        return new MinigameTable(this);
    }

    @Override
    public EndGame getEndGameFunction() {
        return new EndGame(game -> game.getPlayers().size() < 2 || game.getPlayers().isEmpty(),
                game -> game.endGame(game.getPlayers().isEmpty() ? null : game.getPlayers().get(0))); //TODO: dodělat
        //return null;
    }

    @Override
    public void setupPlayerScores() {
        StatsManager statsManager = new StatsManager();
        GameAPI.getInstance().setStatsManager(statsManager);


        PlayerManager.registerPlayersScore(PlayerScore.builder()
                .setName("Win", "Wins")
                .setEconomyReward(getCoins(), 50)
                .setEconomyReward(getTokens(), 25)
                .setEconomyReward(getSouls(), 5)
                .createStat("Wins")
                .addTrigger(new Trigger<>(GameEndEvent.class, new Mapper.SingleMapper<>(event -> (GamePlayer) event.getWinner()), event -> event.getWinner() != null))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder()
                .setName("Loss", "Losses")
                .createStat("Losses")
                .addTrigger(new Trigger<>(GameEndEvent.class, new Mapper.ListMapper<>(GameEndEvent::getLossers), event -> event.getLossers() != null))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder()
                .setName("1stKiller")
                .setDisplayName("1st killer")
                .setEconomyReward(getCoins(), 25)
                .setEconomyReward(getTokens(), 15)
                .setEconomyReward(getSouls(), 3)
                .setAllowedMessage(false)
                .addTrigger(new Trigger<>(GameEndEvent.class, new Mapper.SingleMapper<>(event -> event.getRanking().keySet().stream().toList().get(0)), event -> event.getRanking() != null  && !event.getRanking().isEmpty()))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder()
                .setName("2ndKiller")
                .setDisplayName("2nd killer")
                .setEconomyReward(getCoins(), 20)
                .setEconomyReward(getTokens(), 10)
                .setEconomyReward(getSouls(), 2)
                .setAllowedMessage(false)
                .addTrigger(new Trigger<>(GameEndEvent.class, new Mapper.SingleMapper<>(event -> event.getRanking().keySet().stream().toList().get(1)), event -> event.getRanking() != null  && event.getRanking().size() > 1))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder()
                .setName("3rdKiller")
                .setDisplayName("3rd killer")
                .setEconomyReward(getCoins(), 15)
                .setEconomyReward(getTokens(), 5)
                .setEconomyReward(getSouls(), 1)
                .setAllowedMessage(false)
                .addTrigger(new Trigger<>(GameEndEvent.class, new Mapper.SingleMapper<>(event -> event.getRanking().keySet().stream().toList().get(2)), event -> event.getRanking() != null && event.getRanking().size() > 2))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder() //TODO: Vyřešit spamování killů§
                .setName("Kill", "Kills")
                .setEconomyReward(getCoins(), 4)
                .setEconomyReward(getTokens(), 2)
                .setEconomyReward(getSouls(), 1)
                .setScoreRanking(true)
                .createStat("Kills")
                .addTrigger(new Trigger<>(GamePlayerDeathEvent.class, new Mapper.SingleMapper<>(GamePlayerDeathEvent::getKiller), event -> event.getKiller() != null))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder()
                .setName("Death", "Deaths")
                .createStat("Deaths")
                .addTrigger(new Trigger<>(GamePlayerDeathEvent.class, new Mapper.SingleMapper<>(GamePlayerDeathEvent::getGamePlayer)))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder()  //TODO: dodělat
                .setName("Assist", "Assists")
                .setEconomyReward(getCoins(), 2)
                .setEconomyReward(getTokens(), 1)
                .setEconomyReward(getSouls(), 1)
                .createStat("Assists")
                .addTrigger(new Trigger<>(GamePlayerDeathEvent.class, new Mapper.ListMapper<>(GamePlayerDeathEvent::getAssists), event -> event.getAssists() != null && !event.getAssists().isEmpty()))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder()
                .setName("FirstBlood")
                .setDisplayName("First Blood")
                .setEconomyReward(getCoins(), 10)
                .setEconomyReward(getTokens(), 10)
                .setEconomyReward(getSouls(), 4)
                //.setEconomyReward(levelXP, 25)
                .createStat("First Bloods")
                .addTrigger(new Trigger<>(GamePlayerDeathEvent.class, new Mapper.SingleMapper<>(GamePlayerDeathEvent::getKiller), event -> event.getKiller() != null && event.isFirstGameKill()))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder()
                .setName("LootedMeteorite")
                .setDisplayName("Looted Meteorite Chest")
                .setEconomyReward(getCoins(), 10)
                .setEconomyReward(getTokens(), 5)
                //.setEconomyReward(levelXP, 25)
                .createStat("Looted Meteorite Chests")
                .addTrigger(new Trigger<>(ChestLootEvent.class, new Mapper.SingleMapper<>(ChestLootEvent::getGamePlayer), event -> event.getGamePlayer() != null))
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder() //TODO: dodělat
                .setName("TimePlayed")
                .setDisplayName("Time Played")
                .setEconomyReward(getCoins(), 5)
                .setEconomyReward(getTokens(), 5)
                .build());
        PlayerManager.registerPlayersScore(PlayerScore.builder()
                .setName("EnchantItem")
                .setDisplayName("Enchant Item", "Enchanted Items")
                .setEconomyReward(getCoins(), 2)
                .setRewardLimit(5)
                .addTrigger(new Trigger<>(EnchantItemEvent.class, new Mapper.SingleMapper<>(event -> PlayerManager.getGamePlayer(event.getEnchanter()))))
                .build());


        statsManager.createDatabaseTable();
    }

    @Override
    public void setupGames() {
        if (DataHandler.getMainConfig().getConfig().getString("lobbyPoint") == null){
            Logger.log("Set up the LobbyPoint! /SkyWars howToSetup", Logger.LogType.ERROR);
            return;
        }
        WorldManager.loadLobbyWorld(DataHandler.getMainConfig().getConfig().getString("lobbyPoint").split(";")[0]);
        Location lobbyPoint = DataHandler.getMainConfig().getLocation("lobbyPoint");
        if (lobbyPoint == null){
            Logger.log("Set up the LobbyPoint! /SkyWars howToSetup", Logger.LogType.ERROR);
            return;
        }


        int gamesPerServer = DataHandler.getMainConfig().getConfig().getInt("gamesPerServer");
        if (gamesPerServer == 0){
            gamesPerServer = 3;
        }

        for (int i = 1; i <= gamesPerServer; i++){
            //String id = Util.randomString(6, true, true, false);

            InventoryManager im = new InventoryManager("GameLobby");

            Item selectKit = new Item(new ItemBuilder(Material.NETHER_STAR).hideAllFlags().toItemStack(), 1, "Item.kit_selector", e -> KitInventory.openKitInventory(PlayerManager.getGamePlayer(e.getPlayer())));
            Item perks = new Item(new ItemBuilder(Material.EMERALD).hideAllFlags().toItemStack(), 2, "Item.perks", e -> PerksInventory.openGUI(PlayerManager.getGamePlayer(e.getPlayer())));
            Item voteForMap = new Item(new ItemBuilder(Material.PAPER).hideAllFlags().toItemStack(), 3, "Item.map_vote", e -> VotingInventory.openGUI(PlayerManager.getGamePlayer(e.getPlayer())));
            Item quests = new Item(new ItemBuilder(Material.BOOK).hideAllFlags().toItemStack(), 6, "Item.quests", e -> QuestInventory.openGUI(PlayerManager.getGamePlayer(e.getPlayer())));
            Item cosmetics = new Item(new ItemBuilder(Material.ENDER_CHEST).hideAllFlags().toItemStack(), 7, "Item.game_cosmetics", null);

            im.registerItem(selectKit, voteForMap, perks, cosmetics, quests);

            Game game = new Game("SkyWars-" + i, im, lobbyPoint);
            MapManager mapManager = new MapManager(game);

            ConfigAPI mConfig = DataHandler.getMapsConfig();
            if (mConfig.getConfig().getConfigurationSection("maps") != null) {
                for (String key : mConfig.getConfig().getConfigurationSection("maps").getKeys(false)) {

                    String creators = mConfig.getConfig().getString("maps." + key + ".authors");
                    GameMap map = new GameMap(game, key, creators);

                    if (mConfig.getConfig().get("maps." + key + ".icon") != null){
                        try {
                            map.setIcon(BukkitSerialization.itemStackFromBase64(mConfig.getConfig().getString("maps." + key + ".icon")));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    MapLocation spectatorSpawn = mConfig.getMapLocation("SpectatorSpawn", "maps." + key + ".spectatorSpawn");
                    map.setSpectatorSpawn(spectatorSpawn);

                    Map<String, Location> spawnPoints = new HashMap<>();
                    if (mConfig.getConfig().getConfigurationSection("maps." + key + ".islands") != null) {
                        for (String island : mConfig.getConfig().getConfigurationSection("maps." + key + ".islands").getKeys(false)) {
                            MapLocation spawn = mConfig.getMapLocation(island, "maps." + key + ".islands." + island + ".spawnpoint", true);
                            map.addSpawn(spawn);
                        }
                    }else{
                        Logger.log("Map " + key + " is not set up! You can find out how to set up a map by typing /SkyWars howtosetupmap", Logger.LogType.WARNING);
                        continue;
                    }



                    MapLocation pos1 = mConfig.getMapLocation(key + "_pos1", "maps." + key + ".pos1");
                    MapLocation pos2 = mConfig.getMapLocation(key + "_pos2", "maps." + key + ".pos2");
                    Area area = new Area(map, pos1, pos2, "ARENA");
                    area.setBorder(true);

                    map.setMainArea(area);


                    AreaSettings aSettings = area.getSettings();
                    aSettings.setCanBreakAll(true);
                    aSettings.setCanPlaceAll(true);
                    aSettings.allowChat(true);
                    aSettings.setCanPvP(true);
                    aSettings.setAllowFoodLevelChange(false);
                    aSettings.setAllowWeatherChange(false);
                    aSettings.setAllowItemDrop(true);
                    aSettings.setAllowGhastFireballExplosion(true);
                    aSettings.setAllowChestAccess(true);
                    aSettings.setAllowFireSpread(false);
                    aSettings.setAllowTNTExplosion(true);
                    aSettings.setAllowWaterFlow(true);
                    aSettings.setAllowLavaFlow(false);
                    aSettings.setAllowLavaFire(false);
                    aSettings.setAllowItemDrop(true);
                    aSettings.setKeepInventory(true);
                    aSettings.setAllowEnderpearlFallDamage(false);
                    aSettings.setAllowFallDamage(true);

                    map.registerArea(area);


                    mapManager.addMap(map);
                }
            } else {
                Logger.log("No map is set, I can't continue adding games. You can find out how to set up a map by typing /SkyWars howtosetupmap", Logger.LogType.ERROR);
                return;
            }

            GameManager.registerGame(game);
            new ChestManager(game);
        }
    }

    @Override
    public void setupOther() {
        KitManager kitManager = new KitManager("SkyWars", getCoins(), false, false);
        kitManager.registerKit(new Archer(), new Alchemist(), new BaseballPlayer(), new Healer(), new Knight(), new Ninja(), new Frog(), new Scout(), new Armorer());
        GameAPI.getInstance().setKitManager(kitManager);

        PerkManager perkManager = new PerkManager("SkyWars", getSouls());
        perkManager.registerPerk(new Absorption(), new ArrowRecovery(), new BlazingArrow(), new Digger(), new Ender(), new LuckyCharm(), new Rambo(), new ResistanceBoost(), new VoidScavenger());
        GameAPI.getInstance().setPerkManager(perkManager);

        QuestManager questManager = new QuestManager("SkyWars");
        questManager.registerQuest(new Bloodthirsty(), new Marksman(), new OpeningCarnage(), new Survivor(), new Victorious(), new VoidKill(), new AddictedToBlood(), new AlwaysFirst(), new cz.johnslovakia.skywars.quests.weekly.Marksman(), new ThisGameIsSoEasy());
        GameAPI.getInstance().setQuestManager(questManager);
    }
}
