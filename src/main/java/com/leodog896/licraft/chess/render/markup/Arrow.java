package com.leodog896.licraft.chess.render.markup;

import com.github.bhlangonijr.chesslib.Square;

import java.awt.*;

public record Arrow(Color color, Square from, Square to) implements Markup {
}
