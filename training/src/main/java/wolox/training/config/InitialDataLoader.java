package wolox.training.config;

import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;
import javax.management.relation.RoleNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import wolox.training.models.Privilege;
import wolox.training.models.Role;
import wolox.training.models.User;
import wolox.training.repositories.PrivilegeRepository;
import wolox.training.repositories.RoleRepository;
import wolox.training.repositories.UserRepository;

@Component
public class InitialDataLoader implements
    ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
            = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
            = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        Set<Privilege> adminPrivileges = Sets.newHashSet(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Sets.newHashSet(readPrivilege));

        Role adminRole = null;
        try {
            adminRole = roleRepository.findFirstByName("ROLE_ADMIN").orElseThrow(
                RoleNotFoundException::new);
        } catch (RoleNotFoundException e) {
            e.printStackTrace();
        }
        User user = new User();
        user.setUsername("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setRoles(Sets.newHashSet(adminRole));
        userRepository.save(user);
        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege;
        Optional<Privilege> optionalPrivilege = privilegeRepository.findFirstByName(name);
        if (!optionalPrivilege.isPresent()) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        } else {
            privilege = optionalPrivilege.get();
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Set<Privilege> privileges) {

        Optional<Role> role = roleRepository.findFirstByName(name);
        if (!role.isPresent()) {
            role = Optional.ofNullable(new Role(name));
            role.get().setPrivileges(privileges);
            roleRepository.save(role.get());
        }
        return role.get();
    }
}
