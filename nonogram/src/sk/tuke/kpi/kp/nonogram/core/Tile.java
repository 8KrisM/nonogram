package sk.tuke.kpi.kp.nonogram.core;

public class Tile {
    enum State{
        UNMARKED, BLANK, FILLED
    }
    private State state;

    public void setState(State state) {
        this.state = state;
    }
}
