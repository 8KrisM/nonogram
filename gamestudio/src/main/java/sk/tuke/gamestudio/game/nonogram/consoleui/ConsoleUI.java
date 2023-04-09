package sk.tuke.gamestudio.game.nonogram.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.nonogram.core.GameField;
import sk.tuke.gamestudio.game.nonogram.core.Tile;
import sk.tuke.gamestudio.service.*;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private GameField gameField;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;

    /*@Autowired
    private ScoreServiceJPA scoreService;
    @Autowired
    private RatingServiceJPA ratingService;
    @Autowired
    private CommentServiceJPA commentService;*/

    /*private ScoreServiceJDBC scoreService = new ScoreServiceJDBC();
    /private RatingServiceJDBC ratingService = new RatingServiceJDBC();
    private CommentServiceJDBC commentService = new CommentServiceJDBC();*/
    private String name;

    public ConsoleUI() {
    }

    public ConsoleUI(GameField gameField) {
        this.gameField = gameField;
    }

    public void play() {
        System.out.print("Welcome! Your task is to solve a nonogram. You have 3 chances to check, each check takes down one point from your score.\nEach use of help takes down one point from your score.\nEnter your name: ");
        Scanner nameScanner = new Scanner(System.in);
        name = nameScanner.nextLine();
        System.out.println(name + " it is! Let's play.");

        do {
            show();
            handleInput();
        } while (gameField.getState() == GameField.State.PLAYING);

        show();
        if (gameField.getState() == GameField.State.SOLVED) {
            System.out.println("Solved successfully! Congratulations.");
        } else if (gameField.getState() == GameField.State.FAILED) {
            System.out.println("Oh no! Failed!");
            showCorrectGamefield();
        }
        handleScore();
        handleComment();
        handleRating();
    }

    public void show() {
        System.out.print("  ");
        for (int i = 1; i <= gameField.getColumns(); i++) {
            System.out.print(" " + (char) (i + 64));

        }
        System.out.print("\n   ");
        for (int i = 1; i <= gameField.getColumns(); i++) {
            System.out.print("--");

        }
        System.out.println();
        for (int i = 1; i <= gameField.getRows(); i++) {
            if (i < 10) System.out.print(i + " |");
            else System.out.print(i + "|");
            for (int j = 1; j <= gameField.getColumns(); j++)
                System.out.print(gameField.getGuessedTile(i - 1, j - 1).toString() + " ");
            System.out.print("|");
            int run = 0;
            for (int k = 1; k <= gameField.getColumns(); k++) {
                if (gameField.getTile(i - 1, k - 1).getState() == Tile.State.FILLED) run++;
                else {
                    if (run != 0) System.out.print(" " + run);
                    run = 0;
                }
                if (run != 0 && k == gameField.getColumns()) System.out.print(" " + run);
            }
            System.out.print("\n");
        }
        System.out.print("  ");
        for (int i = 1; i <= gameField.getColumns(); i++) {
            System.out.print("--");
        }
        System.out.println();
        int[][] columnClue = new int[gameField.getColumns()][gameField.getRows() / 2 + 1];
        int clueRow = 0;
        for (int j = 0; j < gameField.getColumns(); j++) {
            int count = 0;
            for (int i = 0; i < gameField.getRows(); i++) {
                if (gameField.getTile(i, j).getState() == Tile.State.FILLED) {
                    count++;
                } else if (count > 0) {
                    columnClue[j][clueRow] = count;
                    count = 0;
                    clueRow++;
                }
            }
            if (count > 0) {
                columnClue[j][clueRow] = count;
            }
            clueRow = 0;
        }
        System.out.print("  ");
        int zeroRow = 0;
        for (int i = 0; i < columnClue[0].length; i++) {
            for (int j = 0; j < columnClue.length; j++) {
                if (columnClue[j][i] != 0) System.out.print(" " + columnClue[j][i]);
                else {
                    System.out.print("  ");
                    zeroRow++;
                }
            }
            System.out.println();
            if (zeroRow == columnClue.length) break;
            else zeroRow = 0;
            System.out.print("  ");
        }
    }

    public void showCorrectGamefield() {
        System.out.print("\nCorrect nonogram:\n ");
        for (int i = 1; i <= gameField.getColumns(); i++) {
            System.out.print("--");

        }
        System.out.println();
        for (int i = 1; i <= gameField.getRows(); i++) {
            System.out.print("|");
            for (int j = 1; j <= gameField.getColumns(); j++)
                System.out.print(gameField.getTile(i - 1, j - 1).toString() + " ");
            System.out.print("|\n");
        }
        System.out.print(" ");
        for (int i = 1; i <= gameField.getColumns(); i++) {
            System.out.print("--");
        }
        System.out.println();
    }

    public void handleInput() {
        Pattern patternCheck = Pattern.compile("C|H|X");
        Pattern patternField = Pattern.compile("(F|B)([A-" + (char) (gameField.getColumns() - 1 + 65) + "])([1-" + gameField.getRows() + "])");

        String input;
        Scanner scanner = new Scanner(System.in);

        Matcher matcherCheck;
        Matcher matcherField;

        do {
            System.out.print("Enter <C> to check,<H> for random help, <X> to fill unmarked as blank, <FA1> to fill A1, <BA1> to mark A1 as blank: ");
            input = scanner.nextLine();
            matcherCheck = patternCheck.matcher(input);
            matcherField = patternField.matcher(input);
        }
        while (!matcherCheck.matches() && !matcherField.matches());
        if (input.equals("H")) gameField.helpRandom();
        if (input.equals("X")) gameField.fillUnmarkedAsBlank();
        if (input.equals("C")) {
            if (!gameField.isSolved()) System.out.println("Incorrect. Left chances: " + gameField.getChances());
        } else {
            if (input.charAt(0) == 'F') {
                gameField.fillTile(input.charAt(2) - 49, input.charAt(1) - 65);
            }
            if (input.charAt(0) == 'B') {
                gameField.markBlankTile(input.charAt(2) - 49, input.charAt(1) - 65);
            }
        }
    }

    public void handleScore(){
        System.out.println("Your score is: " + gameField.getScore());
        try{
            System.out.println("Average score is: "+ ratingService.getAverageRating("Nonogram"));
            scoreService.addScore(new Score("Nonogram", name, gameField.getScore(), new Date(System.currentTimeMillis())));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void handleComment() {
        Pattern patternInput = Pattern.compile("(y|Y)|(n|N)");
        Scanner scanner = new Scanner(System.in);
        String input;
        Matcher matcher;
        do {
            System.out.print("Do you want to add comment? (y/n): ");
            input = scanner.nextLine();
            matcher = patternInput.matcher(input);
        }
        while (!matcher.matches());
        if (input.equals("y") || input.equals("Y")) {
            System.out.print("Write comment: ");
            input = scanner.nextLine();
            try{
                commentService.addComment(new Comment("Nonogram", name, input, new Date(System.currentTimeMillis())));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        do {
            System.out.print("Do you want to see other comments? (y/n): ");
            input = scanner.nextLine();
            matcher = patternInput.matcher(input);
        }
        while (!matcher.matches());
        if (input.equals("y") || input.equals("Y")) {
            try {
                List<Comment> comments = commentService.getComments("Nonogram");
                for (Comment temp : comments) {
                    System.out.println("Player: " + temp.getPlayer() + " says: " + temp.getComment());
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void handleRating() {
        Pattern patternInput = Pattern.compile("(y|Y)|(n|N)");
        Pattern patternRating = Pattern.compile("[1-5]");
        Scanner scanner = new Scanner(System.in);
        String input;
        Matcher matcher;
        do {
            System.out.print("Do you want to add rating? (y/n): ");
            input = scanner.nextLine();
            matcher = patternInput.matcher(input);
        }
        while (!matcher.matches());
        if (input.equals("y") || input.equals("Y")) {
            do {
                System.out.print("Add rating(1-5): ");
                input = scanner.nextLine();
                matcher = patternRating.matcher(input);
            }
            while (!matcher.matches());
            try{
                ratingService.setRating(new Rating("Nonogram", name, Integer.parseInt(input), new Date(System.currentTimeMillis())));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public int selectSize() {
        Pattern pattern = Pattern.compile("5|10");
        Matcher matcher;
        String input;
        Scanner sizeScanner = new Scanner(System.in);
        int size = 0;
        do {
            System.out.print("Enter size of gamefield: (5 or 10) ");
            input = sizeScanner.nextLine();
            matcher = pattern.matcher(input);

        }
        while (!matcher.matches());
        size = Integer.parseInt(input, 10);
        return size;
    }

    public int averageScore() {
        List<Score> scores = scoreService.getTopScores("Nonogram");
        int sum = 0;
        for (Score temp : scores) {
            sum += temp.getPoints();
        }
        if (scores.size() != 0) return sum / scores.size();
        else return 0;
    }
}
