package it.zs0bye.bettersecurity.bukkit.hooks;

import com.comphenix.protocol.ProtocolLibrary;
import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;

public class HProtocolLib {

    private final BetterSecurityBukkit plugin;

    public HProtocolLib(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
    }

    public void unregisterAll() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this.plugin);
    }

}