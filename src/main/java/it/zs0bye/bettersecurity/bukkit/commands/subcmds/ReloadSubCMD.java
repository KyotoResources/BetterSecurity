package it.zs0bye.bettersecurity.bukkit.commands.subcmds;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.commands.BaseCommand;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.files.enums.Lang;
import it.zs0bye.bettersecurity.bukkit.hooks.HooksManager;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import java.util.List;

public class ReloadSubCMD extends BaseCommand {

    private final String command;
    private CommandSender sender;
    private BetterSecurityBukkit plugin;
    private HooksManager hooks;

    public ReloadSubCMD(final String command, final String[] args, final CommandSender sender, final BetterSecurityBukkit plugin) {
        this.command = command;
        this.sender = sender;
        this.plugin = plugin;
        this.hooks = this.plugin.getHooks();
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public ReloadSubCMD(final String command, final List<String> tab, final CommandSender sender) {
        this.command = command;
        if(!sender.hasPermission(this.command + ".command." + this.getName())) return;
        tab.add(this.getName());
    }

    @Override
    protected String getName() {
        return "reload";
    }

    @Override
    protected void execute() {
        if(!this.sender.hasPermission(this.command + ".command." + this.getName())) {
            Lang.INSUFFICIENT_PERMISSIONS.send(this.sender);
            return;
        }

        if(this.plugin.getConfigFile().reload()) {
            for (final Config config : Config.values()) config.reloadConfig();
        }

        if(this.plugin.getLanguagesFile().reload()) {
            for(final Lang lang : Lang.values()) lang.reloadConfig();
        }

        HandlerList.unregisterAll(this.plugin);
        this.plugin.registerListeners();

        this.hooks.unregisterAllProtocols();
        this.plugin.registerProtocols();

        Lang.RELOAD_CONFIGURATIONS.send(this.sender);
    }

}
