/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (C) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.zs0bye.bettersecurity.bukkit.files;

import com.google.common.io.ByteStreams;
import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.readers.Config;
import it.zs0bye.bettersecurity.common.config.ConfigReader;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public class FileHandler {

    private final BetterSecurityBukkit plugin;
    private final String fileName, dirName;
    private final FileType type;

    private FileConfiguration config;
    private File file;
    private String pattern;
    private File directory;

    private final static Map<String, FileConfiguration> files = new HashMap<>();

    @SuppressWarnings("all")
    public FileHandler(final BetterSecurityBukkit plugin, final FileType type) {
        this.plugin = plugin;
        this.type = type;
        this.fileName = this.type.getName();
        this.dirName = this.type.getDirectory();
        this.pattern = "configs/bukkit/" + this.fileName + ".yml";

        if (this.dirName != null) {
            if (this.dirName.length() > 16) return;
            if (!Pattern.compile("^[a-zA-Z _]*").matcher(this.dirName).matches()) return;
            this.pattern = "configs/bukkit/" + this.dirName + "/" + this.fileName + ".yml";
            this.directory = new File(this.plugin.getDataFolder(), this.dirName);
            this.file = new File(this.directory, this.fileName + ".yml");
            if(!this.directory.exists()) this.directory.mkdirs();
            return;
        }
        
        this.file = new File(this.plugin.getDataFolder(), this.fileName + ".yml");
    }

    @SneakyThrows
    public FileHandler saveDefaultConfig() {

        if (!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdirs();

        if (!this.file.exists()) {
            final InputStream is = this.plugin.getResource(this.pattern);
            final OutputStream os = Files.newOutputStream(this.file.toPath());

            if(is == null) return this;
            ByteStreams.copy(is, os);
        }

        final CommentedConfiguration cconfig = CommentedConfiguration.loadConfiguration(this.file);
        cconfig.syncWithConfig(this.file, this.plugin.getResource(this.pattern));
        this.config = this.initConfig();
        files.put(this.fileName.toLowerCase(), this.config);
        return this;
    }

    public boolean reload() {
        files.remove(this.fileName.toLowerCase());
        this.saveDefaultConfig();

        if(this.reloadLang()) return false;
        this.reloadReader();
        return true;
    }

    private void reloadReader() {
        for(final ConfigReader reader : this.type.getReader()) reader.reloadConfig();
    }

    private boolean reloadLang() {
        final Map<FileType, FileHandler> handlers = this.plugin.getHandlers();
        if(!handlers.containsKey(FileType.CONFIG) && !handlers.containsKey(FileType.LANG)) return false;
        if(this.type != FileType.LANG) return false;
        if(this.directory == null || !this.directory.exists()) return false;
        if(this.fileName.equals(Config.SETTINGS_LOCALE.getString())) return false;
        handlers.put(this.type, new FileHandler(this.plugin, this.type).saveDefaultConfig());
        this.reloadReader();
        return true;
    }

    private FileConfiguration initConfig() {
        if(!files.containsKey(this.fileName.toLowerCase()))
            return CommentedConfiguration.loadConfiguration(this.file);
        return files.get(this.fileName.toLowerCase());
    }

}
