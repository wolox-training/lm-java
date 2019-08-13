package wolox.training.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.DTOs.BookInfoDTO;

@Service
public class OpenLibraryService {
    private final static String URL = "https://openlibrary.org/api/books?format=json&jscmd=data&bibkeys=ISBN:";
    private final static String DEFAULT_SUBTITLE = "default_subtitle";


    public BookInfoDTO bookInfo(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(URL + isbn, String.class);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND)
            throw new BookNotFoundException();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(response.getBody());
        } catch (IOException e) {
            throw new BookNotFoundException();
        }
        BookInfoDTO bookInfoDTO = mapper.convertValue(root.path("ISBN:" +isbn), BookInfoDTO.class);
        bookInfoDTO.setIsbn(isbn);
        bookInfoDTO.setSubtitle(DEFAULT_SUBTITLE);
        return bookInfoDTO;
    }
}
