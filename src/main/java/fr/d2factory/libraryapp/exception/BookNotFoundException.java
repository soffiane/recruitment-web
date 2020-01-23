package fr.d2factory.libraryapp.exception;

/**
 * Exception thrown when we dont find a book
 */
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
