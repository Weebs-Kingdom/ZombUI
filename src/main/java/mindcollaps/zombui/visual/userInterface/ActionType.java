package mindcollaps.zombui.visual.userInterface;

import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;

public enum ActionType {
    RIGHT(ClickType.RIGHT),
    LEFT(ClickType.LEFT),
    MIDDLE(ClickType.MIDDLE),
    Q(ClickType.DROP),
    SHIFT_RIGHT(ClickType.SHIFT_RIGHT),
    SHIFT_LEFT(ClickType.SHIFT_LEFT);

    private final ClickType nativeType;

    ActionType(ClickType paramClickType) {
        this.nativeType = paramClickType;
    }

    public static ActionType fromClickType(ClickType paramClickType) {
        return Arrays.stream(values()).filter(paramActionType -> (paramActionType.nativeType == paramClickType)).findFirst().orElse(null);
    }
}
