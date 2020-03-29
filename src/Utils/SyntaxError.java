package Utils;

public class SyntaxError extends RuntimeException {
    private String error;
    private Position pos;

    public SyntaxError(String er, Position po) {
        error = er; pos = po;
    }
}
