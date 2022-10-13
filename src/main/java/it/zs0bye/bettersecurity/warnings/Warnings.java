package it.zs0bye.bettersecurity.warnings;

import it.zs0bye.bettersecurity.BetterSecurity;
import it.zs0bye.bettersecurity.executors.SendExecutors;
import it.zs0bye.bettersecurity.files.enums.Config;
import it.zs0bye.bettersecurity.warnings.enums.TypeWarning;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Warnings {

    private final BetterSecurity plugin;
    private final Player player;
    private final TypeWarning type;
    private final String[] values;
    private final String permission;

    private final Map<String, String> placeholders = new HashMap<>();

    public Warnings(final BetterSecurity plugin, final Player player, final TypeWarning type, final String... values) {
        this.plugin = plugin;
        this.player = player;
        this.type = type;
        this.values = values;
        this.permission = "bettersecurity.broadcast.warnings";
        this.placeholders.put("%player%", this.player.getName());
        this.sendWarningCmds();
        this.sendWarningPBP();
    }

    private void sendWarningCmds() {
        final String command = "/" + this.values[0];
        if(this.type != TypeWarning.COMMANDS) return;
        this.placeholders.put("%command%", command);
        Bukkit.getOnlinePlayers().forEach(players -> {
            if(!players.hasPermission(this.permission)) return;
            SendExecutors.send(this.plugin, Config.WARNINGS_FORMATS_CMDS_FORMAT.getStringList(), players, this.placeholders);
        });

        if(!Config.WARNINGS_LOG_CONSOLE.getBoolean()) return;
        this.plugin.getLogger().info(Config.WARNINGS_FORMATS_CMDS_CONSOLE.getString()
                .replace("%player%", this.player.getName())
                .replace("%command%", command));
    }

    private void sendWarningPBP() {
        if(this.type != TypeWarning.PORT_BYPASS_PREVENTION) return;
        this.placeholders.put("%port%", this.values[0]);
        this.placeholders.put("%ip%", this.values[1]);
        Bukkit.getOnlinePlayers().forEach(players -> {
            if(!players.hasPermission(this.permission)) return;
            SendExecutors.send(this.plugin, Config.WARNINGS_FORMATS_PBP_FORMAT.getStringList(), players, this.placeholders);
        });

        if(!Config.WARNINGS_LOG_CONSOLE.getBoolean()) return;
        this.plugin.getLogger().info(Config.WARNINGS_FORMATS_PBP_CONSOLE.getString()
                .replace("%player%", this.player.getName())
                .replace("%port%", this.values[0])
                .replace("%ip%", this.values[1]));
    }

}
