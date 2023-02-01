package fr.tt54.chessgame.game.objects;

public class GameInfo {

    private boolean canWhiteRock;
    private boolean canWhiteQueenSideRock;

    private boolean canBlackRock;
    private boolean canBlackQueenSideRock;

    private boolean isWhiteAdvancedFromTwo;
    private PieceLocation pawnAdvancedFromTwo;

    public GameInfo(boolean canWhiteRock, boolean canWhiteQueenSideRock, boolean canBlackRock, boolean canBlackQueenSideRock, boolean isWhiteAdvancedFromTwo, PieceLocation pawnAdvancedFromTwo) {
        this.canWhiteRock = canWhiteRock;
        this.canWhiteQueenSideRock = canWhiteQueenSideRock;
        this.canBlackRock = canBlackRock;
        this.canBlackQueenSideRock = canBlackQueenSideRock;
        this.isWhiteAdvancedFromTwo = isWhiteAdvancedFromTwo;
        this.pawnAdvancedFromTwo = pawnAdvancedFromTwo;
    }

    public static GameInfo generate() {
        return new GameInfo(true, true, true, true, true,null);
    }

    public GameInfo copy(){
        return new GameInfo(canWhiteRock, canWhiteQueenSideRock, canBlackRock, canBlackQueenSideRock, isWhiteAdvancedFromTwo, pawnAdvancedFromTwo);
    }

    public boolean isWhiteAdvancedFromTwo() {
        return isWhiteAdvancedFromTwo;
    }

    public void setWhiteAdvancedFromTwo(boolean whiteAdvancedFromTwo) {
        isWhiteAdvancedFromTwo = whiteAdvancedFromTwo;
    }

    public boolean canWhiteRock() {
        return canWhiteRock;
    }

    public void setCanWhiteRock(boolean canWhiteRock) {
        this.canWhiteRock = canWhiteRock;
    }

    public boolean canWhiteQueenSideRock() {
        return canWhiteQueenSideRock;
    }

    public void setCanWhiteQueenSideRock(boolean canWhiteQueenSideRock) {
        this.canWhiteQueenSideRock = canWhiteQueenSideRock;
    }

    public boolean canBlackRock() {
        return canBlackRock;
    }

    public void setCanBlackRock(boolean canBlackRock) {
        this.canBlackRock = canBlackRock;
    }

    public boolean canBlackQueenSideRock() {
        return canBlackQueenSideRock;
    }

    public void setCanBlackQueenSideRock(boolean canBlackQueenSideRock) {
        this.canBlackQueenSideRock = canBlackQueenSideRock;
    }

    public PieceLocation getPawnAdvancedFromTwo() {
        return pawnAdvancedFromTwo;
    }

    public void setPawnAdvancedFromTwo(PieceLocation pawnAdvancedFromTwo) {
        this.pawnAdvancedFromTwo = pawnAdvancedFromTwo;
    }
}