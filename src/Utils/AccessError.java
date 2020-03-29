package Utils;

public class AccessError extends RuntimeException {
    private String error;
    private Position pos;

    public AccessError(String er, Position po) {
        error = er; pos = po;
    }
}
