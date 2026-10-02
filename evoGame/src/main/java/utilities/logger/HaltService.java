package utilities.logger;

import utilities.logger.dataObjects.ErrorEntry;
import utilities.logger.dataObjects.Log;
import utilities.logger.dataObjects.Severity;

public final class HaltService {

    private int mediumStreakThreshold = 3;
    private final Log log;

    SeverityResolver resolver = new SeverityResolver();

    public HaltService(int mediumStreakThreshold, Log log) {
        this.log  = log;
        if (mediumStreakThreshold < 1) {
            throw new IllegalArgumentException("mediumStreakThreshold must be >= 1");
        }
        this.mediumStreakThreshold = mediumStreakThreshold;
    }

    // wrapper for logger
    public Severity getSeverity(String message) {
        return resolver.getSeverity(message);
    }

    public void halt(String errMessage){
        Severity severity = resolver.getSeverity(errMessage);
        if (shouldHalt(severity)){
            System.err.println("HaltService: halting due to severity=" + errMessage + "[" +  severity + "]");
            System.exit(1);
        }
    }

    public boolean shouldHalt(Severity severity) {
        if (severity == null) return false;
        return switch (severity) {
            case UNKNOWN, HIGH -> true;
            case MEDIUM -> consecutiveMediums() >= mediumStreakThreshold;
            case LOW    -> false;
        };
    }

    private int consecutiveMediums() {
        int count = 0;
        var entries = log.getErrorsLog();
        var it = entries.descendingIterator();
        while (it.hasNext()) {
            ErrorEntry entry = it.next();
            if (entry.getSeverity() == Severity.MEDIUM) {
                count++;
                continue;
            }
            break;
        }
        return count;
    }
}