package utilities;

import java.util.Stack;

public class Logger {
    static Logger logger;

    public Stack<String> actions = new Stack<>();

    private Logger(){

    }

    public static Logger getLogger(){
        if(logger == null) logger = new Logger();
        return logger;
    }

    public void addAction(String message){
        actions.push(message);
    }

    public String getAction(){
        return actions.peek();
    }

    public void clearActions(){
        actions.clear();
    }

    public String getSimLog(){
        String [] messages = new String[actions.size()];

        for(int i = actions.size()-1; i >= 0; i--){
            messages[i] = actions.pop();
        }

        return String.join(",\n", messages);
    }
}
