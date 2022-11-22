package zombui.zombui.logic.map.spawner;

import org.bukkit.Location;

import java.io.Serializable;

public class ZombieSpawnPoint implements Serializable {

    private static final long serialVersionUID = 42L;

    private Location spawnerLocation;
    private int spawnRadius = 5;
    private String name;

    public ZombieSpawnPoint(String name, Location spawnerLocation) {
        this.spawnerLocation = spawnerLocation;
        this.name = name;
    }

    public Location getSpawnerLocation() {
        return spawnerLocation;
    }

    public void setSpawnerLocation(Location spawnerLocation) {
        this.spawnerLocation = spawnerLocation;
    }

    public int getSpawnRadius() {
        return spawnRadius;
    }

    public void setSpawnRadius(int spawnRadius) {
        this.spawnRadius = spawnRadius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
