package exception;

/**
 * Exception thrown when a user attempts to perform a transaction
 * (like sending money) without having sufficient funds in their wallet balance.
 */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
