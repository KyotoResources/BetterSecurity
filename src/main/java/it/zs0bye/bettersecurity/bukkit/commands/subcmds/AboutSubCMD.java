package it.zs0bye.bettersecurity.bukkit.commands.subcmds;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.commands.BaseCommand;
import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
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
    private CommandSender sender;

    public AboutSubCMD(final String command, final String[] args, final CommandSender sender, final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.command = command;
        this.sender = sender;

        if(!args[0].equalsIgnoreCase(getName())) return;
        this.execute();
    }

    public AboutSubCMD(final String command, final CommandSender sender, final BetterSecurityBukkit plugin) {
        this.plugin = plugin;
        this.command = command;
        this.sender = sender;
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

        StringUtils.image(this.sender, this.getLegacyText());

        try {
            Class.forName("org.bukkit.command.CommandSender$Spigot");
            this.sender.spigot().sendMessage(this.getClickableLink());
        } catch (final ClassNotFoundException e) {
            this.sender.sendMessage(CStringUtils.center("§e§nhttps://ds.kyotoresources.space"));
        }

        this.sender.sendMessage("");

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
