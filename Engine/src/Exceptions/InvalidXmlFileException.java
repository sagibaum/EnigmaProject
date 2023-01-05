package Exceptions;

public class InvalidXmlFileException extends RuntimeException {

    private final String Message;

    public InvalidXmlFileException(String message) {
        this.Message = message;
    }

    @Override
    public String toString() {
        return "InvalidXmlFileException{" +
                "Message='" + Message + '\'' +
                '}';
    }

    @Override
    public synchronized Throwable getCause() {
        return new Throwable(this.Message);

    }
}
