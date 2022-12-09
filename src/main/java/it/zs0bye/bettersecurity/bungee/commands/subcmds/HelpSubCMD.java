package it.zs0bye.bettersecurity.bungee.commands.subcmds;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.commands.BaseCommand;
import it.zs0bye.bettersecurity.bungee.files.enums.Lang;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Set;

public class HelpSubCMD extends BaseCommand {

    private final String command;
    private CommandSender sender;
    private BetterSecurityBungee plugin;

    public HelpSubCMD(final String command, final CommandSender sender, final BetterSecurityBungee plugin) {
        this.command = command;
        this.sender = sender;
        this.plugin = plugin;
        this.execute();
    }

    public HelpSubCMD(final String command, final Set<String> tab, final CommandSender sender) {
        this.command = command;
        if(!sender.hasPermission(this.command + ".command." + this.getName())) return;
        tab.add(this.getName());
    }

    @Override
    protected String getName() {
        return "help";
    }

    @Override
    protected void execute() {

        if(!this.sender.hasPermission(this.command + ".command." + this.getName())) {
            Lang.INSUFFICIENT_PERMISSIONS.send(this.sender);
            return;
        }

        Lang.HELP_TEXTS.getStringList().forEach(text -> this.sender.sendMessage(TextComponent.fromLegacyText(text
                .replace("%command%", this.command)
                .replace("%version%", version())
                .replace("%proxy%", this.plugin.getProxy().getName())
                .replace("%author%", author()))));

    }

    private String version() {
        return "BetterSecurity v" + this.plugin.getDescription().getVersion();
    }

    private String author() {
        return "Developed by zS0bye";
    }
}
