package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Optional<Book> findFirstByAuthor(String author);
    Optional<Book> findFirstByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE (:publisher IS NULL OR b.publisher LIKE %:publisher)"
        + " AND (:title IS NULL OR b.title LIKE %:author%)"
        + " AND (:title IS NULL OR b.title LIKE %:title%)"
        + " AND (:subtitle IS NULL OR b.subtitle LIKE %:subtitle%)"
        + " AND (:genre IS NULL OR b.genre LIKE %:genre%)"
        + " AND (:year IS NULL OR b.year = :year)"
        + " AND (:pages IS NULL OR b.pages = :pages)"
        + " AND (:isbn IS NULL OR b.isbn = :isbn)")
    Page<Book> findAll(@Param("author") String author, @Param("publisher") String publisher, @Param("genre") String genre, @Param("year") String year, @Param("title") String title, @Param("subtitle") String subtitle, @Param("pages") Integer pages, @Param("isbn") String isbn, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE (:publisher IS NULL OR b.publisher LIKE %:publisher)"
                                    + " AND (:genre IS NULL OR b.genre LIKE %:genre%)"
                                    + " AND (:year IS NULL OR b.year = :year)")
    Page<Book> findByPublisherAndGenreAndYear(@Param("publisher") String publisher, @Param("genre") String genre, @Param("year") String year, Pageable pageable);
}
