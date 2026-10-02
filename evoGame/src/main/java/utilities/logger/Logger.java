package utilities.logger;

import utilities.logger.dataObjects.ErrorEntry;
import utilities.logger.dataObjects.Log;

import java.util.Iterator;


public final class Logger {

    private static volatile Logger instance;

    Log log = new Log();
    HaltService haltService = new HaltService(3, log);

    private Logger() { }

    public static Logger getLogger() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) instance = new Logger();
            }
        }
        return instance;
    }

    // ---- writes -------------------------------------------------------

    public synchronized void logAction(String actionMessage) {
        log.addAction(actionMessage);
    }

    public synchronized void logError(Exception exception) {
        String exceptionMessage = exception.getMessage();
        ErrorEntry errorEntry = new ErrorEntry(exceptionMessage, haltService.getSeverity(exceptionMessage));
        log.addError(errorEntry);
        haltService.halt(exceptionMessage);
    }

    // ---- reads (GUI) --------------------------------------------------

    public synchronized String getLatestAction(){
        return log.getAction();
    }

    // gets nth action recorded below the top
    public synchronized String getAction(int n){
        return log.getAction(n);
    }

    public synchronized String getLatestError(){
        return log.getError().toString();
    }

    // gets the nth error recorded below the top
    public synchronized String getError(int n){
        return log.getError(n).toString();
    }

    // ---- clears -------------------------------------------------------

    public synchronized void clearActions()    { log.clearActions(); }
    public synchronized void clearErrors() { log.clearErrors(); }

    // ---- separated logs ----------------------------------------------

    public synchronized String getActionLog() {
        StringBuilder result = new StringBuilder();
        Iterator<String> it = log.getActionsLog().descendingIterator();
        while (it.hasNext()) {
            result.append(it.next()).append("\n");
        }
        return result.toString();
    }

    public synchronized String getErrorLog() {
        StringBuilder result = new StringBuilder();
        for (Iterator<ErrorEntry> it = log.getErrorsLog().descendingIterator(); it.hasNext(); ) {
            ErrorEntry entry = it.next();
            result
                    .append("[")
                    .append(entry.getSeverity())
                    .append("]")
                    .append(": ")
                    .append(entry.getMessage())
                    .append("\n");
        }
        return result.toString();
    }
}