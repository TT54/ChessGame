package fr.tt54.chessgame.game.objects;

import fr.tt54.chessgame.game.ChessManager;
import fr.tt54.chessgame.game.ChessMouseListener;
import fr.tt54.chessgame.graphic.nodes.PieceNode;

import java.util.*;

public class Piece {

    private PieceNode pieceNode;
    private PieceLocation location;
    private ChessPiece piece;
    private boolean white;

    public Piece(PieceNode pieceNode) {
        this.pieceNode = pieceNode;
        this.location = pieceNode.getLocation();
        this.piece = pieceNode.getPiece();
        this.white = pieceNode.isWhite();
        this.pieceNode.placePiece(this.location);
    }

    public Piece(PieceLocation location, ChessPiece piece, boolean white){
        this.pieceNode = null;
        this.location = location;
        this.piece = piece;
        this.white = white;
    }

    public void setPieceNode(PieceNode pieceNode) {
        this.pieceNode = pieceNode;
    }

    public int getId(){
        return white ? piece.getId() : -piece.getId();
    }

    public void setLocation(PieceLocation location) {
        this.location = location;
        if(this.pieceNode != null)
            this.pieceNode.placePiece(location);
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public PieceNode getPieceNode() {
        return pieceNode;
    }

    public PieceLocation getLocation() {
        return location;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public boolean isWhite() {
        return white;
    }


    public List<PieceLocation> getAvailableMoves(Collection<Piece> pieces, boolean mustCheckKing, boolean isWhiteToMove, GameInfo gameInfo){
        if(isWhiteToMove != this.white && mustCheckKing)
            return Collections.emptyList();

        int[][] board = ChessManager.getBoardFromMap(pieces);

        List<PieceLocation> available = new ArrayList<>();
        PieceLocation loc = this.getLocation();
        int column = loc.getColumnInt();
        int row = loc.getLine();

        int signe = 1;
        if(!this.white)
            signe = -1;

        if(this.getPiece() == ChessPiece.PAWN){
            if(ChessManager.getPiece(column, row + signe, board) == 0) {
                available.add(new PieceLocation(column, row + signe));
                if (row == ((this.white) ? 2 : 7) && ChessManager.getPiece(column, row + signe * 2, board) == 0) {
                    available.add(new PieceLocation(column, row + signe * 2));
                }
            }
            int i = ChessManager.getPiece(column + 1, row + signe, board);
            if(i != 0 && i < 1000 && i * this.getId() < 0)
                available.add(new PieceLocation(column + 1, row + signe));

            i = ChessManager.getPiece(column - 1, row + signe, board);
            if(i != 0 && i < 1000 && i * this.getId() < 0)
                available.add(new PieceLocation(column - 1, row + signe));

            if(row == ((this.white) ? 5 : 4) && gameInfo.isWhiteAdvancedFromTwo() == !this.white && gameInfo.getPawnAdvancedFromTwo() != null){
                if(gameInfo.getPawnAdvancedFromTwo().getColumnInt() == column - 1){
                    available.add(new PieceLocation(column - 1, row + signe));
                } else if(gameInfo.getPawnAdvancedFromTwo().getColumnInt() == column + 1){
                    available.add(new PieceLocation(column + 1, row + signe));
                }
            }

            /*
            if(this.white){
            } else {
                if(ChessManager.getPiece(column, row - 1) == 0) {
                    available.add(new PieceLocation(column, row - 1));
                    if (row == 7 && ChessManager.getPiece(column, row - 2) == 0) {
                        available.add(new PieceLocation(column, row - 2));
                    }
                }
            }*/
        } else if(this.getPiece() == ChessPiece.KNIGHT){
            for(int i = -1; i < 2; i+=2){
                int target = -ChessManager.getPiece(column + i * 2, row + 1, board) * signe;
                if(target >= 0 && target < 10000){
                    available.add(new PieceLocation(column + i * 2, row + 1));
                }

                target = -ChessManager.getPiece(column + i * 2, row - 1,board) * signe;
                if(target >= 0 && target < 10000){
                    available.add(new PieceLocation(column + i * 2, row - 1));
                }

                target = -ChessManager.getPiece(column + 1, row + i * 2, board) * signe;
                if(target >= 0 && target < 10000){
                    available.add(new PieceLocation(column + 1, row + i * 2));
                }

                target = -ChessManager.getPiece(column - 1, row + i * 2, board) * signe;
                if(target >= 0 && target < 10000){
                    available.add(new PieceLocation(column - 1, row + i * 2));
                }
            }
        } else if(this.getPiece() == ChessPiece.KING){
            for(int i = -1; i < 2; i++){
                for(int j = -1; j < 2; j++){
                    if(i != 0 || j != 0){
                        int target = -ChessManager.getPiece(column + i, row + j, board) * signe;
                        if(target >= 0 && target < 10000){
                            if(mustCheckKing) {
                                PieceLocation newLoc = new PieceLocation(column + i, row + j);
                                Map<PieceLocation, Piece> map = ChessManager.movePiece(this, newLoc, true, gameInfo.copy());
                                boolean isPosOk = true;
                                for(Piece p : map.values()){
                                    if(p.isWhite() != this.isWhite()){
                                        if(p.getAvailableMoves(map.values(), false, !isWhiteToMove, gameInfo.copy()).contains(newLoc)){
                                            isPosOk = false;
                                        }
                                    }
                                }

                                if(isPosOk)
                                    available.add(new PieceLocation(column + i, row + j));
                            } else {
                                available.add(new PieceLocation(column + i, row + j));
                            }
                        }
                    }
                }
            }
            if(!mustCheckKing || !ChessManager.isInCheck(isWhiteToMove, pieces, gameInfo)){
                if(this.isWhite()){
                    if(gameInfo.canWhiteRock() && available.contains(new PieceLocation(6, 1))){
                        PieceLocation newLoc = new PieceLocation(7, 1);
                        int target = -ChessManager.getPiece(7, 1, board) * signe;
                        if(target >= 0 && target < 10000){
                            checkKingMove(newLoc, available, mustCheckKing, gameInfo, isWhiteToMove);
                        }
                    } else if(gameInfo.canWhiteQueenSideRock() && available.contains(new PieceLocation(4, 1))){
                        PieceLocation newLoc = new PieceLocation(3, 1);
                        int target = -ChessManager.getPiece(3, 1, board) * signe;
                        if(target >= 0 && target < 10000){
                            PieceLocation fakeLoc = new PieceLocation(2, 1);
                            checkKingMove(fakeLoc, available, mustCheckKing, gameInfo, isWhiteToMove);
                            if(available.contains(fakeLoc)){
                                available.remove(fakeLoc);
                                checkKingMove(newLoc, available, mustCheckKing, gameInfo, isWhiteToMove);
                            }
                        }
                    }
                } else {
                    if(gameInfo.canBlackRock() && available.contains(new PieceLocation(6, 8))){
                        PieceLocation newLoc = new PieceLocation(7, 8);
                        int target = -ChessManager.getPiece(7, 8, board) * signe;
                        if(target >= 0 && target < 10000){
                            checkKingMove(newLoc, available, mustCheckKing, gameInfo, isWhiteToMove);
                        }
                    } else if(gameInfo.canBlackQueenSideRock() && available.contains(new PieceLocation(4, 8))){
                        PieceLocation newLoc = new PieceLocation(3, 8);
                        int target = -ChessManager.getPiece(3, 8, board) * signe;
                        if(target >= 0 && target < 10000){
                            PieceLocation fakeLoc = new PieceLocation(2, 8);
                            checkKingMove(fakeLoc, available, mustCheckKing, gameInfo, isWhiteToMove);
                            if(available.contains(fakeLoc)){
                                available.remove(fakeLoc);
                                checkKingMove(newLoc, available, mustCheckKing, gameInfo, isWhiteToMove);
                            }
                        }
                    }
                }
            }
        } else if(this.getPiece() == ChessPiece.ROOK){
            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column + i, row, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column + i, row));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column - i, row, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column - i, row));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column, row + i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column, row + i));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column, row - i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column, row - i));
                if(target > 0){
                    break;
                }
            }
        } else if(this.getPiece() == ChessPiece.BISHOP){
            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column + i, row + i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column + i, row + i));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column - i, row - i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column - i, row - i));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column - i, row + i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column - i, row + i));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column + i, row - i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column + i, row - i));
                if(target > 0){
                    break;
                }
            }
        }else if(this.getPiece() == ChessPiece.QUEEN){
            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column + i, row, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column + i, row));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column - i, row, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column - i, row));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column, row + i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column, row + i));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column, row - i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column, row - i));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column + i, row + i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column + i, row + i));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column - i, row - i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column - i, row - i));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column - i, row + i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column - i, row + i));
                if(target > 0){
                    break;
                }
            }

            for(int i = 1; i < 8; i++){
                int target = -ChessManager.getPiece(column + i, row - i, board) * signe;
                if(target < 0 || target >= 10000){
                    break;
                }
                available.add(new PieceLocation(column + i, row - i));
                if(target > 0){
                    break;
                }
            }
        }



        if(mustCheckKing){
            List<PieceLocation> toRemove = new ArrayList<>();
            for(PieceLocation locs : available){
                if(ChessManager.isInCheck(isWhiteToMove, ChessManager.movePiece(this, locs, true, gameInfo.copy()).values(), gameInfo)){
                    toRemove.add(locs);
                }
            }
            available.removeAll(toRemove);
        }

        return available;
    }

    private void checkKingMove(PieceLocation newLoc, List<PieceLocation> available, boolean mustCheckKing, GameInfo gameInfo, boolean isWhiteToMove) {
        if(mustCheckKing) {
            Map<PieceLocation, Piece> map = ChessManager.movePiece(this, newLoc, true, gameInfo.copy());
            boolean isPosOk = true;
            for(Piece p : map.values()){
                if(p.isWhite() != this.isWhite()){
                    if(p.getAvailableMoves(map.values(), false, !isWhiteToMove, gameInfo.copy()).contains(newLoc)){
                        isPosOk = false;
                    }
                }
            }

            if(isPosOk)
                available.add(newLoc);
        } else {
            available.add(newLoc);
        }
    }


    public Piece copy(){
        return new Piece(this.location, this.piece, this.white);
    }
}
