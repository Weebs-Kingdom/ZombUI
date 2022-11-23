package zombui.zombui;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import zombui.zombui.commands.CmdZombies;
import zombui.zombui.logic.GameSession;
import zombui.zombui.logic.map.ZombieMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ZombUi extends JavaPlugin {

    private final File zombieMapDat = new File(getDataFolder().getAbsolutePath() + "/zombUiMap.dat");
    //Worldname, Zombie map
    private HashMap<UUID, ZombieMap> zombieMapHashMap;
    private HashMap<UUID, GameSession> gameSessionHashMap = new HashMap<>();

    public static void createFileRootAndFile(File file) {
        if (file.exists()) return;
        String pas = file.getAbsolutePath().replace("\\", "/");
        String[] path = pas.split("/");
        String pat = path[0];
        for (int i = 1; i < path.length - 1; i++) {
            pat = pat + "/" + path[i];
        }
        File dir = new File(pat);
        if (!dir.mkdirs()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {

            }
        }
        createFiles(file);
    }

    private static void createFiles(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onEnable() {
        loadData();

        new CmdZombies(this);
    }

    @Override
    public void onDisable() {
        saveData();
    }

    public boolean saveData() {
        createFileRootAndFile(zombieMapDat);
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(zombieMapDat.getAbsolutePath())));
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
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(zombieMapDat.getAbsolutePath())));
            zombieMapHashMap = (HashMap<UUID, ZombieMap>) in.readObject();
            in.close();
            return true;
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Could not load data from zombieMaps.dat");
            return false;
        }
    }

    public ZombieMap getZombieMap(World world) {
        if (zombieMapHashMap != null)
            return zombieMapHashMap.get(world.getUID());
        return null;
    }

    public void createZombieMap(ZombieMap map, World world) {
        if (zombieMapHashMap == null)
            zombieMapHashMap = new HashMap<>();

        zombieMapHashMap.put(world.getUID(), map);
    }

    public GameSession getGameSession(World world) {
        if (gameSessionHashMap != null)
            return gameSessionHashMap.get(world.getUID());
        return null;
    }

    public void createGameSession(GameSession session, World world) {
        if (gameSessionHashMap == null)
            gameSessionHashMap = new HashMap<>();

        gameSessionHashMap.put(world.getUID(), session);
    }
}
