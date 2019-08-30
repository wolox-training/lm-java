package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findFirstByAuthor(String author);
    Optional<Book> findFirstByIsbn(String isbn);
    @Query("SELECT b FROM Book b WHERE (:publisher IS NULL OR b.publisher LIKE %:publisher)"
                                    + " AND (:genre IS NULL OR b.genre LIKE %:genre%)"
                                    + " AND (:year IS NULL OR b.year = :year)")
    List<Book> findByPublisherAndGenreAndYear(@Param("publisher") String publisher, @Param("genre") String genre, @Param("year") String year);
}
