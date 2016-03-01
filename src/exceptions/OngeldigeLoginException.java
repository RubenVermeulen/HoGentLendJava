package exceptions;

public class OngeldigeLoginException extends RuntimeException{

    public OngeldigeLoginException() {
    }

    public OngeldigeLoginException(String message) {
        super(message);
    }

    public OngeldigeLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public OngeldigeLoginException(Throwable cause) {
        super(cause);
    }

    public OngeldigeLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
