package mindcollaps.zombui.visual.userInterface.gui.generic;

import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.Text;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.MCGui;
import mindcollaps.zombui.visual.userInterface.parts.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ColorPickGui {

    private final ZombUi plugin;

    //GUI
    private final PositionGui.PositionGuiAction action;
    private final String title;
    private final Player player;
    private MCGui mcGui;

    private final Material[] colors = new Material[]{
            Material.WHITE_DYE,
            Material.ORANGE_DYE,
            Material.MAGENTA_DYE,
            Material.LIGHT_BLUE_DYE,
            Material.YELLOW_DYE,
            Material.LIME_DYE,
            Material.PINK_DYE,
            Material.GRAY_DYE,
            Material.LIGHT_GRAY_DYE,
            Material.CYAN_DYE,
            Material.PURPLE_DYE,
            Material.BLUE_DYE,
            Material.BROWN_DYE,
            Material.GREEN_DYE,
            Material.RED_DYE,
            Material.BLACK_DYE,

    };

    public ColorPickGui(ZombUi plugin, String title, Player player, PositionGui.PositionGuiAction action) {
        this.plugin = plugin;
        this.title = title;
        this.player = player;
        this.action = action;
    }

    public void generate() {
        GuiParameters parameters = new GuiParameters();
        parameters.setTitle(title);
        parameters.setSlots(27);

        int slot = 0;
        
    }
}
