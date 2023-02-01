package fr.tt54.chessgame.game;

import fr.tt54.chessgame.game.objects.Piece;
import fr.tt54.chessgame.game.objects.PieceLocation;
import fr.tt54.chessgame.graphic.ChessGraphicManager;
import fr.tt54.chessgame.graphic.nodes.AvailableMoveNode;
import fr.ttgraphiclib.graphics.events.listener.UserListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class ChessMouseListener extends UserListener {

    private PieceLocation selectedCase = null;

    @Override
    public void onMousePressed(MouseEvent event) {
        int x = event.getX() - 9 - 78;
        x /= 90;
        x += 1;

        int y = event.getY() - 9 - 78 - 15;
        y /= 90;
        y = 8 - y;

        int pieceId = ChessManager.getPiece(x, y, ChessManager.chessBoard);
        PieceLocation location = new PieceLocation(x, y);
        ChessGraphicManager.removeMoveNode();

        if(selectedCase != null){
            Piece piece = ChessManager.getPiece(selectedCase);
            if(piece != null){
                List<PieceLocation> locations = piece.getAvailableMoves(ChessManager.pieces.values(), true, ChessManager.isWhiteToMove, ChessManager.gameInfo.copy());
                if(!locations.isEmpty()){
                    if(locations.contains(location)) {
                        ChessManager.movePiece(piece, location, false, ChessManager.gameInfo);

                        ChessManager.checkMate(ChessManager.isWhiteToMove, ChessManager.pieces.values(), ChessManager.gameInfo);
                    }
                    //selectedCase = null;
                    //return;
                }
                selectedCase = null;
            } else {
                selectedCase = null;
                return;
            }

        }
        if(pieceId != 0 && pieceId < 10000){
            Piece piece = ChessManager.getPiece(location);
            if(piece != null){
                selectedCase = piece.getLocation();
                List<PieceLocation> available = piece.getAvailableMoves(ChessManager.pieces.values(), true, ChessManager.isWhiteToMove, ChessManager.gameInfo.copy());
                for(PieceLocation loc : available){
                    AvailableMoveNode node = new AvailableMoveNode(ChessGraphicManager.panel, loc.getGraphicX(), loc.getGraphicY(), 50, 50, Color.RED);
                }
            }
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event) {

    }
}
