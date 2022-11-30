package it.zs0bye.bettersecurity.bukkit.commands;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.commands.subcmds.AboutSubCMD;
import it.zs0bye.bettersecurity.bukkit.commands.subcmds.HelpSubCMD;
import it.zs0bye.bettersecurity.bukkit.commands.subcmds.ReloadSubCMD;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final BetterSecurityBukkit plugin;
    private final String command;

    public MainCommand(final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.command = "bettersecurity";
    }

    private boolean checkArgs(final String args, final String check) {
        return !args.equalsIgnoreCase(check);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String commandLabel, String[] args) {

        if(!cmd.getName().equalsIgnoreCase(this.command)) return true;

        if(!sender.hasPermission(this.command + ".command")) {
            new AboutSubCMD(this.command, sender, this.plugin);
            return true;
        }

        if(args.length == 0) {
            new HelpSubCMD(this.command, sender, commandLabel, this.plugin);
            return true;
        }

        if(args.length == 1) {
            new ReloadSubCMD(this.command, args, sender, this.plugin);
            new AboutSubCMD(this.command, args, sender, this.plugin);

            if(checkArgs(args[0], "reload")
                    && checkArgs(args[0], "about"))
                new HelpSubCMD(this.command, sender, commandLabel, this.plugin);

            return true;
        }

        if(args.length == 2) {
            new HelpSubCMD(this.command, args, sender, commandLabel, this.plugin);

            if(checkArgs(args[0], "help"))
                new HelpSubCMD(this.command, sender, commandLabel, this.plugin);

            return true;
        }

        new HelpSubCMD(this.command, sender, commandLabel, this.plugin);

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if(!cmd.getName().equalsIgnoreCase(this.command)) return new ArrayList<>();

        if(!sender.hasPermission(this.command + ".command")) return new ArrayList<>();

        if (args.length == 1) {
            new HelpSubCMD(this.command, commands, sender);
            new ReloadSubCMD(this.command, commands, sender);
            new AboutSubCMD(this.command, commands, sender);
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        if(args.length == 2) {
            new HelpSubCMD(this.command, commands, args, sender, this.plugin);
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
