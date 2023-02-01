package fr.tt54.chessgame;

import fr.tt54.chessgame.game.ChessManager;
import fr.tt54.chessgame.game.ChessMouseListener;
import fr.tt54.chessgame.graphic.ChessGraphicManager;
import fr.ttgraphiclib.GraphicManager;
import fr.ttgraphiclib.thread.Frame;

public class ChessGame {


    public static void main(String[] args) {
        ChessGraphicManager.enable();

        GraphicManager.setMaxFPS(30);
        GraphicManager.setMaxMovePerSecond(30);
        Frame frame = new Frame("Chess", 900, 900);
        GraphicManager.enable(frame, ChessGraphicManager.panel);
        GraphicManager.registerMouseListener(new ChessMouseListener());

        ChessManager.enable();
    }

}
