package cz.johnslovakia.skywars.utils;

import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.worldManagement.WorldManager;
import cz.johnslovakia.gameapi.game.GameManager;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.task.Task;
import cz.johnslovakia.gameapi.task.tasks.StartCountdownCommand;
import cz.johnslovakia.gameapi.utils.ConfigAPI;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.items.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class Commands implements CommandExecutor {

    private void sendHelpMessage(Player player){
        player.sendMessage("");
        player.sendMessage("§fGame: §aSkyWars §7(v" + SkyWars.getInstance().getDescription().getVersion() + ")");
        player.sendMessage("");
        player.sendMessage("§a/skywars create map <Name> [Authors ...]");
        player.sendMessage("§a/skywars setup");
        player.sendMessage("§a/skywars setLobby");
        player.sendMessage("§a/skywars loadLobbyWorld <WorldName> §7(To Setup the Lobby Location)");
        player.sendMessage("§a/skywars statsHologram §c(Required plugin DecentHolograms!)");
        player.sendMessage("§a/skywars topStatsHologram §c(Required plugin DecentHolograms!)");
        player.sendMessage("");
        player.sendMessage("§a/skywars start");
        player.sendMessage("§a/skywars games");
        player.sendMessage("§a/skywars join <game>");
        player.sendMessage("§6/skywars howToSetup");
        player.sendMessage("");
    }

    //BORDER

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player){
            Player player = (Player) sender;
            GamePlayer gp = PlayerManager.getGamePlayer(player);

            if (player.hasPermission("skywars.admin")){

                if (args.length == 0){
                    sendHelpMessage(player);
                }else {

                    switch (args[0].toLowerCase()) {
                        case "time":
                            PlayerManager.getGamePlayer(player).getPlayerData().getGame().getRunningMainTask().setCounter(Integer.valueOf(args[1]));
                            break;
                        case "meteorite":
                            Task.getTask(gp.getPlayerData().getGame(), "Meteorite").setCounter(20);
                            break;
                        case "items":
                            Inventory inv = Bukkit.createInventory(null, 9);
                            inv.addItem(GhostPearl.getGhostPearlItem().getFinalItemStack(gp));
                            inv.addItem(NetherShield.getNetherShieldItem().getFinalItemStack(gp));
                            inv.addItem(RunItem.getRunItem().getFinalItemStack(gp));
                            inv.addItem(HealingSoup.getHealingSoupItem().getFinalItemStack(gp));
                            inv.addItem(SparkOfLevitationItem.getSparkOfLevitationItem().getFinalItemStack(gp));
                            inv.addItem(SparkOfInvisibilityItem.getSparkOfInvisibilityItem().getFinalItemStack(gp));
                            inv.addItem(ToxicGrenadeItem.getToxicGrenadeItem().getFinalItemStack(gp));
                            player.openInventory(inv);
                            break;
                        case "games":
                            for (Game game : GameManager.getGames()) {
                                player.sendMessage("§a" + game.getName() + " §8(ID: " + game.getID() + ") §7- §fState: §a" + game.getState().toString() + " §7, §fPlayers: §a(" + game.getPlayers().size() + "/" + game.getSettings().getMaxPlayers() + ")");
                            }
                            break;
                        case "win":
                            gp.getPlayerData().getGame().endGame(gp);
                            break;
                        case "join":
                            if (args.length == 2) {
                                Game game = GameManager.getGameByName(args[1]);
                                if (game != null){

                                    if (PlayerManager.getGamePlayer(player).getPlayerData().getGame() != null){
                                        PlayerManager.getGamePlayer(player).getPlayerData().getGame().quitPlayer(player);
                                    }
                                    game.joinPlayer(player);
                                }else{
                                    player.sendMessage("§cThis game does not exist!");
                                }

                            } else {
                                player.sendMessage("§cUsage: /skywars join <game>");
                            }
                            break;
                        case "howtosetupmap":
                            player.sendMessage("");
                            player.sendMessage("§2§lHow to set up a map");
                            player.sendMessage("");
                            player.sendMessage("§a1. §aWith Slime World Manager: §fsave the map either in a database or in a server folder 'slime_worlds' using the /swm import <world name> <mysql/file> command!");
                            player.sendMessage("§a1. §aWithout Slime World Manager: §fupload the map to §a/plugins/skywars/maps");
                            player.sendMessage("§a2. §fCreate the map using §a/skywars create map <Name> [Authors ...]");
                            player.sendMessage("§a3. §fUse §a/skywars setup §fto start setup mode");
                            player.sendMessage("§a4. §fSet up the map using §aall §fthe items in the inventory.");
                            player.sendMessage("§a5. §aRestart the server");
                            player.sendMessage("");
                            break;
                        case "howtosetup":
                            player.sendMessage("");
                            player.sendMessage("§2§lHOW TO SET UP THE PLUGIN");
                            player.sendMessage("");
                            player.sendMessage("§2How to set up the database:");
                            player.sendMessage("§a1. §fGo to §aplugins/skywars/config.yml");
                            player.sendMessage("§a2. §fFind §a\"mysql:\" §fand enter your login details");
                            player.sendMessage("");
                            player.sendMessage("§2How to set up the Lobby:");
                            player.sendMessage("§a1. §fUpload the world to the §aserver folder. If you are using Slime World Manager, it is recommended to import the world using /swm import <world name> <mysql/file>");
                            player.sendMessage("§a2. §fLoad the world using §a/skywars loadLobbyWorld <World> §for another §aWorld Manager.");
                            player.sendMessage("§a3. §fSet the Spawn location using §a/skywars setLobby");
                            player.sendMessage("");
                            player.sendMessage("§2How to set up a map:");
                            player.sendMessage("§a1. §aWith Slime World Manager: §fsave the map either in a database or in a server folder 'slime_worlds' using the /swm import <world name> <mysql/file> command!");
                            player.sendMessage("§a1. §aWithout Slime World Manager: §fupload the map to §a/plugins/skywars/maps");
                            player.sendMessage("§a2. §fCreate the map using §a/skywars create map <Name> [Authors ...]");
                            player.sendMessage("§a3. §fUse §a/skywars setup §fto start setup mode");
                            player.sendMessage("§a4. §fSet up the map using §aall §fthe items in the inventory.");
                            player.sendMessage("");
                            player.sendMessage("§aThen restart the server");
                            player.sendMessage("");
                            break;
                        case "start":
                            if (gp.getPlayerData().getGame().getState() == GameState.WAITING) {
                                gp.getPlayerData().getGame().setState(GameState.STARTING);
                                Task task = new Task(gp.getPlayerData().getGame(), "StartCountdown", gp.getPlayerData().getGame().getSettings().getStartingTime(), new StartCountdownCommand(), GameAPI.getInstance());
                                task.setGame(gp.getPlayerData().getGame());
                            } else if (gp.getPlayerData().getGame().getState() == GameState.STARTING) {
                                Task.getTask(gp.getPlayerData().getGame(), "StartCountdown").setCounter(15);
                            }
                            break;
                        case "help":
                            sendHelpMessage(player);
                            break;
                        case "statshologram":
                            if (args.length == 1) {
                                ConfigAPI config = DataHandler.getMainConfig();
                                Location loc = player.getLocation();

                                config.setLocation("statsHologram", loc, true);
                                config.saveConfig();
                                player.sendMessage("§aStats hologram location saved!");

                            } else {
                                player.sendMessage("§cUsage: /skywars statsHologram");
                            }
                            break;
                        case "setlobby":
                            if (args.length == 1) {
                                ConfigAPI config = DataHandler.getMainConfig();
                                Location loc = player.getLocation();

                                config.setLocation("lobbyPoint", loc, true);
                                config.saveConfig();
                                //player.sendMessage(GameUtil.colorizer(Prefix.LOCAL_PREFIX + " §7Stats hologram location for game §a" + args[0] + " §7saved!"));

                                player.sendMessage("§aLobby location saved!");
                                //player.sendMessage(" §cIf you use SlimeWorldManager, you must also import Lobby World!");
                            } else {
                                player.sendMessage("§cUsage: /skywars setLobby");
                            }

                            break;
                        case "create":
                            if (args[1].equalsIgnoreCase("map")) {
                                if (args.length >= 3) {
                                    FileConfiguration config = DataHandler.getMapsConfig().getConfig();
                                    if (config.get("maps." + args[2]) == null) {
                                        /*if (Bukkit.getPluginManager().getPlugin("SlimeWorldManager") != null) {
                                            SlimeWorldLoaderHandler.saveWorldToConfig(args[2], "mysql");
                                        }*/

                                        //config.set("maps." + args[2] + ".authors", "Unknown");
                                        config.set("maps." + args[2] + ".pos1", null);
                                        config.set("maps." + args[2] + ".pos2", null);

                                        if (args.length > 3) {
                                            StringBuilder sb = new StringBuilder();
                                            for (int i = 3; i < args.length; ++i) {
                                                sb.append(args[i]).append(' ');
                                            }

                                            config.set("maps." + args[2] + ".authors", sb.toString());
                                        }else{
                                            config.set("maps." + args[2] + ".authors", "Unknown");
                                        }


                                        player.sendMessage("§7Map §a" + args[2] + " §7created.");
                                        DataHandler.getMapsConfig().saveConfig();
                                    } else {
                                        player.sendMessage("&cThis map already exist!");
                                    }
                                } else {
                                    player.sendMessage("§cUsage: /skywars create Map <Name>");
                                }
                            }

                            break;
                        case "loadlobbyworld":
                            if (args.length == 2) {
                                player.sendMessage("§7Loading World...");
                                WorldManager.loadWorld( args[1], player);

                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        World bukkitWorld = Bukkit.getWorld(args[1]);

                                        if (bukkitWorld == null){
                                            player.sendMessage("§c§lI can't teleport you to the world! §cMake sure you have the world in the server folder or in /plugins/skywars/maps, or if you're using Slime World Manager, check if you have imported the world.");
                                        }
                                        Location location = new Location(bukkitWorld, 0, 90, 0);
                                        player.teleport(location);

                                        bukkitWorld.setAutoSave(false);

                                    }
                                }.runTaskLater(SkyWars.getInstance(), 6 * 20L);
                            } else {
                                player.sendMessage("§cUsage: /skywars loadLobbyWorld <WorldName>");
                            }

                            break;
                        case "topstatshologram":
                            if (args.length == 1) {
                                ConfigAPI config = DataHandler.getMainConfig();
                                Location loc = player.getLocation();

                                config.setLocation("topStatsHologram", loc, true);
                                config.saveConfig();
                                player.sendMessage("§7Top Stats hologram location saved!");

                            } else {
                                player.sendMessage("§cUsage: /skywars topStatsHologram");
                            }
                            break;

                        case "setup":
                            new SetupMode(player);
                            break;
                        case "reload":
                            GameAPI.getInstance().saveConfig();
                            DataHandler.getMainConfig().saveConfig();
                            DataHandler.getMapsConfig().saveConfig();
                            player.sendMessage("§aConfigs reloaded!");
                            break;
                        default:
                            player.sendMessage("§cUnknown command. Usage command /skywars help to show commands list.");
                            break;
                    }
                }
            }else{
                player.sendMessage("§fSkyWars created by §ajohn.slovakia (Hunzek_) §7(linktree.com/john.slovakia)");
            }
        }
        return false;
    }

}