package zombui.zombui.logic.map.trigger;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class LocationTrigger extends ZombieTrigger {

    private static final long serialVersionUID = 42L;

    //Transient
    private transient boolean triggered = false;
    private transient Player trigger;

    private final int radius;

    public LocationTrigger(TriggerType type, Location location, int radius, String name) {
        super(type, location, name);
        this.radius = radius;
    }

    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().distance(getLocation()) <= radius) {
            if (!triggered) {
                trigger(event.getPlayer(), getZombUI());
                triggered = true;
                trigger = event.getPlayer();
            }
        } else {
            if(trigger != null){
                if(trigger.equals(event.getPlayer()) || !trigger.isOnline()){
                    triggered = false;
                    trigger = null;
                }
            }
        }
    }
}
