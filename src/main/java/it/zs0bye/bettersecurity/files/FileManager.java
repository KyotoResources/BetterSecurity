package it.zs0bye.bettersecurity.files;

import com.google.common.io.ByteStreams;
import it.zs0bye.bettersecurity.BetterSecurity;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public class FileManager {

    private final BetterSecurity plugin;
    private final String fileName;

    private FileConfiguration config;
    private File file;
    private String pattern;
    private File directory;

    private final static Map<String, FileConfiguration> files = new HashMap<>();

    @SuppressWarnings("all")
    public FileManager(final BetterSecurity plugin, final String fileName, final String directory) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.pattern = this.fileName + ".yml";

        if(directory != null) {
            if(directory.length() > 16) return;
            if(!Pattern.compile("^[a-zA-Z _]*").matcher(directory).matches()) return;
            this.pattern = directory + "/" + this.fileName + ".yml";
            this.directory = new File(this.plugin.getDataFolder(), directory);
            this.file = new File(this.directory, this.fileName + ".yml");

            if(!this.directory.exists()) this.directory.mkdirs();
        } else {
            this.file = new File(this.plugin.getDataFolder(), this.fileName + ".yml");
        }

        if(!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdir();
        this.config = this.initConfig();
    }

    @SuppressWarnings("all")
    @SneakyThrows
    public FileManager saveDefaultConfig() {

        if (!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdirs();

        if (!this.file.exists()) {
            final InputStream is = this.plugin.getResource(this.pattern);
            final OutputStream os = new FileOutputStream(this.file);

            ByteStreams.copy(is, os);
        }

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
            return YamlConfiguration.loadConfiguration(this.file);
        return files.get(this.fileName.toLowerCase());
    }

}
