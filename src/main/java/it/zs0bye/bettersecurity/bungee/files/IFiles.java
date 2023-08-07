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

package it.zs0bye.bettersecurity.bungee.files;

import net.md_5.bungee.api.CommandSender;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IFiles {

    void reloadConfig();

    String getPath();

    String getString(final String... var);

    String getCustomString(String replace, final String... var);

    String getCustomString(final String... var);

    List<String> getStringList(final String... var);

    boolean getBoolean(final String... var);

    boolean contains(final String... var);

    int getInt(final String... var);

    void send(final CommandSender sender, final String... var);

    void send(final CommandSender sender, final Map<String, String> placeholders, final String... var);

    void sendList(final CommandSender sender, final String... var);

    void sendList(final CommandSender sender, final Map<String, String> placeholders, final String... var);

    String variables(final String... var);

    Collection<String> getSection(final String... var);

}
