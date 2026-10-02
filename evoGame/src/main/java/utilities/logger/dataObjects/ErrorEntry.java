package utilities.logger.dataObjects;
public class ErrorEntry {
    String message;
    Severity severity;

    public ErrorEntry(String message, Severity severity) {
        this.message = message;
        this.severity = severity;
    }

    @Override
    public String toString(){
        return "[" + severity + "] " + message;
    }
    public String getMessage() {
        return message;
    }
    public Severity getSeverity() {
        return severity;
    }
}