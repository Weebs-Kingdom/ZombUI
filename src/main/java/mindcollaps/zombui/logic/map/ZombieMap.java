package mindcollaps.zombui.logic.map;

import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.logic.map.spawner.ZombieSpawnPoint;
import mindcollaps.zombui.logic.map.trigger.ZombieTrigger;
import mindcollaps.zombui.logic.map.trigger.ZombieTriggerAction;
import org.bukkit.Location;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class ZombieMap implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;
    //Player stuff
    private final Location playerSpawnPoint;
    //Zombie stuff
    private ArrayList<ZombieSpawnPoint> zombieSpawnPoints = new ArrayList<>();
    private ArrayList<ZombieTrigger> zombieTriggers = new ArrayList<>();
    private ArrayList<ZombieTriggerAction> zombieTriggerActions = new ArrayList<>();

    public ZombieMap(Location playerSpawnPoint) {
        this.playerSpawnPoint = playerSpawnPoint;
    }

    //Init process
    public void initWorld(ZombUi zombUi) {
        initZombieTriggers(zombUi);
    }

    public void deactivateWorld(ZombUi zombUi) {
        deactivateZombieTriggers(zombUi);
    }

    private void deactivateZombieTriggers(ZombUi zombUi) {
        for (ZombieTrigger zombieTrigger : zombieTriggers) {
            zombieTrigger.removeListener(zombUi);
        }
    }

    private void initZombieTriggers(ZombUi zombUI) {
        for (ZombieTrigger zombieTrigger : zombieTriggers) {
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
