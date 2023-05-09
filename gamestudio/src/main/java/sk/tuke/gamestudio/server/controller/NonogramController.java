package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.nonogram.core.GameField;
import sk.tuke.gamestudio.game.nonogram.core.Tile;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class NonogramController {

    private GameField field;

    private boolean marking;
    private int[][] columnClues;
    private List<List<Integer>> rowClues;

    private boolean scoreAdded=false;
    @Autowired
    private
    ScoreService scoreService;

    @Autowired
    private UserController userController;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @RequestMapping("/nonogram")
    public String nonogram(@RequestParam int row, @RequestParam int column, Model model) {
        if(field.getState()== GameField.State.PLAYING) {
            if (marking)
                field.markBlankTile(row, column);
            else {
                field.fillTile(row, column);
            }
        }
        prepareModel(model);
        return "nonogram";
    }


    @GetMapping("/nonogram/check")
    public String check(Model model){
        field.isSolved();
        if (field.getState() == GameField.State.SOLVED && userController.isLogged()&&!scoreAdded) {
            scoreService.addScore(new Score("Nonogram",userController.getLoggedUser().getLogin(), field.getScore(), new Date()));
            scoreAdded=true;
        }
        prepareModel(model);
        return "nonogram";
    }

    @GetMapping("/nonogram/help")
    public String help(Model model) {
        if (field.getState() == GameField.State.PLAYING){
            field.helpRandom();
        }
        prepareModel(model);
        return "nonogram";
    }

    @PostMapping("/nonogram/new")
    public String newGame(@RequestParam String size,Model model) {
        int rows=5;
        int columns=5;
        Pattern pattern = Pattern.compile("(\\d+)x(\\d+)");
        Matcher matcher = pattern.matcher(size);
        if (matcher.find()) {
            rows = Integer.parseInt(matcher.group(1));
            columns = Integer.parseInt(matcher.group(2));
        }
        try{
            field = new GameField(rows, columns, GameField.Type.BLACKANDWHITE);
        }
        catch(Exception e){ throw new RuntimeException(e.getMessage());}
        columnClues= field.getColumnClues();
        rowClues=field.rowClues();
        prepareModel(model);
        return "nonogram";
    }

    @GetMapping("/nonogram/mark")
    public String changeMarking(Model model) {
        marking = !marking;
        prepareModel(model);
        return "nonogram";
    }

    @PostMapping("nonogram/comment")
    public String login(@RequestParam String comment, Model model) {
        if (userController.isLogged() &&!comment.isEmpty()) {
            Comment newComment = new Comment("Nonogram",userController.getLoggedUser().getLogin(),comment,new Date(System.currentTimeMillis()));
            commentService.addComment(newComment);
        }
        prepareModel(model);
        return "nonogram";
    }

    @GetMapping("nonogram/rate")
    public String rate(@RequestParam int rating, Model model) {
        if (userController.isLogged()) {
            Rating newRating = new Rating("Nonogram",userController.getLoggedUser().getLogin(),rating,new Date(System.currentTimeMillis()));
            ratingService.setRating(newRating);
        }
        prepareModel(model);
        return "nonogram";
    }

    public String getHtmlField() {
        var sb = new StringBuilder();
        sb.append("<table class='gamefield'>\n");
        for (var row = 0; row < field.getRows(); row++) {
            sb.append("<tr>\n");
            for (var column = 0; column < field.getColumns(); column++) {
                var tile = field.getTile(row, column);
                sb.append("<td class='" + getTileClass(tile) + "'>\n");
                if (tile.getState() != Tile.State.FILLED)
                    sb.append("<a href='/nonogram?row=" + row + "&column=" + column + "'>\n");
                sb.append("<span>" + getTileText(tile) + "</span>\n");
                if (tile.getState() != Tile.State.FILLED)
                    sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
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


    public boolean isMarking() {
        return marking;
    }

    private void prepareModel(Model model) {
        try {
            model.addAttribute("scores", scoreService.getTopScores("Nonogram"));
        }
        catch(Exception ignored){}
        try{
            model.addAttribute("comments", commentService.getComments("Nonogram"));
        }
        catch (Exception ignored){}
        model.addAttribute("field", field);
        model.addAttribute("columnClues",columnClues);
        model.addAttribute("rowClues",rowClues);
        try { model.addAttribute("rating", ratingService.getRating("Nonogram", userController.getLoggedUser().getLogin()));}
        catch (Exception ignored){}
        try { model.addAttribute("averageRating", ratingService.getAverageRating("Nonogram"));}
        catch (Exception ignored){}
    }

}
