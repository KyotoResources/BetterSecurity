package it.zs0bye.bettersecurity.files;

import it.zs0bye.bettersecurity.BetterSecurity;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public class SpigotFile {

    private final BetterSecurity plugin;
    private final File file;

    public SpigotFile(final BetterSecurity plugin) {
        this.plugin = plugin;
        this.file = new File(this.plugin.getDataFolder(), "../../spigot.yml");
    }

    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(this.file);
    }

}
