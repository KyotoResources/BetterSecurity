package it.zs0bye.bettersecurity.bukkit.executors;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.hooks.HooksManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleExecutor extends Executors {

    private final BetterSecurityBukkit plugin;
    private final String execute;
    private final Player player;

    private final HooksManager hooks;

    public ConsoleExecutor(final BetterSecurityBukkit plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        this.hooks = this.plugin.getHooks();
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[CONSOLE] ";
    }

    protected void apply() {

        final String command = this.hooks.getPlaceholders(this.player, this.execute
                .replace(this.getType(), "")
                .replace("%player%", this.player.getName()));

        Bukkit.getScheduler().runTaskLater(this.plugin, () ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command), 2L);

    }
}