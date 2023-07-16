package it.zs0bye.bettersecurity.bungee.hooks.enums;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;

public enum Hooks {
    PROTOCOLIZE("Protocolize");

    private final BetterSecurityBungee plugin;
    private final String path;

    Hooks(final String path) {
        this.plugin = BetterSecurityBungee.getInstance();
        this.path = path;
    }

    public boolean load() {
        return this.plugin.getProxy().getPluginManager().getPlugin(this.path) != null;
    }
}
