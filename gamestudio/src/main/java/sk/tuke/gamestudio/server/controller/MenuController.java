package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.game.nonogram.core.Maps;

import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MenuController {
        private Maps maps=new Maps();

        @GetMapping("/menu")
        public String login(Model model) {
            prepareModel(model);
            return "menu";
        }


    private void prepareModel(Model model) {
        try{
            model.addAttribute("sizes", maps.getAvailableSizes());
        }
        catch(Exception ignored){
        }
    }
}
