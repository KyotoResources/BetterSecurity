/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (c) 2023 KyotoResources
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

package it.zs0bye.bettersecurity.bungee.channels;

import com.google.common.io.ByteArrayDataOutput;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.BungeeUser;
import it.zs0bye.bettersecurity.bungee.files.readers.Tab;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.SoftwareType;
import it.zs0bye.bettersecurity.common.channels.ChannelRegistrar;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabHandler;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabMerge;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.SuggestionProvider;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

@AllArgsConstructor
public class TabMergeChannel extends ChannelRegistrar {

    private final BetterSecurityBungee plugin;

    @Override
    protected String getIdentifier() {
        return "BS-TabComplete";
    }

    @Override
    protected void receiver() {
        final String playerName = this.input.readUTF();
        final String whitelisted = this.input.readUTF();
        final String blacklisted = this.input.readUTF();

        TabMerge.WHITELISTED.add(Arrays.asList(whitelisted.split(", ")));
        TabMerge.BLACKLISTED.add(Arrays.asList(blacklisted.split(", ")));

        final BetterUser user = new BungeeUser(playerName);
        final SuggestionProvider provider = new TabHandler(this.plugin.getLogger(), user, Tab.class, SoftwareType.PROXY).injectTabSuggestions();
        if(provider == null) return;

        final ByteArrayDataOutput stream = ChannelRegistrar.sendOutput("BS-TabComplete",
                String.join(", ", provider.getWhitelisted()),
                String.join(", ", provider.getBlacklisted()));

        ((ProxiedPlayer) user.getPlayer()).getServer().sendData(ChannelRegistrar.PLUGIN_RETURN, stream.toByteArray());
    }
}
