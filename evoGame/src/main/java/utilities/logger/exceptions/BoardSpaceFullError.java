package utilities.logger.exceptions;

@Deprecated
public class BoardSpaceFullError extends RuntimeException {
    public BoardSpaceFullError() {
        super("Board is full, no more spaces left.");
    }
}
