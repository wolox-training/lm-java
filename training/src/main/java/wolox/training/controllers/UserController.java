package wolox.training.controllers;

import java.security.Principal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.BookNotOwnedByUserException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.exceptions.UsernameExistsException;
import wolox.training.models.Book;
import wolox.training.models.DTOs.UserDTO;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.services.SecurityService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SecurityService securityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) throws UsernameExistsException {
        if (userRepository.findFirstByUsername(user.getUsername()).isPresent())
            throw new UsernameExistsException(user.getUsername() + "already exists");
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody UserDTO userDTO, @PathVariable int id) {
        if (userDTO.getId() != id) {
            throw new UserIdMismatchException();
        }
        User user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        user.setFromDTO(userDTO);
        return userRepository.save(user);
    }

    @PutMapping("/{id}/updatePassword")
    public void updatePassword(@RequestBody String password, @PathVariable int id) {
        securityService.updatePassword(password, id);
    }

    @GetMapping
    public Page<User> findAll(@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate start, @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate end,
        @RequestParam(required = false, defaultValue = "") String name, @RequestParam(required = false, defaultValue = "") String username, @PageableDefault(sort = { "username", "name" }, value = 20) Pageable pageable) {
        return userRepository.findAll(start, end, name, username, pageable);
    }

    @GetMapping("/birthdateAndName")
    public Page<User> findByBirthdateBetweenAndNameContainingIgnoreCase(@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate start,
                                                                            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate end,
                                                                            @RequestParam(required = false) String name,
                                                                            @PageableDefault(sort = { "username" , "name" }, value = 20) Pageable pageable) {
        if (name != null) {
            name = name.toLowerCase();
        }
        return userRepository.findByBirthdateBetweenAndNameContainingIgnoreCase(start, end, name, pageable);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable Integer id) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }


    @PutMapping("{id}/addBook")
    public User updateAddBook(@PathVariable int id, @RequestBody Book book) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Book bookToAdd = bookRepository.findById(book.getId()).orElseThrow(BookNotFoundException::new);
        try {
            user.addBook(bookToAdd);
        } catch (BookAlreadyOwnedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
        userRepository.save(user);
        return user;
    }

    @PutMapping("{id}/removeBook")
    public User updateRemoveBook(@PathVariable int id, @RequestBody Book book) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Book bookToRemove = bookRepository.findById(book.getId()).orElseThrow(BookNotFoundException::new);
        try {
            user.removeBook(bookToRemove);
            bookToRemove.removeUser(user);
        } catch (BookNotOwnedByUserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }
        userRepository.save(user);
        return user;
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }
}
