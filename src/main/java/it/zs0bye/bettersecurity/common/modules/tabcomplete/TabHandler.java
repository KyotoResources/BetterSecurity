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

package it.zs0bye.bettersecurity.common.modules.tabcomplete;

import com.mojang.brigadier.tree.CommandNode;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.SoftwareType;
import it.zs0bye.bettersecurity.common.config.ConfigReader;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.modes.BasicMode;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.modes.advanced.AdvancedMode;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.providers.SuggestionProvider;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Getter
public class TabHandler {

    private final Logger logger;
    private final BetterUser user;

    private final Class<? extends Enum> reader;
    private final SoftwareType softwareType;

    public TabHandler(final Logger logger, final BetterUser user, final Class<? extends Enum> reader, final SoftwareType type) {
        this.logger = logger;
        this.user = user;
        this.reader = reader;
        this.softwareType = type;
    }

    private boolean bypass() {
        if (!this.reader("GLOBAL_BYPASS_ENABLED").getBoolean()) return false;
        final String method = this.reader("GLOBAL_BYPASS_METHOD").getString();
        final List<String> players = this.reader("GLOBAL_BYPASS_PLAYERS").getStringList();
        if (method.equals("PERMISSION")) return this.user.hasPermission("bettersecurity.bypass.tab");
        if (method.equals("PLAYERS")) return players.contains(this.user.getName())
                || players.contains(this.user.getUniqueId().toString());
        return false;
    }

    public SuggestionProvider provider() {
        if (this.reader("ADV_MODE_ENABLED").getBoolean()) return new AdvancedMode(this, this.logger, this.user, this.softwareType);
        return new BasicMode(this);
    }

    public SuggestionProvider injectTabSuggestions() {
        if(this.bypass()) return null;
        final SuggestionProvider provider = this.provider();
        provider.addSuggestions();
        return provider;
    }

    public void injectTabSuggestions(final List<String> commands, final String completion, final Consumer<Boolean> cancelled) {
        if (this.bypass()) return;
        this.provider().addSuggestions(commands, completion, cancelled);
    }

    public void injectTabSuggestions(final Set<String> suggestions) {
        if (this.bypass()) return;
        this.provider().addSuggestions(suggestions);
    }

    public void injectTabSuggestions(final Collection<CommandNode<?>> childrens) {
        if (this.bypass()) return;
        this.provider().addSuggestions(childrens);
    }

    public void injectTabChildrens(final String completion, final List<String> childrens, final Consumer<Boolean> cancelled) {
        if (this.bypass()) return;
        this.provider().addChildrens(completion, childrens, cancelled);
    }

    public ConfigReader reader(final String value) {
        return (ConfigReader) Enum.valueOf(this.reader, value);
    }

    public Set<String> sort(final Set<String> list) {
        return new TabSorting(this).sort(list);
    }

}
