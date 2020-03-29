package Utils;

import org.antlr.v4.runtime.Token;

public class Position {
    private int row, col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(Token token) {
        if (token == null)
            this.row = this.col = 0;
        else {
            this.row = token.getLine();
            this.col = token.getCharPositionInLine();
        }
    }

    public String toString() {return "(" + row + ", " + col + ")";}

    public int getRow() {return row;}

    public int getCol() {return col;}
}
