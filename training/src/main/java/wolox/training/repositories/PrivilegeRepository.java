package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Privilege;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
    Optional<Privilege> findFirstByName(String name);
}
