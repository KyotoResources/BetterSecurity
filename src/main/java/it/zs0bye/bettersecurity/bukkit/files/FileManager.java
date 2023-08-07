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
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.nio.file.Files;
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
            if(!this.directory.exists()) this.directory.mkdirs();
            return;
        }
        
        this.file = new File(this.plugin.getDataFolder(), this.fileName + ".yml");
    }

    @SneakyThrows
    public FileManager saveDefaultConfig() {

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
        return true;
    }

    private FileConfiguration initConfig() {
        if(!files.containsKey(this.fileName.toLowerCase()))
            return CommentedConfiguration.loadConfiguration(this.file);
        return files.get(this.fileName.toLowerCase());
    }

}
