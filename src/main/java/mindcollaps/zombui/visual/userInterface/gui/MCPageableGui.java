package mindcollaps.zombui.visual.userInterface.gui;

import org.bukkit.entity.Player;
import mindcollaps.zombui.ZombUi;

import java.util.ArrayList;

public class MCPageableGui {

    /**
     * For reference to own instance and calling of methods etc
     */
    private final ZombUi plugin;
    private final ArrayList<GuiParameters> pages;
    private Player player;

    public MCPageableGui(ZombUi plugin, Player player, ArrayList<GuiParameters> pages) {
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
