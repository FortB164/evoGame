package utilities.logger;

import utilities.logger.dataObjects.Constants;
import utilities.logger.dataObjects.Severity;

import java.util.Map;

public final class SeverityResolver {

    private static final Map<String, Severity> MESSAGE_TO_SEVERITY = Map.of(
            Constants.FAILURE_MESSAGE,  Severity.HIGH,
            Constants.FAILURE_MESSAGE2, Severity.LOW
    );

    public SeverityResolver() { }

    public Severity getSeverity(String message) {
        return MESSAGE_TO_SEVERITY.getOrDefault(message, Severity.UNKNOWN);
    }
}