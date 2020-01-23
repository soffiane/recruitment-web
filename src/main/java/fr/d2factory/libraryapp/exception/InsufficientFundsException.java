package fr.d2factory.libraryapp.exception;

/**
 * Exception thrown when a member has run out of money and can't pay for his books
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
