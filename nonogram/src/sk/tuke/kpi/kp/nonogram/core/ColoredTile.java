package sk.tuke.kpi.kp.nonogram.core;

public class ColoredTile extends Tile{

    private Color color;

    public ColoredTile(Color color){
        this.color=color;
    }

    public ColoredTile(Color color, State state){
        this.setState(state);
        this.color=color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
