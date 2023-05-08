package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private User loggedUser;

    @PostMapping("/login")
    public String login(String login, String passwd) {
        if ("heslo".equals(passwd)) {
            loggedUser = new User(login);
            return "redirect:/menu";
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        loggedUser = null;
        return "redirect:/";
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }
}

