package fr.d2factory.libraryapp.book;

import fr.d2factory.libraryapp.exception.BookNotFoundException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks;
    private Map<Book, LocalDate> borrowedBooks;

    /**
     * Instantiates a new Book repository.
     */
    public BookRepository() {
        this.availableBooks = new HashMap<>();
        this.borrowedBooks = new HashMap<>();
    }

    /**
     * Add books.
     *
     * @param books the books
     */
    public void addBooks(List<Book> books) {
        if (books != null && !books.isEmpty()) {
            availableBooks.putAll(books.stream().collect(Collectors.toMap(Book::getIsbn, Function.identity())));
        }
    }

    /**
     * Find book .
     *
     * @param isbnCode the isbn code
     * @return the book or an exception is thrown if not found
     */
    public Book findBook(long isbnCode) {
        return availableBooks.entrySet().stream()
                .filter(isbnBookEntry -> isbnBookEntry.getValue().getIsbn().getIsbnCode() == isbnCode)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("book with isbncode " + isbnCode + " was not found"))
                .getValue();
    }

    /**
     * Save book borrowed by a member.
     *
     * @param book       the book borrowed
     * @param borrowedAt the date the book is borrowed
     */
    public void saveBookBorrow(Book book, LocalDate borrowedAt) {
        if (book != null) {
            borrowedBooks.put(book, borrowedAt);
            availableBooks.remove(book.getIsbn());
        }
    }

    /**
     * Find borrowed book date.
     *
     * @param book the book
     * @return date the book was borrowed
     */
    public LocalDate findBorrowedBookDate(Book book) {
        return book == null ? null : borrowedBooks.get(book);
    }

    /**
     * Restore book returned into the available book list
     * and also remove it from the borrowedbooks list.
     *
     * @param book  the book returned
     */
    public void returnBook(Book book) {
        availableBooks.putIfAbsent(book.getIsbn(),book);
        borrowedBooks.remove(book);
    }
}
