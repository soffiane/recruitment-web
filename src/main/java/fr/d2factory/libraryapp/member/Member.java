package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.exception.AlreadyBorrowedBookException;
import fr.d2factory.libraryapp.library.Library;

import java.util.ArrayList;
import java.util.List;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {

    /**
     * An initial sum of money the member has
     */
    private float wallet;

    /**
     * List of books borrowed by a Member
     */
    private List<Book> books;

    /**
     * Instantiates a new Member.
     *
     * @param initialWallet the initial wallet
     */
    public Member(float initialWallet) {
        this.wallet = initialWallet;
        this.books = new ArrayList<>();
    }

    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);

    /**
     * Gets wallet.
     *
     * @return the wallet
     */
    public float getWallet() {
        return wallet;
    }

    /**
     * Sets wallet.
     *
     * @param wallet the wallet
     */
    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    /**
     * Return the limit of days to borrow a book
     *
     * @return numbers of max days borrowing a book
     */
    public abstract long getLimitDaysForBook();

    /**
     * Remove a book.
     *
     * @param book the book
     */
    public void removeBook(Book book){
        books.remove(book);
    }

    /**
     * Add a book.
     *
     * @param book the book
     */
    public void addBook(Book book){
        //check if we already added a book with the same isbnCode
        if (books.stream().anyMatch(book1 -> (book1.getIsbn().getIsbnCode() == book.getIsbn().getIsbnCode()))) {
            throw new AlreadyBorrowedBookException("Book with ISBN "+book.getIsbn()+" is already borrowed");
        } else {
            books.add(book);
        }
    }

    /**
     * Get borrowed books list.
     *
     * @return the book list
     */
    public List<Book> getBooks(){
        return books;
    }
}
