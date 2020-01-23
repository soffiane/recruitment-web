package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.exception.AlreadyBorrowedBookException;
import fr.d2factory.libraryapp.exception.BookNotFoundException;
import fr.d2factory.libraryapp.exception.HasLateBooksException;
import fr.d2factory.libraryapp.exception.InsufficientFundsException;
import fr.d2factory.libraryapp.member.ResidentMember;
import fr.d2factory.libraryapp.member.StudentMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;
    private static List<Book> books;


    @BeforeEach
    void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        books= new ArrayList<>();
        mapper.readTree(new File("src/test/resources/books.json")).forEach(this::parseJsonBookObject);
        bookRepository = new BookRepository();
        library = new LibraryImpl(bookRepository);
        bookRepository.addBooks(books);
    }

    private void parseJsonBookObject(JsonNode bookJsonNode) {
        String title = bookJsonNode.path("title").asText();
        String author = bookJsonNode.path("author").asText();
        Long isbnCode = bookJsonNode.path("isbn").path("isbnCode").asLong();
        Book book = new Book(title, author, new ISBN(isbnCode));
        books.add(book);
    }

    @Test
    void member_can_borrow_a_book_if_book_is_available(){
        StudentMember studentMember = new StudentMember(50.0f, LocalDate.now());
        Book book = library.borrowBook(46578964513l, studentMember, LocalDate.now());
        Assertions.assertNotNull(book,"we should be able to borrow a book since it is available");
    }

    @Test
    void borrowed_book_is_no_longer_available(){
        ResidentMember residentMember = new ResidentMember(50.0f);
        library.borrowBook(3326456467846L, residentMember, LocalDate.now());
        StudentMember studentMember = new StudentMember(50.0f, LocalDate.now());
        Assertions.assertThrows(BookNotFoundException.class, () -> {
            library.borrowBook(3326456467846L, studentMember, LocalDate.now());
        },"borrowing an unavailable book should result to a BookNotFoundException exception");
    }

    @Test
    void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
        ResidentMember residentMember = new ResidentMember(50.0f);
        Book book = library.borrowBook(46578964513L, residentMember, LocalDate.now().minusDays(50));
        library.returnBook(book,residentMember);
        Assertions.assertEquals(45.0f,residentMember.getWallet(),"The resident amount of money does not match the amount expected after paying for his book");
    }

    @Test
    void students_pay_10_cents_the_first_30days(){
        StudentMember studentMember = new StudentMember(50.0f, LocalDate.now().minusMonths(13));
        Book book = library.borrowBook(46578964513L, studentMember, LocalDate.now().minusDays(30));
        library.returnBook(book, studentMember);
        Assertions.assertEquals(47.0f, studentMember.getWallet(),"The student amount of money does not match the amount expected after paying for his book");
    }

    @Test
    void students_in_1st_year_are_not_taxed_for_the_first_15days(){
        StudentMember studentMember = new StudentMember(50.0f,LocalDate.now().minusMonths(6));
        Book book = library.borrowBook(46578964513L, studentMember, LocalDate.now().minusDays(30));
        library.returnBook(book, studentMember);
        Assertions.assertEquals(48.5f, studentMember.getWallet(),"The student amount of money does not match the amount expected after paying for his book");
    }
    
    @Test
    void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
        ResidentMember residentMember = new ResidentMember(50.0f);
        Book book = library.borrowBook(46578964513L, residentMember, LocalDate.now().minusDays(80));
        library.returnBook(book, residentMember);
        Assertions.assertEquals(40.0,residentMember.getWallet(),"The resident amount of money does not match the amount expected after paying for his book");

    }

    @Test
    void student_cannot_borrow_book_if_they_have_late_books(){
        StudentMember studentMember = new StudentMember(50.0f,LocalDate.now());
        library.borrowBook(46578964513L, studentMember, LocalDate.now().minusDays(31) );

        Assertions.assertThrows(HasLateBooksException.class, () -> {
            library.borrowBook(968787565445L, studentMember, LocalDate.now());
        },"a student having late books cannot borrow new books");
    }

    @Test
    void resident_cannot_borrow_book_if_they_have_late_books(){
        ResidentMember residentMember = new ResidentMember(50.0f);
        library.borrowBook(46578964513L, residentMember, LocalDate.now().minusDays(61) );

        Assertions.assertThrows(HasLateBooksException.class, () -> {
            library.borrowBook(968787565445L, residentMember, LocalDate.now());
        },"a resident having late books cannot borrow new books");
    }
    
    @Test
    void member_cannot_return_books_if_he_hasnt_enough_money(){
        ResidentMember residentMember = new ResidentMember(10.0f);
        Book book = library.borrowBook(46578964513L, residentMember, LocalDate.now().minusMonths(6));
        Assertions.assertThrows(InsufficientFundsException.class, () -> {
            library.returnBook(book, residentMember);
        },"a member cant return book if he cant afford it");
    }

    @Test
    void member_cant_borrow_same_book_twice(){
        ResidentMember residentMember = new ResidentMember(10.0f);
        library.borrowBook(46578964513L, residentMember, LocalDate.now());
        Assertions.assertThrows(AlreadyBorrowedBookException.class, () -> {
            library.borrowBook(46578964513L, residentMember, LocalDate.now());
        },"a member cant borrow twice the same book");
    }

}
