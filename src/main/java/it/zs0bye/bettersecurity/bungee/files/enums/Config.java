package it.zs0bye.bettersecurity.bungee.files.enums;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.IFiles;
import it.zs0bye.bettersecurity.bungee.utils.StringUtils;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum Config implements IFiles {
    CUSTOM(""),
    SETTINGS_LOCALE("Settings.locale"),
    SETTINGS_PREFIX("Settings.prefix"),
    SETTINGS_CHECK_UPDATE("Settings.check_update"),
    BLOCK_TAB_COMPLETE_ENABLED("Block_Tab_Complete.enabled"),
    BLOCK_TAB_COMPLETE_COMMANDS("Block_Tab_Complete.commands"),
    BLOCK_TAB_COMPLETE_BYPASS_METHOD("Block_Tab_Complete.bypass.method"),
    BLOCK_TAB_COMPLETE_BYPASS_PLAYERS("Block_Tab_Complete.bypass.players"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_ENABLED("Block_Tab_Complete.whitelisted_commands.enabled"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_PARTIAL_MATCHES("Block_Tab_Complete.whitelisted_commands.partial_matches"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_GLOBAL("Block_Tab_Complete.whitelisted_commands.global"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_ENABLED("Block_Tab_Complete.whitelisted_commands.required.enabled"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_GLOBAL_GROUPS("Block_Tab_Complete.whitelisted_commands.required.global.groups"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_GROUPS("Block_Tab_Complete.whitelisted_commands.required.groups"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_PERMISSION(".permission"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_REQUIRED_COMMANDS(".commands"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_SERVER_MODE_ENABLED("Block_Tab_Complete.whitelisted_commands.server_mode.enabled"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_SERVER_MODE_SERVERS("Block_Tab_Complete.whitelisted_commands.server_mode.servers"),
    BLOCK_TAB_COMPLETE_WHITELISTED_COMMANDS_SERVER_MODE_GROUPS(".groups"),
    WARNINGS_FORMAT("Warnings.format"),
    WARNINGS_CONSOLE_ENABLED("Warnings.console.enabled"),
    WARNINGS_CONSOLE_FORMAT("Warnings.console.format"),
    BLOCKS_COMMANDS_ENABLED("Blocks_Commands.enabled"),
    BLOCKS_COMMANDS_WARNING("Blocks_Commands.warning"),
    BLOCKS_COMMANDS_METHOD("Blocks_Commands.method"),
    BLOCKS_COMMANDS_FORCE_CHECK("Blocks_Commands.force_check"),
    BLOCKS_COMMANDS_MESSAGE("Blocks_Commands.message"),
    BLOCKS_COMMANDS("Blocks_Commands.commands"),
    BLOCKS_COMMANDS_SERVER_MODE_ENABLED("Blocks_Commands.server_mode.enabled"),
    BLOCKS_COMMANDS_SERVER_MODE_SERVERS("Blocks_Commands.server_mode.servers"),
    BLOCKS_COMMANDS_SERVER_MODE_METHOD(".method"),
    BLOCKS_COMMANDS_SERVER_MODE_MESSAGE(".message"),
    BLOCKS_COMMANDS_SERVER_MODE_COMMANDS(".commands");

    private final String path;
    private final BetterSecurityBungee plugin;
    private Configuration config;

    Config(final String path) {
        this.path = path;
        this.plugin = BetterSecurityBungee.getInstance();
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
                return CStringUtils.center(replace);
            }
            return replace;
        }

        if(this.getString(var).startsWith("%center%")) {
            final String replace = this.getString(var).replace("%center%", "");
            return CStringUtils.center(replace);
        }

        return this.getString(var);
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        if (this.getCustomString(var).isEmpty()) return;
        StringUtils.send(sender, this.getCustomString(var));
    }

    @Override
    public Collection<String> getSection(final String... var) {
        return this.config.getSection(variables(var).toString()).getKeys();
    }

}
