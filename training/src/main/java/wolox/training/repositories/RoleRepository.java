package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findFirstByName(String name);
}
