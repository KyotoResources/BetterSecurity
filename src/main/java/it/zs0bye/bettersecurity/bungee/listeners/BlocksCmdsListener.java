package it.zs0bye.bettersecurity.bungee.listeners;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.executors.SendExecutors;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.bungee.warnings.Warnings;
import it.zs0bye.bettersecurity.bungee.warnings.enums.TypeWarning;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlocksCmdsListener implements Listener {

    private final BetterSecurityBungee plugin;
    private final Map<String, String> placeholders = new HashMap<>();

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

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%server%", player.getServer().getInfo().getName());
        this.placeholders.put("%command%", "/" + command);

        if(!Config.BLOCKS_COMMANDS_FORCE_CHECK.getBoolean() && event.isCancelled()) return;

        if(player.hasPermission("bettersecuritybungee.bypass.blockscmds")) return;
        if(this.canBlock(command, player)) return;

        this.sendExecutors(command, player);
        event.setCancelled(true);

        if(!Config.BLOCKS_COMMANDS_WARNING.getBoolean()) return;
        new Warnings(this.plugin, player, TypeWarning.COMMANDS, command);
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

    private void sendExecutors(final String command, final ProxiedPlayer player) {

        if(!Config.BLOCKS_COMMANDS_SERVER_MODE_ENABLED.getBoolean()) {
            this.sendDefaultExecutor(player);
            return;
        }

        final String path = this.getPath(player);
        final List<String> commands = Config.CUSTOM.getStringList(path + Config.BLOCKS_COMMANDS_SERVER_MODE_COMMANDS.getPath());
        if(!commands.contains(command)) return;

        if(!Config.CUSTOM.contains(path + Config.BLOCKS_COMMANDS_SERVER_MODE_EXECUTORS.getPath())) {
            this.sendDefaultExecutor(player);
            return;
        }
        SendExecutors.send(this.plugin, Config.CUSTOM.getStringList(path + Config.BLOCKS_COMMANDS_SERVER_MODE_EXECUTORS.getPath()),
                player,
                this.placeholders);
    }

    private void sendDefaultExecutor(final ProxiedPlayer player) {
        SendExecutors.send(this.plugin, Config.BLOCKS_COMMANDS_EXECUTORS.getStringList(), player, this.placeholders);
    }

    private String getPath(final ProxiedPlayer player) {
        final String server = player.getServer().getInfo().getName();
        return Config.BLOCKS_COMMANDS_SERVER_MODE_SERVERS.getPath() + "." + server;
    }

}
