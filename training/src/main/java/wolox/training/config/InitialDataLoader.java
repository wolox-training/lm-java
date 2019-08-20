package wolox.training.config;

import com.google.common.collect.Sets;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
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

        Role adminRole = roleRepository.findFirstByName("ROLE_ADMIN").get();
        if (!userRepository.findFirstByUsername("test").isPresent()) {
            User user = new User();
            user.setUsername("test");
            user.setName("test");
            user.setBirthdate(LocalDate.EPOCH);
            user.setPassword(passwordEncoder.encode("test"));
            user.setRoles(Sets.newHashSet(adminRole));
            userRepository.save(user);
        }
        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        var privilege = privilegeRepository.findFirstByName(name);
        if (!privilege.isPresent()) {
            Privilege newPrivilege = new Privilege(name);
            privilegeRepository.save(newPrivilege);
            privilege = Optional.ofNullable(newPrivilege);
        }
        return privilege.get();
    }

    @Transactional
    Role createRoleIfNotFound(
        String name, Set<Privilege> privileges) {

        var role = roleRepository.findFirstByName(name);
        if (!role.isPresent()) {
            Role newRole = new Role(name);
            newRole.setPrivileges(privileges);
            roleRepository.save(newRole);
            role = Optional.ofNullable(newRole);
        }
        return role.get();
    }
}