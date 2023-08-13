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

package it.zs0bye.bettersecurity.bungee;

import it.zs0bye.bettersecurity.bungee.commands.MainCommand;
import it.zs0bye.bettersecurity.bungee.files.FileHandler;
import it.zs0bye.bettersecurity.bungee.files.FileType;
import it.zs0bye.bettersecurity.bungee.files.readers.Config;
import it.zs0bye.bettersecurity.bungee.files.readers.Lang;
import it.zs0bye.bettersecurity.bungee.listeners.*;
import it.zs0bye.bettersecurity.bungee.modules.Module;
import it.zs0bye.bettersecurity.common.updater.UpdateType;
import it.zs0bye.bettersecurity.common.updater.VandalUpdater;
import it.zs0bye.bettersecurity.common.utils.enums.ConsoleUtils;
import lombok.Getter;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.util.concurrent.TimeUnit;

@Getter
public class BetterSecurityBungee extends Plugin {

    @Getter
    private static BetterSecurityBungee instance;

    private BungeeAudiences adventure;
    private String updateMsg;

    @Override
    public void onEnable() {

        instance = this;
        this.adventure = BungeeAudiences.create(this);

        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "   ▄▄▄▄   ▓█████▄▄▄█████▓▄▄▄█████▓▓█████  ██▀███" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▓█████▄ ▓█   ▀▓  ██▒ ▓▒▓  ██▒ ▓▒▓█   ▀ ▓██ ▒ ██▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██▒ ▄██▒███  ▒ ▓██░ ▒░▒ ▓██░ ▒░▒███   ▓██ ░▄█ ▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██░█▀  ▒▓█  ▄░ ▓██▓ ░ ░ ▓██▓ ░ ▒▓█  ▄ ▒██▀▀█▄  " + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░▓█  ▀█▓░▒████▒ ▒██▒ ░   ▒██▒ ░ ░▒████▒░██▓ ▒██▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░▒▓███▀▒░░ ▒░ ░ ▒ ░░     ▒ ░░   ░░ ▒░ ░░ ▒▓ ░▒▓░" + ConsoleUtils.RESET);
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "    ██████ ▓█████  ▄████▄   █    ██  ██▀███   ██▓▄▄▄█████▓▓██   ██▓" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██    ▒ ▓█   ▀ ▒██▀ ▀█   ██  ▓██▒▓██ ▒ ██▒▓██▒▓  ██▒ ▓▒ ▒██  ██▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░ ▓██▄   ▒███   ▒▓█    ▄ ▓██  ▒██░▓██ ░▄█ ▒▒██▒▒ ▓██░ ▒░  ▒██ ██░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "    ▒   ██▒▒▓█  ▄ ▒▓▓▄ ▄██▒▓▓█  ░██░▒██▀▀█▄  ░██░░ ▓██▓ ░   ░ ▐██▓░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██████▒▒░▒████▒▒ ▓███▀ ░▒▒█████▓ ░██▓ ▒██▒░██░  ▒██▒ ░   ░ ██▒▓░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒ ▒▓▒ ▒ ░░░ ▒░ ░░ ░▒ ▒  ░░▒▓▒ ▒ ▒ ░ ▒▓ ░▒▓░░▓    ▒ ░░      ██▒▒▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░ ░▒  ░ ░ ░ ░  ░  ░  ▒   ░░▒░ ░ ░   ░▒ ░ ▒░ ▒ ░    ░     ▓██ ░▒░" + ConsoleUtils.RESET);
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Developed by zS0bye" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Current version v" + this.getDescription().getVersion() + " ● Running on " + this.getProxy().getName() + ConsoleUtils.RESET);

        this.loadFiles();

        this.loadCommands();
        this.loadListeners();

        this.getProxy().registerChannel("bsecurity:sender");

        this.loadUpdater();
        new Metrics(this, 17435);

        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ The Plug-in was started successfully ;)" + ConsoleUtils.RESET);
        this.getLogger().info("");

    }

    @Override
    public void onDisable() {
        if(this.adventure == null) return;
        this.adventure.close();
    }

    private void loadFiles() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Loading resources.." + ConsoleUtils.RESET);

        new FileHandler(this, FileType.CONFIG).saveDefaultConfig();
        new FileHandler(this, FileType.LANG).saveDefaultConfig();
        Module.loadFiles();

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Resources uploaded successfully!" + ConsoleUtils.RESET);
    }

    private void loadCommands() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Registering commands.." + ConsoleUtils.RESET);

        this.getProxy().getPluginManager().registerCommand(this, new MainCommand(this));

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Commands registered successfully!" + ConsoleUtils.RESET);
    }

    private void loadListeners() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Registering events.." + ConsoleUtils.RESET);

        Module.loadListeners();
        this.getProxy().getPluginManager().registerListener(this, new PluginMessageListener(this));
        this.getProxy().getPluginManager().registerListener(this, new PacketsListener(this));
        WaterTabCompleteListener.register(this);

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Events registered successfully!" + ConsoleUtils.RESET);
    }

    private void loadUpdater() {
        if(!Config.SETTINGS_CHECK_UPDATE.getBoolean()) return;
        final String resourceId = "105608";
        final VandalUpdater vandalUpdater = new VandalUpdater(resourceId, UpdateType.NORMAL);
        vandalUpdater.setUpdateMessage(Lang.UPDATE_NOTIFICATION.getCustomString());
        this.getProxy().getScheduler().schedule(this, () -> {
            this.updateMsg = vandalUpdater.message(this.getLogger(), this.getDescription().getName(),
                    this.getDescription().getVersion(), null, null);
            this.getProxy().getPluginManager().registerListener(this, new UpdaterListener(this));
        }, 20L, 30 * 60, TimeUnit.SECONDS);
    }

}
