package sk.tuke.kpi.kp.nonogram.core;

public class Color {
    private int red, green, blue;

    public Color(int r, int g, int b){
        red=r;
        green=g;
        blue=b;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }

    public int getRed() {
        return red;
    }
}
