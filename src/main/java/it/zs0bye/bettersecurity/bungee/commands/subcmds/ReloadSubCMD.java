package it.zs0bye.bettersecurity.bungee.commands.subcmds;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.commands.BaseCommand;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.bungee.files.enums.Lang;
import net.md_5.bungee.api.CommandSender;

import java.util.Set;

public class ReloadSubCMD extends BaseCommand {

    private final String command;
    private CommandSender sender;
    private BetterSecurityBungee plugin;

    public ReloadSubCMD(final String command, final String[] args, final CommandSender sender, final BetterSecurityBungee plugin) {
        this.command = command;
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public ReloadSubCMD(final String command, final Set<String> tab, final CommandSender sender) {
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

        Lang.RELOAD_CONFIGURATIONS.send(this.sender);
    }

}
