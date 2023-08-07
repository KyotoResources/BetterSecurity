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

package it.zs0bye.bettersecurity.bungee.files;

import com.google.common.io.ByteStreams;
import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public class FileManager {

    private final BetterSecurityBungee plugin;
    private final String fileName;

    private Configuration config;
    private File file;
    private String pattern;
    private File directory;

    private final static Map<String, Configuration> files = new HashMap<>();

    @SuppressWarnings("all")
    @SneakyThrows
    public FileManager(final BetterSecurityBungee plugin, final String fileName, final String directory) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.pattern = "configs/bungee/" + this.fileName + ".yml";

        if(directory != null) {
            if(directory.length() > 16) return;
            if(!Pattern.compile("^[a-zA-Z _]*").matcher(directory).matches()) return;
            this.pattern = "configs/bungee/" + directory + "/" + this.fileName + ".yml";
            this.directory = new File(this.plugin.getDataFolder(), directory);
            this.file = new File(this.directory, this.fileName + ".yml");

            if(!this.directory.exists()) this.directory.mkdirs();
        } else {
            this.file = new File(this.plugin.getDataFolder(), this.fileName + ".yml");
        }

        if(!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdir();
        if(this.file.exists()) this.config = this.initConfig();
    }

    @SuppressWarnings("all")
    @SneakyThrows
    public FileManager saveDefaultConfig() {

        if (!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdirs();

        if (!this.file.exists()) {
            final InputStream is = this.plugin.getResourceAsStream(this.pattern);
            final OutputStream os = new FileOutputStream(this.file);

            ByteStreams.copy(is, os);
            this.file.createNewFile();
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

    @SneakyThrows
    private Configuration initConfig() {
        if(!files.containsKey(this.fileName.toLowerCase()))
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        return files.get(this.fileName.toLowerCase());
    }

}
