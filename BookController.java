    
    package com.example.LibraryManagement.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.LibraryManagement.Model.Book;

@RestController
@RequestMapping("/books")
public class BookController {

    private List<Book> books = new ArrayList<>();

    // POST: Add a new book
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    // GET: Get all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(books);
    }

    // GET: Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {
        for (Book book : books) {
            if (id != null && id.equals(book.getId())) {
                return ResponseEntity.ok(book);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found!");
    }

    // PUT: Update book details
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable Integer id,
            @RequestBody Book updatedBook) {

        for (Book book : books) {
            if (id != null && id.equals(book.getId())) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setCategory(updatedBook.getCategory());
                book.setAvailableCopies(updatedBook.getAvailableCopies());

                return ResponseEntity.ok(book);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found!");
    }

    // DELETE: Delete book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {

        boolean removed = books.removeIf(book -> id != null && id.equals(book.getId()));

        if (removed) {
            return ResponseEntity.ok("Book deleted successfully!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found!");
    }
}