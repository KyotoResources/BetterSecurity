/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (C) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
    BLOCK_TAB_COMPLETE_WATERFALL_PREVENTION("Block_Tab_Complete.waterfall_prevention"),
    BLOCK_TAB_COMPLETE_BLACKLISTED_SUGGESTIONS("Block_Tab_Complete.blacklisted_suggestions"),
    BLOCK_TAB_COMPLETE_BYPASS_METHOD("Block_Tab_Complete.bypass.method"),
    BLOCK_TAB_COMPLETE_BYPASS_PLAYERS("Block_Tab_Complete.bypass.players"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_ENABLED("Block_Tab_Complete.whitelisted_suggestions.enabled"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_PARTIAL_MATCHES("Block_Tab_Complete.whitelisted_suggestions.partial_matches"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_ENABLED_GROUPS("Block_Tab_Complete.whitelisted_suggestions.enabled_groups"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS("Block_Tab_Complete.whitelisted_suggestions.groups"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_SUGGESTIONS(".suggestions"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_REQUIRED_SERVER(".required_server"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_REQUIRED_PERMISSION(".required_permission"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_REQUIRED_PLAYERS(".required_players"),
    BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_GROUPS_IGNORE_SERVERS(".ignore_servers"),
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
        return this.config.getSection(this.variables(var)).getKeys();
    }

}
