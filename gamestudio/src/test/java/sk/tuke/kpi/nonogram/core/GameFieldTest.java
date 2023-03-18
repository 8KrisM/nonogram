package sk.tuke.kpi.nonogram.core;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.nonogram.core.GameField;
import sk.tuke.kpi.kp.nonogram.core.Tile;

public class GameFieldTest {
    private int columns, rows;
    private GameField.Type type;
    private GameField gamefield;

    public GameFieldTest(){
        rows=5;
        columns=5;
        type= GameField.Type.BLACKANDWHITE;
        gamefield= new GameField(rows, columns, type);
    }

    @Test
    public void isSolvedIncorrect(){
        Assertions.assertEquals(false, gamefield.isSolved());
    }

    @Test
    public void isSolvedCorrect(){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                gamefield.markBlankTile(i,j);
            }
        }
        gamefield.fillTile(1,2);
        gamefield.fillTile(1,3);
        gamefield.fillTile(1,4);
        gamefield.fillTile(2,2);
        gamefield.fillTile(2,3);
        gamefield.fillTile(3,2);
        gamefield.fillTile(3,4);
        Assertions.assertEquals(true, gamefield.isSolved());
    }

    @Test
    public void helpRandomTest(){
        int unchanged=(rows*columns)-1;
        int tilesCount=0;
        gamefield.helpRandom();
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(gamefield.getGuessedTile(i,j).getState()== Tile.State.UNMARKED)tilesCount++;
            }
        }
        Assertions.assertEquals(unchanged, tilesCount);
    }
}
