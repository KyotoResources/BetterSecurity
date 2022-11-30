package it.zs0bye.bettersecurity.bukkit.hooks;

import it.zs0bye.bettersecurity.bukkit.utils.StringUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class HPlaceholderAPI  {

    private final Player player;

    public HPlaceholderAPI(final Player player) {
        this.player = player;
    }

    public String getPlaceholders(final String text) {
        return StringUtils.colorize(PlaceholderAPI.setPlaceholders(this.player, text));
    }

}