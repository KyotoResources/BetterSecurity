package it.zs0bye.bettersecurity.bukkit.executors;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.hooks.HooksManager;
import it.zs0bye.bettersecurity.bukkit.reflections.TitleField;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TitleExecutor extends Executors {

    private final HooksManager hooks;
    private final String execute;
    private final Player player;

    public TitleExecutor(final BetterSecurityBukkit plugin, final String execute, final Player player) {
        this.hooks = plugin.getHooks();
        this.execute = execute;
        this.player = player;
        if(!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[TITLE] ";
    }

    protected void apply() {

        String title = this.hooks.replacePlaceholders(this.player, this.execute
                .replace(this.getType(), "")
                .split(";")[0]);

        String subtitle = this.hooks.replacePlaceholders(this.player, this.execute
                .replace(this.getType(), "")
                .split(";")[1]);

        final int fadein = Integer.parseInt(this.execute
                .replace(this.getType(), "")
                .split(";")[2]);
        final int stay = Integer.parseInt(this.execute
                .replace(this.getType(), "")
                .split(";")[3]);
        final int fadeout = Integer.parseInt(this.execute
                .replace(this.getType(), "")
                .split(";")[4]);

        if(subtitle.equalsIgnoreCase("none")) subtitle = "";

        if(title.replaceFirst("@", "")
                .equalsIgnoreCase("none")) title = "";

        if(title.startsWith("@")) {
            final String finalTitle = title;
            final String finalSubtitle = subtitle;
            Bukkit.getOnlinePlayers().forEach(players -> this.run(players, finalTitle, finalSubtitle, fadein, stay, fadeout));
            return;
        }

        this.run(this.player, title, subtitle, fadein, stay, fadeout);
    }

    private void run(Player player, String title, String subtitle, int fadein, int stay, int fadeout) {
        new TitleField(player, title
                .replaceFirst("@", ""), subtitle, fadein, stay, fadeout);
    }
}
