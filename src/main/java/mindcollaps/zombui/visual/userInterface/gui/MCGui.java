package mindcollaps.zombui.visual.userInterface.gui;

import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.userInterface.ActionType;
import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.GuiType;
import mindcollaps.zombui.visual.userInterface.parts.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;
import mindcollaps.zombui.ZombUi;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MCGui implements Listener {

    /**
     * Used Console Prefix
     */
    private static final String consolePrefix = "<MCGui> ";
    private final static HashMap<UUID, MCGui> activeGuis = new HashMap<>();
    protected final Player player;

    /**
     * For reference to own instance and calling of methods etc
     */
    private final ZombUi plugin;
    protected GuiParameters guiParameters;
    protected HashMap<Integer, List<GuiAction>> actions;
    private boolean closed;
    private Inventory inventory;
    private String value;

    public MCGui(ZombUi plugin, Player player, GuiParameters guiParameters) {
        this.plugin = plugin;
        this.player = player;
        this.guiParameters = guiParameters;
        this.closed = false;

        actions = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private static void regGui(UUID uuid, MCGui gui) {
        MCGui active = activeGuis.get(uuid);
        if (active != null) {
            if (active != gui) {
                active.closeInventory();
                activeGuis.remove(uuid);
            }
        }

        activeGuis.put(uuid, gui);
    }

    private static boolean checkActive(UUID uuid, Inventory inv) {
        MCGui active = activeGuis.get(uuid);
        return active.inventory == inv;
    }

    /**
     * Opens the GUI to the player
     */
    public void open() {
        if (closed)
            return;
        regGui(player.getUniqueId(), this);

        buildGuiFromParameters();
        if (this.inventory != null) {
            if (this.guiParameters.getGuiType() == GuiType.DEFAULT_INVENTORY)
                this.player.openInventory(this.inventory);
        }
    }

    /**
     * Closes the inventory
     */
    public void closeInventory() {
        if (closed)
            return;
        if (this.inventory != null)
            this.inventory.close();
    }

    /**
     * Build the GUI (Inventory) from the stored parameters
     */
    private void buildGuiFromParameters() {
        if (this.guiParameters != null) {
            if (this.guiParameters.getGuiType() == GuiType.DEFAULT_INVENTORY) {
                this.inventory = Bukkit.createInventory(player, this.guiParameters.getSlots(), net.kyori.adventure.text.Component.text(guiParameters.getTitle()));
                for (int i = 0; i < guiParameters.getSlots(); i++) {
                    if (this.guiParameters.getComponents().containsKey(i)) {
                        Component component = this.guiParameters.getComponents().get(i);
                        if (component.getItem() != null)
                            this.inventory.setItem(i, component.getItem().get());

                        if (component.getActions() != null)
                            this.actions.put(i, component.getActions());
                    }
                }
            } else {
                InventoryView v = player.openAnvil(player.getLocation(), true);
                AnvilInventory anvInv = (AnvilInventory) v.getTopInventory();

                CustomItem accept = new CustomItem(Material.GREEN_CONCRETE);

                if (guiParameters.getDefaultValue() != null)
                    accept.name(guiParameters.getDefaultValue());
                else
                    accept.name(guiParameters.getGuiType() == GuiType.TYPING_DOUBLE_INVENTORY ? "0.0" : "___");
                accept.lore("Accept");

                anvInv.setFirstItem(accept.get());
                anvInv.setMaximumRepairCost(0);
                this.inventory = anvInv;
            }
        }
    }


    private void closeGui() {
        if (closed)
            return;
        onClose();

        this.closed = true;

        closeInventory();

        if (this.inventory != null) {
            this.inventory.clear();
            this.inventory = null;
        }
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (closed)
            return;
        if (!event.getWhoClicked().equals(this.player))
            return;
        if (!checkActive(event.getWhoClicked().getUniqueId(), event.getInventory()))
            return;

        if (!event.getInventory().equals(this.inventory))
            return;

        if (event.getInventory() instanceof PlayerInventory)
            return;

        Component component = guiParameters.getComponents().get(event.getSlot());

        if (component == null)
            event.setCancelled(true);
        else if (!component.isComponentMovable())
            event.setCancelled(true);

        if (this.guiParameters.getGuiType() == GuiType.TYPING_STRING_INVENTORY || this.guiParameters.getGuiType() == GuiType.TYPING_DOUBLE_INVENTORY) {
            if (event.getInventory() instanceof AnvilInventory anvInv) {
                if (event.getCurrentItem() != null)
                    if (this.guiParameters.getValueAction() == null) {
                        //PluginLogger.logError(new PluginStackTrace(null, "The GUI Action is set incorrectly!", consolePrefix));
                    } else {
                        this.value = anvInv.getRenameText();
                        if (this.guiParameters.getValueAction() == null) {
                            //PluginLogger.logError(new PluginStackTrace(null, "The GUI Action is set incorrectly!", consolePrefix));
                        } else {
                            this.guiParameters.getValueAction().valueConfirmed(value, this);
                        }
                    }

                anvInv.close();
            }
            event.getInventory().close();
        } else if (this.guiParameters.getGuiType() == GuiType.DEFAULT_INVENTORY &&
                this.actions.containsKey(event.getSlot())) {
            if (this.actions == null)
                return;
            ActionType actionType = ActionType.fromClickType(event.getClick());
            (this.actions.get(event.getSlot())).forEach(paramAction -> paramAction.onClick(actionType, this));
        }
    }

    @EventHandler
    public void drag(InventoryDragEvent event) {
        if (closed)
            return;
        if (!event.getWhoClicked().equals(this.player))
            return;
        if (!checkActive(event.getWhoClicked().getUniqueId(), event.getInventory()))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        if (closed)
            return;
        if (event.getPlayer().equals(this.player))
            closeGui();
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (closed)
            return;
        if (!checkActive(event.getPlayer().getUniqueId(), event.getInventory()))
            return;
        if (event.getPlayer().equals(this.player)) {
            closeGui();
        }
    }

    /**
     * @return Returns the value of a get value inventory
     */
    public String getValue() {
        return value;
    }

    public ZombUi getPlugin() {
        return plugin;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClose() {
    }
}
