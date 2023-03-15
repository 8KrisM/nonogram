package sk.tuke.kpi.kp.nonogram.consoleui;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentServiceJDBC;
import sk.tuke.gamestudio.service.RatingServiceJDBC;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;
import sk.tuke.kpi.kp.nonogram.core.GameField;
import sk.tuke.kpi.kp.nonogram.core.Tile;

import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private GameField gameField;
    private ScoreServiceJDBC scoreServiceJDBC = new ScoreServiceJDBC();
    private RatingServiceJDBC ratingServiceJDBC = new RatingServiceJDBC();
    private String name;
    public void play(GameField gameField) {
        this.gameField = gameField;

        System.out.print("Enter your name: ");
        Scanner nameScanner= new Scanner(System.in);
        name=nameScanner.nextLine();
        System.out.println("Hi "+name+"! Let's play.");

        do {
            show();
            handleInput();
        } while(gameField.getState() == GameField.State.PLAYING);

        show();
        if(gameField.getState() == GameField.State.SOLVED) {
            System.out.println("Solved successfully! Congratulations.");
        } else if(gameField.getState() == GameField.State.FAILED){
            System.out.println("Oh no! Failed!");
        }
        scoreServiceJDBC.addScore(new Score("Nonogram", name, 5, new Date(System.currentTimeMillis())));
        handleComment();
        handleRating();
    }

    public void show(){
        System.out.print("  ");
        for (int i = 1; i <= gameField.getColumns(); i++){
            System.out.print(" "+(char)(i+96));

        }
        System.out.print("\n  ");
        for (int i = 1; i <= gameField.getColumns(); i++){
            System.out.print("--");

        }
        System.out.println();
        for(int i = 1; i <= gameField.getRows(); i++){
            System.out.print(i+"|");
            for (int j = 1; j <= gameField.getColumns(); j++) System.out.print(gameField.getGuessedTile(i-1,j-1).toString()+" ");
            System.out.print("|");
            int run=0;
            for(int k = 1; k <= gameField.getColumns(); k++){
                if(gameField.getTile(i-1,k-1).getState()== Tile.State.FILLED) run++;
                else{
                    if(run!=0)System.out.print(" "+run);
                    run=0;
                }
                if(run!=0&&k==gameField.getColumns()) System.out.print(" "+run);
            }
            System.out.print("\n");
        }
        System.out.print("  ");
        for (int i = 1; i <= gameField.getColumns(); i++){
            System.out.print("--");
        }
        System.out.println();
    }
    public void handleInput(){
        Pattern patternCheck=Pattern.compile("C|H");
        Pattern patternField=Pattern.compile("(F|B)([A-"+(char)(gameField.getColumns()+97)+"])([1-"+gameField.getRows()+"])");

        String input;
        Scanner scanner = new Scanner(System.in);

        Matcher matcherCheck;
        Matcher matcherField;

        do{
            System.out.print("Enter <C> to exit,<H> for random help, <FA1> to fill A1, <BA1> to mark A1 as blank: ");
            input = scanner.nextLine();
            matcherCheck= patternCheck.matcher(input);
            matcherField= patternField.matcher(input);
        }
        while(!matcherCheck.matches()&&!matcherField.matches());
        if(input.equals("H")) gameField.helpRandom();
        if(input.equals("C")){
            gameField.isSolved();
        }
        else{
            if(input.charAt(0)=='F'){
                gameField.fillTile(input.charAt(2)-49,input.charAt(1)-65);
            }
            if(input.charAt(0)=='B'){
                gameField.markBlankTile(input.charAt(2)-49,input.charAt(1)-65);
            }
        }
    }

    public void handleComment(){
        CommentServiceJDBC commentServiceJDBC = new CommentServiceJDBC();
        Pattern patternInput=Pattern.compile("(y|Y)|(n|N)");
        Scanner scanner = new Scanner(System.in);
        String input;
        Matcher matcher;
        do{
            System.out.print("Do you want to add comment? (y/n): ");
            input = scanner.nextLine();
            matcher= patternInput.matcher(input);
        }
        while(!matcher.matches());
        if(input.equals("y") || input.equals("Y")){
            System.out.print("Write comment: ");
            input=scanner.nextLine();
            commentServiceJDBC.addComment(new Comment("Nonogram",name,input,new Date(System.currentTimeMillis())));
        }
    }

    public void handleRating(){
        RatingServiceJDBC commentServiceJDBC = new RatingServiceJDBC();
        Pattern patternInput=Pattern.compile("(y|Y)|(n|N)");
        Pattern patternRating= Pattern.compile("[1-5]");
        Scanner scanner = new Scanner(System.in);
        String input;
        Matcher matcher;
        do{
            System.out.print("Do you want to add rating? (y/n): ");
            input = scanner.nextLine();
            matcher= patternInput.matcher(input);
        }
        while(!matcher.matches());
        if(input.equals("y") || input.equals("Y")){
            do {
                System.out.print("Add rating: (1-5)");
                input = scanner.nextLine();
                matcher= patternRating.matcher(input);
            }
            while(!matcher.matches());
            ratingServiceJDBC.setRating(new Rating("Nonogram",name,Integer.parseInt(input),new Date(System.currentTimeMillis())));
        }
    }
}
