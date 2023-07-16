package it.zs0bye.bettersecurity.bungee.tabcomplete;

import it.zs0bye.bettersecurity.bungee.tabcomplete.methods.Method;
import it.zs0bye.bettersecurity.bungee.tabcomplete.methods.MethodType;
import net.md_5.bungee.api.plugin.Cancellable;

import java.util.*;

public abstract class TabProviders {

    protected List<String> legacy(final Method method, final String completion, final List<String> suggestions, final List<String> commands, final Cancellable cancelled) {
        final List<String> newsuggestions = new ArrayList<>();
        suggestions.forEach(suggestion->  {
            if (method.get()) {
                if (!completion.startsWith("/" + suggestion)) return;
                commands.clear();
                cancelled.setCancelled(true);
            }

            if (!completion.startsWith("/") || completion.contains(" ")) return;
            if (completion.startsWith("/" + suggestion)) return;
            commands.clear();

            if (newsuggestions.contains(suggestion)) return;
            newsuggestions.add("/" + suggestion);
        });
        return newsuggestions;
    }

    protected void waterfall(final Method method, final List<String> suggestions, final Iterator<String> iterator) {
        while (iterator.hasNext()) {
            final String command = iterator.next();
            method.setCommand(command);
            if ((method.get() && !suggestions.contains(command))
                    || (!method.get() && suggestions.contains(command))) return;
            iterator.remove();
        }
    }

    protected Map<String, String> childrens(final Method method, final List<String> suggestions) {
        final Map<String, String> newsuggestions = new HashMap<>();
        for (final String suggestion : suggestions) {
            if (method.getType() == MethodType.BLACKLIST || newsuggestions.containsKey(suggestion)) {
                newsuggestions.remove(suggestion);
                break;
            }
            newsuggestions.put(suggestion, "");
        }
        return newsuggestions;
    }

}
