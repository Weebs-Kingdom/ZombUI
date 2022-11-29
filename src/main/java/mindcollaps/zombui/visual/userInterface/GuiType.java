package mindcollaps.zombui.visual.userInterface;

import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

public enum GuiType {
    DEFAULT_INVENTORY(new InventoryType[]{InventoryType.BARREL, InventoryType.CREATIVE, InventoryType.ENDER_CHEST, InventoryType.CHEST, InventoryType.PLAYER, InventoryType.SHULKER_BOX}),
    TYPING_DOUBLE_INVENTORY(new InventoryType[]{InventoryType.ANVIL}),
    TYPING_STRING_INVENTORY(new InventoryType[]{InventoryType.ANVIL});

    private final InventoryType[] nativeType;

    GuiType(InventoryType[] paramClickType) {
        this.nativeType = paramClickType;
    }

    public static GuiType fromInventoryType(InventoryType inventoryType) {
        return Arrays.stream(values())
                .filter(guiType -> (Arrays.stream(guiType.getNativeType()).toList().contains(inventoryType)))
                .findFirst().orElse(DEFAULT_INVENTORY);
    }

    public InventoryType[] getNativeType() {
        return this.nativeType;
    }
}
