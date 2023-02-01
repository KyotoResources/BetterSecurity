package it.zs0bye.bettersecurity.bukkit;

import it.zs0bye.bettersecurity.bukkit.commands.MainCommand;
import it.zs0bye.bettersecurity.bukkit.executors.SendExecutors;
import it.zs0bye.bettersecurity.bukkit.files.FileManager;
import it.zs0bye.bettersecurity.bukkit.files.SpigotFile;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.files.enums.Lang;
import it.zs0bye.bettersecurity.bukkit.hooks.HooksManager;
import it.zs0bye.bettersecurity.bukkit.listeners.*;
import it.zs0bye.bettersecurity.bukkit.utils.VersionUtils;
import it.zs0bye.bettersecurity.common.updater.UpdateType;
import it.zs0bye.bettersecurity.common.updater.VandalUpdater;
import it.zs0bye.bettersecurity.common.utils.enums.ConsoleUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class BetterSecurityBukkit extends JavaPlugin implements PluginMessageListener {

    @Getter
    private static BetterSecurityBukkit instance;

    private FileManager configFile;
    private FileManager languagesFile;
    private SpigotFile spigotFile;
    private BukkitAudiences adventure;

    private HooksManager hooks;

    private String updateMsg;

    private final Map<String, String> cmdsPlaceholders = new HashMap<>();

    @Override
    public void onEnable() {

        instance = this;
        this.adventure = BukkitAudiences.create(this);

        this.getLogger().info(ConsoleUtils.RESET + "");
        this.getLogger().info(ConsoleUtils.YELLOW + "   ▄▄▄▄   ▓█████▄▄▄█████▓▄▄▄█████▓▓█████  ██▀███" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▓█████▄ ▓█   ▀▓  ██▒ ▓▒▓  ██▒ ▓▒▓█   ▀ ▓██ ▒ ██▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██▒ ▄██▒███  ▒ ▓██░ ▒░▒ ▓██░ ▒░▒███   ▓██ ░▄█ ▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██░█▀  ▒▓█  ▄░ ▓██▓ ░ ░ ▓██▓ ░ ▒▓█  ▄ ▒██▀▀█▄  " + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░▓█  ▀█▓░▒████▒ ▒██▒ ░   ▒██▒ ░ ░▒████▒░██▓ ▒██▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░▒▓███▀▒░░ ▒░ ░ ▒ ░░     ▒ ░░   ░░ ▒░ ░░ ▒▓ ░▒▓░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "    ██████ ▓█████  ▄████▄   █    ██  ██▀███   ██▓▄▄▄█████▓▓██   ██▓" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██    ▒ ▓█   ▀ ▒██▀ ▀█   ██  ▓██▒▓██ ▒ ██▒▓██▒▓  ██▒ ▓▒ ▒██  ██▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░ ▓██▄   ▒███   ▒▓█    ▄ ▓██  ▒██░▓██ ░▄█ ▒▒██▒▒ ▓██░ ▒░  ▒██ ██░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "    ▒   ██▒▒▓█  ▄ ▒▓▓▄ ▄██▒▓▓█  ░██░▒██▀▀█▄  ░██░░ ▓██▓ ░   ░ ▐██▓░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██████▒▒░▒████▒▒ ▓███▀ ░▒▒█████▓ ░██▓ ▒██▒░██░  ▒██▒ ░   ░ ██▒▓░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒ ▒▓▒ ▒ ░░░ ▒░ ░░ ░▒ ▒  ░░▒▓▒ ▒ ▒ ░ ▒▓ ░▒▓░░▓    ▒ ░░      ██▒▒▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░ ░▒  ░ ░ ░ ░  ░  ░  ▒   ░░▒░ ░ ░   ░▒ ░ ▒░ ▒ ░    ░     ▓██ ░▒░" + ConsoleUtils.RESET);
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Developed by zS0bye" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Current version v" + this.getDescription().getVersion() + " ● Running on " + this.getServer().getName() + ConsoleUtils.RESET);

        this.loadFiles();

        this.loadHooks();

        this.loadCommands();
        this.loadListeners();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "bsecurity:sender");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "bsecurity:return", this);

        this.registerPlaceholders();

        this.loadUpdater();
        new Metrics(this, 16569);

        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ The Plug-in was started successfully ;)" + ConsoleUtils.RESET);
        this.getLogger().info("");

    }

    private void loadFiles() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Loading resources.." + ConsoleUtils.RESET);

        this.spigotFile = new SpigotFile(this);
        this.configFile = new FileManager(this, "config", null).saveDefaultConfig();
        this.languagesFile = new FileManager(this, Config.SETTINGS_LOCALE.getString(), "languages")
                .saveDefaultConfig();

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Resources uploaded successfully!" + ConsoleUtils.RESET);
    }

    private void loadHooks() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Loading Hooks.." + ConsoleUtils.RESET);

        this.hooks = new HooksManager(this);
        this.registerProtocols();

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Hooks loaded successfully!" + ConsoleUtils.RESET);
    }

    @SuppressWarnings("all")
    private void loadCommands() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Registering commands.." + ConsoleUtils.RESET);

        this.getCommand("bettersecurity").setExecutor(new MainCommand(this));
        this.getCommand("bettersecurity").setTabCompleter(new MainCommand(this));

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Commands registered successfully!" + ConsoleUtils.RESET);
    }

    private void loadListeners() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Registering events.." + ConsoleUtils.RESET);

        new TabComplete();
        this.registerListeners();

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Events registered successfully!" + ConsoleUtils.RESET);
    }

    public void registerListeners() {
        new BlocksCmdsListener(this);
        new BlockSyntaxListener(this);
        new CmdsOnlyConsoleListener(this);
        new CmdsOnlyPlayersListener(this);
        new UnknownCommandListener(this);
        new BlockCustomCmdsListener(this);

        this.getServer().getPluginManager().registerEvents(new PermissionPreventionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PortBypassPreventionListener(this), this);

        if(VersionUtils.legacy()) return;
        this.getServer().getPluginManager().registerEvents(new BlockTabCompleteListener(), this);
    }

    private void loadUpdater() {
        if(!Config.SETTINGS_CHECK_UPDATE_ENABLED.getBoolean()) return;
        final String resourceId = "105608";
        final UpdateType updateType = UpdateType.valueOf(Config.SETTINGS_CHECK_UPDATE_TYPE.getString());
        final VandalUpdater vandalUpdater = new VandalUpdater(resourceId, updateType);
        vandalUpdater.setUpdateMessage(Lang.UPDATE_NOTIFICATION.getCustomString());
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            this.updateMsg = vandalUpdater.message(this.getLogger(), this.getName(),
                    this.getDescription().getVersion(), this.getServer().getUpdateFolderFile(), this.getDataFolder());
            this.getServer().getPluginManager().registerEvents(new UpdaterListener(this), this);
        }, 20L, 30 * 60 * 20L);
    }

    public void registerProtocols() {
        this.hooks.regChatProtocol();
        this.hooks.regTabProtocol();
    }

    private void registerPlaceholders() {
        this.cmdsPlaceholders.put("%unknown_command%", this.spigotFile.getConfig().getString("messages.unknown-command"));
    }

    @SneakyThrows
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {

        if(!Config.WARNINGS_PROXY.getBoolean()) return;
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        final String subChannel = in.readUTF();
        this.readCMDChannel(subChannel, in);
        this.readPBPChannel(subChannel, in);
    }

    @SneakyThrows
    private void readCMDChannel(final String subChannel, final DataInputStream in) {
        if (!subChannel.equalsIgnoreCase("WarningCMD")) return;

        final String player = in.readUTF();
        final String command = in.readUTF();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%player%", player);
        placeholders.put("%command%", command);

        Bukkit.getOnlinePlayers().forEach(players -> {
            if(!players.hasPermission("bettersecurity.broadcast.warnings")) return;
            SendExecutors.send(this, Config.WARNINGS_FORMATS_CMDS_FORMAT.getStringList(), players, placeholders);
        });
    }

    @SneakyThrows
    private void readPBPChannel(final String subChannel, final DataInputStream in) {
        if (!subChannel.equalsIgnoreCase("WarningPBP")) return;

        final String player = in.readUTF();
        final String port = in.readUTF();
        final String ip = in.readUTF();

        final Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%player%", player);
        placeholders.put("%port%", port);
        placeholders.put("%ip%", ip);

        Bukkit.getOnlinePlayers().forEach(players -> {
            if(!players.hasPermission("bettersecurity.broadcast.warnings")) return;
            SendExecutors.send(this, Config.WARNINGS_FORMATS_PBP_FORMAT.getStringList(), players, placeholders);
        });
    }

}
