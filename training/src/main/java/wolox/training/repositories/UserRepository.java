package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findFirstByUsername(String username);
    List<User> findByBirthdateBetweenAndNameContainingIgnoreCase(LocalDate start, LocalDate end, String name);
}
