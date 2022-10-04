package it.zs0bye.bettersecurity.hooks.enums;

import it.zs0bye.bettersecurity.BetterSecurity;
import it.zs0bye.bettersecurity.files.enums.Config;
import lombok.Getter;
import org.bukkit.Bukkit;

public enum Hooks {
    PLACEHOLDERAPI("PlaceholderAPI"),
    PROTOCOLLIB("ProtocolLib"),
    VAULT("Vault");

    private final BetterSecurity plugin;
    private final String path;

    @Getter
    private boolean check;

    Hooks(final String path) {
        this.plugin = BetterSecurity.getInstance();
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
