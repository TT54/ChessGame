package fr.tt54.chessgame.game;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.tt54.chessgame.ChessGame;
import fr.tt54.chessgame.ai.minmax.MinMax;
import fr.tt54.chessgame.game.objects.ChessPiece;
import fr.tt54.chessgame.game.objects.GameInfo;
import fr.tt54.chessgame.game.objects.Piece;
import fr.tt54.chessgame.game.objects.PieceLocation;
import fr.tt54.chessgame.graphic.ChessGraphicManager;
import fr.tt54.chessgame.graphic.nodes.PieceNode;
import fr.ttgraphiclib.GraphicManager;
import fr.ttgraphiclib.graphics.nodes.GraphicNode;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ChessManager {

    private static MinMax minMax;

    protected static final Map<PieceLocation, Piece> pieces = new HashMap<>();
    protected static int[][] chessBoard = new int[8][8]; // column / line

    protected static boolean isWhiteToMove = true;

    protected static GameInfo gameInfo = GameInfo.generate();

    public static void enable(){
        //generateBoard();
        generateTestBoard();

        minMax = new MinMax(1);
        System.out.println(minMax.evaluatePos(pieces.values(), gameInfo));
    }

    private static void generateBoard() {
        clearPieces();
        gameInfo = GameInfo.generate();
        isWhiteToMove = true;
        List<GraphicNode> toRemove = new ArrayList<>();
        toRemove.addAll(GraphicManager.getPanel().getNodes().stream().filter(node -> node instanceof PieceNode).collect(Collectors.toList()));
        toRemove.forEach(node -> GraphicManager.getPanel().removeNode(node));

        for(int i = 0; i < 2; i++) {
            boolean white = i == 0;
            int line = i == 0 ? 1 : 8;
            placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.KING, white, new PieceLocation('e', line))));
            placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.QUEEN, white, new PieceLocation('d', line))));
            placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.BISHOP, white, new PieceLocation('c', line))));
            placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.BISHOP, white, new PieceLocation('f', line))));
            placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.KNIGHT, white, new PieceLocation('g', line))));
            placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.KNIGHT, white, new PieceLocation('b', line))));
            placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.ROOK, white, new PieceLocation('a', line))));
            placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.ROOK, white, new PieceLocation('h', line))));

            for(int j = 0; j < 8; j++){
                placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.PAWN, white, new PieceLocation((char) (j + 97), line == 1 ? 2 : 7))));
            }
        }
    }

    private static void generateTestBoard(){
        clearPieces();
        gameInfo = GameInfo.generate();
        isWhiteToMove = true;
        List<GraphicNode> toRemove = new ArrayList<>();
        toRemove.addAll(GraphicManager.getPanel().getNodes().stream().filter(node -> node instanceof PieceNode).collect(Collectors.toList()));
        toRemove.forEach(node -> GraphicManager.getPanel().removeNode(node));

        placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.PAWN, true, new PieceLocation('c', 3))));
        placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.PAWN, true, new PieceLocation('h', 3))));
        placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.KNIGHT, false, new PieceLocation('d', 5))));

        placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.KING, false, new PieceLocation('e', 8))));
        placePiece(new Piece(new PieceNode(ChessGraphicManager.panel, ChessPiece.KING, true, new PieceLocation('e', 1))));
    }

    private static void placePiece(Piece piece){
        pieces.put(piece.getLocation(), piece);
        chessBoard[piece.getLocation().getColumnInt() - 1][piece.getLocation().getLine() - 1] = piece.getId();
    }

    private static void clearPieces(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                chessBoard[i][j] = 0;
            }
        }
        pieces.clear();
    }

    public static int getPiece(int column, int row, int[][] board) {
        if(column <= 0 || row <= 0 || column > 8 || row > 8)
            return 10000;
        return board[column-1][row-1];
    }

    public static Piece getPiece(PieceLocation location){
        return pieces.get(location);
    }

    public static Map<PieceLocation, Piece> movePiece(Piece piece, PieceLocation selectedCase, boolean simulate, GameInfo gameInfo) {
        if(!simulate) {
            if(isInCheck(isWhiteToMove, movePiece(piece, selectedCase, true, gameInfo.copy()).values(), gameInfo.copy()))
                return pieces;



            PieceLocation loc = piece.getLocation();
            chessBoard[loc.getColumnInt() - 1][loc.getLine() - 1] = 0;

            if (chessBoard[selectedCase.getColumnInt() - 1][selectedCase.getLine() - 1] != 0) {
                Piece p = pieces.get(selectedCase);
                ChessGraphicManager.panel.removeNode(p.getPieceNode());
            }

            chessBoard[selectedCase.getColumnInt() - 1][selectedCase.getLine() - 1] = piece.getId();

            if(piece.getPiece() == ChessPiece.KING){
                if(isWhiteToMove){
                    gameInfo.setCanWhiteRock(false);
                    gameInfo.setCanWhiteQueenSideRock(false);
                } else {
                    gameInfo.setCanBlackRock(false);
                    gameInfo.setCanBlackQueenSideRock(false);
                }
            } else if(piece.getPiece() == ChessPiece.ROOK){
                if(piece.getLocation().equals(new PieceLocation(1, 8))){
                    gameInfo.setCanBlackQueenSideRock(false);
                } else if(piece.getLocation().equals(new PieceLocation(1, 1))){
                    gameInfo.setCanWhiteQueenSideRock(false);
                } else if(piece.getLocation().equals(new PieceLocation(8, 1))){
                    gameInfo.setCanWhiteRock(false);
                } else if(piece.getLocation().equals(new PieceLocation(8, 8))){
                    gameInfo.setCanBlackRock(false);
                }
            }

            if(piece.getPiece() == ChessPiece.PAWN && ((loc.getLine() == 2 && selectedCase.getLine() == 4) || (loc.getLine() == 7 && selectedCase.getLine() == 5))) {
                //Pion avancé de 2
                gameInfo.setWhiteAdvancedFromTwo(isWhiteToMove);
                gameInfo.setPawnAdvancedFromTwo(selectedCase);
            } else if(piece.getPiece() == ChessPiece.PAWN && (selectedCase.getLine() == 8 || selectedCase.getLine() == 1)){
                //Pion tranformé en Dame
                piece.setPiece(ChessPiece.QUEEN);
                piece.getPieceNode().setPiece(ChessPiece.QUEEN);
            } else {
                //Prise en passant
                PieceLocation l = gameInfo.getPawnAdvancedFromTwo();
                if(gameInfo.getPawnAdvancedFromTwo() != null && piece.getPiece() == ChessPiece.PAWN && selectedCase.equals(new PieceLocation(l.getColumn(), l.getLine() + (gameInfo.isWhiteAdvancedFromTwo() ? -1 : 1)))){
                    Piece p = pieces.get(l);
                    ChessGraphicManager.panel.removeNode(p.getPieceNode());
                    pieces.remove(gameInfo.getPawnAdvancedFromTwo());
                    chessBoard[l.getColumnInt() - 1][l.getLine() - 1] = 0;
                }
                gameInfo.setPawnAdvancedFromTwo(null);
            }

            if(piece.getPiece() == ChessPiece.KING){
                if(piece.isWhite() && piece.getLocation().distance(selectedCase) >= 2) {
                    if (selectedCase.equals(new PieceLocation(3, 1))) {
                        PieceLocation from = new PieceLocation(1, 1);
                        PieceLocation to = new PieceLocation(4, 1);
                        Piece p = pieces.get(from);
                        pieces.put(to, p);
                        pieces.remove(from);
                        p.setLocation(to);

                        chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                        chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                    } else {
                        PieceLocation from = new PieceLocation(8, 1);
                        PieceLocation to = new PieceLocation(6, 1);
                        Piece p = pieces.get(from);
                        pieces.put(to, p);
                        pieces.remove(from);
                        p.setLocation(to);

                        chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                        chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                    }
                } else if(!piece.isWhite() && piece.getLocation().distance(selectedCase) >= 2) {
                    if (selectedCase.equals(new PieceLocation(3, 8))) {
                        PieceLocation from = new PieceLocation(1, 8);
                        PieceLocation to = new PieceLocation(4, 8);
                        Piece p = pieces.get(from);
                        pieces.put(to, p);
                        pieces.remove(from);
                        p.setLocation(to);

                        chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                        chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                    } else {
                        PieceLocation from = new PieceLocation(8, 8);
                        PieceLocation to = new PieceLocation(6, 8);
                        Piece p = pieces.get(from);
                        pieces.put(to, p);
                        pieces.remove(from);
                        p.setLocation(to);

                        chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                        chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                    }
                }
            }

            pieces.remove(loc);
            pieces.put(selectedCase, piece);

            piece.setLocation(selectedCase);
            isWhiteToMove = !isWhiteToMove;

            return pieces;
        } else {
            Map<PieceLocation, Piece> piecesMap = copyPieces();
            PieceLocation loc = piece.getLocation();
            Piece copiedPiece = piece.copy();
            copiedPiece.setLocation(selectedCase);

            if(copiedPiece.getPiece() == ChessPiece.KING){
                if(isWhiteToMove){
                    gameInfo.setCanWhiteRock(false);
                    gameInfo.setCanWhiteQueenSideRock(false);
                } else {
                    gameInfo.setCanBlackRock(false);
                    gameInfo.setCanBlackQueenSideRock(false);
                }
            } else if(copiedPiece.getPiece() == ChessPiece.ROOK){
                if(copiedPiece.getLocation().equals(new PieceLocation(1, 8))){
                    gameInfo.setCanBlackQueenSideRock(false);
                } else if(copiedPiece.getLocation().equals(new PieceLocation(1, 1))){
                    gameInfo.setCanWhiteQueenSideRock(false);
                } else if(copiedPiece.getLocation().equals(new PieceLocation(8, 1))){
                    gameInfo.setCanWhiteRock(false);
                } else if(copiedPiece.getLocation().equals(new PieceLocation(8, 8))){
                    gameInfo.setCanBlackRock(false);
                }
            }

            if(copiedPiece.getPiece() == ChessPiece.PAWN && ((loc.getLine() == 2 && selectedCase.getLine() == 4) || (loc.getLine() == 7 && selectedCase.getLine() == 5))) {
                //Pion avancé de 2
                gameInfo.setWhiteAdvancedFromTwo(isWhiteToMove);
                gameInfo.setPawnAdvancedFromTwo(selectedCase);
            } else if(copiedPiece.getPiece() == ChessPiece.PAWN && (selectedCase.getLine() == 8 || selectedCase.getLine() == 1)){
                //Pion tranformé en Dame
                copiedPiece.setPiece(ChessPiece.QUEEN);
            } else {
                //Prise en passant
                PieceLocation l = gameInfo.getPawnAdvancedFromTwo();
                if(gameInfo.getPawnAdvancedFromTwo() != null && copiedPiece.getPiece() == ChessPiece.PAWN && selectedCase.equals(new PieceLocation(l.getColumn(), l.getLine() + (gameInfo.isWhiteAdvancedFromTwo() ? -1 : 1)))){
                    Piece p = piecesMap.get(l);
                    piecesMap.remove(gameInfo.getPawnAdvancedFromTwo());
                    chessBoard[l.getColumnInt() - 1][l.getLine() - 1] = 0;
                }
                gameInfo.setPawnAdvancedFromTwo(null);
            }

            if(copiedPiece.getPiece() == ChessPiece.KING){
                if(copiedPiece.isWhite() && copiedPiece.getLocation().distance(selectedCase) >= 2) {
                    if (selectedCase.equals(new PieceLocation(3, 1))) {
                        PieceLocation from = new PieceLocation(1, 1);
                        PieceLocation to = new PieceLocation(4, 1);
                        Piece p = piecesMap.get(from);
                        piecesMap.put(to, p);
                        piecesMap.remove(from);
                        p.setLocation(to);

                        chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                        chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                    } else {
                        PieceLocation from = new PieceLocation(8, 1);
                        PieceLocation to = new PieceLocation(6, 1);
                        Piece p = piecesMap.get(from);
                        piecesMap.put(to, p);
                        piecesMap.remove(from);
                        p.setLocation(to);

                        chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                        chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                    }
                } else if(!copiedPiece.isWhite() && copiedPiece.getLocation().distance(selectedCase) >= 2) {
                    if (selectedCase.equals(new PieceLocation(3, 8))) {
                        PieceLocation from = new PieceLocation(1, 8);
                        PieceLocation to = new PieceLocation(4, 8);
                        Piece p = piecesMap.get(from);
                        piecesMap.put(to, p);
                        piecesMap.remove(from);
                        p.setLocation(to);

                        chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                        chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                    } else {
                        PieceLocation from = new PieceLocation(8, 8);
                        PieceLocation to = new PieceLocation(6, 8);
                        Piece p = piecesMap.get(from);
                        piecesMap.put(to, p);
                        piecesMap.remove(from);
                        p.setLocation(to);

                        chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                        chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                    }
                }
            }

            piecesMap.remove(loc);
            piecesMap.put(selectedCase, copiedPiece);

            return piecesMap;
        }
    }

    public static Map<PieceLocation, Piece> simulateMove(Piece piece, Map<PieceLocation, Piece> piecesMap, PieceLocation selectedCase, boolean isWhiteToMove){
        PieceLocation loc = piece.getLocation();
        Piece copiedPiece = piece.copy();
        copiedPiece.setLocation(selectedCase);
        int[][] chessBoard = getBoardFromMap(piecesMap.values());

        if(copiedPiece.getPiece() == ChessPiece.KING){
            if(isWhiteToMove){
                gameInfo.setCanWhiteRock(false);
                gameInfo.setCanWhiteQueenSideRock(false);
            } else {
                gameInfo.setCanBlackRock(false);
                gameInfo.setCanBlackQueenSideRock(false);
            }
        } else if(copiedPiece.getPiece() == ChessPiece.ROOK){
            if(copiedPiece.getLocation().equals(new PieceLocation(1, 8))){
                gameInfo.setCanBlackQueenSideRock(false);
            } else if(copiedPiece.getLocation().equals(new PieceLocation(1, 1))){
                gameInfo.setCanWhiteQueenSideRock(false);
            } else if(copiedPiece.getLocation().equals(new PieceLocation(8, 1))){
                gameInfo.setCanWhiteRock(false);
            } else if(copiedPiece.getLocation().equals(new PieceLocation(8, 8))){
                gameInfo.setCanBlackRock(false);
            }
        }

        if(copiedPiece.getPiece() == ChessPiece.PAWN && ((loc.getLine() == 2 && selectedCase.getLine() == 4) || (loc.getLine() == 7 && selectedCase.getLine() == 5))) {
            //Pion avancé de 2
            gameInfo.setWhiteAdvancedFromTwo(isWhiteToMove);
            gameInfo.setPawnAdvancedFromTwo(selectedCase);
        } else if(copiedPiece.getPiece() == ChessPiece.PAWN && (selectedCase.getLine() == 8 || selectedCase.getLine() == 1)){
            //Pion tranformé en Dame
            copiedPiece.setPiece(ChessPiece.QUEEN);
        } else {
            //Prise en passant
            PieceLocation l = gameInfo.getPawnAdvancedFromTwo();
            if(gameInfo.getPawnAdvancedFromTwo() != null && copiedPiece.getPiece() == ChessPiece.PAWN && selectedCase.equals(new PieceLocation(l.getColumn(), l.getLine() + (gameInfo.isWhiteAdvancedFromTwo() ? -1 : 1)))){
                Piece p = piecesMap.get(l);
                piecesMap.remove(gameInfo.getPawnAdvancedFromTwo());
                chessBoard[l.getColumnInt() - 1][l.getLine() - 1] = 0;
            }
            gameInfo.setPawnAdvancedFromTwo(null);
        }

        if(copiedPiece.getPiece() == ChessPiece.KING){
            if(copiedPiece.isWhite() && copiedPiece.getLocation().distance(selectedCase) >= 2) {
                if (selectedCase.equals(new PieceLocation(3, 1))) {
                    PieceLocation from = new PieceLocation(1, 1);
                    PieceLocation to = new PieceLocation(4, 1);
                    Piece p = piecesMap.get(from);
                    piecesMap.put(to, p);
                    piecesMap.remove(from);
                    p.setLocation(to);

                    chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                    chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                } else {
                    PieceLocation from = new PieceLocation(8, 1);
                    PieceLocation to = new PieceLocation(6, 1);
                    Piece p = piecesMap.get(from);
                    piecesMap.put(to, p);
                    piecesMap.remove(from);
                    p.setLocation(to);

                    chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                    chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                }
            } else if(!copiedPiece.isWhite() && copiedPiece.getLocation().distance(selectedCase) >= 2) {
                if (selectedCase.equals(new PieceLocation(3, 8))) {
                    PieceLocation from = new PieceLocation(1, 8);
                    PieceLocation to = new PieceLocation(4, 8);
                    Piece p = piecesMap.get(from);
                    piecesMap.put(to, p);
                    piecesMap.remove(from);
                    p.setLocation(to);

                    chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                    chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                } else {
                    PieceLocation from = new PieceLocation(8, 8);
                    PieceLocation to = new PieceLocation(6, 8);
                    Piece p = piecesMap.get(from);
                    piecesMap.put(to, p);
                    piecesMap.remove(from);
                    p.setLocation(to);

                    chessBoard[from.getColumnInt() - 1][from.getLine() - 1] = 0;
                    chessBoard[to.getColumnInt() - 1][to.getLine() - 1] = p.getId();
                }
            }
        }

        piecesMap.remove(loc);
        piecesMap.put(selectedCase, copiedPiece);

        return piecesMap;
    }

    private static Map<PieceLocation, Piece> copyPieces() {
        Map<PieceLocation, Piece> copy = new HashMap<>();
        for(Map.Entry<PieceLocation, Piece> entry : pieces.entrySet()){
            PieceLocation loc = entry.getKey();
            copy.put(new PieceLocation(loc.getColumn(), loc.getLine()), entry.getValue().copy());
        }
        return copy;
    }


    public static boolean checkMate(boolean white, Collection<Piece> pieces, GameInfo info){
        for(Piece piece : pieces){
            if(piece.getAvailableMoves(pieces, true, white, info).size() > 0){
                return false;
            }
        }
        System.out.println("Check Mated !");
        ChessManager.generateTestBoard();
        return true;
    }

    public static boolean isInCheck(boolean white, Collection<Piece> pieces, GameInfo info){
        List<Piece> piecesList = new ArrayList<>(pieces);
        Piece king = null;
        for(Piece piece : piecesList){
            if(piece.getPiece() == ChessPiece.KING && white == piece.isWhite()){
                king = piece;
                break;
            }
        }

        if(king == null)
            return true;

        for(Piece piece : piecesList){
            if(piece.isWhite() != white){
                if(piece.getAvailableMoves(pieces, false, isWhiteToMove, info).contains(king.getLocation()))
                    return true;
            }
        }

        return false;
    }

    public static boolean isInCheck(boolean white, int[][] board){
        /*Piece king = null;
        for(Piece piece : pieces.values()){
            if(piece.getPiece() == ChessPiece.KING && white == piece.isWhite()){
                king = piece;
                break;
            }
        }

        if(king == null)
            return true;

        for(Piece piece : pieces.values()){
            if(piece.isWhite() != white){
                if(piece.getAvailableMoves().contains(king.getLocation()))
                    return true;
            }
        }

        return false;*/
        int[][] tempBoard = Arrays.copyOf(board, board[0].length);

        for(int i = 0; i < tempBoard.length; i++){
            for(int j = 0; j < tempBoard.length; j++){

            }
        }

        return false;
    }

    //TODO retirer l'usage de ces doubles tableaux et n'utiliser que des collections / map
    public static int[][] getBoardFromMap(Collection<Piece> pieces) {
        int[][] board = new int[8][8];

        for(int i = 0; i < 64; i++){
            board[i/8][i%8] = 0;
        }

        for(Piece piece : pieces){
            board[piece.getLocation().getColumnInt() - 1][piece.getLocation().getLine() - 1] = (piece.isWhite() ? 1 : -1) * piece.getPiece().getId();
        }

        return board;
    }
}
