package it.zs0bye.bettersecurity.bukkit.protocols;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.TabComplete;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import it.zs0bye.bettersecurity.bukkit.utils.VersionUtils;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TabProtocol extends PacketAdapter {

    public TabProtocol(final BetterSecurityBukkit plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE);
        if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @SneakyThrows
    @Override
    public void onPacketReceiving(final PacketEvent event) {

        if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;

        final List<String> commands = Config.BLOCK_TAB_COMPLETE_BLACKLISTED_SUGGESTIONS.getStringList();

        final PacketContainer packet = event.getPacket();
        final Player player = event.getPlayer();

        if (player == null) return;

        final String completion = packet.getSpecificModifier(String.class).read(0).toLowerCase();

        final TabComplete tabComplete = new TabComplete(player);
        if(tabComplete.bypass()) return;
        this.replaceSuggestions(completion, player);

        if((completion.contains(":") || completion.length() <= 1)
                || (completion.startsWith("/") && !completion.contains(" "))
                ||(completion.startsWith("/") && completion.contains(":"))) {
            event.setCancelled(true);
            return;
        }

        commands.forEach(command -> {
            if (!completion.startsWith("/" + command)) return;
            event.setCancelled(true);
        });

    }

    @SneakyThrows
    private void replaceSuggestions(final String completion, final Player player) {
        if (!VersionUtils.legacy()) return;
        final PacketContainer serverPacket = new PacketContainer(PacketType.Play.Server.TAB_COMPLETE);

        List<String> completions = new ArrayList<>(new TabComplete(player).getCompletions(true));

        if(Config.BLOCK_TAB_COMPLETE_WHITELISTED_SUGGESTIONS_PARTIAL_MATCHES.getBoolean())
            completions = StringUtil.copyPartialMatches(completion, completions, new ArrayList<>());

        serverPacket.getStringArrays().write(0, completions.toArray(new String[0]));

        if(!completion.contains(" ")) ProtocolLibrary.getProtocolManager().sendServerPacket(player, serverPacket);
    }

}
