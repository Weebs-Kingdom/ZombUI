package zombui.zombui.logic.map;

import org.bukkit.Location;
import org.checkerframework.checker.units.qual.A;
import zombui.zombui.ZombUI;
import zombui.zombui.logic.map.spawner.ZombieSpawnPoint;
import zombui.zombui.logic.map.trigger.ZombieTrigger;
import zombui.zombui.logic.map.trigger.ZombieTriggerAction;

import java.io.Serializable;
import java.util.ArrayList;

public class ZombieMap implements Serializable {

    private static final long serialVersionUID = 42L;

    //Zombie stuff
    private ArrayList<ZombieSpawnPoint> zombieSpawnPoints = new ArrayList<>();
    private ArrayList<ZombieTrigger> zombieTriggers = new ArrayList<>();
    private ArrayList<ZombieTriggerAction> zombieTriggerActions = new ArrayList<>();

    //Player stuff
    private Location playerSpawnPoint;

    public ZombieMap(Location playerSpawnPoint) {
        this.playerSpawnPoint = playerSpawnPoint;
    }

    //Init process
    private void initZombieTriggers(ZombUI zombUI) {
        for(ZombieTrigger zombieTrigger : zombieTriggers) {
            zombieTrigger.initListener(zombUI);
        }
    }

    public ArrayList<ZombieSpawnPoint> getZombieSpawnPoints() {
        return zombieSpawnPoints;
    }

    public ArrayList<ZombieTrigger> getZombieTriggers() {
        return zombieTriggers;
    }

    public ArrayList<ZombieTriggerAction> getZombieTriggerActions() {
        return zombieTriggerActions;
    }

    public Location getPlayerSpawnPoint() {
        return playerSpawnPoint;
    }
}
