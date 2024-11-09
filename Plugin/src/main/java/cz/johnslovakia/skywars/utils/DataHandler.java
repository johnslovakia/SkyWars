package cz.johnslovakia.skywars.utils;

import com.osiris.dyml.Yaml;
import com.osiris.dyml.exceptions.*;
import cz.johnslovakia.gameapi.utils.ConfigAPI;
import cz.johnslovakia.skywars.SkyWars;

import java.io.IOException;

public class DataHandler {

    private static ConfigAPI mainConfig = new ConfigAPI(SkyWars.getInstance().getDataFolder().toString(), "config.yml", SkyWars.getInstance());;
    private static ConfigAPI mapsConfig = new ConfigAPI(SkyWars.getInstance().getDataFolder().toString(), "maps.yml", SkyWars.getInstance());

    /*public DataHandler(){
        mainConfig = new ConfigAPI(Islands.getInstance().getDataFolder().toString(), "config.yml", Islands.getInstance());
        mapsConfig = new ConfigAPI(Islands.getInstance().getDataFolder().toString(), "maps.yml", Islands.getInstance());
    }*/

    public static ConfigAPI getMapsConfig() {
        return mapsConfig;
    }

    public static ConfigAPI getMainConfig() {
        return mainConfig;
    }


    public static void createMainConfig(){
        Yaml yaml = new Yaml(SkyWars.getInstance().getDataFolder() + "/config.yml", true);
        try {
            yaml.load(); // Also supports InputStreams and Strings as input
            yaml.put("server").setDefValues("play.your_server.com");
            yaml.put("useVault").setDefValues(false);

            yaml.put("lobby_server").setDefValues("");

            yaml.put("server_motd")
                    .setDefValues("\"GameState: %gamestate% GameTime: %gametime% Phase: %phase% Players: %players% MaxPlayers: %max_players% Red Nexus: %red_nexus_hp% Blue Nexus: %blue_nexus_hp% Green Nexus: %green_nexus_hp% Yellow Nexus: %yellow_nexus_hp%\"")
                    .setComments("Placeholders: %gamestate%, %gametime%, %players%, %max_players%, %red_nexus_hp%, %blue_nexus_hp%, %green_nexus_hp%, %yellow_nexus_hp%, %phase%");


            yaml.put("mysql").setCountTopLineBreaks(1).addComments("DATABASE");
            yaml.put("mysql", "enabled").setDefValues(true);
            yaml.put("mysql", "host");
            yaml.put("mysql", "port").setDefValues(3306);
            yaml.put("mysql", "username");
            yaml.put("mysql", "password");
            yaml.put("mysql", "database");
            yaml.put("mysql", "usessl").setDefValues(false);
            yaml.put("mysql", "sqlUrl").setDefValues("jdbc:mysql://{host}:{port}/{database}?autoReconnect=true&allowMultiQueries=true&useSSL={usessl}");


            yaml.add("game_time").setDefValues(480).addComments("SETTINGS");
            yaml.add("max_players").setDefValues(12);
            yaml.add("min_players").setDefValues(6);
            yaml.add("starting_time").setDefValues(60);
            yaml.add("players_needed_to_reduce_starting_time").setDefValues(10);
            yaml.add("reduced_time").setDefValues(20);
            yaml.add("max_island_chests").setDefValues(3);
            yaml.add("gamesPerServer").setDefValues(3);

            yaml.put("kits").setCountTopLineBreaks(1).addComments("KIT PRICES");
            yaml.put("kits", "armorer").setDefValues(80);
            yaml.put("kits", "frog").setDefValues(80);
            yaml.put("kits", "baseballplayer").setDefValues(80);
            yaml.put("kits", "healer").setDefValues(80);
            yaml.put("kits", "knight").setDefValues(80);
            yaml.put("kits", "ninja").setDefValues(80);
            yaml.put("kits", "archer").setDefValues(80);
            yaml.put("kits", "scout").setDefValues(80);
            yaml.put("kits", "alchemist").setDefValues(80);

            yaml.put("perks").setCountTopLineBreaks(1).addComments("PERK PRICES");
            yaml.put("perks", "absorption", "level1").setDefValues(425);
            yaml.put("perks", "absorption", "level2").setDefValues(600);
            yaml.put("perks", "absorption", "level3").setDefValues(800);
            yaml.put("perks", "absorption", "level4").setDefValues(900);
            yaml.put("perks", "arrow_recovery", "level1").setDefValues(500);
            yaml.put("perks", "arrow_recovery", "level2").setDefValues(600);
            yaml.put("perks", "arrow_recovery", "level3").setDefValues(800);
            yaml.put("perks", "arrow_recovery", "level4").setDefValues(900);
            yaml.put("perks", "arrow_recovery", "level5").setDefValues(1000);
            yaml.put("perks", "blazing_arrow", "level1").setDefValues(500);
            yaml.put("perks", "blazing_arrow", "level2").setDefValues(600);
            yaml.put("perks", "blazing_arrow", "level3").setDefValues(800);
            yaml.put("perks", "blazing_arrow", "level4").setDefValues(900);
            yaml.put("perks", "blazing_arrow", "level5").setDefValues(1000);
            yaml.put("perks", "digger", "level1").setDefValues(600);
            yaml.put("perks", "digger", "level2").setDefValues(900);
            yaml.put("perks", "ender", "level1").setDefValues(800);
            yaml.put("perks", "ender", "level2").setDefValues(1400);
            yaml.put("perks", "ender", "level3").setDefValues(1600);
            yaml.put("perks", "ender", "level4").setDefValues(2000);
            yaml.put("perks", "lucky_charm", "level1").setDefValues(500);
            yaml.put("perks", "lucky_charm", "level2").setDefValues(650);
            yaml.put("perks", "lucky_charm", "level3").setDefValues(900);
            yaml.put("perks", "lucky_charm", "level4").setDefValues(1200);
            yaml.put("perks", "lucky_charm", "level5").setDefValues(1600);
            yaml.put("perks", "rambo", "level1").setDefValues(800);
            yaml.put("perks", "rambo", "level2").setDefValues(1500);
            yaml.put("perks", "rambo", "level3").setDefValues(2000);
            yaml.put("perks", "resistance_boost", "level1").setDefValues(400);
            yaml.put("perks", "resistance_boost", "level2").setDefValues(500);
            yaml.put("perks", "resistance_boost", "level3").setDefValues(725);
            yaml.put("perks", "void_scavenger", "level1").setDefValues(425);
            yaml.put("perks", "void_scavenger", "level2").setDefValues(500);
            yaml.put("perks", "void_scavenger", "level3").setDefValues(600);
            yaml.put("perks", "void_scavenger", "level4").setDefValues(800);
            yaml.put("perks", "void_scavenger", "level5").setDefValues(900);

            yaml.put("kill_sounds").setCountTopLineBreaks(1).addComments("COSMETICS PRICES");
            yaml.put("kill_sounds", "epic").setDefValues(12000);
            yaml.put("kill_sounds", "rare").setDefValues(10000);
            yaml.put("kill_sounds", "uncommon").setDefValues(8000);
            yaml.put("kill_sounds", "common").setDefValues(5000);
            yaml.put("projectile_trails").setCountTopLineBreaks(1);
            yaml.put("projectile_trails", "common").setDefValues(5000);
            yaml.put("first_and_final_kill_effects").setCountTopLineBreaks(1);
            yaml.put("first_and_final_kill_effects", "legendary").setDefValues(12000);
            yaml.put("first_and_final_kill_effects", "epic").setDefValues(10000);
            yaml.put("first_and_final_kill_effects", "rare").setDefValues(8000);
            yaml.put("first_and_final_kill_effects", "uncommon").setDefValues(7000);
            yaml.put("first_and_final_kill_effects", "common").setDefValues(5000);
            yaml.put("kill_messages").setCountTopLineBreaks(1);
            yaml.put("kill_messages", "legendary").setDefValues(20000);
            yaml.put("kill_messages", "epic").setDefValues(15000);
            yaml.put("kill_messages", "rare").setDefValues(12000);
            yaml.put("kill_messages", "uncommon").setDefValues(10000);
            yaml.put("kill_messages", "common").setDefValues(8000);

            yaml.save();

        } catch (IOException | YamlReaderException | IllegalListException | DuplicateKeyException | NotLoadedException | IllegalKeyException | YamlWriterException e) {
            throw new RuntimeException(e);
        }

    }
}
