package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findFirstByUsername(String username);
    @Query("SELECT u FROM User u WHERE (CAST(:startDate AS date) IS NULL OR u.birthdate >= :startDate)"
        + " AND (CAST(:endDate AS date) IS NULL OR u.birthdate <= :endDate)"
        + " AND (:name IS NULL OR LOWER(u.name) LIKE %:name%)")
    List<User> findByBirthdateBetweenAndNameContainingIgnoreCase(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("name") String name);
}
