package fr.tt54.chessgame.graphic.nodes;

import fr.tt54.chessgame.game.objects.ChessPiece;
import fr.tt54.chessgame.game.objects.PieceLocation;
import fr.ttgraphiclib.graphics.GraphicPanel;
import fr.ttgraphiclib.graphics.events.NodeClickedEvent;
import fr.ttgraphiclib.graphics.interfaces.ClickAction;
import fr.ttgraphiclib.graphics.nodes.ImageNode;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class PieceNode extends ImageNode {

    private ChessPiece piece;
    private final boolean white;
    private PieceLocation location;

    public PieceNode(GraphicPanel panel, ChessPiece piece, boolean white, PieceLocation location) {
        super(panel, 0, 0, 100, 100, getPieceImage(piece, white));
        this.piece = piece;
        this.white = white;
        this.location = location;
    }

    public void setPiece(ChessPiece piece) {
        try {
            this.image = ImageIO.read(getPieceImage(piece, white));
        } catch (IOException var12) {
            var12.printStackTrace();
        }
        this.piece = piece;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public PieceLocation getLocation() {
        return location;
    }

    public void setLocation(PieceLocation location) {
        this.location = location;
    }

    private static URL getPieceImage(ChessPiece piece, boolean white) {
        return PieceNode.class.getResource("/" + (white ? "white_" : "black_") + piece.name().toLowerCase() + ".png");
    }

    public void placePiece(PieceLocation location){
        this.location = location;
        int column = (int) location.getColumn() - 97;
        int x = -369 + (column) * 91;
        this.setX(x);

        int row = 8 - location.getLine();
        int y = -369 + row * 91;
        this.setY(y);
    }

    public boolean isWhite() {
        return this.white;
    }
}
