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
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class BookTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book oneTestBook;
    private Book twoTestBook;

    @Before
    public void setUp() {
        oneTestBook = new Book();
        oneTestBook.setAuthor("Stephen King");
        oneTestBook.setGenre("Terror");
        oneTestBook.setImage("image.png");
        oneTestBook.setIsbn("4772-0888");
        oneTestBook.setPages(123);
        oneTestBook.setPublisher("Viking Press");
        oneTestBook.setTitle("It");
        oneTestBook.setSubtitle("Worst clown ever");
        oneTestBook.setYear("1986");
        twoTestBook = new Book();
        twoTestBook.setAuthor("Lorant Mikolas");
        twoTestBook.setGenre("Action");
        twoTestBook.setImage("image2.png");
        twoTestBook.setIsbn("4772-0123");
        twoTestBook.setPages(123);
        twoTestBook.setPublisher("Editorialis");
        twoTestBook.setTitle("King Artur");
        twoTestBook.setSubtitle("The worst king");
        twoTestBook.setYear("1999");
        entityManager.persist(twoTestBook);
        entityManager.flush();
    }

    @Test
    public void whenFindAll_thenReturnBookList() {
        assertThat(bookRepository.findAll()).hasSize(2);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateBookWithoutUsername_thenThrowException() {
        oneTestBook.setTitle(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithEmptyTitle_thenThrowException() {
        oneTestBook.setTitle("");
        bookRepository.save(oneTestBook);
    }

    @Test
    public void whenCreateBook_thenBookIsPersisted() {
        Book persistedBook = bookRepository.findFirstByAuthor(twoTestBook.getAuthor())
            .orElse(new Book());
        assertThat(persistedBook.getTitle()).isEqualTo(twoTestBook.getTitle());
    }
}
