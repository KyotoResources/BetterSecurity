package it.zs0bye.bettersecurity.bukkit;

import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TabComplete {

    private final Player player;

    private final String bypass_method;
    private final List<String> bypass_players;
    private final List<String> whitelisted_cmds_global;
    private final boolean required_enabled;

    private Permission permission;
    private String required_permission;
    private List<String> required_commands;

    public TabComplete(final Player player) {
        this.player = player;
        this.bypass_method = Config.BLOCK_TAB_COMPLETE_BYPASS_METHOD.getString();
        this.bypass_players = Config.BLOCK_TAB_COMPLETE_BYPASS_PLAYERS.getStringList();
        this.whitelisted_cmds_global = Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GLOBAL.getStringList();
        this.required_enabled = Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_ENABLED.getBoolean();
    }

    public TabComplete(final Player player, final String group) {
        this.player = player;
        this.bypass_method = Config.BLOCK_TAB_COMPLETE_BYPASS_METHOD.getString();
        this.bypass_players = Config.BLOCK_TAB_COMPLETE_BYPASS_PLAYERS.getStringList();
        this.whitelisted_cmds_global = Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GLOBAL.getStringList();
        this.required_enabled = Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_ENABLED.getBoolean();

        final String path = Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_PERMISSIONS.getPath() + "." + group;
        this.required_permission = Config.CUSTOM.getString(path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_PERMISSION.getPath());
        this.required_commands = Config.CUSTOM.getStringList(path + Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_COMMANDS.getPath());

        this.permission = new Permission(this.required_permission, PermissionDefault.OP);
        this.permission.addParent("bettersecurity.tab.*", true);
    }

    public boolean bypass() {
        if(this.bypass_method.equals("PERMISSION")) return player.hasPermission("bettersecurity.bypass.antitab");
        if(this.bypass_method.equals("PLAYERS")) return this.bypass_players.contains(player.getName())
                || this.bypass_players.contains(player.getUniqueId().toString());
        return false;
    }

    private List<String> completions(final boolean legacy) {
        final List<String> completions = new ArrayList<>();
        final String first = legacy ? "/" : "";
        if(!this.required_enabled) return completions;
        if(!this.player.hasPermission(this.permission)) return completions;
        this.required_commands.forEach(command -> completions.add(first + command));
        return completions;
    }

    public static List<String> getCompletions(final Player player, final boolean legacy) {
        final List<String> completions = new ArrayList<>();
        final String first = legacy ? "/" : "";
        if(!Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_ENABLED.getBoolean()) return completions;
        Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GLOBAL.getStringList().forEach(command -> completions.add(first + command));
        Config.BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_PERMISSIONS.getConfigurationSection().forEach(group ->
                completions.addAll(new TabComplete(player, group).completions(legacy)));
        return completions;
    }

}
