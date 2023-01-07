package it.zs0bye.bettersecurity.bungee.warnings;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.executors.SendExecutors;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.bungee.warnings.enums.TypeWarning;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

public class Warnings {

    private final BetterSecurityBungee plugin;
    private final ProxiedPlayer player;
    private final TypeWarning type;
    private final String[] values;
    private final String permission;

    private final Map<String, String> placeholders = new HashMap<>();

    public Warnings(final BetterSecurityBungee plugin, final ProxiedPlayer player, final TypeWarning type, final String... values) {
        this.plugin = plugin;
        this.player = player;
        this.type = type;
        this.values = values;
        this.permission = "bettersecuritybungee.broadcast.warnings";
        this.placeholders.put("%player%", this.player.getName());
        this.placeholders.put("%server%", this.player.getServer().getInfo().getName());
        this.sendWarningCmds();
        this.sendWarningSpamCmd();
    }

    private void sendWarningCmds() {
        final String command = "/" + this.values[0];
        if(this.type != TypeWarning.COMMANDS) return;
        this.placeholders.put("%command%", command);

        if(Config.WARNINGS_LOG_CONSOLE.getBoolean()) {
            this.plugin.getLogger().info(Config.WARNINGS_FORMATS_CMDS_CONSOLE.getString()
                    .replace("%player%", this.player.getName())
                    .replace("%command%", command));
        }

        this.plugin.getProxy().getPlayers().forEach(players -> {
            if(!players.hasPermission(this.permission)) return;
            SendExecutors.send(this.plugin, Config.WARNINGS_FORMATS_CMDS_FORMAT.getStringList(), players, this.placeholders);
        });
    }

    private void sendWarningSpamCmd() {
        final String command = "/" + this.values[0];
        if(this.type != TypeWarning.PREVENT_COMMAND_SPAM) return;
        this.placeholders.put("%command%", command);

        if(Config.WARNINGS_LOG_CONSOLE.getBoolean()) {
            this.plugin.getLogger().info(Config.WARNINGS_FORMATS_PCS_CONSOLE.getString()
                    .replace("%player%", this.player.getName())
                    .replace("%command%", command));
        }

        this.plugin.getProxy().getPlayers().forEach(players -> {
            if(!players.hasPermission(this.permission)) return;
            SendExecutors.send(this.plugin, Config.WARNINGS_FORMATS_PCS_FORMAT.getStringList(), players, this.placeholders);
        });
    }

}
