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
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatProtocol extends PacketAdapter {

    private String target;
    private String replace;

    public ChatProtocol(final BetterSecurityBukkit plugin, final String message) {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.CHAT);

        if(!Config.REPLACE_CUSTOM_MESSAGES_ENABLED.getBoolean()) return;

        ProtocolLibrary.getProtocolManager().addPacketListener(this);

        final String path = Config.REPLACE_CUSTOM_MESSAGES.getPath() + "." + message;

        this.target = Config.CUSTOM.getString(path + Config.REPLACE_CUSTOM_MESSAGES_TARGET.getPath());
        this.replace = Config.CUSTOM.getString(path + Config.REPLACE_CUSTOM_MESSAGES_REPLACE.getPath());

    }

    @SneakyThrows
    @Override
    public void onPacketSending(final PacketEvent event) {

        if(!Config.REPLACE_CUSTOM_MESSAGES_ENABLED.getBoolean()) return;

        final PacketContainer packet = event.getPacket();
        final Player player = event.getPlayer();

        if (player == null) return;

        final StructureModifier<WrappedChatComponent> chat = packet.getChatComponents();
        final WrappedChatComponent wrapped = chat.read(0);

        if (wrapped == null) {
            final StructureModifier<BaseComponent[]> modifier = packet.getSpecificModifier(BaseComponent[].class);

            final BaseComponent[] components = modifier.readSafely(0);

            if (components == null) return;

            String message = ComponentSerializer.toString(components);

            if(message == null) return;
            if(this.notFoundTarget(message)) return;

            message = ComponentSerializer.toString(TextComponent.fromLegacyText(this.replace));

            modifier.write(0, ComponentSerializer.parse(message));
            return;
        }

        String message = wrapped.getJson();

        if(message == null) return;
        if(this.notFoundTarget(wrapped.getJson())) return;

        message = ComponentSerializer.toString(TextComponent.fromLegacyText(this.replace));

        chat.write(0, WrappedChatComponent.fromJson(message));
    }

    private boolean notFoundTarget(final String json) {
        final BaseComponent[] components = ComponentSerializer.parse(json);
        final String component = ChatColor.stripColor(BaseComponent.toLegacyText(components));
        return !component.equals(this.target);
    }

}