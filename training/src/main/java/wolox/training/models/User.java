package wolox.training.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Preconditions;
import java.time.LocalDate;
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

@Entity
@Table(name = "users")
public class User {

    private static final String NULL_PARAMETER = "The parameter should not be null";

    private static final String EMPTY_STRING = "String should have at least 1 character";

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String username;

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

    public User() {
        this.books = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Preconditions.checkNotNull(username, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(username), EMPTY_STRING);
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(username), EMPTY_STRING);
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        Preconditions.checkNotNull(birthdate, NULL_PARAMETER);
        this.birthdate = birthdate;
    }

    public Set<Book> getBooks() {
        return (Set<Book>) Collections.unmodifiableSet(books);
    }

    public void setBooks(Set<Book> books) {
        Preconditions.checkNotNull(books, NULL_PARAMETER);
        this.books = books;
    }

    public void addBook(Book book) throws BookAlreadyOwnedException {
        Preconditions.checkNotNull(book, NULL_PARAMETER);
        if (books.contains(book)) {
            throw new BookAlreadyOwnedException("User already owns given book");
        }
        books.add(book);
    }

    public void removeBook(Book book) throws BookNotOwnedByUserException {
        Preconditions.checkNotNull(book, NULL_PARAMETER);
        if (!this.books.remove(book)) {
            throw new BookNotOwnedByUserException("User doesn't own book");
        }
    }
}
