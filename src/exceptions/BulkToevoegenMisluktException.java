package exceptions;

public class BulkToevoegenMisluktException extends Exception {

    public BulkToevoegenMisluktException() {
    }

    public BulkToevoegenMisluktException(String message) {
        super(message);
    }

    public BulkToevoegenMisluktException(String message, Throwable cause) {
        super(message, cause);
    }

    public BulkToevoegenMisluktException(Throwable cause) {
        super(cause);
    }

    public BulkToevoegenMisluktException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
