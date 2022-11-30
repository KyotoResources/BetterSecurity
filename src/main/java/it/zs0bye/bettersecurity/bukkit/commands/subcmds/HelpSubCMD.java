package it.zs0bye.bettersecurity.bukkit.commands.subcmds;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.commands.BaseCommand;
import it.zs0bye.bettersecurity.bukkit.files.enums.Lang;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpSubCMD extends BaseCommand {

    private final String command;
    private CommandSender sender;
    private String alias;
    private String[] args;
    private BetterSecurityBukkit plugin;

    public HelpSubCMD(final String command, final CommandSender sender, final String alias, final BetterSecurityBukkit plugin) {
        this.command = command;
        this.sender = sender;
        this.alias = alias;
        this.plugin = plugin;
        this.execute();
    }

    public HelpSubCMD(final String command, final String[] args, final CommandSender sender, final String alias, final BetterSecurityBukkit plugin) {
        this.command = command;
        this.args = args;
        this.sender = sender;
        this.alias = alias;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(getName())) return;
        this.execute();
    }

    public HelpSubCMD(final String command, List<String> tab, final CommandSender sender) {
        this.command = command;
        if(!sender.hasPermission(this.command + ".command." + getName())) return;
        tab.add(getName());
    }

    public HelpSubCMD(final String command, final List<String> tab, final String[] args, final CommandSender sender, final BetterSecurityBukkit plugin) {
        this.command = command;
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(getName())) return;
        if(!sender.hasPermission(this.command + ".command." + getName())) return;
        if(args.length == 2)
            tab.addAll(Lang.HELP_TEXTS.getConfigurationSection());
    }

    @Override
    protected String getName() {
        return "help";
    }

    @Override
    protected void execute() {

        if(!sender.hasPermission(this.command + ".command." + getName())) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        int page = 1;
        int max = 0;

        if (args != null && args.length == 2
                && !NumberUtils.isDigits(args[1])) {
            Lang.IS_NOT_NUMBER.send(sender);
            return;
        }

        if(args != null && args.length == 2) page = NumberUtils.toInt(args[1]);

        for (String pages : Lang.HELP_TEXTS.getConfigurationSection()) {
            final int key = NumberUtils.toInt(pages);
            if(key > max) max = key;
        }

        String nextPage = (page + 1) + "";

        final String currPage = page + "";
        final String maxPage = max + "";

        if(page == max) nextPage = Lang.HELP_PLACEHOLDERS_MAX_PAGE.getString();

        if(!Lang.CUSTOM.contains(Lang.HELP_TEXTS.getPath(), "." + currPage)) {
            Lang.HELP_ERRORS_PAGE_NOT_FOUND.send(sender);
            return;
        }

        for(String msg : Lang.CUSTOM.getStringList(Lang.HELP_TEXTS.getPath(), "." + currPage)) {
            sender.sendMessage(msg
                    .replace("%command%", this.alias)
                    .replace("%server%", this.plugin.getServer().getName())
                    .replace("%version%", this.version())
                    .replace("%author%", this.author())
                    .replace("%page%", currPage)
                    .replace("%nextpage%", nextPage)
                    .replace("%maxpage%", maxPage));
        }

    }

    private String version() {
        return "BetterSecurity v" + this.plugin.getDescription().getVersion();
    }

    private String author() {
        return "Developed by zS0bye";
    }
}
