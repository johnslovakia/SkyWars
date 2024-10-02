package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.skywars.chest.ChestManager;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.events.TaskEvent;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.users.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CountdownListener implements Listener {

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

        /*if (e.getTask().getId().equalsIgnoreCase("ThrowableTNT")){
            if (e.getCounter() == 0){
                Location location = DataHandler.getMapsConfig().getLocation("maps." + game.getPlayingArena().getID() + ".throwableTNT");
                location.setWorld(game.getPlayingArena().getWorld());
                if (location == null){
                    return;
                }
                spawnHolograms(game, location);

                Block block = location.subtract(0.0D, 1.0D, 0.0D).getBlock();
                if (block != null) {
                    block.setType(Material.DIAMOND_BLOCK);
                }
                e.getTask().cancelRunnable(false);
            }
        }*/

        if (e.getTask().getId().equalsIgnoreCase("ChestRefill")){
            if (e.getCounter() == 0){
                MessageManager.get(game.getParticipants(), "chat.chest_refilled")
                        .send();
                //ChestManager.refill();
                ChestManager.getChestManager(game).refill();
                if (e.getTask().getRestartCount() <= 3) {
                    e.getTask().restart();
                }
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
