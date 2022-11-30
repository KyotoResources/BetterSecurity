package it.zs0bye.bettersecurity.bungee;

import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TabComplete {

    private final ProxiedPlayer player;

    private final String bypass_method;
    private final List<String> bypass_players;

    public TabComplete(final ProxiedPlayer player) {
        this.player = player;
        this.bypass_method = Config.BLOCK_TAB_COMPLETE_BYPASS_METHOD.getString();
        this.bypass_players = Config.BLOCK_TAB_COMPLETE_BYPASS_PLAYERS.getStringList();
    }

    public boolean bypass() {
        if(this.bypass_method.equals("PERMISSION")) return player.hasPermission("bettersecuritybungee.bypass.antitab");
        if(this.bypass_method.equals("PLAYERS")) return this.bypass_players.contains(player.getName())
                || this.bypass_players.contains(player.getUniqueId().toString());
        return false;
    }

    private static List<String> initCompletions(final ProxiedPlayer player, final String group, final boolean legacy) {
        final List<String> completions = new ArrayList<>();
        final String first = legacy ? "/" : "";

        if(!Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_ENABLED.getBoolean()) return completions;
        final String path = Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_GROUPS.getPath() + "." + group;

        final String permission = Config.CUSTOM.getString(path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_PERMISSION.getPath());
        final List<String> commands = Config.CUSTOM.getStringList(path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_COMMANDS.getPath());
        if(!player.hasPermission(permission)) return completions;
        commands.forEach(command -> completions.add(first + command));
        return completions;
    }

    private static List<String> getServerMode(final ProxiedPlayer player) {
        final List<String> completions = new ArrayList<>();
        if(!Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_SERVER_MODE_ENABLED.getBoolean()) return completions;
        final String server = player.getServer().getInfo().getName();
        final String path = Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_SERVER_MODE_SERVERS.getPath() + "." + server;
        return Config.CUSTOM.getStringList(path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_SERVER_MODE_GROUPS.getPath());
    }

    public static List<String> getCompletions(final ProxiedPlayer player, final boolean legacy) {
        final List<String> completions = new ArrayList<>();
        final String first = legacy ? "/" : "";
        if(!Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_ENABLED.getBoolean()) return completions;
        Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GLOBAL.getStringList().forEach(command -> completions.add(first + command));
        Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_GLOBAL_GROUPS.getStringList().forEach(group ->
                completions.addAll(TabComplete.initCompletions(player, group, legacy)));
        TabComplete.getServerMode(player).forEach(group ->
                completions.addAll(TabComplete.initCompletions(player, group, legacy)));

        return completions;
    }

}
