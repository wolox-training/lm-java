package wolox.training.models.DTOs;

import com.google.common.base.Preconditions;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.thymeleaf.util.StringUtils;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotOwnedByUserException;
import wolox.training.models.Book;
import wolox.training.models.Role;
import wolox.training.utils.MessageConstants;

public class UserDTO {

    private int id;

    private String username;

    private String name;

    private LocalDate birthdate;

    private Set<Book> books;

    private Set<Role> roles;

    public UserDTO() {
        this.books = new HashSet<>();
        this.roles = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Preconditions.checkNotNull(username, MessageConstants.NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(username), MessageConstants.EMPTY_STRING);
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name, MessageConstants.NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(name), MessageConstants.EMPTY_STRING);
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        Preconditions.checkNotNull(birthdate, MessageConstants.NULL_PARAMETER);
        this.birthdate = birthdate;
    }

    public Set<Book> getBooks() {
        return (Set<Book>) Collections.unmodifiableSet(books);
    }

    public void setBooks(Set<Book> books) {
        Preconditions.checkNotNull(books, MessageConstants.NULL_PARAMETER);
        this.books = books;
    }

    public void addBook(Book book) throws BookAlreadyOwnedException {
        Preconditions.checkNotNull(book, MessageConstants.NULL_PARAMETER);
        if (books.contains(book)) {
            throw new BookAlreadyOwnedException("User already owns given book");
        }
        books.add(book);
    }

    public void removeBook(Book book) throws BookNotOwnedByUserException {
        Preconditions.checkNotNull(book, MessageConstants.NULL_PARAMETER);
        if (!this.books.remove(book)) {
            throw new BookNotOwnedByUserException("User doesn't own book");
        }
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void setRoles(Set<Role> roles) {
        Preconditions.checkNotNull(roles);
        this.roles = roles;
    }
}
