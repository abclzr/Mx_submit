package Utils;

public class SemanticError extends RuntimeException {
    private String error;
    private Position pos;

    public SemanticError(String er, Position po) {
        error = er; pos = po;
    }

    @Override
    public String getMessage() {
        return pos.toString() + error;
    }
}
