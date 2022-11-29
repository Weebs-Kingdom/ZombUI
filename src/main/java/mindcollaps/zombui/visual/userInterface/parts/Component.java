package mindcollaps.zombui.visual.userInterface.parts;

import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.CustomItem;

import java.util.List;

public interface Component {

    boolean isComponentMovable();

    List<GuiAction> getActions();

    CustomItem getItem();
}
