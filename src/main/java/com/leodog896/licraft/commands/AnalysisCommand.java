package com.leodog896.licraft.commands;

import com.leodog896.licraft.chess.ChessInstance;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class AnalysisCommand extends Command {

    public AnalysisCommand() {
        super("analyze");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("LICRAFT: You must be a player to run this command!");
                return;
            }

            Instance instance = ChessInstance.generate();

            player.setInstance(instance, ChessInstance.SPAWN_POSITION.withDirection(player.getPosition().direction()));
        });
    }

}
