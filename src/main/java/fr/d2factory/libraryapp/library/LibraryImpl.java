package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.exception.BookNotFoundException;
import fr.d2factory.libraryapp.exception.HasLateBooksException;
import fr.d2factory.libraryapp.member.Member;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * The Library and its features.
 */
public class LibraryImpl implements Library {

    private BookRepository bookRepository;

    /**
     * Instantiates a new LibraryImpl.
     *
     * @param bookRepository the book repository
     */
    public LibraryImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
        //retrieve the book list of the member
        List<Book> listOfBookBorrowed = member.getBooks();
        //check if the member is late for at least one of the book he borrowed so he can't borrow new ones
        if (listOfBookBorrowed != null && listOfBookBorrowed.size() > 0) {
            listOfBookBorrowed.forEach(
                    book -> {
                        if (ChronoUnit.DAYS.between(bookRepository.findBorrowedBookDate(book), LocalDate.now()) > member.getLimitDaysForBook()) {
                            throw new HasLateBooksException("member can't borrow books since he has books to return");
                        }
                    }
            );
        }
        //check if the book the member want to borrow is available
        Book book = bookRepository.findBook(isbnCode);
        //if not, launch an exception
        if (book == null) {
            throw new BookNotFoundException("This book has already been borrowed by someone else");
        }
        //if available, first we add the book to the member book list (can cast an exception)
        member.addBook(book);
        //and then, save the book to the borrowed book list and remove it from the available book list
        bookRepository.saveBookBorrow(book, borrowedAt);
        return book;
    }

    @Override
    public void returnBook(Book book, Member member) {
        //retrieve the date when the book was borrowed
        LocalDate dateBorrow = bookRepository.findBorrowedBookDate(book);
        //calculate the amount to pay when returning the book
        member.payBook((int) ChronoUnit.DAYS.between(dateBorrow, LocalDate.now()));
        //the returned book is back to the available book list
        bookRepository.returnBook(book);
        //and removed from the book list of the member
        member.removeBook(book);
    }
}
