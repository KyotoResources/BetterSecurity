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

package it.zs0bye.bettersecurity.bukkit.files.readers;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.FileConfigReader;
import it.zs0bye.bettersecurity.bukkit.files.FileType;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.config.ConfigReader;
import lombok.Getter;

import java.util.*;

public enum Config implements ConfigReader {
    INSTANCE(""),
    SETTINGS_LOCALE("Settings.locale"),
    SETTINGS_PREFIX("Settings.prefix"),
    SETTINGS_CHECK_UPDATE_ENABLED("Settings.check_update.enabled"),
    SETTINGS_CHECK_UPDATE_TYPE("Settings.check_update.type"),
    MODULES_TAB_COMPLETE("Modules.tab_complete"),
    MODULES_COMMANDS("Modules.commands"),
    WARNINGS_PROXY("Warnings.proxy"),
    WARNINGS_FORMATS_CMDS_FORMAT("Warnings.formats.commands.format"),
    WARNINGS_FORMATS_CMDS_CONSOLE("Warnings.formats.commands.console"),
    WARNINGS_FORMATS_PBP_FORMAT("Warnings.formats.port_bypass_prevention.format"),
    WARNINGS_FORMATS_PBP_CONSOLE("Warnings.formats.port_bypass_prevention.console"),
    WARNINGS_LOG_CONSOLE("Warnings.log_console"),
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

    private final FileConfigReader reader;

    @Getter
    private final String path;

    @Getter
    private final FileType type;

    Config(final String path) {
        this.reader = new FileConfigReader(BetterSecurityBukkit.getInstance(), path, FileType.CONFIG);
        this.path = this.reader.getPath();
        this.type = this.reader.getType();
    }

    @Override
    public String variables(final String... var) {
        return this.reader.variables(var);
    }

    @Override
    public void reloadConfig() {
        this.reader.reloadConfig();
    }

    @Override
    public Object getObject(final String... var) {
        return this.reader.getObject(var);
    }

    @Override
    public String getString(final String... var) {
        return this.reader.getString(var);
    }

    @Override
    public List<String> getStringList(final String... var) {
        return this.reader.getStringList(var);
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.reader.getBoolean(var);
    }

    @Override
    public boolean contains(final String... var) {
        return this.reader.contains(var);
    }

    @Override
    public int getInt(final String... var) {
        return this.reader.getInt(var);
    }

    @Override
    public String getCustomString(final String replace) {
        return this.reader.getCustomString(replace);
    }

    @Override
    public String getCustomString(String... var) {
        return this.reader.getCustomString(var);
    }

    @Override
    public void send(final BetterUser user, final String... var) {
        this.reader.send(user, var);
    }

    @Override
    public void send(final BetterUser user, final Map<String, String> placeholders, final String... var) {
        this.reader.send(user, placeholders, var);
    }

    @Override
    public void sendList(final BetterUser user, final String... var) {
        this.reader.sendList(user, new HashMap<>(), var);
    }

    @Override
    public void sendList(final BetterUser user, final Map<String, String> placeholders, final String... var) {
        this.reader.sendList(user, placeholders, var);
    }

    @Override
    public Collection<String> getSection(final String... var) {
        return this.reader.getSection(var);
    }

}
