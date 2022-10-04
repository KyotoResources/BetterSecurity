package it.zs0bye.bettersecurity.hooks;

import it.zs0bye.bettersecurity.BetterSecurity;
import it.zs0bye.bettersecurity.files.enums.Config;
import it.zs0bye.bettersecurity.hooks.enums.Hooks;
import it.zs0bye.bettersecurity.protocols.ChatProtocol;
import it.zs0bye.bettersecurity.protocols.TabProtocol;
import it.zs0bye.bettersecurity.utils.StringUtils;
import lombok.Getter;

import org.bukkit.entity.Player;

@Getter
public class HooksManager {

    private final BetterSecurity plugin;

    public HooksManager(final BetterSecurity plugin) {
        this.plugin = plugin;
        this.load();
    }

    public void load() {
        Hooks.PLACEHOLDERAPI.load();
        Hooks.PROTOCOLLIB.load();
        Hooks.VAULT.load();
    }

    public String getPlaceholders(final Player player, final String message) {
        if(!Hooks.PLACEHOLDERAPI.isCheck()) return StringUtils.colorize(message);
        return new HPlaceholderAPI(player).getPlaceholders(message);
    }

    public void regChatProtocol() {
        if(!Hooks.PROTOCOLLIB.isCheck()) return;
        Config.REPLACE_CUSTOM_MESSAGES.getConfigurationSection().forEach(message -> new ChatProtocol(this.plugin, message));
    }

    public void regTabProtocol() {
        if(!Hooks.PROTOCOLLIB.isCheck()) return;
        new TabProtocol(this.plugin);
    }

    public void unregisterAllProtocols() {
        if(!Hooks.PROTOCOLLIB.isCheck()) return;
        new HProtocolLib(this.plugin).unregisterAll();
    }

    public boolean isPlayerInGroup(final Player player, final String group) {
        if(!Hooks.VAULT.isCheck()) return false;
        return new HVaultAPI(player).isPlayerInGroup(group);
    }


}
