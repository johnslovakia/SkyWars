package cz.johnslovakia.skywars.events;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FireballLaunchEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Fireball fireball;

    public FireballLaunchEvent(Player player, Fireball fireball){
        this.player = player;
        this.fireball = fireball;
    }

    public Player getPlayer() {
        return player;
    }

    public Fireball getFireball() {
        return fireball;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}