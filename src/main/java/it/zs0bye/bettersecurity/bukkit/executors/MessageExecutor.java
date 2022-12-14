package it.zs0bye.bettersecurity.bukkit.executors;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.hooks.HooksManager;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageExecutor extends Executors {

    private final HooksManager hooks;
    private final String execute;
    private final Player player;

    public MessageExecutor(final BetterSecurityBukkit plugin, final String execute, final Player player) {
        this.hooks = plugin.getHooks();
        this.execute = execute;
        this.player = player;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[MESSAGE] ";
    }

    protected void apply() {

        String msg = this.hooks.getPlaceholders(this.player, this.execute
                .replace(this.getType(), "")
                .replace("%prefix%", Config.SETTINGS_PREFIX.getString()));
        if(msg.contains("%center%")) msg = CStringUtils.center(msg.replace("%center%", ""));

        if(msg.startsWith("@")) {
            final String finalMsg = msg;
            Bukkit.getOnlinePlayers().forEach(players -> this.run(players, finalMsg));
            return;
        }

        this.run(this.player, msg);
    }

    private void run(final Player player, final String msg) {
        player.sendMessage(msg
                .replaceFirst("@", ""));
    }

}