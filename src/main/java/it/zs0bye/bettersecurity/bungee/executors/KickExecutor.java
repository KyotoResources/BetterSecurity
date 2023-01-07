package it.zs0bye.bettersecurity.bungee.executors;

import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class KickExecutor extends Executors {

    private final ProxiedPlayer player;

    private String execute;

    public KickExecutor(final String execute, final ProxiedPlayer player) {
        this.player = player;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[KICK] ";
    }

    protected void apply() {
        final String msg = this.execute
                .replace("%prefix%", Config.SETTINGS_PREFIX.getString())
                .replace("\\n", "\n");
        this.player.disconnect(TextComponent.fromLegacyText(msg));
    }

}