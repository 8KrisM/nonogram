package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Customer;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.UserException;
import sk.tuke.gamestudio.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserServiceRest {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public void addUser(@RequestBody Customer customer) {
        try {
            userService.addUser(customer);
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
    }

    @PostMapping
    public boolean validCredentials(@RequestBody Customer customer) {
        try {
            return userService.validCredentials(customer);
        } catch (Exception e) {
            throw new ScoreException(e.getMessage());
        }
    }
}