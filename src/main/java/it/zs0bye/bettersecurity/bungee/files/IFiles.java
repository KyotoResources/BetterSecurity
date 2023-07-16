package it.zs0bye.bettersecurity.bungee.files;

import net.md_5.bungee.api.CommandSender;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IFiles {

    void reloadConfig();

    String getPath();

    String getString(final String... var);

    String getCustomString(String replace, final String... var);

    String getCustomString(final String... var);

    List<String> getStringList(final String... var);

    boolean getBoolean(final String... var);

    boolean contains(final String... var);

    int getInt(final String... var);

    void send(final CommandSender sender, final String... var);

    void send(final CommandSender sender, final Map<String, String> placeholders, final String... var);

    void sendList(final CommandSender sender, final String... var);

    void sendList(final CommandSender sender, final Map<String, String> placeholders, final String... var);

    String variables(final String... var);

    Collection<String> getSection(final String... var);

}
