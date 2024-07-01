package com.leodog896.licraft.commands;

import com.leodog896.licraft.Lobby;
import com.leodog896.licraft.Messages;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class LobbyCommand extends Command {

    public LobbyCommand() {
        super("lobby");

        setDefaultExecutor(((sender, context) -> {
            if (!(sender instanceof Player player)) {
                return;
            }

            player.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            Messages.PREFIX + "Sending to lobby..."
                    )
            );

            Lobby.sendToLobby(player);
        }));
    }

}
