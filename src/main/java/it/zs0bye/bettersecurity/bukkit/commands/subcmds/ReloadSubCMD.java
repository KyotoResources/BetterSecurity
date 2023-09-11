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

package it.zs0bye.bettersecurity.bukkit.commands.subcmds;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.commands.BaseCommand;
import it.zs0bye.bettersecurity.bukkit.files.FileHandler;
import it.zs0bye.bettersecurity.bukkit.files.readers.Config;
import it.zs0bye.bettersecurity.bukkit.files.readers.Lang;
import it.zs0bye.bettersecurity.bukkit.hooks.HooksHandler;
import it.zs0bye.bettersecurity.common.BetterUser;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReloadSubCMD extends BaseCommand {

    private final String command;
    private BetterUser user;
    private BetterSecurityBukkit plugin;
    private HooksHandler hooks;

    public ReloadSubCMD(final String command, final String[] args, final BetterUser user, final BetterSecurityBukkit plugin) {
        this.command = command;
        this.user = user;
        this.plugin = plugin;
        this.hooks = this.plugin.getHooks();
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public ReloadSubCMD(final String command, final List<String> tab, final CommandSender sender) {
        this.command = command;
        if(!sender.hasPermission(this.command + ".command." + this.getName())) return;
        tab.add(this.getName());
    }

    @Override
    protected String getName() {
        return "reload";
    }

    @Override
    protected void execute() {
        if(!this.user.hasPermission(this.command + ".command." + this.getName())) {
            Lang.INSUFFICIENT_PERMISSIONS.send(this.user);
            return;
        }

        final List<FileHandler> handlers = new ArrayList<>();
        final Map<Integer, FileHandler> map = new HashMap<>();
        this.plugin.getHandlers().values().forEach(file -> map.put(file.getType().getPriority(), file));
        for(final Integer priority : map.keySet()) handlers.add(map.get(priority));

        handlers.forEach(file -> {
            if(file.reload()) return;
            Lang.RELOAD_NEW_LANGUAGE_DETECTED.send(this.user, new HashMap<String, String>(){{
                put("%lang%", Config.SETTINGS_LOCALE.getString());
                put("%command%", command);
            }});
        });

        HandlerList.unregisterAll(this.plugin);
        this.plugin.registerListeners();

        this.hooks.unregisterAllProtocols();
        this.plugin.registerProtocols();

        Lang.RELOAD_CONFIGURATIONS.send(this.user);
    }

}
