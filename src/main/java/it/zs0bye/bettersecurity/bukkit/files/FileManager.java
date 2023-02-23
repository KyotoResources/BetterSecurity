package it.zs0bye.bettersecurity.bukkit.files;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public class FileManager {

    private final BetterSecurityBukkit plugin;
    private final String fileName;

    private FileConfiguration config;
    private File file;
    private String pattern;
    private File directory;

    private final static Map<String, FileConfiguration> files = new HashMap<>();

    @SuppressWarnings("all")
    public FileManager(final BetterSecurityBukkit plugin, final String fileName, final String directory) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.pattern = "configs/bukkit/" + this.fileName + ".yml";

        if (directory != null) {
            if (directory.length() > 16) return;
            if (!Pattern.compile("^[a-zA-Z _]*").matcher(directory).matches()) return;
            this.pattern = "configs/bukkit/" + directory + "/" + this.fileName + ".yml";
            this.directory = new File(this.plugin.getDataFolder(), directory);
            this.file = new File(this.directory, this.fileName + ".yml");
            return;
        }
        
        this.file = new File(this.plugin.getDataFolder(), this.fileName + ".yml");
    }

    @SneakyThrows
    public FileManager saveDefaultConfig() {
        if(!this.file.exists()) this.plugin.saveResource(this.pattern, false);
        final CommentedConfiguration cconfig = CommentedConfiguration.loadConfiguration(this.file);
        cconfig.syncWithConfig(this.file, this.plugin.getResource(this.pattern));
        this.config = this.initConfig();
        files.put(this.fileName.toLowerCase(), this.config);
        return this;
    }

    public boolean reload() {
        files.remove(this.fileName.toLowerCase());
        this.saveDefaultConfig();
        return true;
    }

    private FileConfiguration initConfig() {
        if(!files.containsKey(this.fileName.toLowerCase()))
            return CommentedConfiguration.loadConfiguration(this.file);
        return files.get(this.fileName.toLowerCase());
    }

}
