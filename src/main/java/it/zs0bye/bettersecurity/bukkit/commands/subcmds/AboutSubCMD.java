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
import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class AboutSubCMD extends BaseCommand {

    private final String command;
    private BetterSecurityBukkit plugin;
    private BetterUser user;

    public AboutSubCMD(final String command, final String[] args, final BetterUser user, final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.command = command;
        this.user = user;

        if(!args[0].equalsIgnoreCase(getName())) return;
        this.execute();
    }

    public AboutSubCMD(final String command, final BetterUser user, final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.command = command;
        this.user = user;
        this.execute();
    }

    public AboutSubCMD(final String command, final List<String> tab, final CommandSender sender) {
        this.command = command;
        if(!sender.hasPermission(this.command + ".command." + getName())) return;
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "about";
    }

    @Override
    protected void execute() {

        final CommandSender sender = (CommandSender) this.user.getSender();
        StringUtils.image(sender, this.getLegacyText());

        try {
            Class.forName("org.bukkit.command.CommandSender$Spigot");
            sender.spigot().sendMessage(this.getClickableLink());
        } catch (final ClassNotFoundException e) {
            sender.sendMessage(CStringUtils.center("§e§nhttps://ds.kyotoresources.space"));
        }

        sender.sendMessage("");

    }

    private String[] getLegacyText() {
        final List<String> text = Arrays.asList(
                "",
                "&6&lBETTER SECURITY &8‑ v" + this.plugin.getDescription().getVersion(),
                "",
                "&7Rate our service by giving us",
                "&7a positive review &e✭✭✭✭✭&7!",
                "",
                "&e● &7Developed by &f&nzS0bye&r",
                "");
        return text.toArray(new String[0]);
    }

    @SuppressWarnings("deprecation")
    private TextComponent getClickableLink() {
        final TextComponent text = new TextComponent(CStringUtils.center("§7‹ §e● §7› §f§l§nCLICK TO OPEN DISCORD SUPPORT§r §7‹ §e● §7›"));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6§nClick Me!").create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://ds.kyotoresources.space"));
        return text;
    }

}
