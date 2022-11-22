package zombui.zombui.visual.userInterface.gui;

import org.bukkit.entity.Player;
import zombui.zombui.ZombUI;

import java.util.ArrayList;

public class MCPageableGui {

    /**
     * For reference to own instance and calling of methods etc
     */
    private final ZombUI plugin;
    private final ArrayList<GuiParameters> pages;
    private Player player;

    public MCPageableGui(ZombUI plugin, Player player, ArrayList<GuiParameters> pages) {
        this.pages = pages;
        this.plugin = plugin;
    }

    /**
     * @param i The index of the page you want to load
     */
    public void loadPage(int i) {
        if (pages.size() > i)
            return;

        new MCGui(plugin, player, pages.get(i)).open();
    }

    public ArrayList<GuiParameters> getPages() {
        return pages;
    }
}
