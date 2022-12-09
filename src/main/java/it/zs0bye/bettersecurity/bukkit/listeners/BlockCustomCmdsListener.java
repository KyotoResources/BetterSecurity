package it.zs0bye.bettersecurity.bukkit.listeners;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.warnings.Warnings;
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.warnings.enums.TypeWarning;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;
import java.util.Map;

public class BlockCustomCmdsListener implements Listener {

    private final BetterSecurityBukkit plugin;
    private final Map<String, String> placeholders;

    private String regCommand;
    private List<String> executors;
    private boolean warning;
    private Permission permission;
    private String permission_required;
    private List<String> required_players;

    public BlockCustomCmdsListener(final BetterSecurityBukkit plugin, final String command) {
        this.plugin = plugin;
        this.placeholders = this.plugin.getCmdsPlaceholders();

        if(!Config.BLOCK_CUSTOM_COMMANDS_ENABLED.getBoolean()) return;

        final String path = Config.BLOCK_CUSTOM_COMMANDS.getPath() + "." + command;
        final String warning_path = path + Config.BLOCK_CUSTOM_COMMANDS_WARNING.getPath();
        final String permission_path = path + Config.BLOCK_CUSTOM_COMMANDS_PERMISSION_REQUIRED.getPath();
        final String required_players_path = path + Config.BLOCK_CUSTOM_COMMANDS_REQUIRED_PLAYERS.getPath();

        this.regCommand = Config.CUSTOM.getString(path + Config.BLOCK_CUSTOM_COMMANDS_COMMAND.getPath());
        this.executors = Config.CUSTOM.getStringList(path + Config.BLOCK_CUSTOM_COMMANDS_EXECUTORS.getPath());

        if(this.contains(warning_path)) this.warning = Config.CUSTOM.getBoolean(warning_path);

        if(this.contains(permission_path)) {
            this.permission_required = Config.CUSTOM.getString(permission_path);
            this.permission = new Permission(this.permission_required, PermissionDefault.OP);
            this.permission.addParent("bettersecurity.bypass.*", true);
        }

        if(this.contains(required_players_path)) this.required_players = Config.CUSTOM.getStringList(required_players_path);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event) {

        if(!Config.BLOCK_CUSTOM_COMMANDS_ENABLED.getBoolean()) return;

        final Player player = event.getPlayer();
        final String command = event.getMessage()
                .split(" ")[0]
                .toLowerCase()
                .replaceFirst("/", "");

        if(this.permission_required != null && player.hasPermission(this.permission)) return;
        if(this.required_players != null && (this.required_players.contains(player.getName()) || this.required_players.contains(player.getUniqueId().toString()))) return;
        if(!this.regCommand.equalsIgnoreCase(command)) return;

        this.placeholders.put("%player%", player.getName());
        this.placeholders.put("%command%", "/" + command);

        SendExecutors.send(this.plugin, this.executors, player, this.placeholders);
        event.setCancelled(true);

        if(!this.warning) return;
        new Warnings(this.plugin, player, TypeWarning.COMMANDS, command);
    }

    private boolean contains(final String path) {
        return Config.CUSTOM.contains(path);
    }

}