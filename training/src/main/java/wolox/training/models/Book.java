package wolox.training.models;

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
import wolox.training.exceptions.BookNotOwnedByUserException;

@Entity
@ApiModel(description = "Books from the TrainingApi")
public class Book {

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
    @ApiModelProperty(notes = "The number of pages the book has.")
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
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Set<User> getUsers() {
        return (Set<User>) Collections.unmodifiableSet(users);
    }

    public void removeUser(User user) throws BookNotOwnedByUserException {
        if (!this.users.remove(user)) {
            throw new BookNotOwnedByUserException("User doesn't own book");
        }
    }
}
