package it.zs0bye.bettersecurity.files.enums;

import it.zs0bye.bettersecurity.BetterSecurity;
import it.zs0bye.bettersecurity.files.IFiles;
import it.zs0bye.bettersecurity.utils.StringUtils;
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
    BLOCK_TAB_COMPLETE_COMMANDS("Block_Tab_Complete.commands"),
    BLOCK_TAB_COMPLETE_BYPASS_METHOD("Block_Tab_Complete.bypass.method"),
    BLOCK_TAB_COMPLETE_BYPASS_PLAYERS("Block_Tab_Complete.bypass.players"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_ENABLED("Block_Tab_Complete.whitelisted_commands.enabled"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GLOBAL("Block_Tab_Complete.whitelisted_commands.global"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_ENABLED("Block_Tab_Complete.whitelisted_commands.required.enabled"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_PERMISSIONS("Block_Tab_Complete.whitelisted_commands.required.permissions"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_PERMISSION(".permission"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_COMMANDS(".commands"),
    WARNINGS_FORMAT("Warnings.format"),
    WARNINGS_CONSOLE_ENABLED("Warnings.console.enabled"),
    WARNINGS_CONSOLE_FORMAT("Warnings.console.format"),
    BLOCK_SYNTAX_ENABLED("Block_Syntax.enabled"),
    BLOCK_SYNTAX_WARNING("Block_Syntax.warning"),
    BLOCK_SYNTAX_EXECUTORS("Block_Syntax.executors"),
    BLOCKS_COMMANDS_ENABLED("Blocks_Commands.enabled"),
    BLOCKS_COMMANDS_WARNING("Blocks_Commands.warning"),
    BLOCKS_COMMANDS_METHOD("Blocks_Commands.method"),
    BLOCKS_COMMANDS_EXECUTORS("Blocks_Commands.executors"),
    BLOCKS_COMMANDS("Blocks_Commands.commands"),
    COMMANDS_ONLY_CONSOLE_ENABLED("Commands_Only_Console.enabled"),
    COMMANDS_ONLY_CONSOLE_WARNING("Commands_Only_Console.warning"),
    COMMANDS_ONLY_CONSOLE_EXECUTORS("Commands_Only_Console.executors"),
    COMMANDS_ONLY_CONSOLE("Commands_Only_Console.commands"),
    COMMANDS_ONLY_PLAYERS_ENABLED("Commands_Only_Players.enabled"),
    COMMANDS_ONLY_PLAYERS_WARNING("Commands_Only_Players.warning"),
    COMMANDS_ONLY_PLAYERS_EXECUTORS("Commands_Only_Players.executors"),
    COMMANDS_ONLY_PLAYERS_BYPASS("Commands_Only_Players.bypass_players"),
    COMMANDS_ONLY_PLAYERS("Commands_Only_Players.commands"),
    BLOCK_CUSTOM_COMMANDS_ENABLED("Block_Custom_Commands.enabled"),
    BLOCK_CUSTOM_COMMANDS("Block_Custom_Commands.commands"),
    BLOCK_CUSTOM_COMMANDS_COMMAND(".command"),
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
    PORT_BYPASS_PREVENTION_WHITELISTED_IPS("Port_Bypass_Prevention.whitelisted_ips"),
    PORT_BYPASS_PREVENTION_KICK_MESSAGE("Port_Bypass_Prevention.kick_message"),
    PORT_BYPASS_PREVENTION_ALREADY_CONNECTED("Port_Bypass_Prevention.already_connected");

    private final String path;
    private final BetterSecurity plugin;
    private FileConfiguration config;

    Config(final String path) {
        this.path = path;
        this.plugin = BetterSecurity.getInstance();
        this.reloadConfig();
    }

    @Override
    public StringBuilder variables(final String... var) {
        StringBuilder builder = new StringBuilder();
        for(String texts : var) {
            builder.append(texts);
        }
        builder.append(path);
        return builder;
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
        return StringUtils.colorize(this.config.getString(variables(var).toString()));
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        for (String setList : this.config.getStringList(variables(var).toString())) {
            list.add(StringUtils.colorize(setList));
        }
        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.config.getBoolean(variables(var).toString());
    }

    @Override
    public boolean contains(final String... var) {
        return this.config.contains(variables(var).toString());
    }

    @Override
    public int getInt(final String... var) {
        return this.config.getInt(variables(var).toString());
    }

    @Override
    public String getCustomString(final String... var) {

        if (this.getString(var).startsWith("%prefix%")) {
            String replace = this.getString(var).replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
                replace = replace.replace("%center%", "");
                return StringUtils.center(replace);
            }
            return replace;
        }

        if(this.getString(var).startsWith("%center%")) {
            final String replace = this.getString(var).replace("%center%", "");
            return StringUtils.center(replace);
        }

        return this.getString(var);
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        if (this.getCustomString(var).isEmpty()) return;
        sender.sendMessage(this.getCustomString(var));
    }

    @SuppressWarnings("all")
    @Override
    public Set<String> getConfigurationSection(final String... var) {
        return this.config.getConfigurationSection(variables(var).toString()).getKeys( false);
    }

}
