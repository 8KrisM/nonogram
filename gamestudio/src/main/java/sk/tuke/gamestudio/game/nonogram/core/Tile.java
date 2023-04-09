package sk.tuke.gamestudio.game.nonogram.core;

public class Tile {
    public enum State {
        UNMARKED, BLANK, FILLED
    }

    private State state;

    public Tile() {
        state = State.UNMARKED;
    }

    public Tile(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        if (state == State.FILLED) return "■";
        else if (state == State.BLANK) return ".";
        else return " ";
    }
}
