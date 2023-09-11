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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Command implements ConfigReader {
    INSTANCE(""),
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
    BLOCK_CUSTOM_COMMANDS_REQUIRED_PLAYERS(".required_players");

    private final FileConfigReader reader;

    @Getter
    private final String path;

    @Getter
    private final FileType type;

    Command(final String path) {
        this.reader = new FileConfigReader(BetterSecurityBukkit.getInstance(), path, FileType.COMMAND);
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
