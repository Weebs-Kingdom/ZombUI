package mindcollaps.zombui.visual.userInterface.parts;

import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.userInterface.GuiAction;

import java.util.List;

public interface Component {

    boolean isComponentMovable();

    List<GuiAction> getActions();

    CustomItem getItem();
}
