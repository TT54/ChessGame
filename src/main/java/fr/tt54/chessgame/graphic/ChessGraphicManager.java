package fr.tt54.chessgame.graphic;

import fr.tt54.chessgame.graphic.nodes.AvailableMoveNode;
import fr.tt54.chessgame.graphic.nodes.BoardNode;
import fr.tt54.chessgame.graphic.nodes.PieceNode;
import fr.ttgraphiclib.graphics.GraphicPanel;

import java.util.List;

public class ChessGraphicManager {

    public static final GraphicPanel panel = new GraphicPanel();

    private static BoardNode board;
    private static List<PieceNode> pieces;


    public static void enable(){
        board = new BoardNode(panel, -400, -400, 800, 800);
    }

    public static void removeMoveNode(){
        panel.getNodes().removeIf(node -> node instanceof AvailableMoveNode);
    }

}
