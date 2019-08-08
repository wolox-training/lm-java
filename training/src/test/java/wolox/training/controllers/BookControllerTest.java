package wolox.training.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository mockBookRepository;
    private Book oneTestBook;
    private Book twoTestBook;
    private String bookJson;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
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
        bookJson = objectMapper.writeValueAsString(oneTestBook);
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
    }

    @Test
    public void whenFindByIdWhichExists_thenBookIsReturned() throws Exception {
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(Optional.ofNullable(oneTestBook));
        mvc.perform(get("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(this.bookJson));
    }

    @Test
    public void whenFindAllBooks_thenBooksAreReturned() throws Exception {
        Mockito.when(mockBookRepository.findAll()).thenReturn(
            Arrays.asList(new Book[] { oneTestBook, twoTestBook }));
        mvc.perform(get("/api/books/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].title", is(oneTestBook.getTitle())))
            .andExpect(jsonPath("$[1].title", is(twoTestBook.getTitle())));
    }

    @Test
    public void whenDeleteBookWhichNotExists_thenBookNotFound() throws Exception {
        Mockito.when(mockBookRepository.findById(3L)).thenReturn(Optional.empty());
        mvc.perform(delete("/api/books/3")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteBookWhichExists_thenOkResponse() throws Exception {
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(Optional.ofNullable(oneTestBook));
        mvc.perform(delete("/api/books/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
