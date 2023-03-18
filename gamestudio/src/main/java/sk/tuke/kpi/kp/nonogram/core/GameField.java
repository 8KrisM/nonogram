package sk.tuke.kpi.kp.nonogram.core;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class GameField {

    public enum State{
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
    private LocalTime timeAtStart;

    public GameField(int rows, int columns, Type type){
        this.rows=rows;
        this.columns=columns;
        this.tiles= new Tile[rows][columns];
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                tiles[i][j]=new Tile();
            }
        }
        this.guessedTiles= new Tile[rows][columns];
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                guessedTiles[i][j]=new Tile();
            }
        }
        this.type=type;
        state=State.PLAYING;
        getRandomNonogram();
        timeAtStart=LocalTime.now();
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile[][] getGuessedTiles() {
        return guessedTiles;
    }

    public Type getType() {
        return type;
    }

    public State getState() {
        return state;
    }

    public LocalTime getTimeAtStart() {
        return timeAtStart;
    }
    public Tile getGuessedTile(int row, int column){ return guessedTiles[row][column];}
    public Tile getTile(int row, int column){ return tiles[row][column];}

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public void setGuessedTiles(Tile[][] guessedTiles) {
        this.guessedTiles = guessedTiles;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setTimeAtStart(LocalTime timeAtStart) {
        this.timeAtStart = timeAtStart;
    }


    private void getRandomNonogram(){
        type=Type.BLACKANDWHITE;
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                tiles[i][j].setState(Tile.State.BLANK);
            }
        }
        tiles[1][2].setState(Tile.State.FILLED);
        tiles[1][3].setState(Tile.State.FILLED);
        tiles[1][4].setState(Tile.State.FILLED);
        tiles[2][2].setState(Tile.State.FILLED);
        tiles[2][3].setState(Tile.State.FILLED);
        tiles[3][2].setState(Tile.State.FILLED);
        tiles[3][4].setState(Tile.State.FILLED);
    }

    public Boolean isSolved(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(tiles[i][j].getState()!=guessedTiles[i][j].getState()) {
                    state=State.FAILED;
                    return false;
                }
            }
        }
        state=State.SOLVED;
        return true;
    }

    public void markBlankTile(int row, int column){
        guessedTiles[row][column].setState(Tile.State.BLANK);
    }
    public void fillTile(int row, int column){
        guessedTiles[row][column].setState(Tile.State.FILLED);
    }
    public void helpRandom(){
        int randomRow;
        int randomColumn;
        do {
            randomRow = (int) (Math.random() * rows);
            randomColumn = (int) (Math.random() * columns);
        }
        while(guessedTiles[randomRow][randomColumn].getState() != Tile.State.UNMARKED);
        guessedTiles[randomRow][randomColumn].setState(tiles[randomRow][randomColumn].getState());
    }

}
