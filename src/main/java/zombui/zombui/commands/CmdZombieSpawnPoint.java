package zombui.zombui.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CmdZombieSpawnPoint extends Command {


    protected CmdZombieSpawnPoint() {
        super("ZombieSpawnPoint");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return false;
    }

    private void setZombieSpawnPoint() {

    }

    private void removeZombieSpawnPoint() {

    }
}
