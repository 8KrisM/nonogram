package sk.tuke.kpi.kp.nonogram;

import sk.tuke.kpi.kp.nonogram.consoleui.ConsoleUI;
import sk.tuke.kpi.kp.nonogram.core.GameField;

public class Main {
    public static void main(String[] args) {
        GameField gamefield= new GameField(5,5, GameField.Type.BLACKANDWHITE);
        ConsoleUI ui = new ConsoleUI();
        ui.play(gamefield);
    }
}
