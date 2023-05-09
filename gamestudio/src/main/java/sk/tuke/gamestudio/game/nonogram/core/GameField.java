package sk.tuke.gamestudio.game.nonogram.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameField{

    public class TilesFactory {
        public Tile[][] createEmptyTiles(String type) {
            Tile[][] emptyTiles = new Tile[rows][columns];
            if (type == "guessed") for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    emptyTiles[i][j] = new Tile();
                }
            }
            else {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
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
    private int[][] columnClues;

    public GameField(int rows, int columns, Type type) throws Exception {
        if(rows<5||rows>99||columns<5||columns>99) throw new Exception("Invalid size");
        this.chances = 3;
        this.helpUses = 0;
        this.rows = rows;
        this.columns = columns;
        TilesFactory factory = new TilesFactory();
        this.tiles = factory.createEmptyTiles("tiles");

        this.guessedTiles = factory.createEmptyTiles("guessed");
        this.type = type;
        state = State.PLAYING;
        try {
            tiles = getRandomNonogram();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        columnClues=columnClue();
        timeAtStart = LocalTime.now();
    }

    public GameField(int rows, int columns, Type type, Boolean creator) throws Exception {
        if(rows<5||rows>99||columns<5||columns>99) throw new Exception("Invalid size");
        this.chances = 3;
        this.helpUses = 0;
        this.rows = rows;
        this.columns = columns;
        TilesFactory factory = new TilesFactory();
        this.tiles = factory.createEmptyTiles("tiles");

        this.guessedTiles = factory.createEmptyTiles("guessed");
        this.type = type;
        state = State.PLAYING;
        tiles = factory.createEmptyTiles("guessed");
        columnClues=columnClue();
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

    public int getChances() {
        return chances;
    }

    public int[][] getColumnClues() {
        return columnClues;
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

    public void setChances(int chances) {
        this.chances = chances;
    }

    public Tile[][] getRandomNonogram() throws IOException {
        TilesFactory factory = new TilesFactory();
        Tile[][] randomNonogram = factory.createEmptyTiles("tiles");
        InputStream inputStream = GameField.class.getResourceAsStream("/maps/" + rows + "x" + columns);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        Random random = new Random();
        int randomLine = random.nextInt(lines);
        inputStream = GameField.class.getResourceAsStream("/maps/" + rows + "x" + columns);
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
        if (state == State.FAILED) return false;
        if (state == State.PLAYING) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if ((tiles[i][j].getState() == Tile.State.FILLED && guessedTiles[i][j].getState() != Tile.State.FILLED) || (tiles[i][j].getState() == Tile.State.UNMARKED && guessedTiles[i][j].getState() == Tile.State.FILLED) || (tiles[i][j].getState() == Tile.State.BLANK && guessedTiles[i][j].getState() == Tile.State.FILLED)) {
                        chances--;
                        if (chances < 1) {
                            state = State.FAILED;
                        }
                        return false;
                    }
                }
            }
        }
        state = State.SOLVED;
        return true;
    }

    public void markBlankTile(int row, int column) {
        if ((row < rows && row >= 0) && (column < columns && column >= 0))
            guessedTiles[row][column].setState(Tile.State.BLANK);
    }

    public void fillTile(int row, int column) {
        if ((row < rows && row >= 0) && (column < columns && column >= 0))
            guessedTiles[row][column].setState(Tile.State.FILLED);
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

    public boolean areThereIncorrectlySolved() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (guessedTiles[i][j].getState() != tiles[i][j].getState()) return true;
            }
        }
        return false;
    }

    public int getScore() {
        int score = 10 - (3 - chances) - (helpUses);
        if (score < 0 || state == State.FAILED) return 0;
        else return score;
    }

    public void fillUnmarkedAsBlank() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (guessedTiles[i][j].getState() == Tile.State.UNMARKED) guessedTiles[i][j].setState(Tile.State.BLANK);
            }
        }
    }

    public List<List<Integer>> rowClues() {
        List<List<Integer>> rowClues= new ArrayList<>();
        for(int i=0; i<rows; i++) {
            int run = 0;
            List<Integer> clue = new ArrayList<>();
            for (int k = 0; k < columns; k++) {
                if (getTile(i, k).getState() == Tile.State.FILLED) run++;
                else {
                    if (run != 0) clue.add(run);
                    run = 0;
                }
                if (run != 0 && k == columns - 1) clue.add(run);
            }
            rowClues.add(clue);
        }
        int maxLength=0;
        for (List<Integer> list : rowClues) {
            int size = list.size();
            if (size > maxLength) {
                maxLength = size;
            }
        }
        for (List<Integer> list : rowClues) {
            int size = list.size();
            if (size < maxLength) {
                int diff = maxLength - size;
                List<Integer> zeros = new ArrayList<>(Collections.nCopies(diff, 0));
                list.addAll(zeros);
            }
        }
        return rowClues;
    }

    public int[][] columnClue() {
        int realRows=rows/2+1;
        int[][] columnClues = new int[realRows][columns];
        int[][] shortened;
        int zeros;
        int clueRow = 0;
        for (int j = 0; j < columns; j++) {
            int count = 0;
            for (int i = 0; i < rows; i++) {
                if (getTile(i, j).getState() == Tile.State.FILLED) {
                    count++;
                } else if (count > 0) {
                    columnClues[clueRow][j] = count;
                    count = 0;
                    clueRow++;
                }
            }
            if (count > 0) {
                columnClues[clueRow][j] = count;
            }
            clueRow = 0;
        }
        int zeroRow=0;
        for(int i=0; i<realRows; i++){
            for(int j=0; j<columns; j++){
                if(columnClues[i][j]==0)zeroRow++;
            }
            if(zeroRow==columns){
                if(realRows>i+1)realRows=i;
                break;
            }
            zeroRow=0;
        }
        if(zeroRow==columns) {
            int realColumnClues[][] = new int[realRows][columns];
            for (int i = 0; i < realRows; i++) {
                for (int j = 0; j < columns; j++) {
                    realColumnClues[i][j] = columnClues[i][j];
                }
            }
            return realColumnClues;
        }
        else return columnClues;
    }

    public void save() throws Exception{
        Maps.save(rows, columns, guessedTiles);
    }

}
