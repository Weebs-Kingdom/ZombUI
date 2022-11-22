package zombui.zombui.logic.map.trigger;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import zombui.zombui.ZombUI;

import java.io.Serializable;

public abstract class ZombieTrigger implements Serializable, Listener {

    private static final long serialVersionUID = 42L;

    //Transient
    private transient boolean triggered = false;
    private transient boolean registered = false;
    private transient ZombUI zombUI;

    private String name;
    private TriggerType type;
    private Location location;
    private ZombieTriggerAction[] actions;

    public ZombieTrigger(TriggerType type, Location location, String name) {
        this.type = type;
        this.location = location;
        this.name = name;
    }

    public void initListener(ZombUI zombUI) {
        if(!registered) {
            zombUI.getServer().getPluginManager().registerEvents(this, zombUI);
            this.zombUI = zombUI;
            registered = true;
        }
    }

    public void removeListener(ZombUI zombUI) {
        HandlerList.unregisterAll(this);
        this.registered = false;
    }

    public void trigger(Player player, ZombUI zombUI) {
        if(triggered && type == TriggerType.ONCE) return;

        triggered = true;
        for(ZombieTriggerAction response : actions) {
            response.action(player, zombUI);
        }

        if(type == TriggerType.ONCE) removeListener(zombUI);
    }


    public Location getLocation() {
        return location;
    }

    public TriggerType getType() {
        return type;
    }

    public ZombieTriggerAction[] getActions() {
        return actions;
    }

    public ZombUI getZombUI() {
        return zombUI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum TriggerType {
        ONCE, REPEAT
    }
}
