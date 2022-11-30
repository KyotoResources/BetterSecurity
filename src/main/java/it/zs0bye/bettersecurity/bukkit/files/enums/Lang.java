package it.zs0bye.bettersecurity.bukkit.files.enums;

import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.files.IFiles;
import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Lang implements IFiles {
    CUSTOM(""),
    IS_NOT_NUMBER("is_not_number"),
    INSUFFICIENT_PERMISSIONS("insufficient_permissions"),
    UPDATE_NOTIFICATION("update_notification"),
    HELP_TEXTS("Help_Command.texts"),
    HELP_PLACEHOLDERS_MAX_PAGE("Help_Command.placeholders.max_page"),
    HELP_ERRORS_PAGE_NOT_FOUND("Help_Command.errors.page_not_found"),
    RELOAD_CONFIGURATIONS("Reload_Command.configurations");

    private final BetterSecurityBukkit plugin;
    private final String path;
    private FileConfiguration lang;

    Lang(final String path) {
        this.path = path;
        this.plugin = BetterSecurityBukkit.getInstance();
        this.reloadConfig();
    }

    @Override
    public StringBuilder variables(final String... var) {
        StringBuilder builder = new StringBuilder();
        for(String texts : var) {
            builder.append(texts);
        }
        builder.append(path);
        return builder;
    }

    @Override
    public void reloadConfig() {
        this.lang = this.plugin.getLanguagesFile().getConfig();
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getString(final String... var) {
        return StringUtils.colorize(this.lang.getString(variables(var).toString()));
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        for (String setList : this.lang.getStringList(variables(var).toString())) {
            list.add(StringUtils.colorize(setList));
        }
        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.lang.getBoolean(variables(var).toString());
    }

    @Override
    public boolean contains(final String... var) {
        return this.lang.contains(variables(var).toString());
    }

    @Override
    public int getInt(final String... var) {
        return this.lang.getInt(variables(var).toString());
    }

    @Override
    public String getCustomString(final String... var) {

        if (this.getString(var).startsWith("%prefix%")) {
            String replace = this.getString(var).replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
                replace = replace.replace("%center%", "");
                return CStringUtils.center(replace);
            }
            return replace;
        }

        if(this.getString(var).startsWith("%center%")) {
            final String replace = this.getString(var).replace("%center%", "");
            return CStringUtils.center(replace);
        }

        return this.getString(var);
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        if (this.getCustomString(var).isEmpty()) return;
        sender.sendMessage(this.getCustomString(var));
    }

    @SuppressWarnings("all")
    @Override
    public Set<String> getConfigurationSection(final String... var) {
        return this.lang.getConfigurationSection(variables(var).toString()).getKeys( false);
    }
}
