package it.zs0bye.bettersecurity.bukkit.files.enums;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.IFiles;
import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public enum Config implements IFiles {
    CUSTOM(""),
    SETTINGS_LOCALE("Settings.locale"),
    SETTINGS_PREFIX("Settings.prefix"),
    SETTINGS_CHECK_UPDATE_ENABLED("Settings.check_update.enabled"),
    SETTINGS_CHECK_UPDATE_TYPE("Settings.check_update.type"),
    SETTINGS_HOOKS_PLACEHOLDERAPI("Settings.hooks.PlaceholderAPI"),
    SETTINGS_HOOKS_PROTOCOLLIB("Settings.hooks.ProtocolLib"),
    BLOCK_TAB_COMPLETE_ENABLED("Block_Tab_Complete.enabled"),
    BLOCK_TAB_COMPLETE_BLACKLISTED_SUGGESTIONS("Block_Tab_Complete.blacklisted_suggestions"),
    BLOCK_TAB_COMPLETE_BYPASS_METHOD("Block_Tab_Complete.bypass.method"),
    BLOCK_TAB_COMPLETE_BYPASS_PLAYERS("Block_Tab_Complete.bypass.players"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_ENABLED("Block_Tab_Complete.whitelisted_suggestions.enabled"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_PARTIAL_MATCHES("Block_Tab_Complete.whitelisted_suggestions.partial_matches"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_ENABLED_GROUPS("Block_Tab_Complete.whitelisted_suggestions.enabled_groups"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS("Block_Tab_Complete.whitelisted_suggestions.groups"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_SUGGESTIONS(".suggestions"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_REQUIRED_PERMISSION(".required_permission"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_REQUIRED_PLAYERS(".required_players"),
    WARNINGS_PROXY("Warnings.proxy"),
    WARNINGS_FORMATS_CMDS_FORMAT("Warnings.formats.commands.format"),
    WARNINGS_FORMATS_CMDS_CONSOLE("Warnings.formats.commands.console"),
    WARNINGS_FORMATS_PBP_FORMAT("Warnings.formats.port_bypass_prevention.format"),
    WARNINGS_FORMATS_PBP_CONSOLE("Warnings.formats.port_bypass_prevention.console"),
    WARNINGS_LOG_CONSOLE("Warnings.log_console"),
    UNKNOWN_COMMAND_ENABLED("Unknown_Command.enabled"),
    UNKNOWN_COMMAND_PRIORITY("Unknown_Command.priority"),
    UNKNOWN_COMMAND_EXEMPT_COMMANDS("Unknown_Command.exempt_commands"),
    UNKNOWN_COMMAND_USE_SPIGOT_MESSAGE("Unknown_Command.use_spigot_message"),
    UNKNOWN_COMMAND_EXECUTORS("Unknown_Command.executors"),
    BLOCK_SYNTAX_ENABLED("Block_Syntax.enabled"),
    BLOCK_SYNTAX_PRIORITY("Block_Syntax.priority"),
    BLOCK_SYNTAX_WARNING("Block_Syntax.warning"),
    BLOCK_SYNTAX_EXECUTORS("Block_Syntax.executors"),
    BLOCKS_COMMANDS_ENABLED("Blocks_Commands.enabled"),
    BLOCKS_COMMANDS_PRIORITY("Blocks_Commands.priority"),
    BLOCKS_COMMANDS_WARNING("Blocks_Commands.warning"),
    BLOCKS_COMMANDS_METHOD("Blocks_Commands.method"),
    BLOCKS_COMMANDS_EXECUTORS("Blocks_Commands.executors"),
    BLOCKS_COMMANDS("Blocks_Commands.commands"),
    COMMANDS_ONLY_CONSOLE_ENABLED("Commands_Only_Console.enabled"),
    COMMANDS_ONLY_CONSOLE_PRIORITY("Commands_Only_Console.priority"),
    COMMANDS_ONLY_CONSOLE_WARNING("Commands_Only_Console.warning"),
    COMMANDS_ONLY_CONSOLE_EXECUTORS("Commands_Only_Console.executors"),
    COMMANDS_ONLY_CONSOLE("Commands_Only_Console.commands"),
    COMMANDS_ONLY_PLAYERS_ENABLED("Commands_Only_Players.enabled"),
    COMMANDS_ONLY_PLAYERS_PRIORITY("Commands_Only_Players.priority"),
    COMMANDS_ONLY_PLAYERS_WARNING("Commands_Only_Players.warning"),
    COMMANDS_ONLY_PLAYERS_EXECUTORS("Commands_Only_Players.executors"),
    COMMANDS_ONLY_PLAYERS_BYPASS("Commands_Only_Players.bypass_players"),
    COMMANDS_ONLY_PLAYERS("Commands_Only_Players.commands"),
    BLOCK_CUSTOM_COMMANDS_ENABLED("Block_Custom_Commands.enabled"),
    BLOCK_CUSTOM_COMMANDS("Block_Custom_Commands.commands"),
    BLOCK_CUSTOM_COMMANDS_COMMAND(".command"),
    BLOCK_CUSTOM_COMMANDS_COMMANDS(".commands"),
    BLOCK_CUSTOM_COMMANDS_PRIORITY(".priority"),
    BLOCK_CUSTOM_COMMANDS_EXECUTORS(".executors"),
    BLOCK_CUSTOM_COMMANDS_WARNING(".warning"),
    BLOCK_CUSTOM_COMMANDS_PERMISSION_REQUIRED(".permission_required"),
    BLOCK_CUSTOM_COMMANDS_REQUIRED_PLAYERS(".required_players"),
    REPLACE_CUSTOM_MESSAGES_ENABLED("Replace_Custom_Messages.enabled"),
    REPLACE_CUSTOM_MESSAGES("Replace_Custom_Messages.messages"),
    REPLACE_CUSTOM_MESSAGES_TARGET(".target"),
    REPLACE_CUSTOM_MESSAGES_REPLACE(".replace"),
    PERMISSION_PREVENTION_ENABLED("Permission_Prevention.enabled"),
    PERMISSION_PREVENTION_CHECKS_CHAT("Permission_Prevention.checks.chat"),
    PERMISSION_PREVENTION_CHECKS_CMDS("Permission_Prevention.checks.commands"),
    PERMISSION_PREVENTION_CHECKS_INVENTORY("Permission_Prevention.checks.inventory"),
    PERMISSION_PREVENTION_CHECKS_INTERACT("Permission_Prevention.checks.interact"),
    PERMISSION_PREVENTION_CHECKS_MOVEMENT("Permission_Prevention.checks.movement"),
    PERMISSION_PREVENTION_CHECKS_JOIN("Permission_Prevention.checks.join"),
    PERMISSION_PREVENTION_CHECKS_LEFT("Permission_Prevention.checks.left"),
    PERMISSION_PREVENTION_REMOVE_PERMISSION("Permission_Prevention.remove_permission"),
    PERMISSION_PREVENTION_REMOVE_GROUP("Permission_Prevention.remove_group"),
    PERMISSION_PREVENTION_PUNISHMENT("Permission_Prevention.punishment"),
    PERMISSION_PREVENTION_GLOBAL_BYPASS("Permission_Prevention.global_bypass"),
    PERMISSION_PREVENTION_GLOBAL_BYPASS_ENABLED("Permission_Prevention.global_bypass.enabled"),
    PERMISSION_PREVENTION_OPERATORS("Permission_Prevention.operators"),
    PERMISSION_PREVENTION_OPERATORS_ENABLED("Permission_Prevention.operators.enabled"),
    PERMISSION_PREVENTION_GROUPS_ENABLED("Permission_Prevention.groups.enabled"),
    PERMISSION_PREVENTION_GROUPS_LIST("Permission_Prevention.groups.list"),
    PERMISSION_PREVENTION_PERMISSIONS_ENABLED("Permission_Prevention.permissions.enabled"),
    PERMISSION_PREVENTION_PERMISSIONS_LIST("Permission_Prevention.permissions.list"),
    PERMISSION_PREVENTION_BYPASS_PLAYERS(".bypass_players"),
    PORT_BYPASS_PREVENTION_ENABLED("Port_Bypass_Prevention.enabled"),
    PORT_BYPASS_PREVENTION_WARNING("Port_Bypass_Prevention.warning"),
    PORT_BYPASS_PREVENTION_WHITELISTED_IPS("Port_Bypass_Prevention.whitelisted_ips"),
    PORT_BYPASS_PREVENTION_SAFETY_KICKS("Port_Bypass_Prevention.safety_kicks"),
    PORT_BYPASS_PREVENTION_KICK_MESSAGE("Port_Bypass_Prevention.kick_message"),
    PORT_BYPASS_PREVENTION_ALREADY_CONNECTED_ENABLED("Port_Bypass_Prevention.already_connected.enabled"),
    PORT_BYPASS_PREVENTION_ALREADY_CONNECTED_MSG("Port_Bypass_Prevention.already_connected.message");

    private final String path;
    private final BetterSecurityBukkit plugin;
    private FileConfiguration config;

    Config(final String path) {
        this.path = path;
        this.plugin = BetterSecurityBukkit.getInstance();
        this.reloadConfig();
    }

    @Override
    public String variables(final String... var) {
        final StringBuilder builder = new StringBuilder();
        for(String texts : var) builder.append(texts);
        builder.append(path);
        return builder.toString();
    }

    @Override
    public void reloadConfig() {
        this.config = this.plugin.getConfigFile().getConfig();
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getString(final String... var) {
        return StringUtils.colorize(this.config.getString(this.variables(var)));
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        for (String setList : this.config.getStringList(this.variables(var))) {
            list.add(StringUtils.colorize(setList));
        }
        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.config.getBoolean(this.variables(var));
    }

    @Override
    public boolean contains(final String... var) {
        return this.config.contains(this.variables(var));
    }

    @Override
    public int getInt(final String... var) {
        return this.config.getInt(this.variables(var));
    }

    @Override
    public String getCustomString(String replace, final String... var) {
        if (replace.startsWith("%prefix%")) {
            replace = replace.replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
                replace = replace.replace("%center%", "");
                return CStringUtils.center(replace);
            }
            return replace;
        }

        if(replace.startsWith("%center%")) {
            replace = replace.replace("%center%", "");
            return CStringUtils.center(replace);
        }
        return replace;
    }

    @Override
    public String getCustomString(final String... var) {
        return this.getCustomString(this.getString(var), var);
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        this.send(sender, new HashMap<>(), var);
    }

    @Override
    public void send(final CommandSender sender, final Map<String, String> placeholders, final String... var) {
        String message = this.getCustomString(this.getString(var), var);
        if (message.isEmpty()) return;
        for (String key : placeholders.keySet()) message = message.replace(key, placeholders.get(key));
        sender.sendMessage(message);
    }

    @Override
    public void sendList(final CommandSender sender, final String... var) {
        this.sendList(sender, new HashMap<>(), var);
    }

    @Override
    public void sendList(final CommandSender sender, final Map<String, String> placeholders, final String... var) {
        if (this.getStringList(var).isEmpty()) return;
        this.getStringList(var).forEach(msg -> {
            for (String key : placeholders.keySet()) msg = msg.replace(key, placeholders.get(key));
            sender.sendMessage(this.getCustomString(msg, var));
        });
    }

    @SuppressWarnings("all")
    @Override
    public Set<String> getConfigurationSection(final String... var) {
        return this.config.getConfigurationSection(this.variables(var)).getKeys( false);
    }

}
