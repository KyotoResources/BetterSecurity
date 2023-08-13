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
public class FileHandler {

    private final BetterSecurityBungee plugin;
    private final String fileName, dirName;
    private final FileType type;

    private Configuration config;
    private String pattern;
    private File file, directory;

    @Getter
    private final static Map<FileType, Configuration> files = new HashMap<>();
    @Getter
    private final static Map<FileType, FileHandler> handlers = new HashMap<>();

    @SuppressWarnings("all")
    @SneakyThrows
    public FileHandler(final BetterSecurityBungee plugin, final FileType type) {
        this.plugin = plugin;
        this.type = type;
        this.fileName = this.type.getName();
        this.dirName = this.type.getDirectory();
        this.pattern = "configs/bungee/" + this.fileName + ".yml";

        if(this.dirName != null) {
            if(this.dirName.length() > 16) return;
            if(!Pattern.compile("^[a-zA-Z _]*").matcher(this.dirName).matches()) return;
            this.pattern = "configs/bungee/" + this.dirName + "/" + this.fileName + ".yml";
            this.directory = new File(this.plugin.getDataFolder(), this.dirName);
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
    public FileHandler saveDefaultConfig() {

        if (!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdirs();

        if (!this.file.exists()) {
            final InputStream is = this.plugin.getResourceAsStream(this.pattern);
            final OutputStream os = new FileOutputStream(this.file);

            ByteStreams.copy(is, os);
            this.file.createNewFile();
        }

        this.config = this.initConfig();
        files.put(this.type, this.config);
        handlers.put(this.type, this);
        System.out.println("test save - " + this.fileName);
        for(final ConfigReader reader : this.type.getReader()) reader.reloadConfig();
        return this;
    }

    public void reload() {
        files.remove(this.type);
        handlers.remove(this.type);
        this.saveDefaultConfig();
    }

    @SneakyThrows
    private Configuration initConfig() {
        if(!files.containsKey(this.type))
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        return files.get(this.type);
    }

    public static Configuration getConfig(final FileType type) {
        if(!handlers.containsKey(type)) return null;
        return handlers.get(type).getConfig();
    }

}
