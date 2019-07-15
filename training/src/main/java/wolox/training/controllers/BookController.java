package wolox.training.controllers;

import javax.swing.text.html.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Integer id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

    @GetMapping
    public Iterable findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/title/{bookAuthor}")
    public Book findByAuthor(@PathVariable String author) {
        return bookRepository.findFirstByAuthor(author).orElseThrow(BookNotFoundException::new);
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Integer id) {
        return bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }
}
