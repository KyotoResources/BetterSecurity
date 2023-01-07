package it.zs0bye.bettersecurity.bungee.executors;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerExecutor extends Executors {

    private final BetterSecurityBungee plugin;
    private final ProxiedPlayer player;

    private String execute;

    public PlayerExecutor(final BetterSecurityBungee plugin, final String execute, final ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[PLAYER] ";
    }

    protected void apply() {

        final String command = this.execute
                .replace(this.getType(), "");

        this.plugin.getProxy().getPluginManager().dispatchCommand(this.player, command);
    }
}