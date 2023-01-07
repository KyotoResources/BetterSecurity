package it.zs0bye.bettersecurity.bungee.executors;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.Map;

public class SendExecutors {

    @SafeVarargs
    public static void send(final BetterSecurityBungee plugin, final List<String> executors, final ProxiedPlayer player, final Map<String, String>... placeholders) {
        if(executors.isEmpty()) return;
        executors.forEach(executor -> {

            for (Map<String, String> placeholder : placeholders) {
                for (String key : placeholder.keySet())
                    executor = executor.replace(key, placeholder.get(key));
            }

            new KickExecutor(executor, player);
            new ActionExecutor(plugin, executor, player);
            new ConsoleExecutor(plugin, executor, player);
            new MessageExecutor(plugin, executor, player);
            new PlayerExecutor(plugin, executor, player);
            new TitleExecutor(plugin, executor, player);
        });
    }

}
