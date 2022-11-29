package mindcollaps.zombui.visual.userInterface.gui.generic;

import org.bukkit.entity.Player;
import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.GuiType;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.MCGui;

public class DefaultGuis {

    public static void generateStringGui(ZombUi zombUI, Player player, String defaultValue, GuiAction guiAction) {
        GuiParameters parameters = new GuiParameters(GuiType.TYPING_STRING_INVENTORY);
        parameters.setDefaultValue(defaultValue);
        parameters.setValueAction(guiAction);

        new MCGui(zombUI, player, parameters).open();
    }

    public static void generateDoubleGui(ZombUi zombUI, Player player, double defaultValue, GuiAction guiAction) {
        GuiParameters parameters = new GuiParameters(GuiType.TYPING_DOUBLE_INVENTORY);
        parameters.setDefaultValue(String.valueOf(defaultValue));
        parameters.setValueAction(guiAction);

        new MCGui(zombUI, player, parameters).open();
    }
}
