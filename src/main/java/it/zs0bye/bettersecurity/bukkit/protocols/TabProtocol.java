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

package it.zs0bye.bettersecurity.bukkit.protocols;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.BukkitUser;
import it.zs0bye.bettersecurity.bukkit.files.readers.Tab;
import it.zs0bye.bettersecurity.bukkit.modules.Module;
import it.zs0bye.bettersecurity.bukkit.utils.VersionUtils;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.SoftwareType;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabHandler;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabProtocol extends PacketAdapter {

    public TabProtocol(final BetterSecurityBukkit plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE);
        if(Module.TAB_COMPLETE.isDisabled()) return;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @SneakyThrows
    @Override
    public void onPacketReceiving(final PacketEvent event) {
        if(Module.TAB_COMPLETE.isDisabled()) return;

        final PacketContainer packet = event.getPacket();
        final Player player = event.getPlayer();
        final BetterUser user = new BukkitUser(player);

        final String completion = packet.getSpecificModifier(String.class).read(0).toLowerCase();
        final List<String> commands = new ArrayList<>();

        new TabHandler(this.plugin.getLogger(), user, Tab.class, SoftwareType.CRAFTBUKKIT).injectTabSuggestions(commands, completion, event::setCancelled);

        if(completion.contains(" ")) return;
        this.replaceSuggestions(commands, player);
        event.setCancelled(true);
    }

    @SneakyThrows
    private void replaceSuggestions(final List<String> commands, final Player player) {
        if (!VersionUtils.legacy()) return;
        final PacketContainer serverPacket = new PacketContainer(PacketType.Play.Server.TAB_COMPLETE);
        serverPacket.getStringArrays().write(0, commands.toArray(new String[0]));
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, serverPacket);
    }

}
