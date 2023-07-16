package it.zs0bye.bettersecurity.bungee;

import it.zs0bye.bettersecurity.bungee.commands.MainCommand;
import it.zs0bye.bettersecurity.bungee.files.FileManager;
import it.zs0bye.bettersecurity.bungee.files.enums.Config;
import it.zs0bye.bettersecurity.bungee.files.enums.Lang;
import it.zs0bye.bettersecurity.bungee.hooks.HooksManager;
import it.zs0bye.bettersecurity.bungee.listeners.*;
import it.zs0bye.bettersecurity.common.updater.UpdateType;
import it.zs0bye.bettersecurity.common.updater.VandalUpdater;
import it.zs0bye.bettersecurity.common.utils.enums.ConsoleUtils;
import lombok.Getter;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.util.concurrent.TimeUnit;

@Getter
public class BetterSecurityBungee extends Plugin {

    @Getter
    private static BetterSecurityBungee instance;

    private FileManager configFile;
    private FileManager languagesFile;
    private BungeeAudiences adventure;
    private HooksManager hooks;

    private String updateMsg;

    @Override
    public void onEnable() {

        instance = this;
        this.adventure = BungeeAudiences.create(this);

        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "   ▄▄▄▄   ▓█████▄▄▄█████▓▄▄▄█████▓▓█████  ██▀███" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▓█████▄ ▓█   ▀▓  ██▒ ▓▒▓  ██▒ ▓▒▓█   ▀ ▓██ ▒ ██▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██▒ ▄██▒███  ▒ ▓██░ ▒░▒ ▓██░ ▒░▒███   ▓██ ░▄█ ▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██░█▀  ▒▓█  ▄░ ▓██▓ ░ ░ ▓██▓ ░ ▒▓█  ▄ ▒██▀▀█▄  " + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░▓█  ▀█▓░▒████▒ ▒██▒ ░   ▒██▒ ░ ░▒████▒░██▓ ▒██▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░▒▓███▀▒░░ ▒░ ░ ▒ ░░     ▒ ░░   ░░ ▒░ ░░ ▒▓ ░▒▓░" + ConsoleUtils.RESET);
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "    ██████ ▓█████  ▄████▄   █    ██  ██▀███   ██▓▄▄▄█████▓▓██   ██▓" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██    ▒ ▓█   ▀ ▒██▀ ▀█   ██  ▓██▒▓██ ▒ ██▒▓██▒▓  ██▒ ▓▒ ▒██  ██▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░ ▓██▄   ▒███   ▒▓█    ▄ ▓██  ▒██░▓██ ░▄█ ▒▒██▒▒ ▓██░ ▒░  ▒██ ██░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "    ▒   ██▒▒▓█  ▄ ▒▓▓▄ ▄██▒▓▓█  ░██░▒██▀▀█▄  ░██░░ ▓██▓ ░   ░ ▐██▓░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒██████▒▒░▒████▒▒ ▓███▀ ░▒▒█████▓ ░██▓ ▒██▒░██░  ▒██▒ ░   ░ ██▒▓░" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ▒ ▒▓▒ ▒ ░░░ ▒░ ░░ ░▒ ▒  ░░▒▓▒ ▒ ▒ ░ ▒▓ ░▒▓░░▓    ▒ ░░      ██▒▒▒" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "  ░ ░▒  ░ ░ ░ ░  ░  ░  ▒   ░░▒░ ░ ░   ░▒ ░ ▒░ ▒ ░    ░     ▓██ ░▒░" + ConsoleUtils.RESET);
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Developed by zS0bye" + ConsoleUtils.RESET);
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Current version v" + this.getDescription().getVersion() + " ● Running on " + this.getProxy().getName() + ConsoleUtils.RESET);

        this.loadFiles();
        this.loadHooks();

        this.loadCommands();
        this.loadListeners();

        this.getProxy().registerChannel("bsecurity:sender");

        this.loadUpdater();
        new Metrics(this, 17435);

        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ The Plug-in was started successfully ;)" + ConsoleUtils.RESET);
        this.getLogger().info("");

    }

    @Override
    public void onDisable() {
        if(this.adventure == null) return;
        this.adventure.close();
    }

    private void loadFiles() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Loading resources.." + ConsoleUtils.RESET);

        this.configFile = new FileManager(this, "config", null).saveDefaultConfig();
        this.languagesFile = new FileManager(this, Config.SETTINGS_LOCALE.getString(), "languages")
                .saveDefaultConfig();

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Resources uploaded successfully!" + ConsoleUtils.RESET);
    }

    private void loadCommands() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Registering commands.." + ConsoleUtils.RESET);

        this.getProxy().getPluginManager().registerCommand(this, new MainCommand(this));

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Commands registered successfully!" + ConsoleUtils.RESET);
    }

    private void loadListeners() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Registering events.." + ConsoleUtils.RESET);

        this.getProxy().getPluginManager().registerListener(this, new BlocksCmdsListener(this));
        this.getProxy().getPluginManager().registerListener(this, new PluginMessageListener(this));
        this.getProxy().getPluginManager().registerListener(this, new ManageTabCompleteListener(this));
        this.getProxy().getPluginManager().registerListener(this, new PreventCmdSpamListener(this));
        this.getProxy().getPluginManager().registerListener(this, new CommandsPacketListener(this));

        if(Config.MANAGE_TAB_COMPLETE_WATERFALL_PREVENTION.getBoolean())
            this.getProxy().getPluginManager().registerListener(this, new WaterTabCompleteListener(this));

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Events registered successfully!" + ConsoleUtils.RESET);
    }

    private void loadHooks() {
        this.getLogger().info("");
        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Loading Hooks.." + ConsoleUtils.RESET);

        this.hooks = new HooksManager(this);
//        this.hooks.registerTabPacket();

        this.getLogger().info(ConsoleUtils.YELLOW + "┃ Hooks loaded successfully!" + ConsoleUtils.RESET);
    }

    private void loadUpdater() {
        if(!Config.SETTINGS_CHECK_UPDATE.getBoolean()) return;
        final String resourceId = "105608";
        final VandalUpdater vandalUpdater = new VandalUpdater(resourceId, UpdateType.NORMAL);
        vandalUpdater.setUpdateMessage(Lang.UPDATE_NOTIFICATION.getCustomString());
        this.getProxy().getScheduler().schedule(this, () -> {
            this.updateMsg = vandalUpdater.message(this.getLogger(), this.getDescription().getName(),
                    this.getDescription().getVersion(), null, null);
            this.getProxy().getPluginManager().registerListener(this, new UpdaterListener(this));
        }, 20L, 30 * 60, TimeUnit.SECONDS);
    }


}
