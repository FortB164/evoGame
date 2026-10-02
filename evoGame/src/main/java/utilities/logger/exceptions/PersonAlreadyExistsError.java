package utilities.logger.exceptions;

@Deprecated
public class PersonAlreadyExistsError extends RuntimeException {
    public PersonAlreadyExistsError(){super("Person already exists.");}
}
