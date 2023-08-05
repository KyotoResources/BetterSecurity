package it.zs0bye.bettersecurity.bungee.tabcomplete;

import it.zs0bye.bettersecurity.bungee.tabcomplete.methods.Method;
import it.zs0bye.bettersecurity.bungee.tabcomplete.methods.MethodType;
import net.md_5.bungee.api.plugin.Cancellable;

import java.util.*;

public abstract class TabProviders {

    protected Set<String> legacy(final MethodType methodType, final String completion, final Set<String> newsuggestions, final List<String> oldsuggestions, final List<String> commands, final Cancellable cancelled) {
        String firstcompletion = completion.contains(" ") ? completion.split(" ")[0] : completion;
        firstcompletion = firstcompletion.replaceFirst("/", "");
        final Method method = new Method(methodType, oldsuggestions, firstcompletion);
        for(final String suggestion : oldsuggestions) {

            if (methodType == MethodType.BLACKLIST && method.contains()) {
                commands.clear();
                cancelled.setCancelled(true);
            }

            if (!completion.startsWith("/")) break;
            if (!completion.contains(" ")) cancelled.setCancelled(false);
            if (completion.startsWith("/" + suggestion) || completion.contains(" ")) {
                if (methodType == MethodType.WHITELIST && method.contains()) cancelled.setCancelled(false);
                break;
            }

            this.childrens(true, method, newsuggestions, suggestion);
        }

        return newsuggestions;
    }

    protected Set<String> childrens(final Method method, final Set<String> newsuggestions, final List<String> oldsuggestions) {
        for (String suggestion : oldsuggestions) {
            this.childrens(false, method, newsuggestions, suggestion);
        }
        return newsuggestions;
    }

    protected void childrens(final boolean legacy, final Method method, final Set<String> newsuggestions, String suggestion) {
        method.setCommand(suggestion);
        suggestion = legacy ? "/" + suggestion : suggestion;
        if(!method.contains()) return;
        if (method.getType() == MethodType.BLACKLIST && method.contains() || newsuggestions.contains(suggestion)) {
            newsuggestions.remove(suggestion);
            return;
        }
        newsuggestions.add(suggestion);
    }

}
