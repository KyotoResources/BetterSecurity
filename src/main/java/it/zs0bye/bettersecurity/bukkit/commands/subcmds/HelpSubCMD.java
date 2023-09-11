/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (C) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.zs0bye.bettersecurity.bukkit.commands.subcmds;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.commands.BaseCommand;
import it.zs0bye.bettersecurity.bukkit.files.readers.Lang;
import it.zs0bye.bettersecurity.common.BetterUser;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public class HelpSubCMD extends BaseCommand {

    private final String command;
    private BetterUser user;
    private String alias;
    private String[] args;
    private BetterSecurityBukkit plugin;

    public HelpSubCMD(final String command, final BetterUser user, final String alias, final BetterSecurityBukkit plugin) {
        this.command = command;
        this.user = user;
        this.alias = alias;
        this.plugin = plugin;
        this.execute();
    }

    public HelpSubCMD(final String command, final String[] args, final BetterUser user, final String alias, final BetterSecurityBukkit plugin) {
        this.command = command;
        this.args = args;
        this.user = user;
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

    public HelpSubCMD(final String command, final List<String> tab, final String[] args, final CommandSender sender) {
        this.command = command;
        if(!args[0].equalsIgnoreCase(getName())) return;
        if(!sender.hasPermission(this.command + ".command." + getName())) return;
        if(args.length == 2) tab.addAll(Lang.HELP_TEXTS.getSection());
    }

    @Override
    protected String getName() {
        return "help";
    }

    @Override
    protected void execute() {

        if(!this.user.hasPermission(this.command + ".command." + getName())) {
            Lang.INSUFFICIENT_PERMISSIONS.send(this.user);
            return;
        }

        int page = 1;
        int max = 0;

        if (args != null && args.length == 2
                && !NumberUtils.isDigits(args[1])) {
            Lang.IS_NOT_NUMBER.send(this.user);
            return;
        }

        if(args != null && args.length == 2) page = NumberUtils.toInt(args[1]);

        for (String pages : Lang.HELP_TEXTS.getSection()) {
            final int key = NumberUtils.toInt(pages);
            if(key > max) max = key;
        }

        String nextPage = String.valueOf(page + 1);

        final String currPage = String.valueOf(page);
        final String maxPage = String.valueOf(max);

        if(page == max) nextPage = Lang.HELP_PLACEHOLDERS_MAX_PAGE.getString();

        if(!Lang.INSTANCE.contains(Lang.HELP_TEXTS.getPath(), "." + currPage)) {
            Lang.HELP_ERRORS_PAGE_NOT_FOUND.send(this.user);
            return;
        }

        String finalNextPage = nextPage;
        Lang.INSTANCE.sendList(this.user,
                new HashMap<String, String>() {{
                    put("%command%", alias);
                    put("%server%", plugin.getServer().getName());
                    put("%plugin%", plugin.getName());
                    put("%version%", "v" + plugin.getDescription().getVersion());
                    put("%author%", author());
                    put("%page%", currPage);
                    put("%nextpage%", finalNextPage);
                    put("%maxpage%", maxPage);
                }},
                Lang.HELP_TEXTS.getPath(), "." + currPage);

    }

    private String author() {
        return "Developed by zS0bye";
    }
}
