package it.zs0bye.bettersecurity.bungee.files.enums;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.IFiles;
import it.zs0bye.bettersecurity.bungee.utils.StringUtils;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

import java.util.*;

public enum Config implements IFiles {
    CUSTOM(""),
    SETTINGS_LOCALE("Settings.locale"),
    SETTINGS_PREFIX("Settings.prefix"),
    SETTINGS_CHECK_UPDATE("Settings.check_update"),
    MANAGE_TAB_COMPLETE_ENABLED("Manage_Tab_Complete.enabled"),
    MANAGE_TAB_COMPLETE_IGNORE_WATERFALL_WARNING("Manage_Tab_Complete.ignore_waterfall_warning"),
    MANAGE_TAB_COMPLETE_IGNORE_1_13_TAB_COMPLETE("Manage_Tab_Complete.ignore_1_13_tab_complete"),
    MANAGE_TAB_COMPLETE_PARTIAL_MATCHES("Manage_Tab_Complete.partial_matches"),
    MANAGE_TAB_COMPLETE_GLOBAL_BYPASS_ENABLED("Manage_Tab_Complete.global_bypass.enabled"),
    MANAGE_TAB_COMPLETE_GLOBAL_BYPASS_METHOD("Manage_Tab_Complete.global_bypass.method"),
    MANAGE_TAB_COMPLETE_GLOBAL_BYPASS_PLAYERS("Manage_Tab_Complete.global_bypass.players"),
    MANAGE_TAB_COMPLETE_SUGGESTIONS("Manage_Tab_Complete.suggestions"),
    MANAGE_TAB_COMPLETE_METHOD("Manage_Tab_Complete.method"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_ENABLED("Manage_Tab_Complete.groups_mode.enabled"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_ENABLED_GROUPS("Manage_Tab_Complete.groups_mode.enabled_groups"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_GROUPS("Manage_Tab_Complete.groups_mode.groups"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_GROUPS_PRIORITY(".priority"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_GROUPS_METHOD(".method"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_GROUPS_SUGGESTIONS(".suggestions"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_GROUPS_REQUIRED_SERVERS(".required_servers"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_GROUPS_REQUIRED_PERMISSION(".required_permission"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_GROUPS_REQUIRED_PLAYERS(".required_players"),
    MANAGE_TAB_COMPLETE_GROUPS_MODE_GROUPS_IGNORE_SERVERS(".ignore_servers"),
    WARNINGS_PROXY("Warnings.proxy"),
    WARNINGS_FORMATS_CMDS_FORMAT("Warnings.formats.commands.format"),
    WARNINGS_FORMATS_CMDS_CONSOLE("Warnings.formats.commands.console"),
    WARNINGS_FORMATS_PCS_FORMAT("Warnings.formats.prevent_command_spam.format"),
    WARNINGS_FORMATS_PCS_CONSOLE("Warnings.formats.prevent_command_spam.console"),
    WARNINGS_LOG_CONSOLE("Warnings.log_console"),
    BLOCKS_COMMANDS_ENABLED("Blocks_Commands.enabled"),
    BLOCKS_COMMANDS_WARNING("Blocks_Commands.warning"),
    BLOCKS_COMMANDS_METHOD("Blocks_Commands.method"),
    BLOCKS_COMMANDS_FORCE_CHECK("Blocks_Commands.force_check"),
    BLOCKS_COMMANDS_EXECUTORS("Blocks_Commands.executors"),
    BLOCKS_COMMANDS("Blocks_Commands.commands"),
    BLOCKS_COMMANDS_SERVER_MODE_ENABLED("Blocks_Commands.server_mode.enabled"),
    BLOCKS_COMMANDS_SERVER_MODE_SERVERS("Blocks_Commands.server_mode.servers"),
    BLOCKS_COMMANDS_SERVER_MODE_METHOD(".method"),
    BLOCKS_COMMANDS_SERVER_MODE_EXECUTORS(".executors"),
    BLOCKS_COMMANDS_SERVER_MODE_COMMANDS(".commands"),
    PREVENT_COMMAND_SPAM_ENABLED("Prevent_Command_Spam.enabled"),
    PREVENT_COMMAND_SPAM_WARNING("Prevent_Command_Spam.warning"),
    PREVENT_COMMAND_SPAM_DELAY("Prevent_Command_Spam.delay"),
    PREVENT_COMMAND_SPAM_COMMAND_LIMIT("Prevent_Command_Spam.command_limit"),
    PREVENT_COMMAND_SPAM_BLOCK_COMMAND("Prevent_Command_Spam.block_command"),
    PREVENT_COMMAND_SPAM_EXECUTORS("Prevent_Command_Spam.executors"),
    PREVENT_COMMAND_SPAM_METHOD("Prevent_Command_Spam.method"),
    PREVENT_COMMAND_SPAM_COMMANDS("Prevent_Command_Spam.commands");

    private final String path;
    private final BetterSecurityBungee plugin;
    private Configuration config;

    Config(final String path) {
        this.path = path;
        this.plugin = BetterSecurityBungee.getInstance();
        this.reloadConfig();
    }

    @Override
    public String variables(final String... var) {
        StringBuilder builder = new StringBuilder();
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

    public Object getObject(final String... var) {
        return this.config.get(this.variables(var));
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
        StringUtils.send(sender, message);
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
            sender.sendMessage(TextComponent.fromLegacyText(this.getCustomString(msg, var)));
        });
    }

    @Override
    public Collection<String> getSection(final String... var) {
        return this.config.getSection(this.variables(var)).getKeys();
    }

}
