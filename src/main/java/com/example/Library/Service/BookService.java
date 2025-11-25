package com.example.Library.Service;

import com.example.Library.Entity.Book;
import com.example.Library.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired // Injects the BookRepository dependency
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void updateBook(Long id, Book updatedBook) {
        Optional<Book> existing = bookRepository.findById(id);
        if (existing.isPresent()) {
            Book book = existing.get();
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setAvailableCopies(updatedBook.getAvailableCopies());
            bookRepository.save(book);
        }
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void updateBookCopies(Long id, int i) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        bookOpt.ifPresent(book -> {
            int newCount = book.getAvailableCopies() + i;
            if (newCount >= 0) {
                book.setAvailableCopies(newCount);
                bookRepository.save(book);
            }
        });

    }

    public long getBooksCount() {
        return bookRepository.count();
    }
}
