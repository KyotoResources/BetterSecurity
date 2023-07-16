package it.zs0bye.bettersecurity.bungee.commands;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.commands.subcmds.AboutSubCMD;
import it.zs0bye.bettersecurity.bungee.commands.subcmds.HelpSubCMD;
import it.zs0bye.bettersecurity.bungee.commands.subcmds.ReloadSubCMD;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainCommand extends Command implements TabExecutor {

    private final BetterSecurityBungee plugin;

    public MainCommand(final BetterSecurityBungee plugin) {
        super("bettersecuritybungee", "", "bsecuritybungee", "securitybungee", "bsbungee", "bsb");
        this.plugin = plugin;
    }

    private boolean checkArgs(final String[] args, final String... commands) {
        return Arrays.stream(commands).anyMatch(args[0]::equalsIgnoreCase);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {

        if(!sender.hasPermission(this.getName() + ".command")) {
            new AboutSubCMD(this.getName(), sender, this.plugin);
            return;
        }

        if(args.length == 0) {
            new HelpSubCMD(this.getName(), sender, this.plugin);
            return;
        }

        if(args.length == 1) {
            new ReloadSubCMD(this.getName(), args, sender, this.plugin);
            new AboutSubCMD(this.getName(), args, sender, this.plugin);

            if(checkArgs(args, "reload", "about")) return;
            new HelpSubCMD(this.getName(), sender,this.plugin);
            return;
        }

        new HelpSubCMD(this.getName(), sender, this.plugin);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {

        final Set<String> completions = new HashSet<>();

        if(!sender.hasPermission(this.getName() + ".command")) return new ArrayList<>();

        if (args.length == 1) {
            new HelpSubCMD(this.getName(), completions, sender);
            new ReloadSubCMD(this.getName(), completions, sender);
            new AboutSubCMD(this.getName(), completions, sender);
        }

        return completions;
    }
}
