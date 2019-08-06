package wolox.training.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User oneTestUser;
    private User twoTestUser;

    @Before
    public void setUp() {
        oneTestUser = new User();
        oneTestUser.setUsername("ramselton");
        oneTestUser.setName("Ramiro Selton");
        oneTestUser.setBirthdate(LocalDate.parse("1999-03-27"));
        twoTestUser = new User();
        twoTestUser.setUsername("lorantmik");
        twoTestUser.setName("Lorant Mikolas");
        twoTestUser.setBirthdate(LocalDate.parse("1997-05-14"));
        entityManager.persist(twoTestUser);
        entityManager.flush();
    }

    @Test
    public void whenFindAll_thenReturnUserList() {
        assertThat(userRepository.findAll()).hasSize(2);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateUserWithoutUsername_thenThrowException() {
        oneTestUser.setUsername(null);
        userRepository.save(oneTestUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateUserWithEmptyName_thenThrowException() {
        oneTestUser.setName("");
        userRepository.save(oneTestUser);
    }

    @Test
    public void whenCreateUser_thenUserIsPersisted() {
        User persistedUser = userRepository.findFirstByUsername(twoTestUser.getUsername())
            .orElse(new User());
        assertThat(persistedUser.getUsername()).isEqualTo(twoTestUser.getUsername());
    }
}
