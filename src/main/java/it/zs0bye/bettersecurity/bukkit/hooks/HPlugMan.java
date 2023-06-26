package it.zs0bye.bettersecurity.bukkit.hooks;

import com.rylinaux.plugman.PlugMan;
import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import lombok.SneakyThrows;

public class HPlugMan {

    private final BetterSecurityBukkit plugin;

    public HPlugMan(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.register();
    }

    @SneakyThrows
    public void register() {
        final PlugMan plugman = PlugMan.getInstance();
        if(plugman == null) return;
        if(plugman.getIgnoredPlugins() == null) return;
        plugman.getIgnoredPlugins().add(this.plugin.getName());
    }

}
