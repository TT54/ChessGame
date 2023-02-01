package fr.tt54.chessgame.graphic.nodes;

import fr.ttgraphiclib.graphics.GraphicPanel;
import fr.ttgraphiclib.graphics.nodes.RectangleNode;

import java.awt.*;

public class AvailableMoveNode extends RectangleNode {
    public AvailableMoveNode(GraphicPanel panel, double x, double y, double baseWidth, double baseHeight, Color color) {
        super(panel, x - 450, y - 450, baseWidth, baseHeight, color);
    }
}
