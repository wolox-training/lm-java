package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {
    Optional<Book> findFirstByAuthor(String author);
}
