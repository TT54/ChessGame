package fr.tt54.chessgame.graphic.nodes;

import fr.ttgraphiclib.graphics.GraphicPanel;
import fr.ttgraphiclib.graphics.nodes.ImageNode;

public class BoardNode extends ImageNode {
    public BoardNode(GraphicPanel panel, double x, double y, double width, double height) {
        super(panel, x, y, width, height, BoardNode.class.getResource("/chess_board.png"));
    }
}
