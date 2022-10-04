package it.zs0bye.bettersecurity;

import it.zs0bye.bettersecurity.files.enums.Config;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Warnings {

    private final BetterSecurity plugin;
    private final Player player;
    private final String command;

    public Warnings(final BetterSecurity plugin, final Player player, final String command) {
        this.plugin = plugin;
        this.player = player;
        this.command = "/" + command;
    }

    @SneakyThrows
    public void warn() {

        this.sendConsole();

        final String format = Config.WARNINGS_FORMAT.getString()
                .replace("%player%", this.player.getName())
                .replace("%command%", this.command);

        Bukkit.getOnlinePlayers().forEach(players -> {
            if(!players.hasPermission("bettersecurity.broadcast.warnings")) return;
            players.sendMessage(format);
        });

    }

    private void sendConsole() {
        if(!Config.WARNINGS_CONSOLE_ENABLED.getBoolean()) return;

        final String format = Config.WARNINGS_CONSOLE_FORMAT.getString()
                .replace("%player%", this.player.getName())
                .replace("%command%", this.command);

        this.plugin.getLogger().info(format);
    }

}
