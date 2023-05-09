package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Customer;
import sk.tuke.gamestudio.service.UserService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private Customer loggedCustomer;
    private boolean register=false;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String login,@RequestParam String passwd, Model model) {
        if (!register){
            Customer temp= new Customer(login, passwd);
            if (userService.validCredentials(temp)) {
                loggedCustomer = temp;
                return "redirect:/menu";
            }
            else{
            }
        }
        else {
            Customer temp=new Customer(login, passwd);
            try{
                userService.addUser(temp);
                loggedCustomer = temp;
            }
            catch(Exception e){
                return "redirect:/";
            }
            return "redirect:/menu";
        }
        return "redirect:/";
    }

    @GetMapping("/login/register")
    public String changeRegister(Model model) {
        register = !register;
        return "redirect:/";
    }

    public boolean isRegister() {
        return register;
    }

    @GetMapping("/logout")
    public String logout() {
        loggedCustomer = null;
        return "redirect:/";
    }

    public Customer getLoggedUser() {
        return loggedCustomer;
    }

    public boolean isLogged() {
        return loggedCustomer != null;
    }

}

