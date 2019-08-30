package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import wolox.training.services.OpenLibraryService;

@Api(value="Book Management")
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

    @ApiOperation(value = "View list of all books", response = Iterable.class)
    @GetMapping
    public Page<Book> findAll(@RequestParam(required = false) String author,
                                @RequestParam(required = false) String title,
                                @RequestParam(required = false) String subtitle,
                                @RequestParam(required = false) String publisher,
                                @RequestParam(required = false) String genre,
                                @RequestParam(required = false) String year,
                                @RequestParam(required = false) Integer pages,
                                @RequestParam(required = false) String isbn,
                                @PageableDefault(sort = { "title", "subtitle", "isbn" }, value = 20) Pageable pageable) {
        return bookRepository.findAll(author, publisher, genre, year, title, subtitle, pages, isbn, pageable);
    }

    @GetMapping("/publisherAndGenreAndYear")
    public Page<Book> findByPublisherAndGenreAndYear(@RequestParam(required = false) String publisher,
                                                        @RequestParam(required = false) String genre,
                                                        @RequestParam(required = false) String year,
                                                        @PageableDefault(sort = { "title", "subtitle", "isbn" }, value = 20) Pageable pageable) {
        return bookRepository.findByPublisherAndGenreAndYear(publisher, genre, year, pageable);
    }

    @GetMapping("/author/{author}")
    public Book findByAuthor(@PathVariable String author) {
        return bookRepository.findFirstByAuthor(author).orElseThrow(BookNotFoundException::new);
    }

    @GetMapping("/isbn/{isbn}")
    public Book findByIsbn(@PathVariable String isbn) {
        return bookRepository.findFirstByIsbn(isbn).orElse(bookRepository.save(new Book(openLibraryService.bookInfo(isbn))));
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }
}
