package fr.d2factory.libraryapp.book;

/**
 * A simple representation of a book
 */
public class Book {
    private String title;
    private String author;
    private ISBN isbn;

    public Book() {
    }

    public Book(String title, String author, ISBN isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public ISBN getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        return "Book { " +
                "title= '" + title + '\'' +
                ", author= '" + author + '\'' +
                ", isbn= " + isbn +
                '}';
    }
}
