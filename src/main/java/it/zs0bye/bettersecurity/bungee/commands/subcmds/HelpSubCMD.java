package it.zs0bye.bettersecurity.bungee.commands.subcmds;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.commands.BaseCommand;
import it.zs0bye.bettersecurity.bungee.files.enums.Lang;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Set;

public class HelpSubCMD extends BaseCommand {

    private final String command;
    private CommandSender sender;
    private String[] args;
    private BetterSecurityBungee plugin;

    public HelpSubCMD(final String command, final CommandSender sender, final BetterSecurityBungee plugin) {
        this.command = command;
        this.sender = sender;
        this.plugin = plugin;
        this.execute();
    }

    public HelpSubCMD(final String command, final String[] args, final CommandSender sender, final BetterSecurityBungee plugin) {
        this.command = command;
        this.args = args;
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public HelpSubCMD(final String command, final Set<String> tab, final CommandSender sender) {
        this.command = command;
        if(!sender.hasPermission(this.command + ".command." + this.getName())) return;
        tab.add(this.getName());
    }

    public HelpSubCMD(final String command, final Set<String> tab, final String[] args, final CommandSender sender, final BetterSecurityBungee plugin) {
        this.command = command;
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        if(!sender.hasPermission(this.command + ".command." + this.getName())) return;
        if(args.length != 2)  return;
        tab.addAll(Lang.CUSTOM.getSection(Lang.HELP_CATEGORIES.getPath(), this.command, Lang.HELP_PAGES.getPath()));
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

        int page = 1;
        int max = 0;

        if (args != null && args.length == 2
                && !NumberUtils.isDigits(args[1])) {
            Lang.IS_NOT_NUMBER.send(this.sender);
            return;
        }

        if(args != null && args.length == 2)
            page = Integer.parseInt(args[1]);

        for (String pages : Lang.CUSTOM.getSection(
                Lang.HELP_CATEGORIES.getPath(),
                this.command,
                Lang.HELP_PAGES.getPath())) {
            int key = Integer.parseInt(pages);
            if(key > max) {
                max = key;
            }
        }

        String currPage = String.valueOf(page);
        String nextPage = String.valueOf(page + 1);
        String maxPage = String.valueOf(max);

        if(page == max) {
            nextPage = Lang.HELP_PLACEHOLDERS_MAX_PAGE.getCustomString();
        }

        if(!Lang.CUSTOM.contains(Lang.HELP_CATEGORIES.getPath(),
                this.command,
                Lang.HELP_PAGES.getPath(),
                "." + currPage)) {
            Lang.HELP_ERRORS_PAGE_NOT_FOUND.send(this.sender);
            return;
        }

        for(String msg : Lang.CUSTOM.getStringList(
                Lang.HELP_CATEGORIES.getPath(),
                this.command,
                Lang.HELP_PAGES.getPath(),
                "." + currPage)) {
            this.sender.sendMessage(TextComponent.fromLegacyText(msg
                    .replace("%command%", this.command)
                    .replace("%version%", version())
                    .replace("%proxy%", this.plugin.getProxy().getName())
                    .replace("%author%", author())
                    .replace("%page%", currPage)
                    .replace("%nextpage%", nextPage)
                    .replace("%maxpage%", maxPage)));
        }

    }

    private String version() {
        return "BetterSecurity v" + this.plugin.getDescription().getVersion();
    }

    private String author() {
        return "Developed by zS0bye";
    }
}
