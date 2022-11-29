package mindcollaps.zombui.logic.playerclass;

import mindcollaps.zombui.ZombUi;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public abstract class PlayerClass implements Listener {

    private String name;
    private ItemStack icon;
    private int health;
    private ArrayList<ItemStack> inventory;

    public PlayerClass(String name, ItemStack icon, int health, ArrayList<ItemStack> inventory, ZombUi zombUI) {
        this.name = name;
        this.icon = icon;
        this.health = health;
        this.inventory = inventory;

        zombUI.getServer().getPluginManager().registerEvents(this, zombUI);
    }
}
