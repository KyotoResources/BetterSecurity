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
public class TabGroupsMode extends TabProviders {

    private final ProxiedPlayer player;
    private final TabComplete tab;

    public TabGroupsMode(final ProxiedPlayer player,  final TabComplete tab) {
        this.player = player;
        this.tab = tab;
    }

    private boolean hasBypass() {
        return false;
    }

    private MethodType getMethodType(final TabGroup group) {
        return this.tab.getMethodType(group.getMethodPath(), group.getName(), group.getPermission());
    }

    public void initTabLegacy(final List<String> commands, final String completion, final Cancellable cancelled) {

        final List<String> suggestions = new ArrayList<>();

//        grouploop:
//        for (final TabGroup group : TabGroupsMode.groups(player)) {
//            final Method method = new Method(this.getMethodType(group), group.getSuggestions(), completion);
//
//            for(final String suggestion : group.getSuggestions()) {
//                if (method.get()) {
//                    if (!completion.startsWith("/" + suggestion)) continue grouploop;
//                    commands.clear();
//                    cancelled.setCancelled(true);
//                }
//
//                if (!completion.startsWith("/") || completion.contains(" ")) continue grouploop;
//                if (completion.startsWith("/" + suggestion)) continue grouploop;
//                commands.clear();
//
//                if (newsuggestions.contains(suggestion)) continue grouploop;
//                newsuggestions.add("/" + suggestion);
//            }
//        }

        TabGroupsMode.groups(player).forEach(group ->
                suggestions.addAll(this.legacy(new Method(this.getMethodType(group), group.getSuggestions(), completion),
                completion,
                group.getSuggestions(),
                commands,
                cancelled)));

        commands.addAll(Config.MANAGE_TAB_COMPLETE_PARTIAL_MATCHES.getBoolean() ?
                CStringUtils.copyPartialMatches(completion, suggestions) :
                suggestions);

        if (!suggestions.isEmpty()) return;
        commands.clear();
        cancelled.setCancelled(true);
    }

    public void initTabWaterfall(final Iterator<String> iterator) {
        TabGroupsMode.groups(player).forEach(group ->
                this.waterfall(new Method(this.getMethodType(group), group.getSuggestions(), null),
                group.getSuggestions(), iterator));
    }

    public void initTabChildren(final Collection<CommandNode<?>> childrens) {
        final Map<String, String> suggestions = new HashMap<>();
        final List<TabGroup> groups = TabGroupsMode.groups(player);

        for (int i = groups.size() - 1; i >= 0; i--) {
            final TabGroup group = groups.get(i);
            suggestions.putAll(this.childrens(new Method(this.getMethodType(group), group.getSuggestions(), null), group.getSuggestions()));
        }

        childrens.removeIf(node -> {
            for(final String suggestion : suggestions.keySet()) {
                if(node.getName().equalsIgnoreCase(suggestion)) return false;
            }
            return true;
        });
    }

    private static Collection<String> enabledGroups() {
        final Collection<String> groups = new HashSet<>();
        final List<String> enabled_groups = Config.MANAGE_TAB_COMPLETE_GROUPS_MODE_ENABLED_GROUPS.getStringList();
        if(enabled_groups.contains("*")) {
            groups.addAll(Config.MANAGE_TAB_COMPLETE_GROUPS_MODE_GROUPS.getSection());
            return groups;
        }
        groups.addAll(enabled_groups);
        return groups;
    }

    public static List<TabGroup> groups(final ProxiedPlayer player) {
        final BetterSecurityBungee plugin = BetterSecurityBungee.getInstance();
        final List<TabGroup> groups = new ArrayList<>();
        final Map<Integer, TabGroup> map = new HashMap<>();
        for(final String egroup : enabledGroups()) {
            final TabGroup group = new TabGroup(plugin, egroup, player, new TabComplete(plugin, player));
            map.put(group.getPriority(), group);
        }
        for(final Integer priority : map.keySet()) groups.add(map.get(priority));
        return groups;
    }

}
