package cz.johnslovakia.skywars.boards;

import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.events.GameJoinEvent;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.gameapi.task.Task;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.GamePlayerType;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.Logger;
import cz.johnslovakia.gameapi.utils.StringUtils;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.structures.Meteorite;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Scoreboard implements Listener {

    @EventHandler
    public void onGameJoin(GameJoinEvent e) {
        Player player = e.getGamePlayer().getOnlinePlayer();

        FastBoard board = new FastBoard(player);
        e.getGamePlayer().setScoreboard(board);

        new BukkitRunnable(){
            @Override
            public void run() {
                GamePlayer gamePlayer = e.getGamePlayer();
                if (gamePlayer == null || !gamePlayer.isInGame() || gamePlayer.getType().equals(GamePlayerType.DISCONNECTED) || !gamePlayer.getPlayer().isOnline()){
                    this.cancel();
                    return;
                }
                updateBoard(board);
            }
        }.runTaskTimer(SkyWars.getInstance(), 0, 20L);

    }

    public String replaceLine(String line, GamePlayer gamePlayer){
        if (gamePlayer.getPlayerData().getGame() == null){
            return "";
        }
        Game game = gamePlayer.getPlayerData().getGame();
        GameState state = gamePlayer.getPlayerData().getGame().getState();


        Date date = new Date();
        String dateFormat = new SimpleDateFormat("MM/dd/yyyy").format(date);
        line = line.replaceAll("%date%", dateFormat);

        line = line.replaceAll("%game_id%", game.getID());
        line = line.replaceAll("%players%", "" + game.getPlayers().size());
        line = line.replaceAll("%max_players%", "" + game.getSettings().getMaxPlayers());
        line = line.replaceAll("%kit%", getKit(gamePlayer));
        line = line.replaceAll("%map%", (game.getPlayingMap() != null ? game.getPlayingMap().getName() : "None"));

        if (state.equals(GameState.PREPARATION) || state.equals(GameState.INGAME)) {
            line = line.replaceAll("%chest_refill%", state.equals(GameState.PREPARATION) ? "Preparing..." : (Task.getTask(game, "ChestRefill") != null ? StringUtils.getDurationString(Task.getTask(game, "ChestRefill").getCounter()) : "00:00"));
            line = line.replaceAll("%meteorite%", state.equals(GameState.PREPARATION) ? "Preparing..." : (Task.getTask(game, "Meteorite") != null ? StringUtils.getDurationString(Task.getTask(game, "Meteorite").getCounter()) : "§a" + (Meteorite.getMeteorite(game) != null && Meteorite.getMeteorite(game).isOpened() ? MessageManager.get(gamePlayer, "scoreboard.meteorite_looted").getTranslated() :  MessageManager.get(gamePlayer, "scoreboard.meteorite_crashed").getTranslated())));
            line = line.replaceAll("%alive_enemies%", "" + game.getPlayers().stream().filter(gp -> gp != gamePlayer).toList().size());

            int playersAtStart = (int) game.getMetadata().get("players_at_start");
            line = line.replaceAll("%players_at_start%", "" + (playersAtStart - 1));
        }

        line = line.replaceAll("%kills%", "" + gamePlayer.getScoreByName("Kill").getScore());
        line = line.replaceAll("%assists%", "" + gamePlayer.getScoreByName("Assist").getScore());

        return line;
    }

    public String getTitle(GamePlayer gamePlayer){
        if (gamePlayer.getPlayerData().getGame() == null){
            return "";
        }

        GameState state = gamePlayer.getPlayerData().getGame().getState();
        String language = gamePlayer.getLanguage().getName().toLowerCase();
        File messagesFolder = new File(SkyWars.getInstance().getDataFolder(), "languages");
        File file = new File(messagesFolder, language + "_scoreboard.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (state.equals(GameState.WAITING)) {
            if (config.getString("scoreboard.Waiting.title") != null){
                return replaceTime(config.getString("scoreboard.Waiting.title"), gamePlayer);
            }
        }else if (state.equals(GameState.STARTING)) {
            if (config.getString("scoreboard.Waiting.title") != null){
                return replaceTime(config.getString("scoreboard.Starting.title"), gamePlayer);
            }
        }else if (state.equals(GameState.PREPARATION) || state.equals(GameState.INGAME)) {
            if (config.getString("scoreboard.InGame.title") != null){
                return replaceTime(config.getString("scoreboard.InGame.title"), gamePlayer);
            }
        }else if (state.equals(GameState.ENDING)) {
            if (config.getString("scoreboard.Ending.title") != null){
                return replaceTime(config.getString("scoreboard.Ending.title"), gamePlayer);
            }
        }

        return "§2§lSkyWars";
    }

    public String replaceTime(String line, GamePlayer gamePlayer){
        Game game = gamePlayer.getPlayerData().getGame();
        if (game.getRunningMainTask() != null) {
            line = line.replaceAll("%time%", StringUtils.getDurationString(game.getRunningMainTask().getCounter()));
        }else{
            line = line.replaceAll("%time%", "00:00");
        }
        return line;
    }




    private void updateBoard(FastBoard board) {
        GamePlayer gamePlayer = PlayerManager.getGamePlayer(board.getPlayer());
        board.updateTitle(getTitle(gamePlayer));

        List<String> lines = new ArrayList<>();

        for (String line : getLines(gamePlayer)){
            lines.add(replaceLine(line, gamePlayer));
        }

        board.updateLines(lines);
    }

    public List<String> getLines(GamePlayer gamePlayer){
        if (gamePlayer.getPlayerData().getGame() == null){
            return null;
        }
        GameState state = gamePlayer.getPlayerData().getGame().getState();
        String language = gamePlayer.getLanguage().getName().toLowerCase();
        File messagesFolder = new File(SkyWars.getInstance().getDataFolder(), "languages");
        File file = new File(messagesFolder, language + "_scoreboard.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> lines = new ArrayList<>();


        if (state.equals(GameState.WAITING)) {
            if (config.getStringList("scoreboard.Waiting.lines").isEmpty()){
                Logger.log("There is no scoreboard for " + language + "!", Logger.LogType.ERROR);
                lines.add("§cNo Scoreboard for " + language);
                return lines;
            }
            lines.addAll(config.getStringList("scoreboard.Waiting.lines"));
        }else if (state.equals(GameState.STARTING)) {
            if (config.getStringList("scoreboard.Starting.lines").isEmpty()){
                Logger.log("There is no scoreboard for " + language + "!", Logger.LogType.ERROR);
                lines.add("§cNo Scoreboard for " + language);
                return lines;
            }
            lines.addAll(config.getStringList("scoreboard.Starting.lines"));
        }else if (state.equals(GameState.PREPARATION) || state.equals(GameState.INGAME)) {
            if (config.getStringList("scoreboard.InGame.lines").isEmpty()){
                Logger.log("There is no scoreboard for " + language + "!", Logger.LogType.ERROR);
                lines.add("§cNo Scoreboard for " + language);
                return lines;
            }
            lines.addAll(config.getStringList("scoreboard.InGame.lines"));
        }else if (state.equals(GameState.ENDING)) {
            if (config.getStringList("scoreboard.Ending.lines").isEmpty()){
                Logger.log("There is no scoreboard for " + language + "!", Logger.LogType.ERROR);
                lines.add("§cNo Scoreboard for " + language);
                return lines;
            }
            lines.addAll(config.getStringList("scoreboard.Ending.lines"));
        }else{
            lines.add("§cSetup Mode");
        }

        return lines;
    }

    public static String getKit(GamePlayer gamePlayer){
        if (gamePlayer.getPlayerData().getKit() == null){
            return MessageManager.get(gamePlayer, "scoreboard.kit.none").getTranslated();
        }else{
            return gamePlayer.getPlayerData().getKit().getName();
        }
    }
}
