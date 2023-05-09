package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.nonogram.core.Maps;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MenuController {

    private boolean howTo=false;
    @GetMapping("/menu")
    public String menu(Model model) {
        prepareModel(model);
        return "menu";
    }

    @GetMapping("/menu/create")
    public String create(Model model) {
        prepareModel(model);
        return "createsize";
    }

    @GetMapping("/menu/howTo")
    public String howTo(Model model) {
        howTo=!howTo;
        prepareModel(model);
        return "menu";
    }

    public boolean isHowTo() {
        return howTo;
    }

    private void prepareModel(Model model) {
        try{
            model.addAttribute("sizes", Maps.getAvailableSizes());
        }
        catch(Exception ignored){
        }
    }
}
