package utilities.logger.exceptions;

import utilities.logger.dataObjects.Constants;

public class SampleCustomException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public SampleCustomException(){
        super(Constants.FAILURE_MESSAGE);
    }
}