package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.game.GameManager;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.skywars.utils.DataHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    public String replaceLine(String line, Game game){
        GameState state = game.getState();

        line = line.replaceAll("%gamestate%", state.name());
        line = line.replaceAll("%gametime%", (game.getRunningMainTask() != null ? "" + game.getRunningMainTask().getCounter() : "00:00"));
        line = line.replaceAll("%players%", "" + game.getPlayers().size());
        line = line.replaceAll("%max_players%", "" + game.getSettings().getMaxPlayers());

        return line;
    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPing(ServerListPingEvent e) {
        FileConfiguration config = DataHandler.getMainConfig().getConfig();
        /*Game game = GameManager.getGames().get(0);
        if (game == null){
            e.setMotd("Set up the game!");
            return;
        }

        String motd = config.getString("server_motd");
        if (motd == null){
            motd = "Configure motd in config.yml";
        }

        motd = replaceLine(motd, GameManager.getGames().get(0));

        if (motd.contains("/newline/")) {
            String[] arrSplit = motd.split("/newline/");
            motd = arrSplit[0] + "\n" + arrSplit[1];
        }*/

        StringBuilder motd = new StringBuilder();
        for (Game game : GameManager.getGames()){
            if (!motd.isEmpty()){
                motd.append(" ");
            }
            motd.append(getMotdForGame(game));
        }

        e.setMotd(motd.toString());
    }

    public String getMotdForGame(Game game){
        String abc = game.getName().substring(game.getName().length() - 1);
        boolean canJoin = (game.getState().equals(GameState.WAITING) || game.getState().equals(GameState.STARTING));

        return abc + ";" + canJoin + ":" + game.getPlayers().size() + ":" + game.getSettings().getMaxPlayers();
    }


    /*String mtd = "Loading";

        String map = "§c§lNONE";
        if (GameManager.getGame().isVoting()) {
            map = "§cVoting";
        } else {
            map = "§e" + GameManager.getGame().getPlayingArena().getID();
        }

        List<String> lore = new ArrayList<String>();

        String state = "RESTARTING";


        if (translated.contains("/newline/")){
            String[] arrSplit = translated.split("/newline/");



        Arena arena = GameManager.getGame().getPlayingArena();

        if (GameManager.getGame().getState().equals(GameState.LOBBY)) {
            if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
                lore.add("§7Players: §c" + GameManager.getGame().getAlivePlayers().size() + "§7/§c" + GameManager.getGame().getSettings().getMaxPlayers());
                lore.add("§7Status: §rWaiting");
                lore.add("§7Map: §r" + map);
                state = "FULL_ARENA";
            } else {
                lore.add("§7Players: §c" + GameManager.getGame().getAlivePlayers().size() + "§7/§c" + GameManager.getGame().getSettings().getMaxPlayers());
                lore.add("§7Status: §rrWaiting");
                lore.add("§7Map: §r" + map);
                state = "LOBBY";
            }
        }else if(GameManager.getGame().getState().equals(GameState.STARTING)){
            if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
                lore.add("§7Players: §c" + GameManager.getGame().getAlivePlayers().size() + "§7/§c" + GameManager.getGame().getSettings().getMaxPlayers());
                lore.add("§7Status: §rStarting");
                lore.add("§7Map: §r" + map);
                String time = GameUtil.getDurationString(GameManager.getGame().getTask().getCounter());
                lore.add("§7Starting in: §e" + time);
                state = "FULL_ARENA";
            } else {
                lore.add("§7Players: §c" + GameManager.getGame().getAlivePlayers().size() + "§7/§c" + GameManager.getGame().getSettings().getMaxPlayers());
                lore.add("§7Status: §rStarting");
                lore.add("§7Map: §r" + map);
                String time = GameUtil.getDurationString(GameManager.getGame().getTask().getCounter());
                lore.add("§7Starting in: §e" + time);
                state = "STARTING";
            }
        } else if (GameManager.getGame().getState().equals(GameState.INGAME) || GameManager.getGame().getState().equals(GameState.PREPARATION)) {
            String time = GameUtil.getDurationString(GameManager.getGame().getTask().getCounter());
            if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
                lore.add("§7Players: §c" + GameManager.getGame().getAlivePlayers().size() + "§7/§c" + GameManager.getGame().getSettings().getMaxPlayers());
                lore.add("§7Status: §eInGame");
                lore.add("§7Map: §r" + map);
                lore.add("§7Phase: §e" + Phase.getPhase(GameManager.getGame()));
                lore.add("§7Time: §e" + time);
                lore.add("§7Nexuses:");
                lore.add("  §cRed Nexus: §e" + Nexus.getNexus(arena, TeamManager.getTeam("Red")));
                lore.add("  §9Blue Nexus: §e" + Nexus.getNexus(arena, TeamManager.getTeam("Blue")));
                lore.add("  §aGreen Nexus: §e" + Nexus.getNexus(arena, TeamManager.getTeam("Green")));
                lore.add("  §eYellow Nexus: §e" + Nexus.getNexus(arena, TeamManager.getTeam("Yellow")));
                state = "INGAME";
            } else {
                lore.add("§7Players: §c" + GameManager.getGame().getAlivePlayers().size() + "§7/§c" + GameManager.getGame().getSettings().getMaxPlayers());
                lore.add("§7Status: §eInGame");
                lore.add("§7Map: §r" + map);
                lore.add("§7Phase: §e" + Phase.getPhase(GameManager.getGame()));
                lore.add("§7Time: §e" + time);
                lore.add("§r ");
                lore.add("§7Nexuses:");
                lore.add("  §cRed Nexus: §e" + Nexus.getNexus(arena, TeamManager.getTeam("Red")));
                lore.add("  §9Blue Nexus: §e" + Nexus.getNexus(arena, TeamManager.getTeam("Blue")));
                lore.add("  §aGreen Nexus: §e" + Nexus.getNexus(arena, TeamManager.getTeam("Green")));
                lore.add("  §eYellow Nexus: §e" + Nexus.getNexus(arena, TeamManager.getTeam("Yellow")));
                state = "INGAME";
            }

        }

        String lor = "";


        int i = 0;
        for (String l : lore) {

            if (lore.size() == i+1) {
                lor = lor + l;
            } else {
                lor = lor + l + "-§r";
            }

            i++;
        }

        mtd = state + "#" + lor;

        e.setMotd(mtd);

    }*/
}
