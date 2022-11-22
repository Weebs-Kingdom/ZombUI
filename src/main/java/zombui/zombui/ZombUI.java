package zombui.zombui;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import zombui.zombui.logic.map.ZombieMap;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ZombUI extends JavaPlugin {

    //Worldname, Zombie map
    private HashMap<UUID, ZombieMap> zombieMapHashMap;

    @Override
    public void onEnable() {
        loadData();
    }

    @Override
    public void onDisable() {
        saveData();
    }

    public boolean saveData() {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream("zombieMaps.dat")));
            out.writeObject(zombieMapHashMap);
            out.close();
            return true;
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Could not save data to zombieMaps.dat", e);
            return false;
        }
    }

    public boolean loadData() {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream("zombieMaps.dat")));
            zombieMapHashMap = (HashMap<UUID, ZombieMap>) in.readObject();
            in.close();
            return true;
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Could not load data from zombieMaps.dat", e);
            return false;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return super.onCommand(sender, command, label, args);
    }

    public ZombieMap getZombieMap(World world){
        if(zombieMapHashMap != null)
            return zombieMapHashMap.get(world.getUID());
        return null;
    }

    public void createZombieMap(ZombieMap map, World world){
        if(zombieMapHashMap ==null)
            zombieMapHashMap = new HashMap<>();

        zombieMapHashMap.put(world.getUID(), map);
    }
}
