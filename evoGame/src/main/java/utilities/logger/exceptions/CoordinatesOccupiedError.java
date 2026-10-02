package utilities.logger.exceptions;

@Deprecated
public class CoordinatesOccupiedError extends RuntimeException {
    public CoordinatesOccupiedError() {
        super("Coordinates are already occupied.");
    }
}
