package net.jiussa.nottetris;

public class GridPosition {
    public int x;
    public int y;

    public GridPosition(int x, int y) {
        set(x, y);
    }

    public GridPosition() {
        this(0, 0);
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}