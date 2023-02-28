package sk.tuke.kpi.kp.nonogram.core;

import java.util.ArrayList;
import java.util.Arrays;

public class GameField {

    private enum State{
        PLAYING, SOLVED, FAILED
    }

    public enum Type{
        BLACKANDWHITE, COLOR
    }

    private ArrayList<Color> colors;
    private Tile[][] tiles;
    private Tile[][] guessedTiles;
    private int columns;
    private int rows;
    private Type type;
    private State state;
    private int timer;

    public GameField(int rows, int columns, Type type){
        this.rows=rows;
        this.columns=columns;
        this.type=type;
        state=State.PLAYING;
        tiles = getRandomNonogram();
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public Tile getTile(int row, int column) {
        return this.tiles[row][column];
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getTimer() {
        return timer;
    }

    private Tile[][] getRandomNonogram() {
        return new Tile[0][];
    }

    public Boolean isSolved(){
        return Arrays.deepEquals(tiles, guessedTiles);
    }

    public void markBlankTile(int row, int column){
        guessedTiles[row][column].setState(Tile.State.BLANK);
    }
    public void fillTile(int row, int column, Color color){
        guessedTiles[row][column].setState(Tile.State.FILLED);
    }
    public void helpRandom(){

    }
}
