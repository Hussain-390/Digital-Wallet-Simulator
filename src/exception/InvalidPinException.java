package exception;

/**
 * Exception thrown when a user enters an incorrect PIN during validation
 * or while changing their PIN.
 */
public class InvalidPinException extends Exception {
    public InvalidPinException(String message) {
        super(message);
    }
}
