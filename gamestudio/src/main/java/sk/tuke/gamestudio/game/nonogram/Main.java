package sk.tuke.gamestudio.game.nonogram;

import sk.tuke.gamestudio.game.nonogram.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.nonogram.core.GameField;

public class Main {
    public static void main(String[] args) throws Exception {
        ConsoleUI ui = new ConsoleUI();
        int size = ui.selectSize();
        GameField gamefield = new GameField(size, size, GameField.Type.BLACKANDWHITE);
        new ConsoleUI(gamefield).play();
    }
}
