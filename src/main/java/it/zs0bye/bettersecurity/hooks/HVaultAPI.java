package it.zs0bye.bettersecurity.hooks;

import it.zs0bye.bettersecurity.BetterSecurity;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class HVaultAPI {

    private final BetterSecurity plugin;
    private Permission api;
    private final Player player;

    public HVaultAPI(final Player player) {
        this.plugin = BetterSecurity.getInstance();
        this.player = player;
        this.check();
    }

    private void check() {
        if (this.setupPermissions()) return;
        this.plugin.getLogger().severe("Disabled due to no Vault dependency found!");
        Bukkit.getPluginManager().disablePlugin(this.plugin);
    }

    private boolean setupPermissions() {
        final RegisteredServiceProvider<Permission> rsp = this.plugin.getServer().getServicesManager().getRegistration(Permission.class);
        if(rsp == null) return false;
        api = rsp.getProvider();
        return true;
    }

    public boolean isPlayerInGroup(final String group) {
        return this.api.playerInGroup(this.player, group);
    }

}
