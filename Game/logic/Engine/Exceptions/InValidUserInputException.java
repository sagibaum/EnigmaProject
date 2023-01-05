package Exceptions;

public class InValidUserInputException extends RuntimeException{
    private final String Message;
    public InValidUserInputException(String message) {
        this.Message=message;
    }

    @Override
    public String toString() {
        return "InValidUserInputException{" +
                "Message='" + Message + '\'' +
                '}';
    }

    @Override
    public synchronized Throwable getCause() {
        return new Throwable("Invalid input:\n"+this.Message);
    }

    @Override
    public String getMessage() {
        return this.Message;
    }
}
