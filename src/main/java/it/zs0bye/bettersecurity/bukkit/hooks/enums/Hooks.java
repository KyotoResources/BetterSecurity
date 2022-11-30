package it.zs0bye.bettersecurity.bukkit.hooks.enums;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import lombok.Getter;
import org.bukkit.Bukkit;

public enum Hooks {
    PLACEHOLDERAPI("PlaceholderAPI"),
    PROTOCOLLIB("ProtocolLib");

    private final BetterSecurityBukkit plugin;
    private final String path;

    @Getter
    private boolean check;

    Hooks(final String path) {
        this.plugin = BetterSecurityBukkit.getInstance();
        this.path = path;
    }

    public void load() {
        if(!Config.valueOf("SETTINGS_HOOKS_" + path.toUpperCase()).getBoolean()) {
            check = false;
            return;
        }

        if (Bukkit.getPluginManager().getPlugin(path) != null) {
            check = true;
            return;
        }

        this.plugin.getLogger().severe("Could not find " + path + " This plugin is required.");
        Bukkit.getPluginManager().disablePlugin(this.plugin);
        check = false;
    }
}
