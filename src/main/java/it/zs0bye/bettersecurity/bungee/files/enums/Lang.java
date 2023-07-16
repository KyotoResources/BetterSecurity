package it.zs0bye.bettersecurity.bungee.files.enums;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.files.IFiles;
import it.zs0bye.bettersecurity.bungee.utils.StringUtils;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

import java.util.*;

public enum Lang implements IFiles {
    CUSTOM(""),
    IS_NOT_NUMBER("is_not_number"),
    INSUFFICIENT_PERMISSIONS("insufficient_permissions"),
    UPDATE_NOTIFICATION("update_notification"),
    HELP_TEXTS("Help_Command.texts"),
    RELOAD_CONFIGURATIONS("Reload_Command.configurations");

    private final BetterSecurityBungee plugin;
    private final String path;
    private Configuration lang;

    Lang(final String path) {
        this.path = path;
        this.plugin = BetterSecurityBungee.getInstance();
        this.reloadConfig();
    }

    @Override
    public String variables(final String... var) {
        StringBuilder builder = new StringBuilder();
        for(String texts : var) builder.append(texts);
        builder.append(path);
        return builder.toString();
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
        return StringUtils.colorize(this.lang.getString(this.variables(var)));
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        for (String setList : this.lang.getStringList(this.variables(var))) {
            list.add(StringUtils.colorize(setList));
        }
        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.lang.getBoolean(this.variables(var));
    }

    @Override
    public boolean contains(final String... var) {
        return this.lang.contains(this.variables(var));
    }

    @Override
    public int getInt(final String... var) {
        return this.lang.getInt(this.variables(var));
    }

    @Override
    public String getCustomString(String replace, final String... var) {
        if (replace.startsWith("%prefix%")) {
            replace = replace.replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
                replace = replace.replace("%center%", "");
                return CStringUtils.center(replace);
            }
            return replace;
        }

        if(replace.startsWith("%center%")) {
            replace = replace.replace("%center%", "");
            return CStringUtils.center(replace);
        }
        return replace;
    }

    @Override
    public String getCustomString(final String... var) {
        return this.getCustomString(this.getString(var), var);
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        this.send(sender, new HashMap<>(), var);
    }

    @Override
    public void send(final CommandSender sender, final Map<String, String> placeholders, final String... var) {
        String message = this.getCustomString(this.getString(var), var);
        if (message.isEmpty()) return;
        for (String key : placeholders.keySet()) message = message.replace(key, placeholders.get(key));
        StringUtils.send(sender, message);
    }

    @Override
    public void sendList(final CommandSender sender, final String... var) {
        this.sendList(sender, new HashMap<>(), var);
    }

    @Override
    public void sendList(final CommandSender sender, final Map<String, String> placeholders, final String... var) {
        if (this.getStringList(var).isEmpty()) return;
        this.getStringList(var).forEach(msg -> {
            for (String key : placeholders.keySet()) msg = msg.replace(key, placeholders.get(key));
            sender.sendMessage(TextComponent.fromLegacyText(this.getCustomString(msg, var)));
        });
    }

    @SuppressWarnings("all")
    @Override
    public Collection<String> getSection(final String... var) {
        return this.lang.getSection(this.variables(var)).getKeys();
    }
}
