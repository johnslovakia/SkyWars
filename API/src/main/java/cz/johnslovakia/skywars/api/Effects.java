package cz.johnslovakia.skywars.api;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public interface Effects {

    public void playHearthEffect(Location location);
    public void playRedstoneEffect(Location location);
    public void playSquidEffect(Location location, Plugin plugin);
    public void playTornadoEffect(Location location, Plugin plugin);
    public void playWaveEffect(Location location, Plugin plugin);
    public void playBurningShoesEffect(Location location, Plugin plugin);
    public void trail(Location location, Trail trail);
}
