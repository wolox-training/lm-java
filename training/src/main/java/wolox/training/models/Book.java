package wolox.training.models;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.thymeleaf.util.StringUtils;
import wolox.training.exceptions.BookNotOwnedByUserException;

@Entity
@ApiModel(description = "Books from the TrainingApi")
public class Book {

    private static final String NULL_PARAMETER = "The parameter should not be null";

    private static final String EMPTY_STRING = "String should have at least 1 character";

    private static final String NEGATIVE_QUANTITY = "The quantity should be positive integer";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @ApiModelProperty(notes = "The book genre: horror, comedy, drama, etc.")
    private String genre;

    @Column(nullable = false)
    @ApiModelProperty(notes = "The book author, e.g., Borges.")
    private String author;

    @Column(nullable = false)
    @ApiModelProperty(notes = "The images of the book.")
    private String image;

    @Column(nullable = false, unique = true)
    @ApiModelProperty(notes = "The title of the book, e.g., The Wolox Chronicles.")
    private String title;

    @Column(nullable = false)
    @ApiModelProperty(notes = "The subtitle of the book, e.g., The tale of an old pull request.")
    private String subtitle;

    @Column(nullable = false)
    @ApiModelProperty(notes = "The publisher of the book, e.g., Salamandra.")
    private String publisher;

    @Column(nullable = false)
    @ApiModelProperty(notes = "The year of publication: 2019, 2018, etc.")
    private String year;

    @Column(nullable = false)
    @ApiModelProperty(notes = "The number of pages the book has.")
    private int pages;

    @Column(nullable = false)
    @ApiModelProperty(notes = "International Standard Book Number that identifies the book.")
    private String isbn;

    @ManyToMany(mappedBy = "books")
    @ApiModelProperty(notes = "Set of users that own the book.")
    private Set<User> users;

    public Book() {
        this.users = new HashSet<>();
    }


    public long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        Preconditions.checkNotNull(genre, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(genre), EMPTY_STRING);
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        Preconditions.checkNotNull(author, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(author), EMPTY_STRING);
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        Preconditions.checkNotNull(image, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(image), EMPTY_STRING);
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Preconditions.checkNotNull(title, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(title), EMPTY_STRING);
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        Preconditions.checkNotNull(subtitle, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(subtitle), EMPTY_STRING);
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        Preconditions.checkNotNull(publisher, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(publisher), EMPTY_STRING);
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        Preconditions.checkNotNull(year, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(year), EMPTY_STRING);
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        Preconditions.checkArgument(pages > 0, NEGATIVE_QUANTITY);
        Preconditions.checkNotNull(pages, NULL_PARAMETER);
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        Preconditions.checkNotNull(isbn, NULL_PARAMETER);
        Preconditions.checkArgument(!StringUtils.isEmpty(isbn), EMPTY_STRING);
        this.isbn = isbn;
    }

    public Set<User> getUsers() {
        return (Set<User>) Collections.unmodifiableSet(users);
    }

    public void removeUser(User user) throws BookNotOwnedByUserException {
        Preconditions.checkNotNull(user, NULL_PARAMETER);
        if (!this.users.remove(user)) {
            throw new BookNotOwnedByUserException("User doesn't own book");
        }
    }
}
