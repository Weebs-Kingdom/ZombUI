package zombui.zombui.visual.userInterface.parts;

import zombui.zombui.visual.CustomItem;
import zombui.zombui.visual.userInterface.GuiAction;

import java.util.List;

public interface Component {

    boolean isComponentMovable();
    List<GuiAction> getActions();
    CustomItem getItem();
}
