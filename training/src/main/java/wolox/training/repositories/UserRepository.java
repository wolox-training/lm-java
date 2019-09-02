package wolox.training.repositories;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    Optional<User> findFirstByUsername(String username);

    @Query("SELECT u FROM User u WHERE (CAST(:startDate AS date) IS NULL OR u.birthdate >= :startDate)"
        + " AND (CAST(:endDate AS date) IS NULL OR u.birthdate <= :endDate)"
        + " AND (:name IS NULL OR LOWER(u.name) LIKE %:name%)"
        + " AND (:username IS NULL OR LOWER(u.username) LIKE %:username%)")
    Page<User> findAll(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("name") String name, @Param("username") String username, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (CAST(:startDate AS date) IS NULL OR u.birthdate >= :startDate)"
        + " AND (CAST(:endDate AS date) IS NULL OR u.birthdate <= :endDate)"
        + " AND (:name IS NULL OR LOWER(u.name) LIKE %:name%)")
    Page<User> findByBirthdateBetweenAndNameContainingIgnoreCase(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("name") String name, Pageable pageable);
}
