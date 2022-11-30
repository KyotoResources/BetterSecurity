package it.zs0bye.bettersecurity.bukkit.protocols;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import it.zs0bye.bettersecurity.bukkit.BetterSecurityBukkit;
import it.zs0bye.bettersecurity.bukkit.TabComplete;
import it.zs0bye.bettersecurity.bukkit.checks.VersionCheck;
import it.zs0bye.bettersecurity.bukkit.files.enums.Config;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TabProtocol extends PacketAdapter {

    public TabProtocol(final BetterSecurityBukkit plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE);

        if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;

        if (!VersionCheck.legacy()) return;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);

    }

    @SneakyThrows
    @Override
    public void onPacketReceiving(final PacketEvent event) {

        if(!Config.BLOCK_TAB_COMPLETE_ENABLED.getBoolean()) return;

        final List<String> commands = Config.BLOCK_TAB_COMPLETE_COMMANDS.getStringList();

        final PacketContainer packet = event.getPacket();
        final Player player = event.getPlayer();

        if (player == null) return;

        final String completion = packet.getSpecificModifier(String.class).read(0).toLowerCase();

        final TabComplete tabComplete = new TabComplete(player);
        if(tabComplete.bypass()) return;

        final PacketContainer serverPacket = new PacketContainer(PacketType.Play.Server.TAB_COMPLETE);

        final List<String> completions = new ArrayList<>(TabComplete.getCompletions(player, true));
        final String[] matches = StringUtil.copyPartialMatches(completion, completions, new ArrayList<>())
                .toArray(new String[0]);

        serverPacket.getStringArrays().write(0, matches);

        if(!completion.contains(" ")) ProtocolLibrary.getProtocolManager().sendServerPacket(player, serverPacket);

        commands.forEach(command -> {
            if ((completion.startsWith("/") && !completion.contains(" "))
                    || (completion.startsWith("/" + command))
                    || (completion.startsWith("/") && completion.contains(":"))) {
                event.setCancelled(true);
            }
        });

    }

}
