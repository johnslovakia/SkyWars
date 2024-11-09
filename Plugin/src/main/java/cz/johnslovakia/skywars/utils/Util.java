package cz.johnslovakia.skywars.utils;

import cz.johnslovakia.gameapi.utils.Sounds;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.api.Trail;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Util {

    public static int getPrice(String path, int defaultPrice){
        int price = DataHandler.getMainConfig().getConfig().getInt(path);
        return (price != 0 ? price : defaultPrice);
    }

    public static int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }
}
