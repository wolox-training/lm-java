package wolox.training.services;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.exceptions.UsernameExistsException;
import wolox.training.models.User;
import wolox.training.repositories.RoleRepository;
import wolox.training.repositories.UserRepository;

@Service
public class SecurityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUserAccount(User user)
        throws UsernameExistsException {

        if (userRepository.findFirstByUsername(user.getUsername()).isPresent()) {
            throw new UsernameExistsException
                ("There is an account with that username: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Sets.newHashSet(roleRepository.findFirstByName("ROLE_USER").get()));
        return userRepository.save(user);
    }

    public User updatePassword(String password, int id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}
