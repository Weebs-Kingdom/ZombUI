package zombui.zombui.visual.userInterface.parts;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import zombui.zombui.visual.CustomItem;
import zombui.zombui.visual.userInterface.GuiAction;

import java.util.List;

public class Button implements Component {

    private final List<GuiAction> actions;
    private CustomItem item;

    /**
     * Constructor for a gui button
     *
     * @param item    to be used
     * @param actions that are supported
     */
    public Button(CustomItem item, GuiAction... actions) {
        this.actions = List.of(actions);
        this.item = item;
    }

    /**
     * Change material of button
     *
     * @param material to be used
     * @return changed button instance
     */
    public CustomItem setItem(Material material) {
        this.item = new CustomItem(material);
        return this.item;
    }

    /**
     * Change material of button
     *
     * @param stack to use
     * @return changed button instance
     */
    public CustomItem setItem(ItemStack stack) {
        this.item = new CustomItem(stack, false);
        return this.item;
    }

    @Override
    public boolean isComponentMovable() {
        return false;
    }

    /**
     * Get the supported actions
     *
     * @return supported actions as list
     */
    public List<GuiAction> getActions() {
        return actions;
    }

    /**
     * Get the used Item
     *
     * @return CustomItem in use
     */
    @Override
    public CustomItem getItem() {
        return item;
    }
}
