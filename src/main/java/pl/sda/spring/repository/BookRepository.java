package pl.sda.spring.repository;

import org.springframework.stereotype.Repository;
import pl.sda.spring.model.Book;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Repository
public class BookRepository {

    private Set<Book> books = initialize();

    private Set<Book> initialize() {
        return Set.of(new Book(1L, "Kaczanowski", "Testy", null),
                new Book(2L, "Rowling", "Harry Potter", null),
                new Book(3L, "Tolkien", "Wladca Pierscieni", null),
                new Book(4L, "Bloch", "Effective Java", null),
                new Book(5L, "Dostojewski", "Zbrodnia i kara", null));
    }

    public Optional<Book> borrowBook(String title, LocalDate borrowedTill) {
        Optional<Book> foundBook = books.stream()
                .filter(book -> title.equals(book.getTitle()))
                .filter(book -> book.getBorrowedTill() == null)
                .findAny();
        if (foundBook.isPresent()) {
            Book book = foundBook.get();
            book.setBorrowedTill(borrowedTill);
        }
        return foundBook;
    }

    public Book add(String author, String title) {
        Long nextId = generateNextId();
        Book bookToAdd = new Book(nextId, author, title, null);
        books.add(bookToAdd);
        return bookToAdd;
    }

    public void remove(Long id) {
        books.remove(books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElseThrow()
        );
    }

    public Optional<Book> returnBook(Long id) {
        Optional<Book> bookToReturn = books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
        if(bookToReturn.isPresent()){
            bookToReturn.get().setBorrowedTill(null);
        }
        return bookToReturn;
    }

    private Long generateNextId() {
        return books.stream()
                .mapToLong(Book::getId)
                .max()
                .orElseThrow() + 1;
    }
}
