package utilities.logger.exceptions;
@Deprecated
public class CoordinatesEmptyError extends RuntimeException {
    public CoordinatesEmptyError() { super("Coordinates are empty."); }
}
