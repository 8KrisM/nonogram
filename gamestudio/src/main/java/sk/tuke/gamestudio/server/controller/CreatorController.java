package sk.tuke.gamestudio.server.controller;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.nonogram.core.GameField;
import sk.tuke.gamestudio.game.nonogram.core.Tile;

import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class CreatorController {
    private int[][] columnClues;
    private List<List<Integer>> rowClues;
    private GameField field;
    private boolean marking;
    private boolean saved=false;


    @RequestMapping("/creator/mark")
    public String creatorMark(@RequestParam int row, @RequestParam int column, Model model) {
            if (marking)
                field.markBlankTile(row, column);
            else {
                field.fillTile(row, column);
            }
        prepareModel(model);
        return "create";
    }

    @PostMapping("/creator")
    public String newField(@RequestParam String rows, @RequestParam String columns, Model model) {
        saved=false;
        try{
            field = new GameField(Integer.parseInt(rows),Integer.parseInt(columns), GameField.Type.BLACKANDWHITE,true);
        }
        catch(Exception e){
            return "redirect:/menu";
        }
        columnClues= field.getColumnClues();
        rowClues=field.rowClues();
        prepareModel(model);
        return "create";
    }


    @GetMapping("/creator/marking")
    public String changeMarking(Model model) {
        marking = !marking;
        prepareModel(model);
        return "create";
    }

    @GetMapping("/creator/save")
    public String save(Model model) {
        try{
            field.save();
            saved=true;
        }
        catch(Exception e){
            saved=false;
            return "create";
        }
        prepareModel(model);
        return "create";
    }

    public boolean isSaved() {
        return saved;
    }

    public boolean isMarking() {
        return marking;
    }

    public String getTileClass(Tile tile) {
        switch (tile.getState()) {
            case UNMARKED:
                return "unmarked";
            case BLANK:
                return "blank";
            case FILLED:
                return "filled";
            default:
                throw new IllegalArgumentException("Not valid tile state" + tile.getState());
        }
    }

    public List<Integer> getOneRowClue(int row){
        return rowClues.get(row);
    }

    public String getTileText(Tile tile) {
        switch (tile.getState()) {
            case UNMARKED:
                return "";
            case BLANK:
                return "X";
            case FILLED:
                return "";
            default:
                throw new IllegalArgumentException("Not valid tile state" + tile.getState());
        }
    }
    public void prepareModel(Model model){
        model.addAttribute("field", field);
        model.addAttribute("columnClues",columnClues);
        model.addAttribute("rowClues",rowClues);
    }
}
