package net.jiussa.nottetris;

import com.badlogic.gdx.graphics.Color;

public enum Tetromino {
    I(Color.CYAN, new GridPosition(3, Constants.GRID_HEIGHT_VIS - 2), new GridPosition[][] {
        {new GridPosition(0, 2), new GridPosition(1, 2), new GridPosition(2, 2),
         new GridPosition(3, 2)},
        {new GridPosition(2, 0), new GridPosition(2, 1), new GridPosition(2, 2),
         new GridPosition(2, 3)},
        {new GridPosition(0, 1), new GridPosition(1, 1), new GridPosition(2, 1),
         new GridPosition(3, 1)},
        {new GridPosition(1, 0), new GridPosition(1, 1), new GridPosition(1, 2),
         new GridPosition(1, 3)}}),

    J(Color.BLUE, new GridPosition(3, Constants.GRID_HEIGHT_VIS - 1), new GridPosition[][] {
        {new GridPosition(0, 1), new GridPosition(1, 1), new GridPosition(2, 1),
         new GridPosition(0, 2)},
        {new GridPosition(1, 0), new GridPosition(1, 1), new GridPosition(1, 2),
         new GridPosition(2, 2)},
        {new GridPosition(2, 0), new GridPosition(0, 1), new GridPosition(1, 1),
         new GridPosition(2, 1)},
        {new GridPosition(0, 0), new GridPosition(1, 0), new GridPosition(1, 1),
         new GridPosition(1, 2)}}),

    L(Color.ORANGE, new GridPosition(3, Constants.GRID_HEIGHT_VIS - 1), new GridPosition[][] {
        {new GridPosition(0, 1), new GridPosition(1, 1), new GridPosition(2, 1),
         new GridPosition(2, 2)},
        {new GridPosition(1, 0), new GridPosition(2, 0), new GridPosition(1, 1),
         new GridPosition(1, 2)},
        {new GridPosition(0, 0), new GridPosition(0, 1), new GridPosition(1, 1),
         new GridPosition(2, 1)},
        {new GridPosition(1, 0), new GridPosition(1, 1), new GridPosition(0, 2),
         new GridPosition(1, 2)}}),

    O(Color.YELLOW, new GridPosition(4, Constants.GRID_HEIGHT_VIS), new GridPosition[][] {
        {new GridPosition(0, 0), new GridPosition(1, 0), new GridPosition(0, 1),
         new GridPosition(1, 1)},
        {new GridPosition(0, 0), new GridPosition(1, 0), new GridPosition(0, 1),
         new GridPosition(1, 1)},
        {new GridPosition(0, 0), new GridPosition(1, 0), new GridPosition(0, 1),
         new GridPosition(1, 1)},
        {new GridPosition(0, 0), new GridPosition(1, 0), new GridPosition(0, 1),
         new GridPosition(1, 1)}}),

    S(Color.GREEN, new GridPosition(3, Constants.GRID_HEIGHT_VIS - 1), new GridPosition[][] {
        {new GridPosition(0, 1), new GridPosition(1, 1), new GridPosition(1, 2),
         new GridPosition(2, 2)},
        {new GridPosition(2, 0), new GridPosition(1, 1), new GridPosition(2, 1),
         new GridPosition(1, 2)},
        {new GridPosition(0, 0), new GridPosition(1, 0), new GridPosition(1, 1),
         new GridPosition(2, 1)},
        {new GridPosition(1, 0), new GridPosition(0, 1), new GridPosition(1, 1),
         new GridPosition(0, 2)}}),

    T(Color.PURPLE, new GridPosition(3, Constants.GRID_HEIGHT_VIS - 1), new GridPosition[][] {
        {new GridPosition(0, 1), new GridPosition(1, 1), new GridPosition(2, 1),
         new GridPosition(1, 2)},
        {new GridPosition(1, 0), new GridPosition(1, 1), new GridPosition(2, 1),
         new GridPosition(1, 2)},
        {new GridPosition(1, 0), new GridPosition(0, 1), new GridPosition(1, 1),
         new GridPosition(2, 1)},
        {new GridPosition(1, 0), new GridPosition(0, 1), new GridPosition(1, 1),
         new GridPosition(1, 2)}}),

    Z(Color.RED, new GridPosition(3, Constants.GRID_HEIGHT_VIS - 1), new GridPosition[][] {
        {new GridPosition(1, 1), new GridPosition(2, 1), new GridPosition(0, 2),
         new GridPosition(1, 2)},
        {new GridPosition(1, 0), new GridPosition(1, 1), new GridPosition(2, 1),
         new GridPosition(2, 2)},
        {new GridPosition(1, 0), new GridPosition(2, 0), new GridPosition(0, 1),
         new GridPosition(1, 1)},
        {new GridPosition(0, 0), new GridPosition(0, 1), new GridPosition(1, 1),
         new GridPosition(1, 2)}});

    public final Color colour;
    public final Color colourHighlight;
    public final Color colourShadow;

    public final GridPosition spawnPosition;
    public final GridPosition[][] rotations;

    private Tetromino(Color colour, GridPosition spawnPosition, GridPosition[][] rotations) {
        this.colour = colour;
        this.spawnPosition = spawnPosition;
        this.rotations = rotations;

        colourHighlight = colour.cpy().add(0.6f, 0.6f, 0.6f, 0);
        colourShadow = colour.cpy().sub(0.6f, 0.6f, 0.6f, 0);
    }
}