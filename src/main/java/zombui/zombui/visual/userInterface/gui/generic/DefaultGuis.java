package zombui.zombui.visual.userInterface.gui.generic;

import org.bukkit.entity.Player;
import zombui.zombui.ZombUI;
import zombui.zombui.visual.userInterface.GuiAction;
import zombui.zombui.visual.userInterface.GuiType;
import zombui.zombui.visual.userInterface.gui.GuiParameters;
import zombui.zombui.visual.userInterface.gui.MCGui;

public class DefaultGuis {

    public static void generateStringGui(ZombUI zombUI, Player player, String defaultValue, GuiAction guiAction){
        GuiParameters parameters = new GuiParameters(GuiType.TYPING_STRING_INVENTORY);
        parameters.setDefaultValue(defaultValue);
        parameters.setValueAction(guiAction);

        new MCGui(zombUI, player, parameters).open();
    }

    public static void generateDoubleGui(ZombUI zombUI, Player player, double defaultValue, GuiAction guiAction){
        GuiParameters parameters = new GuiParameters(GuiType.TYPING_DOUBLE_INVENTORY);
        parameters.setDefaultValue(String.valueOf(defaultValue));
        parameters.setValueAction(guiAction);

        new MCGui(zombUI, player, parameters).open();
    }
}
