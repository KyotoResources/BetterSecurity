package it.zs0bye.bettersecurity.bungee;

import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.bungee.utils.StringUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Warnings {

    private final BetterSecurityBungee plugin;
    private final ProxiedPlayer player;
    private final String command;

    public Warnings(final BetterSecurityBungee plugin, final ProxiedPlayer player, final String command) {
        this.plugin = plugin;
        this.player = player;
        this.command = "/" + command;
    }

    public void warn() {

        final String format = Config.WARNINGS_FORMAT.getString()
                .replace("%server%", this.player.getServer().getInfo().getName())
                .replace("%player%", this.player.getName())
                .replace("%command%", this.command);

        this.plugin.getProxy().getPlayers().forEach(players -> {
            if(!players.hasPermission("bettersecuritybungee.broadcast.warnings")) return;
            StringUtils.send(players, format);
        });

        if(!Config.WARNINGS_CONSOLE_ENABLED.getBoolean()) return;

        final String cFormat = Config.WARNINGS_CONSOLE_FORMAT.getString()
                .replace("%player%", this.player.getName())
                .replace("%command%", this.command);

        this.plugin.getLogger().info(cFormat);
    }

}
