package wolox.training.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Preconditions;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Id;
import org.thymeleaf.util.StringUtils;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotOwnedByUserException;
import wolox.training.utils.MessageConstants;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "book_users",
        joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @JsonIgnoreProperties("users")
    private Set<Book> books;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"))
    @JsonIgnoreProperties("users")
    private Set<Role> roles;

    public User() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Preconditions.checkNotNull(password, MessageConstants.NULL_PARAMETER);
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        Preconditions.checkNotNull(roles, MessageConstants.NULL_PARAMETER);
        this.roles = roles;
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
}
