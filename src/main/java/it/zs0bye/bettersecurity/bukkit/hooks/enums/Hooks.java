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

package it.zs0bye.bettersecurity.bukkit.hooks.enums;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.modules.Module;
import it.zs0bye.bettersecurity.common.utils.enums.ConsoleUtils;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public enum Hooks {
    PLACEHOLDERAPI("PlaceholderAPI"),
    PROTOCOLLIB("ProtocolLib"),
    PLUGMAN("PlugMan");

    private final String name;
    private final BetterSecurityBukkit plugin;
    private final Set<String> registered = new HashSet<>();

    Hooks(final String name) {
        this.name = name;
        this.plugin = BetterSecurityBukkit.getInstance();
    }

    public boolean isNotLoaded() {
        if(Bukkit.getPluginManager().getPlugin(this.name) == null) return true;
        if(this.registered.contains(this.name)) return false;
        this.plugin.getLogger().info(ConsoleUtils.YELLOW + "┃ • Registered " + this.name + " hook." + ConsoleUtils.RESET);
        this.registered.add(this.name);
        return false;
    }

    public boolean securityWarning() {
        if(!this.isNotLoaded()) return false;
        if(Module.TAB_COMPLETE.isDisabled()) return true;
        final Logger logger = this.plugin.getLogger();
        logger.warning("----------------------------------------- TAB COMPLETE NOT SAFE -----------------------------------------");
        logger.warning("You are using the \"TabComplete\" module, but you don't have ProtocolLib.");
        logger.warning("If you want to manage the complete tab, this plugin is essential for your security!");
        logger.warning("Do you want to know why? I'll give you a list:");
        logger.warning("1) Without ProtocolLib, you will not be able to manage the complete tab for server versions 1.8-1.12.2.");
        logger.warning("2) Without ProtocolLib, if you have a 1.13+ server with ViaBackwards + ViaRewind,");
        logger.warning("   players joining with 1.8-1.12 clients will be able to bypass tab-complete handling.");
        logger.warning("3) Without ProtocolLib, if you have a server 1.13+ client like \"MeteorClient\", they can see your");
        logger.warning("   plugins using the following command: .server plugins MassScan");
        logger.warning("--- To ignore this warning, insert the \"ProtocolLib\" plugin (https://ci.dmulloy2.net/job/ProtocolLib/) ---");
        return true;
    }

}
