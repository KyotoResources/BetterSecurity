package it.zs0bye.bettersecurity.bungee.listeners;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.Warnings;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.bungee.utils.StringUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.List;

public class BlocksCmdsListener implements Listener {

    private final BetterSecurityBungee plugin;

    public BlocksCmdsListener(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final ChatEvent event) {

        if(!Config.BLOCKS_COMMANDS_ENABLED.getBoolean()) return;

        String command = event.getMessage();

        if(!command.startsWith("/")) return;

        command = command
                .split(" ")[0]
                .toLowerCase()
                .replaceFirst("/", "");

        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if(!Config.BLOCKS_COMMANDS_FORCE_CHECK.getBoolean() && event.isCancelled()) return;

        if(player.hasPermission("bettersecuritybungee.bypass.blockscmds")) return;
        if(this.canBlock(command, player)) return;

        StringUtils.send(player, this.getMessage(command, player));
        event.setCancelled(true);

        if(!Config.BLOCKS_COMMANDS_WARNING.getBoolean()) return;
        new Warnings(this.plugin, player, command).warn();
    }

    private boolean canBlock(final String command, final ProxiedPlayer player) {
        final String method = this.getMethod(command, player);
        final List<String> commands = this.getWhitelistedCommands(player);
        if(commands.isEmpty()) return true;
        if(method.equals("BLACKLIST")) return !commands.contains(command);
        if(method.equals("WHITELIST")) return commands.contains(command);
        return true;
    }

    private List<String> getWhitelistedCommands(final ProxiedPlayer player) {
        final List<String> commands = new ArrayList<>(Config.BLOCKS_COMMANDS.getStringList());

        if(!Config.BLOCKS_COMMANDS_SERVER_MODE_ENABLED.getBoolean()) return commands;
        final String path = this.getPath(player);
        commands.addAll(Config.CUSTOM.getStringList(path + Config.BLOCKS_COMMANDS_SERVER_MODE_COMMANDS.getPath()));
        return commands;
    }

    private String getMethod(final String command, final ProxiedPlayer player) {
        final String method = Config.BLOCKS_COMMANDS_METHOD.getString();

        if(!Config.BLOCKS_COMMANDS_SERVER_MODE_ENABLED.getBoolean()) return method;
        final String path = this.getPath(player);
        final List<String> commands = Config.CUSTOM.getStringList(path + Config.BLOCKS_COMMANDS_SERVER_MODE_COMMANDS.getPath());
        if(!commands.contains(command)) return method;

        return Config.CUSTOM.getString(path + Config.BLOCKS_COMMANDS_SERVER_MODE_METHOD.getPath());
    }

    private String getMessage(final String command, final ProxiedPlayer player) {
        final String message = Config.BLOCKS_COMMANDS_MESSAGE.getCustomString();

        if(!Config.BLOCKS_COMMANDS_SERVER_MODE_ENABLED.getBoolean()) return message;
        final String path = this.getPath(player);
        final List<String> commands = Config.CUSTOM.getStringList(path + Config.BLOCKS_COMMANDS_SERVER_MODE_COMMANDS.getPath());
        if(!commands.contains(command)) return message;

        if(!Config.CUSTOM.contains(path + Config.BLOCKS_COMMANDS_SERVER_MODE_MESSAGE.getPath())) return message;
        return Config.CUSTOM.getCustomString(path + Config.BLOCKS_COMMANDS_SERVER_MODE_MESSAGE.getPath());
    }

    private String getPath(final ProxiedPlayer player) {
        final String server = player.getServer().getInfo().getName();
        return Config.BLOCKS_COMMANDS_SERVER_MODE_SERVERS.getPath() + "." + server;
    }

}
