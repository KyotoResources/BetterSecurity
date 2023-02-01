package it.zs0bye.bettersecurity.bukkit.executors;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class SendExecutors {

    @SafeVarargs
    public static void send(final BetterSecurityBukkit plugin, final List<String> executors, final Player player, final Map<String, String>... placeholders) {
        if(executors.isEmpty()) return;
        executors.forEach(executor -> {

            for (Map<String, String> placeholder : placeholders) {
                for (String key : placeholder.keySet())
                    executor = executor.replace(key, placeholder.get(key));
            }

            new ActionExecutor(plugin, executor, player);
            new BossBarExecutor(plugin, executor, player);
            new ConsoleExecutor(plugin, executor, player);
            new MessageExecutor(plugin, executor, player);
            new PlayerExecutor(plugin, executor, player);
            new SoundExecutor(plugin, executor, player);
            new TitleExecutor(plugin, executor, player);
            new MiniMessageExecutor(plugin, executor, player);
        });
    }

}
