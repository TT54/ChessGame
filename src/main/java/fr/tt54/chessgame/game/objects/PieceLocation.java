package fr.tt54.chessgame.game.objects;

import java.util.Objects;

public class PieceLocation {

    private char column;
    private int line;

    public PieceLocation(char column, int line) {
        this.column = column;
        this.line = line;
    }

    public PieceLocation(int column, int line){
        this.column = (char) (column + 96);
        this.line = line;
    }

    public char getColumn() {
        return column;
    }

    public int getColumnInt(){
        return (int) column - 96;
    }

    public void setColumn(char column) {
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getGraphicX(){
        return (this.getColumnInt() - 1) * 91 + 87;
    }

    public int getGraphicY(){
        return (8 - this.getLine()) * 91 + 88;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceLocation location = (PieceLocation) o;
        return column == location.column && line == location.line;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, line);
    }

    public int distance(PieceLocation other) {
        int line = this.getLine() - other.getLine();
        int column = this.getColumnInt() - other.getColumnInt();
        return (int) Math.sqrt(line * line + column * column);
    }
}
