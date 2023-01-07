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

import java.util.*;

public class PreventCmdSpamListener implements Listener {

    private final BetterSecurityBungee plugin;
    private final Map<String, String> placeholders = new HashMap<>();

    public PreventCmdSpamListener(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    private static final Map<ProxiedPlayer, Long> cooldowns = new HashMap<>();
    private static final Map<ProxiedPlayer, List<String>> commands = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final ChatEvent event) {

        if(!Config.PREVENT_COMMAND_SPAM_ENABLED.getBoolean()) return;
        if(!event.getMessage().startsWith("/")) return;

        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase()
                .replaceFirst("/", "");

        if (!(event.getSender() instanceof ProxiedPlayer)) return;

        final ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        this.placeholders.put("%command%", "/" + command);
        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%server%", player.getServer().getInfo().getName());

        final int delay = Config.PREVENT_COMMAND_SPAM_DELAY.getInt();

        if(player.hasPermission("bettersecuritybungee.bypass.cmdspam")) return;
        if(this.canBlock(command)) return;

        if(cooldowns.containsKey(player)) {
            final long secondsLeft = ((cooldowns.get(player) / 1000) + delay) - (System.currentTimeMillis() / 1000);
            if(this.punishPlayer(player, command, secondsLeft)) {
                event.setCancelled(Config.PREVENT_COMMAND_SPAM_BLOCK_COMMAND.getBoolean());
                return;
            }
        }

        cooldowns.put(player, System.currentTimeMillis());
    }

    private boolean punishPlayer(final ProxiedPlayer player, final String command, final long seconds) {
        if (seconds > 0) {
            this.placeholders.put("%seconds%", seconds + "");
            if(Config.PREVENT_COMMAND_SPAM_COMMAND_LIMIT.getInt() <= 0) {
                this.sendExecutor(player, command);
                return true;
            }
            this.addCommands(player, command);
            this.placeholders.put("%commands%", commands.get(player).size() + "");
            if (commands.containsKey(player) && commands.get(player).size() < Config.PREVENT_COMMAND_SPAM_COMMAND_LIMIT.getInt()) return true;
            this.sendExecutor(player, command);
            return true;
        }
        cooldowns.remove(player);
        commands.remove(player);
        return false;
    }

    private void sendExecutor(final ProxiedPlayer player, final String command) {
        new Warnings(this.plugin, player, TypeWarning.PREVENT_COMMAND_SPAM, command);
        SendExecutors.send(this.plugin, Config.PREVENT_COMMAND_SPAM_EXECUTORS.getStringList(), player, this.placeholders);
    }

    private void addCommands(final ProxiedPlayer player, final String command) {
        if(!commands.containsKey(player)) {
            commands.put(player, Collections.singletonList(command));
            return;
        }
        final List<String> addcmds = new ArrayList<>(commands.get(player));
        addcmds.add(command);
        commands.put(player, addcmds);
    }

    private boolean canBlock(final String command) {
        final String method = Config.PREVENT_COMMAND_SPAM_METHOD.getString();
        final List<String> commands = Config.PREVENT_COMMAND_SPAM_COMMANDS.getStringList();
        if(commands.isEmpty()) return true;
        if(method.equals("BLACKLIST")) return !commands.contains(command);
        if(method.equals("WHITELIST")) return commands.contains(command);
        return true;
    }

}
