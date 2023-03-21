package sk.tuke.kpi.kp.nonogram;

import sk.tuke.kpi.kp.nonogram.consoleui.ConsoleUI;
import sk.tuke.kpi.kp.nonogram.core.GameField;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        int size = ui.selectSize();
        GameField gamefield = new GameField(size, size, GameField.Type.BLACKANDWHITE);
        ui.play(gamefield);
    }
}
