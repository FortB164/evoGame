package utilities.logger.dataObjects;

import java.util.ArrayDeque;

public class Log {

    ArrayDeque<String> actions;
    ArrayDeque<ErrorEntry> errors;

    public Log(){
        actions = new ArrayDeque<>();
        errors = new ArrayDeque<>();
    }

    public void addAction(String action){
        actions.push(action);
    }
    public  void addError(ErrorEntry error){
        errors.push(error);
    }
    public String getAction(int depthFromTop) {
        return actions.stream()
                .skip(depthFromTop)
                .findFirst()
                .orElse(null);
    }
    public ErrorEntry getError(int depthFromTop) {
        return errors.stream()
                .skip(depthFromTop)
                .findFirst()
                .orElse(null);
    }
    public String getAction() {
        return getAction(0);
    }
    public ErrorEntry getError() {
        return getError(0);
    }
    public void clearActions(){
        actions.clear();
    }
    public void clearErrors(){
        errors.clear();
    }
    public ArrayDeque<String> getActionsLog(){
        return actions;
    }
    public ArrayDeque<ErrorEntry> getErrorsLog(){
        return errors;
    }
}