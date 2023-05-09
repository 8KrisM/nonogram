package sk.tuke.kpi.nonogram.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.nonogram.core.GameField;
import sk.tuke.gamestudio.game.nonogram.core.Tile;

import java.io.IOException;

public class GameFieldTest {
    private int columns, rows;
    private GameField.Type type;
    private GameField gamefield;

    public GameFieldTest() {
        rows = 5;
        columns = 5;
        type = GameField.Type.BLACKANDWHITE;
        try{
            gamefield = new GameField(rows, columns, type);
        }
        catch (Exception e){throw new RuntimeException(e.getMessage());}
    }

    @Test
    public void isSolvedIncorrect() {
        Assertions.assertEquals(false, gamefield.isSolved());
    }

    @Test
    public void isSolvedCorrect() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gamefield.getGuessedTile(i, j).setState(gamefield.getTile(i, j).getState());
            }
        }
        Assertions.assertEquals(true, gamefield.isSolved());
    }

    @Test
    public void helpRandomTest() {
        int unchanged = (rows * columns) - 1;
        int tilesCount = 0;
        gamefield.helpRandom();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (gamefield.getGuessedTile(i, j).getState() == Tile.State.UNMARKED) tilesCount++;
            }
        }
        Assertions.assertEquals(unchanged, tilesCount);
    }

    @Test
    public void runningRandomHelpGamefieldSizeTimesShouldSolveNonogram() {
        for (int i = 0; i < rows * columns; i++) gamefield.helpRandom();
        Assertions.assertEquals(true, gamefield.isSolved());
    }

    @Test
    public void getRandomNonogramShouldBeRandom() throws IOException {
        Tile[][] randomNonogram0 = gamefield.getRandomNonogram();
        Tile[][] randomNonogram1 = gamefield.getRandomNonogram();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (randomNonogram0[i][j].getState() != randomNonogram1[i][j].getState()) return;
                else randomNonogram1 = gamefield.getRandomNonogram();
            }
        }
    }


}
