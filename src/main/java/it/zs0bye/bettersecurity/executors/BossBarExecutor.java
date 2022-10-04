package it.zs0bye.bettersecurity.executors;

import it.zs0bye.bettersecurity.BetterSecurity;
import it.zs0bye.bettersecurity.hooks.HooksManager;
import it.zs0bye.bettersecurity.reflections.BossBarField;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BossBarExecutor extends Executors {

    private final BetterSecurity plugin;
    private final String execute;
    private final Player player;

    private final HooksManager hooks;

    public BossBarExecutor(final BetterSecurity plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        this.hooks = this.plugin.getHooks();
        if(!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[BOSSBAR] ";
    }

    protected void apply() {

        final String title = this.hooks.getPlaceholders(this.player, execute
                .replace(this.getType(), "")
                .split(";")[0]);

        final String color = execute
                .replace(this.getType(), "")
                .split(";")[1]
                .toUpperCase();

        final String style = execute
                .replace(this.getType(), "")
                .split(";")[2]
                .toUpperCase();

        final double progress = Double.parseDouble(execute
                .replace(this.getType(), "")
                .split(";")[3]);

        final int times = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[4]);

        if(title.startsWith("@")) {
            Bukkit.getOnlinePlayers().forEach(players -> this.run(players, title, color, style, progress, times));
            return;
        }

        this.run(this.player, title, color, style, progress, times);
    }

    private void run(final Player player, final String title, final String color, final String style, final double progress, final int times) {
        new BossBarField(this.plugin, player, title
                .replaceFirst("@", ""), color, style, progress, times);
    }
}
