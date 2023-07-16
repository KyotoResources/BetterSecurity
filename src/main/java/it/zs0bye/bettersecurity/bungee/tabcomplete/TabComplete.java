package it.zs0bye.bettersecurity.bungee.tabcomplete;

import com.mojang.brigadier.tree.CommandNode;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.bungee.tabcomplete.methods.Method;
import it.zs0bye.bettersecurity.bungee.tabcomplete.methods.MethodType;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;

import java.util.*;

@Getter
public class TabComplete extends TabProviders {

    private final BetterSecurityBungee plugin;
    private final ProxiedPlayer player;

    private final String bypass_method;
    private final List<String> bypass_players;
    private final List<String> suggestions;
    private final String method;

    private final TabGroupsMode groupsMode;

    public TabComplete(final BetterSecurityBungee plugin, final ProxiedPlayer player) {
        this.plugin = plugin;
        this.player = player;
        this.bypass_method = Config.MANAGE_TAB_COMPLETE_BYPASS_METHOD.getString();
        this.bypass_players = Config.MANAGE_TAB_COMPLETE_BYPASS_PLAYERS.getStringList();
        this.suggestions = Config.MANAGE_TAB_COMPLETE_SUGGESTIONS.getStringList();
        this.method = Config.MANAGE_TAB_COMPLETE_METHOD.getString();
        this.groupsMode = new TabGroupsMode(this.player, this);
    }

    protected MethodType getMethodType(final String path, String group, String permission) {
        group = group.isEmpty() ? group : "." + group;
        permission = permission.isEmpty() ? "bettersecurity.tab" + group : permission;
        for(final MethodType type : MethodType.values()) {
            if(!this.player.hasPermission(permission + "." + type.getName())) continue;
            return type;
        }
        final String configType = Config.CUSTOM.getString(path).toUpperCase();
        if (!MethodType.getTypes().contains(configType)) {
            this.plugin.getLogger().warning("There was a problem in this path \"" + path + "\"! It can only be either WHITELIST or BLACKLIST. By default it was set to WHITELIST.");
            return MethodType.WHITELIST;
        }
        return MethodType.valueOf(configType);
    }

    private MethodType getMethodType() {
        return this.getMethodType(Config.MANAGE_TAB_COMPLETE_METHOD.getPath(), "", "");
    }

    public boolean bypass() {
        if (this.bypass_method.equals("PERMISSION")) return player.hasPermission("bettersecurity.bypass.tab");
        if (this.bypass_method.equals("PLAYERS")) return this.bypass_players.contains(player.getName())
                || this.bypass_players.contains(player.getUniqueId().toString());
        return false;
    }

    public void applyTabLegacy(final List<String> commands, final String completion, final Cancellable cancelled) {
        if (Config.MANAGE_TAB_COMPLETE_GROUPS_MODE_ENABLED.getBoolean()) {
            this.groupsMode.initTabLegacy(commands, completion, cancelled);
            return;
        }
        this.initTabLegacy(commands, completion, cancelled);
    }

    public void applyTabWaterfall(final Iterator<String> iterator) {
        if (Config.MANAGE_TAB_COMPLETE_GROUPS_MODE_ENABLED.getBoolean()) {
            this.groupsMode.initTabWaterfall(iterator);
            return;
        }
        this.initTabWaterfall(iterator);
    }

    public void applyTabChildren(final Collection<CommandNode<?>> childrens) {
        if (Config.MANAGE_TAB_COMPLETE_GROUPS_MODE_ENABLED.getBoolean()) {
            this.groupsMode.initTabChildren(childrens);
            return;
        }
        this.initTabChildren(childrens);
    }

    private void initTabLegacy(final List<String> commands, final String completion, final Cancellable cancelled) {
        final List<String> suggestions = this.legacy(
                new Method(this.getMethodType(), this.suggestions, completion),
                completion,
                this.suggestions,
                commands,
                cancelled);

        commands.addAll(Config.MANAGE_TAB_COMPLETE_PARTIAL_MATCHES.getBoolean() ?
                CStringUtils.copyPartialMatches(completion, suggestions) :
                suggestions);

        if (!suggestions.isEmpty()) return;
        commands.clear();
        cancelled.setCancelled(true);
    }

    private void initTabWaterfall(final Iterator<String> iterator) {
        this.waterfall(new Method(this.getMethodType(), this.suggestions, null), this.suggestions, iterator);
    }

    private void initTabChildren(final Collection<CommandNode<?>> childrens) {
        final Map<String, String> suggestions = this.childrens(new Method(this.getMethodType(), this.suggestions, null), this.suggestions);

        childrens.removeIf(node -> {
            for(final String suggestion : suggestions.keySet()) {
                if(node.getName().equalsIgnoreCase(suggestion)) return false;
            }
            return true;
        });
    }

}
