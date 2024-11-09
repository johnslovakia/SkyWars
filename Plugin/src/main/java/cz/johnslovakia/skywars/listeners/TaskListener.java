package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.game.map.Area;
import cz.johnslovakia.gameapi.game.map.GameMap;
import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.chest.ChestManager;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.events.TaskEvent;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.users.GamePlayer;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TaskListener implements Listener {

    //public static Map<Game, List<ArmorStand>> armorstands = new HashMap<>();
    public static Map<Game, BukkitTask> tasks = new HashMap<>();

    /*public void spawnHolograms(Game game, Location location){
        ArmorStand supportArmorStand = (ArmorStand) location.getWorld().spawn(location.add(0.0, 0.25D, 0.0), ArmorStand.class);
        supportArmorStand.setVisible(false);
        supportArmorStand.setGravity(false);
        supportArmorStand.setCustomNameVisible(false);
        supportArmorStand.setHelmet(new ItemStack(Material.TNT));

        ArmorStand firstArmorStand = (ArmorStand) location.getWorld().spawn(location.add(0.0, 0.20D, 0.0), ArmorStand.class);
        firstArmorStand.setCustomName("§c§lThrowable TNT");
        firstArmorStand.setCustomNameVisible(true);
        firstArmorStand.setVisible(false);
        firstArmorStand.setGravity(false);

        BukkitTask task = new BukkitRunnable(){
            int x = -360;
            @Override
            public void run() {
                if (supportArmorStand != null) {
                    /*Location l = supportArmorStand.getLocation();
                    l.setYaw(x);
                    supportArmorStand.teleport(l);
                    //supportArmorStand.setHeadPose(new EulerAngle(Math.toRadians(x), 0, 0));
                    x++;*

                    Location rotatingLoc = supportArmorStand.getLocation().clone();
                    float yaw = rotatingLoc.getYaw() + 4;
                    if (yaw >= 180)
                        yaw *= -1;
                    rotatingLoc.setYaw(yaw);
                    supportArmorStand.teleport(rotatingLoc);

                    //supportArmorStand.setHeadPose(supportArmorStand.getHeadPose().add(0, 0, 0.17));
                }
            }
        }.runTaskTimer(SkyWars.getInstance(), 0, 1);

        armorstands.put(game, Arrays.asList(supportArmorStand, firstArmorStand));
        tasks.put(game, task);
    }*/

    private double time;

    @EventHandler
    public void onTask(TaskEvent e){
        Game game = e.getTask().getGame();

        if (game.getState() != GameState.INGAME){
            return;
        }

        if (e.getTask().getId().equalsIgnoreCase("ChestRefill")){
            if (e.getCounter() == 0){
                ChestManager.getChestManager(game).refill();
                MessageManager.get(game.getParticipants(), "chat.chest_refilled")
                        .send();

                if (e.getTask().getRestartCount() <= 3) {
                    e.getTask().restart();
                }
            }
        }

        if (e.getTask().equals(game.getRunningMainTask())){
            GameMap map = game.getPlayingMap();
            World world = map.getWorld();
            Area mainArea = map.getMainArea();
            WorldBorder worldBorder = world.getWorldBorder();

            double width = Math.abs(mainArea.getLocation1().getX() - mainArea.getLocation2().getX());
            double length = Math.abs(mainArea.getLocation1().getZ() - mainArea.getLocation2().getZ());
            double size = Math.max(width, length);

            if (e.getCounter() == 120){
                MessageManager.get(game.getParticipants(), "chat.border_shrinking")
                                .send();

                worldBorder.setCenter(map.getMainArea().getCenter());
                worldBorder.setSize(size);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        double speed = (size >= 150 ? 0.065 : 0.04);
                        worldBorder.setSize(worldBorder.getSize() - speed);

                        if (game.getPlayingMap() == null || !game.getState().equals(GameState.INGAME) || worldBorder.getSize() <= 15) {
                            cancel();
                        }
                    }
                }.runTaskTimer(SkyWars.getInstance(), 1L, 1L);
            }
        }

        if (e.getTask().getId().equalsIgnoreCase("TimePlaying")){
            if (e.getCounter() == 0){
                if (game.getState() != GameState.INGAME){
                    e.getTask().cancel();
                    return;
                }
                for (GamePlayer player : game.getPlayers()) {
                    player.getScoreByName("TimePlayed").increaseScore();
                }
                if (e.getTask().getRestartCount() <= 2) {
                    e.getTask().restart();
                }
            }
        }
    }
}
