package wolox.training.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotOwnedByUserException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository mockBookRepository;
    @MockBean
    private UserRepository mockUserRepository;

    private Book oneTestBook;
    private Book twoTestBook;
    private User oneTestUser;
    private User twoTestUser;
    private User threeTestUser;
    private String userJson;
    private String userTwoJson;

    private ObjectMapper objectMapper;

    @Before
    public void setUp()
        throws JsonProcessingException, BookAlreadyOwnedException, BookNotOwnedByUserException {
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
        oneTestUser = new User();
        oneTestUser.setUsername("ramselton");
        oneTestUser.setName("Ramiro Selton");
        oneTestUser.setBirthdate(LocalDate.parse("1999-03-27"));
        oneTestUser.addBook(oneTestBook);
        twoTestUser = new User();
        twoTestUser.setUsername("ramselton");
        twoTestUser.setName("Ramiro Selton");
        twoTestUser.setBirthdate(LocalDate.parse("1999-03-27"));
        twoTestUser.addBook(oneTestBook);
        userJson = objectMapper.writeValueAsString(oneTestUser);
        userTwoJson = objectMapper.writeValueAsString(twoTestUser);
        twoTestUser.removeBook(oneTestBook);

    }

    @Test
    public void whenFindByIdWhichExists_thenUserIsReturned() throws Exception {
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.ofNullable(oneTestUser));
        mvc.perform(get("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(oneTestUser.getUsername())));
    }

    @Test
    public void whenFindAllUsers_thenUsersAreReturned() throws Exception {
        Mockito.when(mockUserRepository.findAll()).thenReturn(
            Arrays.asList(new User[] { oneTestUser, twoTestUser }));
        mvc.perform(get("/api/users/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].username", is(oneTestUser.getUsername())))
            .andExpect(jsonPath("$[1].username", is(twoTestUser.getUsername())));
    }

    @Test
    public void whenDeleteUserWhichNotExists_thenUserNotFound() throws Exception {
        Mockito.when(mockUserRepository.findById(3)).thenReturn(Optional.empty());
        mvc.perform(delete("/api/users/3")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteUserWhichExists_thenOkResponse() throws Exception {
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.ofNullable(oneTestUser));
        mvc.perform(delete("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void whenAddBook_BookAddedToUser() throws Exception {
        Mockito.when(mockUserRepository.findById(2)).thenReturn(Optional.ofNullable(twoTestUser));
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(Optional.ofNullable(oneTestBook));
        String testBook = objectMapper.writeValueAsString(oneTestBook);
        testBook = testBook.replace("0","1");
        mvc.perform(put("/api/users/2/addBook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testBook)
            .param("id","2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(oneTestUser.getUsername())))
            .andExpect(jsonPath("$.books", hasSize(1)));
    }

    @Test
    public void whenAddBook_BookAlreadyOwned() throws Exception {
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.ofNullable(oneTestUser));
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(Optional.ofNullable(oneTestBook));
        String testBook = objectMapper.writeValueAsString(oneTestBook);
        testBook = testBook.replace("0","1");
        mvc.perform(put("/api/users/1/addBook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testBook))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void  removeBook_BookNotOwned() throws Exception {
        Mockito.when(mockUserRepository.findById(1)).thenReturn(Optional.ofNullable(oneTestUser));
        Mockito.when(mockBookRepository.findById(1L)).thenReturn(Optional.ofNullable(oneTestBook));
        String testBook = objectMapper.writeValueAsString(oneTestBook);
        testBook = testBook.replace("0","1");
        mvc.perform(put("/api/users/1/addBook")
            .contentType(MediaType.APPLICATION_JSON)
            .content(testBook))
            .andExpect(status().isBadRequest());
    }
}
