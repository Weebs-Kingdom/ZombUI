package mindcollaps.zombui.visual.userInterface.gui;

import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.GuiType;
import mindcollaps.zombui.visual.userInterface.parts.Component;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;

public class GuiParameters {

    private final InventoryType inventoryType;
    private final HashMap<Integer, Component> components;
    private final GuiType guiType;
    private String title;
    private int slots;
    private GuiAction valueAction;
    private String defaultValue;

    /**
     * Base Constructor for Custom Gui
     */
    public GuiParameters() {
        this.components = new HashMap<>();
        this.title = "Unnamed";
        this.slots = 27;
        this.inventoryType = InventoryType.CHEST;
        this.guiType = GuiType.fromInventoryType(this.inventoryType);
    }

    /**
     * Constructor with custom type
     *
     * @param type of gui
     */
    public GuiParameters(GuiType type) {
        this.components = new HashMap<>();
        this.title = "Unnamed";
        this.slots = 27;
        this.guiType = type;
        this.inventoryType = this.getTypeFromGuiType(type);
    }

    /**
     * get the type of gui
     *
     * @param type used
     * @return
     */
    private InventoryType getTypeFromGuiType(GuiType type) {
        return type == GuiType.DEFAULT_INVENTORY ? InventoryType.CHEST : InventoryType.ANVIL;
    }

    /**
     * Getter for the title
     *
     * @return title as string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title of the gui
     *
     * @param title as string
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the slot count
     *
     * @return slot count as int
     */
    public int getSlots() {
        return slots;
    }

    /**
     * Setter for slot count
     *
     * @param slots as int (multiple of 3)
     */
    public void setSlots(int slots) {
        this.slots = slots;
    }

    /**
     * Get Components of gui
     *
     * @return components as hashmap(int, component)
     */
    public HashMap<Integer, Component> getComponents() {
        return components;
    }

    /**
     * Get type of inventory
     *
     * @return InventoryType
     */
    public InventoryType getInventoryType() {
        return inventoryType;
    }

    /**
     * get gui type
     *
     * @return GuiType
     */
    public GuiType getGuiType() {
        return guiType;
    }

    /**
     * Get Value Action
     *
     * @return GuiAction
     */
    public GuiAction getValueAction() {
        return valueAction;
    }

    /**
     * set value action
     *
     * @param valueAction (GuiAction) to set
     */
    public void setValueAction(GuiAction valueAction) {
        this.valueAction = valueAction;
    }

    /**
     * Get default value
     *
     * @return string
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * set default value
     *
     * @param defaultValue as string
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
