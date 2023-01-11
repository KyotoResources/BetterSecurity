package it.zs0bye.bettersecurity.bungee;

import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
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

    private static List<String> initCompletions(final List<String> completions, final boolean legacy) {
        final List<String> newCompletions = new ArrayList<>();
        final String first = legacy ? "/" : "";
        for(final String command : completions) newCompletions.add(first + command);
        return newCompletions;
    }

    private static List<String> groupsCompletions(final ProxiedPlayer player) {

        final List<String> completions = new ArrayList<>();
        final String server = player.getServer().getInfo().getName();

        if(TabComplete.enabledGroups().isEmpty()) return completions;
        for(final String group : TabComplete.enabledGroups()) {
            final String path = Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GROUPS.getPath() + "." + group;
            final String serverPath = path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GROUPS_REQUIRED_SERVER.getPath();
            final String permissionPath = path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GROUPS_REQUIRED_PERMISSION.getPath();
            final String playersPath = path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GROUPS_REQUIRED_PLAYERS.getPath();
            final String ignoreServersPath = path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GROUPS_IGNORE_SERVERS.getPath();

            if(Config.CUSTOM.contains(serverPath) && !Config.CUSTOM.getString(serverPath).equalsIgnoreCase(server)) continue;
            if(Config.CUSTOM.contains(permissionPath) && !player.hasPermission(Config.CUSTOM.getString(permissionPath))) continue;
            if(Config.CUSTOM.contains(playersPath) && !Config.CUSTOM.getStringList(playersPath).contains(player.getName())) continue;
            if(Config.CUSTOM.contains(ignoreServersPath) && Config.CUSTOM.getStringList(ignoreServersPath).contains(server.toLowerCase())) continue;

            completions.addAll(Config.CUSTOM.getStringList(path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GROUPS_SUGGESTIONS.getPath()));
        }

        return completions;
    }

    private static Collection<String> enabledGroups() {
        final Collection<String> groups = new ArrayList<>();
        final List<String> enabled_groups = Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_ENABLED_GROUPS.getStringList();
        if(enabled_groups.contains("*")) {
            groups.addAll(Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GROUPS.getSection());
            return groups;
        }
        groups.addAll(enabled_groups);
        return groups;
    }

    public static List<String> getCompletions(final ProxiedPlayer player, final boolean legacy) {
        final List<String> completions = new ArrayList<>();
        if(!Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_ENABLED.getBoolean()) return completions;
        completions.addAll(TabComplete.initCompletions(TabComplete.groupsCompletions(player), legacy));
        return completions;
    }

}
