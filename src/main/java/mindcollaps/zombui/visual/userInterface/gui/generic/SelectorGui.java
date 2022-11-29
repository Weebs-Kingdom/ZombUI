package mindcollaps.zombui.visual.userInterface.gui.generic;

import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.ObjectSelector;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.SelectorAction;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.userInterface.ActionType;
import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.GuiType;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.MCGui;
import mindcollaps.zombui.visual.userInterface.parts.Button;

import java.util.List;

public class SelectorGui<E> {

    /**
     * For reference to own instance and calling of methods etc
     */
    private final ZombUi plugin;

    private final ObjectSelector<E> selector;
    private final SelectorAction<E> action;


    //GUI
    private final String title;
    private final Player player;
    private final boolean createButton;
    private MCGui mcGui;

    /**
     * Constructor for Selector Guis
     *
     * @param plugin       The plugin instance
     * @param selector     The name selector
     * @param action       The selector action
     * @param player       The player the GUI is shown to
     * @param title        The title of the GUI
     * @param createButton Weather you want to have a create button in your selector GUI
     */
    public SelectorGui(ZombUi plugin, ObjectSelector<E> selector, SelectorAction<E> action, Player player, String title, boolean createButton) {
        this.plugin = plugin;
        this.selector = selector;
        this.action = action;
        this.player = player;
        this.title = title;
        this.createButton = createButton;
    }

    /**
     * Methode for generating a new Page
     *
     * @param i Seiten Nummer
     */
    public void generatePage(int i) {
        GuiParameters page = new GuiParameters(GuiType.DEFAULT_INVENTORY);
        page.setTitle(title);
        page.setSlots(27);

        @NotNull List<E> data = selector.getData();

        int slotId = 10;
        for (int j = (i * 7); j < (i * 7) + 7; j++) {
            if (data.size() > j) {
                E o = data.get(j);
                page.getComponents().put(slotId,
                        new Button(
                                selector.getItem(o),
                                new GuiAction() {
                                    @Override
                                    public void onClick(ActionType actionType, MCGui gui) {
                                        action.selected(o);
                                    }
                                }));
                slotId++;
            } else {
                break;
            }
        }

        if (data.size() > ((i) * 7) + 7)
            page.getComponents().put(19, new Button(
                    new CustomItem(Material.PAPER, "Next"),
                    new GuiAction() {
                        @Override
                        public void onClick(ActionType actionType, MCGui gui) {
                            generatePage(i + 1);
                        }
                    }
            ));

        if (i != 0)
            page.getComponents().put(20, new Button(
                    new CustomItem(Material.PAPER, "Previous"),
                    new GuiAction() {
                        @Override
                        public void onClick(ActionType actionType, MCGui gui) {
                            generatePage(i - 1);
                        }
                    }
            ));

        if (createButton)
            page.getComponents().put(26, new Button(
                    new CustomItem(Material.GREEN_CONCRETE, "Create"),
                    new GuiAction() {
                        @Override
                        public void onClick(ActionType actionType, MCGui gui) {
                            action.create(mcGui);
                        }
                    }
            ));

        page.getComponents().put(25, new Button(
                new CustomItem(Material.BARRIER, "Back"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        action.goBack(mcGui);
                    }
                }
        ));

        openGui(page);
    }

    /**
     * Methode for closing the gui
     */
    public void close() {
        if (this.mcGui != null)
            this.mcGui.closeInventory();
    }

    /**
     * Methode for opening a gui for a player
     *
     * @param parameters gui parameters
     */
    private void openGui(GuiParameters parameters) {
        this.mcGui = new MCGui(plugin, player, parameters);
        this.mcGui.open();
    }
}

