package fr.d2factory.libraryapp.exception;

/**
 * Exception thrown when you try to borrow the same book twice
 */
public class AlreadyBorrowedBookException extends RuntimeException {
    public AlreadyBorrowedBookException(String message) {
        super(message);
    }
}
