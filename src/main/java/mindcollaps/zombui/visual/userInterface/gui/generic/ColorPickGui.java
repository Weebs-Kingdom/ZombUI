package mindcollaps.zombui.visual.userInterface.gui.generic;

import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.userInterface.ActionType;
import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.MCGui;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.SelectorAction;
import mindcollaps.zombui.visual.userInterface.parts.Button;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ColorPickGui {

    private final ZombUi plugin;

    //GUI
    private final String title;
    private final Player player;
    private MCGui mcGui;
    private SelectorAction<String> action;

    private final Material[] colors = new Material[]{
            Material.WHITE_DYE,
            Material.ORANGE_DYE,
            Material.LIGHT_BLUE_DYE,
            Material.YELLOW_DYE,
            Material.LIME_DYE,
            Material.PINK_DYE,
            Material.GRAY_DYE,
            Material.LIGHT_GRAY_DYE,
            Material.CYAN_DYE,
            Material.PURPLE_DYE,
            Material.BLUE_DYE,
            Material.GREEN_DYE,
            Material.RED_DYE,
            Material.BLACK_DYE,

    };

    public ColorPickGui(ZombUi plugin, String title, Player player, SelectorAction<String> action) {
        this.plugin = plugin;
        this.title = title;
        this.player = player;
        this.action = action;
    }

    public void generate() {
        GuiParameters parameters = new GuiParameters();
        parameters.setTitle(title);
        parameters.setSlots(54);

        int slot = 10;
        for (Material color : colors) {
            parameters.getComponents().put(slot, new Button(
                    new CustomItem(color, "&f" + color.name().replace("_", " ")),
                    new GuiAction() {
                        @Override
                        public void onClick(ActionType actionType, MCGui gui) {
                            action(color, null);
                        }
                    }));

            slot++;
            if(slot == 17)
                slot = 19;
            else if(slot == 26)
                slot = 28;
            else if(slot == 35)
                slot = 40;
        }

        mcGui = new MCGui(plugin, player, parameters);
        mcGui.open();
    }

    private void action(Material color, MCGui gui) {
        switch (color) {
            case WHITE_DYE -> action.selected("&f");
            case ORANGE_DYE -> action.selected("&6");
            case LIGHT_BLUE_DYE -> action.selected("&b");
            case YELLOW_DYE -> action.selected("&e");
            case LIME_DYE -> action.selected("&a");
            case PINK_DYE -> action.selected("&d");
            case GRAY_DYE -> action.selected("&8");
            case LIGHT_GRAY_DYE -> action.selected("&7");
            case CYAN_DYE -> action.selected("&3");
            case PURPLE_DYE -> action.selected("&5");
            case BLUE_DYE -> action.selected("&9");
            case GREEN_DYE -> action.selected("&2");
            case RED_DYE -> action.selected("&c");
            case BLACK_DYE -> action.selected("&0");
        }
        action.goBack(gui);
    }
}
