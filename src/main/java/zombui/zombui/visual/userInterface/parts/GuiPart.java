package zombui.zombui.visual.userInterface.parts;

import zombui.zombui.visual.CustomItem;
import zombui.zombui.visual.userInterface.GuiAction;

import java.util.List;

public class GuiPart implements Component {

    private final boolean isMovable;

    /**
     * The CustomItem in use
     */
    private final CustomItem item;

    /**
     * Constructor for new GuiPart
     *
     * @param item CustomItem to use
     */
    public GuiPart(CustomItem item) {
        item.name("");
        this.item = item;
        this.isMovable = false;
    }

    /**
     * Constructor for new GuiPart
     *
     * @param item      to use
     * @param isMovable true or false
     */
    public GuiPart(CustomItem item, boolean isMovable) {
        item.name("");
        this.item = item;
        this.isMovable = isMovable;
    }

    /**
     * Check if item can be moved
     *
     * @return true if item can be moved
     */
    @Override
    public boolean isComponentMovable() {
        return isMovable;
    }

    /**
     * get registered gui actions of item
     *
     * @return list of GuiActions
     */
    @Override
    public List<GuiAction> getActions() {
        return null;
    }

    /**
     * get used CustomItem
     *
     * @return CustomItem
     */
    @Override
    public CustomItem getItem() {
        return item;
    }
}
