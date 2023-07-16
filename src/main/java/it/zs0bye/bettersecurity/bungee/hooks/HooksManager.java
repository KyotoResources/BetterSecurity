package it.zs0bye.bettersecurity.bungee.hooks;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.hooks.enums.Hooks;
import lombok.Getter;

@Getter
public class HooksManager {

    private final BetterSecurityBungee plugin;

    public HooksManager(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    public void registerTabPacket() {
        if(!Hooks.PROTOCOLIZE.load()) return;
        new HProtocolize(this.plugin);
    }

}
