package fr.tt54.chessgame.ai.minmax;

import fr.tt54.chessgame.game.objects.GameInfo;
import fr.tt54.chessgame.game.objects.Piece;
import fr.tt54.chessgame.game.objects.PieceLocation;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class MinMax {

    private int maxDepth;

    public MinMax(int maxDepth) {
        this.maxDepth = maxDepth;
    }
/*
    public PieceLocation choseBestMove(Map<PieceLocation, Piece> pieces, boolean isWhiteToMove, GameInfo info){

    }

    public int evaluateMove(Map<PieceLocation, Piece> pieces, boolean isWhiteToMove, GameInfo info, int range, PieceLocation move, boolean wantWhite){
        if(range >= maxDepth){
            return evaluatePos(pieces.values(), info);
        }

        for(Piece piece : pieces.values()){
            if(piece.isWhite() == isWhiteToMove){
                for(PieceLocation testMove : piece.getAvailableMoves(pieces.values(), true, isWhiteToMove, info)){

                }
            }
        }

        if(isWhiteToMove){
            //TODO trouver le max
        } else {
            //TODO trouver le min
        }
    }*/

    public int evaluatePos(Collection<Piece> pieces, GameInfo gameInfo){
        Collection<Piece> p = createCopy(pieces);
        int score = 0;
        for(Piece piece : p){
            score += piece.getPiece().getValue() * (piece.isWhite() ? 10 : -10);
            score += piece.getAvailableMoves(p, true, piece.isWhite(), gameInfo).size() * (piece.isWhite() ? 1 : -1);
        }
        return score;
    }


    public Collection<Piece> createCopy(Collection<Piece> pieces){
        return pieces.stream().map(Piece::copy).collect(Collectors.toList());
    }
}
