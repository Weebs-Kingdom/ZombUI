package mindcollaps.zombui.commands;

import mindcollaps.zombui.ZombUi;
import org.bukkit.command.CommandExecutor;
import org.jetbrains.annotations.NotNull;

public abstract class ZombieCommand implements CommandExecutor {

    private ZombUi zombUI;

    private String invoke;

    public ZombieCommand(@NotNull String invoke, ZombUi zombUI) {
        this.invoke = invoke;
        this.zombUI = zombUI;
    }

    public ZombUi getZombUI() {
        return zombUI;
    }

    public String getInvoke() {
        return invoke;
    }
}
