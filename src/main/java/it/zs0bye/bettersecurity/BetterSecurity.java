package it.zs0bye.bettersecurity;

import it.zs0bye.bettersecurity.checks.VersionCheck;
import it.zs0bye.bettersecurity.commands.MainCommand;
import it.zs0bye.bettersecurity.files.FileManager;
import it.zs0bye.bettersecurity.files.enums.Config;
import it.zs0bye.bettersecurity.files.enums.Lang;
import it.zs0bye.bettersecurity.hooks.HooksManager;
import it.zs0bye.bettersecurity.utils.enums.ConsoleUtils;
import it.zs0bye.bettersecurity.listeners.*;
import lombok.Getter;
import me.onlyfire.vandalupdater.UpdateType;
import me.onlyfire.vandalupdater.VandalUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class BetterSecurity extends JavaPlugin {

    @Getter
    private static BetterSecurity instance;

    private FileManager configFile;
    private FileManager languagesFile;

    private HooksManager hooks;

    private final Map<String, String> cmdsPlaceholders = new HashMap<>();

    @Override
    public void onEnable() {

        instance = this;

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
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Current version v" + this.getDescription().getVersion() + " ● Running the free version for " + this.getServer().getName() + ConsoleUtils.RESET);

        this.loadFiles();

        this.loadHooks();

        this.loadCommands();
        this.loadListeners();

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

        this.registerListeners();

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Events registered successfully!" + ConsoleUtils.RESET);
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BlockSyntaxListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlocksCmdsListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CmdsOnlyConsoleListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CmdsOnlyPlayersListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PermissionPreventionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PortBypassPreventionListener(), this);

        Config.BLOCK_CUSTOM_COMMANDS.getConfigurationSection().forEach(command ->
                Bukkit.getPluginManager().registerEvents(new BlockCustomCmdsListener(this, command), this));

        if(VersionCheck.legacy()) return;
        Bukkit.getPluginManager().registerEvents(new BlockTabCompleteListener(), this);
    }

    private void loadUpdater() {
        if(!Config.SETTINGS_CHECK_UPDATE_ENABLED.getBoolean()) return;
        final String resourceId = "105608";
        final UpdateType updateType = UpdateType.valueOf(Config.SETTINGS_CHECK_UPDATE_TYPE.getString());
        final VandalUpdater vandalUpdater = new VandalUpdater(this, resourceId, updateType);
        vandalUpdater.setUpdateMessage(Lang.UPDATE_NOTIFICATION.getCustomString());
        vandalUpdater.runTaskTimerAsynchronously(this, 20L, 30 * 60 * 20L);
    }

    public void registerProtocols() {
        this.hooks.regChatProtocol();
        this.hooks.regTabProtocol();
    }

    private void registerPlaceholders() {
        this.cmdsPlaceholders.put("%unknown_command%", this.getServer().spigot().getConfig().getString("messages.unknown-command"));
    }

}
