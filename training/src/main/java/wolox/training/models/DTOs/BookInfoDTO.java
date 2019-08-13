package wolox.training.models.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookInfoDTO implements Serializable {
    @JsonProperty
    private String isbn;
    @JsonProperty
    private String title;
    @JsonProperty
    private String subtitle;
    @JsonProperty
    private PublisherDTO[] publishers;
    @JsonProperty("publish_date")
    private String publishDate;
    @JsonProperty("number_of_pages")
    private int pages;
    @JsonProperty
    private AuthorDTO[] authors;

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setPublishers(PublisherDTO[] publishers) {
        this.publishers = publishers;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setAuthors(AuthorDTO[] authors) {
        this.authors = authors;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public PublisherDTO[] getPublishers() {
        return publishers;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public int getPages() {
        return pages;
    }

    public AuthorDTO[] getAuthors() {
        return authors;
    }
}
