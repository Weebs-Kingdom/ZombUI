package zombui.zombui.logic.map.trigger;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import zombui.zombui.ZombUI;

import java.io.Serializable;

 public abstract class ZombieTriggerAction implements Serializable {

    private static final long serialVersionUID = 42L;

    private String name;
    private Location location;

     public ZombieTriggerAction(Location location) {
         this.location = location;
     }

     public abstract void action(Player player, ZombUI zombUI);

     public Location getLocation() {
         return location;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }
 }
