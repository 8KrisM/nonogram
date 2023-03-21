package sk.tuke.kpi.kp.nonogram.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameField {

    public class TilesFactory{
        public Tile[][] createEmptyTiles(String type){
            Tile[][] emptyTiles = new Tile[rows][columns];
            if(type=="guessed")for(int i=0; i<rows; i++){
                for(int j=0; j<columns; j++){
                    emptyTiles[i][j] = new Tile();
                }
            }
            else{
                for(int i=0; i<rows; i++){
                    for(int j=0; j<columns; j++){
                        emptyTiles[i][j] = new Tile();
                        emptyTiles[i][j].setState(Tile.State.BLANK);
                    }
                }
            }
            return emptyTiles;
        }
    }
    public enum State {
        PLAYING, SOLVED, FAILED
    }

    public enum Type {
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
    private int chances;
    private int helpUses;

    public GameField(int rows, int columns, Type type) {
        this.chances=3;
        this.helpUses=0;
        this.rows = rows;
        this.columns = columns;
        TilesFactory factory = new TilesFactory();
        this.tiles = factory.createEmptyTiles("tiles");

        this.guessedTiles = factory.createEmptyTiles("guessed");
        this.type = type;
        state = State.PLAYING;
        try {
            tiles=getRandomNonogram();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        timeAtStart = LocalTime.now();
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

    public Tile getGuessedTile(int row, int column) {
        return guessedTiles[row][column];
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getChances() { return chances; }

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

    public void setChances(int chances) { this.chances = chances; }

    public Tile[][] getRandomNonogram() throws IOException {
        TilesFactory factory= new TilesFactory();
        Tile[][] randomNonogram= factory.createEmptyTiles("tiles");
        InputStream inputStream = GameField.class.getResourceAsStream("/" + rows + "x" + columns);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        Random random = new Random();
        int randomLine = random.nextInt(lines);
        inputStream = GameField.class.getResourceAsStream("/" + rows + "x" + columns);
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int currentLine = 0;
        while ((line = reader.readLine()) != null) {
            if (currentLine == randomLine) break;
            currentLine++;
        }
        reader.close();
        int i = 0;
        while (i < line.length()) {
            randomNonogram[Integer.parseInt(line.substring(i, i + 2), 10) - 1][Integer.parseInt(line.substring(i + 2, i + 4), 10) - 1].setState(Tile.State.FILLED);
            i = i + 5;
        }
        return randomNonogram;
    }

    public Boolean isSolved() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (tiles[i][j].getState() != guessedTiles[i][j].getState()) {
                    chances--;
                    if(chances<1) {
                        state = State.FAILED;
                    }
                    return false;
                }
            }
        }
        state = State.SOLVED;
        return true;
    }

    public void markBlankTile(int row, int column) {
        if((row<rows&&row>=0)&&(column<columns&&column>=0))guessedTiles[row][column].setState(Tile.State.BLANK);
    }

    public void fillTile(int row, int column) {
        if((row<rows&&row>=0)&&(column<columns&&column>=0))guessedTiles[row][column].setState(Tile.State.FILLED);
    }

    public void helpRandom() {
        int randomRow;
        int randomColumn;
        do {
            randomRow = (int) (Math.random() * rows);
            randomColumn = (int) (Math.random() * columns);
        }
        while (guessedTiles[randomRow][randomColumn].getState() == tiles[randomRow][randomColumn].getState() && areThereIncorrectlySolved());
        helpUses++;
        guessedTiles[randomRow][randomColumn].setState(tiles[randomRow][randomColumn].getState());
    }
    public boolean areThereIncorrectlySolved(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(guessedTiles[i][j].getState() != tiles[i][j].getState()) return true;
            }
        }
        return false;
    }

    public int getScore(){
        int score=10-(3-chances)-(helpUses);
        if(score<0||state==State.FAILED) return 0;
        else return score;
    }

    public void fillUnmarkedAsBlank(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(guessedTiles[i][j].getState()== Tile.State.UNMARKED) guessedTiles[i][j].setState(Tile.State.BLANK);
            }
        }
    }
}
