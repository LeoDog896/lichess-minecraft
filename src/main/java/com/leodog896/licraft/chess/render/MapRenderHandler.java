package com.leodog896.licraft.chess.render;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.leodog896.licraft.chess.ChessGame;
import net.minestom.server.adventure.audience.PacketGroupingAudience;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ItemFrameMeta;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.map.Framebuffer;
import net.minestom.server.map.MapColors;
import net.minestom.server.map.framebuffers.DirectFramebuffer;
import net.minestom.server.map.framebuffers.Graphics2DFramebuffer;

public class MapRenderHandler implements RenderHandler {
    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;
    private static final int BOARD_SIZE = BOARD_WIDTH * BOARD_HEIGHT;

    private static int MAP_ID_COUNTER = 1;

    private int first_id;
    private Entity[] screen = new Entity[BOARD_WIDTH * BOARD_HEIGHT];

    private ChessGame game;

    public MapRenderHandler(ChessGame game) {
        this.game = game;
    }

    private void clear() {
        DirectFramebuffer framebuffer = new DirectFramebuffer();
        for (int i = 0; i < Framebuffer.WIDTH; i++) {
            for (int j = 0; j < Framebuffer.HEIGHT; j++) {
                framebuffer.set(i, j, MapColors.NONE.baseColor());
            }
        }

        for (int i = first_id; i < first_id + BOARD_SIZE; i++) {
            game.audience().sendGroupedPacket(framebuffer.preparePacket(i));
        }
    }

    private void renderBoard(Board board, PacketGroupingAudience audience) {
        for (int i = first_id; i < first_id + BOARD_SIZE; i++) {
            Graphics2DFramebuffer graphics2DFramebuffer = new Graphics2DFramebuffer();
            Square square = Square.values()[i - first_id];

            graphics2DFramebuffer.getRenderer().drawString(board.getPiece(square).name(), 10, 10);

            audience.sendGroupedPacket(graphics2DFramebuffer.preparePacket(i));
        }
    }

    @Override
    public void rerender(PacketGroupingAudience audience) {
        renderBoard(game.getBoard(), audience);
    }

    @Override
    public void load() {
        first_id = MAP_ID_COUNTER;

        for (int i = 0; i < BOARD_SIZE; i++) {
            Entity entity = new Entity(EntityType.ITEM_FRAME);

//            entity.setInvisible(true);

            ItemFrameMeta itemFrameMeta = (ItemFrameMeta) entity.getEntityMeta();

            ItemStack mapItem = ItemStack.builder(Material.FILLED_MAP)
                    .set(ItemComponent.MAP_ID, MAP_ID_COUNTER++)
                    .build();

            itemFrameMeta.setItem(mapItem);
            itemFrameMeta.setOrientation(ItemFrameMeta.Orientation.UP);

            // start from the bottom left to correlate with a1 - h8
            // add 0.5 for the item frame size
            entity.setInstance(game.getInstance(), new Pos(
                    0.5 + i % BOARD_WIDTH,
                    2.1,
                    0.5 + (double) (i / BOARD_HEIGHT)
            ).withDirection(new Pos(0, 1, 0)));

            screen[i] = entity;
        }

        renderBoard(game.getBoard(), game.audience());
    }

    @Override
    public void move(Move move) {

    }

    @Override
    public void unload() {
        for (Entity entity : screen) {
            entity.remove();
        }
    }
}
