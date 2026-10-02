package utilities.logger.exceptions;

@Deprecated
public class CoordinatesOutOfBoundsError extends RuntimeException {
    public CoordinatesOutOfBoundsError() {
        super("Coordinates are out of bounds.");
    }
}
