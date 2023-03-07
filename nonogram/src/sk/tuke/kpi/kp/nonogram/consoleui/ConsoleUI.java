package sk.tuke.kpi.kp.nonogram.consoleui;

import sk.tuke.kpi.kp.nonogram.core.GameField;
import sk.tuke.kpi.kp.nonogram.core.Tile;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private GameField gameField;
    public void play(GameField gameField) {
        this.gameField = gameField;
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
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

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
}
