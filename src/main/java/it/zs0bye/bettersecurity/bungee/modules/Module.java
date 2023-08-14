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

package it.zs0bye.bettersecurity.bungee.modules;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.FileHandler;
import it.zs0bye.bettersecurity.bungee.files.FileType;
import it.zs0bye.bettersecurity.bungee.files.readers.Config;
import it.zs0bye.bettersecurity.bungee.listeners.BlocksCmdsListener;
import it.zs0bye.bettersecurity.bungee.listeners.ManageTabCompleteListener;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Listener;

import java.lang.reflect.Constructor;
import java.util.Map;

@Getter
public enum Module {
    TAB_COMPLETE(FileType.TAB, ManageTabCompleteListener.class),
    COMMANDS(FileType.COMMAND, BlocksCmdsListener.class);

    private final BetterSecurityBungee plugin;

    private final FileType type;
    private final Class<? extends Listener>[] listeners;

    @SafeVarargs
    Module(final FileType type, final Class<? extends Listener>... listeners) {
        this.plugin = BetterSecurityBungee.getInstance();
        this.type = type;
        this.listeners = listeners;
    }

    public boolean isDisabled() {
        return !Config.valueOf("MODULES_" + this.name()).getBoolean();
    }

    public void loadFile(final Map<FileType, FileHandler> handlers) {
        handlers.put(this.type, new FileHandler(this.plugin, this.type).saveDefaultConfig());
    }

    @SneakyThrows
    public void loadListener() {
        for (final Class<?> classListener : this.listeners) {
            final Constructor<?> constructor = classListener.getConstructor(BetterSecurityBungee.class);
            final Listener listener = (Listener) constructor.newInstance(this.plugin);
            this.plugin.getProxy().getPluginManager().registerListener(this.plugin, listener);
        }
    }

    public static void loadListeners() {
        for (final Module module : Module.values()) module.loadListener();
    }

    public static void loadFiles(final Map<FileType, FileHandler> handlers) {
        for (final Module module : Module.values()) module.loadFile(handlers);
    }

}
