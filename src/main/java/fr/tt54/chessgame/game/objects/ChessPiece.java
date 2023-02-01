package fr.tt54.chessgame.game.objects;

import java.util.HashMap;
import java.util.Map;

public enum ChessPiece {

    PAWN(1, 1),
    KNIGHT(3, 2),
    BISHOP(3, 3),
    ROOK(5, 5),
    QUEEN(9, 9),
    KING(800, 800);


    private final int value;
    private final int id;
    ChessPiece(int value, int id) {
        this.value = value;
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public int getId() {
        return id;
    }


    private static final Map<Integer, ChessPiece> pieces = new HashMap<>();

    static {
        for(ChessPiece piece : ChessPiece.values()){
            pieces.put(piece.getId(), piece);
        }
    }

    public static ChessPiece getPiece(int value){
        return pieces.get(Math.abs(value));
    }
}
