package it.zs0bye.bettersecurity.bungee.executors;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ActionExecutor extends Executors {

    private final BetterSecurityBungee plugin;
    private final ProxiedPlayer player;

    private String execute;

    public ActionExecutor(final BetterSecurityBungee plugin, final String execute, final ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[ACTION] ";
    }

    protected void apply() {

        final String msg = this.execute.replace("%prefix%", Config.SETTINGS_PREFIX.getString());

        if(msg.startsWith("@")) {
            this.plugin.getAdventure().players().sendActionBar(Component.text(msg
                    .replaceFirst("@", "")));
            return;
        }

        this.plugin.getAdventure().player(player).sendActionBar(Component.text(msg));
    }

}