package it.zs0bye.bettersecurity.bukkit.executors;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.hooks.HooksManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerExecutor extends Executors {

    private final HooksManager hooks;
    private final String execute;
    private final Player player;

    public PlayerExecutor(final BetterSecurityBukkit plugin, final String execute, final Player player) {
        this.hooks = plugin.getHooks();
        this.execute = execute;
        this.player = player;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[PLAYER] ";
    }

    protected void apply() {

        final String command = this.hooks.replacePlaceholders(this.player, this.execute
                .replace(this.getType(), ""));

        Bukkit.dispatchCommand(this.player, command);
    }
}