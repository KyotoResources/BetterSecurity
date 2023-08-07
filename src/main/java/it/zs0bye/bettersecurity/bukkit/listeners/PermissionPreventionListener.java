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

package it.zs0bye.bettersecurity.bukkit.listeners;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.HashSet;
import java.util.Set;

public class PermissionPreventionListener implements Listener {

    private final BetterSecurityBukkit plugin;

    private final static Set<Player> punishment = new HashSet<>();

    public PermissionPreventionListener(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        if(!Config.PERMISSION_PREVENTION_ENABLED.getBoolean()) return;
        if(!Config.PERMISSION_PREVENTION_CHECKS_CHAT.getBoolean()) return;
        final Player player = event.getPlayer();
        if(!this.checks(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        if(!Config.PERMISSION_PREVENTION_ENABLED.getBoolean()) return;
        if(!Config.PERMISSION_PREVENTION_CHECKS_CMDS.getBoolean()) return;
        final Player player = event.getPlayer();
        if(!this.checks(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if(!Config.PERMISSION_PREVENTION_ENABLED.getBoolean()) return;
        if(!Config.PERMISSION_PREVENTION_CHECKS_INVENTORY.getBoolean()) return;
        if(event.getCurrentItem() == null) return;
        final Player player = (Player) event.getWhoClicked();
        if(!this.checks(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        if(!Config.PERMISSION_PREVENTION_ENABLED.getBoolean()) return;
        if(!Config.PERMISSION_PREVENTION_CHECKS_INTERACT.getBoolean()) return;
        final Player player = event.getPlayer();
        if(!this.checks(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if(!Config.PERMISSION_PREVENTION_ENABLED.getBoolean()) return;
        if(!Config.PERMISSION_PREVENTION_CHECKS_MOVEMENT.getBoolean()) return;
        final Player player = event.getPlayer();
        if(!this.checks(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if(!Config.PERMISSION_PREVENTION_ENABLED.getBoolean()) return;
        if(!Config.PERMISSION_PREVENTION_CHECKS_JOIN.getBoolean()) return;
        final Player player = event.getPlayer();
        this.checks(player);
    }

    @EventHandler
    public void oQuit(final PlayerQuitEvent event) {
        if(!Config.PERMISSION_PREVENTION_ENABLED.getBoolean()) return;
        if(!Config.PERMISSION_PREVENTION_CHECKS_LEFT.getBoolean()) return;
        final Player player = event.getPlayer();
        this.checks(player);
    }

    private boolean isNotPunished(final Player player, final String path, final String type, final String value) {
        if(this.bypass_players(path, player)) return true;

        if(type.equals("PERMISSION") || type.equals("GROUP")) Bukkit.getScheduler().runTaskLater(this.plugin, () ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.valueOf("PERMISSION_PREVENTION_REMOVE_" + type).getString()
                        .replace("%player%", player.getName())
                        .replace("%" + type.toLowerCase() + "%", value)), 2L);

        if(!punishment.contains(player) && type.equals("DEFAULT")) {
            SendExecutors.send(this.plugin, Config.PERMISSION_PREVENTION_PUNISHMENT.getStringList(), player);
            punishment.add(player);
        }
        return false;
    }

    private boolean isNotBypassable(final Player player) {
        if(!Config.PERMISSION_PREVENTION_GLOBAL_BYPASS_ENABLED.getBoolean()) return false;
        final String path = Config.PERMISSION_PREVENTION_GLOBAL_BYPASS.getPath();
        return this.bypass_players(path, player);
    }

    private boolean checks(final Player player) {
        if(this.isNotBypassable(player)) return false;
        if(this.groups(player)) return true;
        if(this.permissions(player)) return true;
        return operators(player);
    }

    private boolean operators(final Player player) {
        if(!Config.PERMISSION_PREVENTION_OPERATORS_ENABLED.getBoolean()) return false;
        final String path = Config.PERMISSION_PREVENTION_OPERATORS.getPath();

        if(!player.isOp()) return false;
        if(this.isNotPunished(player, path, "DEFAULT", null)) return false;
        player.setOp(false);
        return true;
    }

    private boolean groups(final Player player) {
        if(!Config.PERMISSION_PREVENTION_GROUPS_ENABLED.getBoolean()) return false;
        if(player.isOp()) return false;
        for(final String group : Config.PERMISSION_PREVENTION_GROUPS_LIST.getConfigurationSection()) {
            if(!player.hasPermission("group." + group)) continue;
            final String path = Config.PERMISSION_PREVENTION_GROUPS_LIST.getPath() + "." + group;

            if(this.isNotPunished(player, path, "GROUP", group)) continue;
            if(this.isNotPunished(player, path, "DEFAULT", null)) continue;
            return true;
        }
        return false;
    }

    private boolean permissions(final Player player) {
        if(!Config.PERMISSION_PREVENTION_PERMISSIONS_ENABLED.getBoolean()) return false;
        if(player.isOp()) return false;
        for(final String permission : Config.PERMISSION_PREVENTION_PERMISSIONS_LIST.getConfigurationSection()) {
            final String permconvert = permission.replace("_", ".");
            if(!player.hasPermission(permconvert)) continue;
            final String path = Config.PERMISSION_PREVENTION_PERMISSIONS_LIST.getPath() + "." + permission;

            if(this.isNotPunished(player, path, "PERMISSION", permconvert)) continue;
            if(this.isNotPunished(player, path, "DEFAULT", null)) continue;
            return true;
        }
        return false;
    }

    private boolean bypass_players(String path, final Player player) {
        path = path + Config.PERMISSION_PREVENTION_BYPASS_PLAYERS.getPath();
        if(!Config.CUSTOM.contains(path)) return false;
        return Config.CUSTOM.getStringList(path).contains(player.getName()) || Config.CUSTOM.getStringList(path).contains(player.getUniqueId().toString());
    }

}
