package it.zs0bye.bettersecurity.hooks;

import com.comphenix.protocol.ProtocolLibrary;
import it.zs0bye.bettersecurity.BetterSecurity;

public class HProtocolLib {

    private final BetterSecurity plugin;

    public HProtocolLib(final BetterSecurity plugin) {
        this.plugin = plugin;
    }

    public void unregisterAll() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this.plugin);
    }

}