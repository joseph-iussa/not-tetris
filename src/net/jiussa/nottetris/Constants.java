package net.jiussa.nottetris;

public class Constants {
    public static final int APP_WIDTH = 400; // In pixels.
    public static final int APP_HEIGHT = 800;
    public static final int GRID_WIDTH = 10; // In cells.
    public static final int CELL_SIZE = APP_WIDTH / GRID_WIDTH; // In pixels.
    public static final int GRID_HEIGHT = 22;
    public static final int GRID_HEIGHT_VIS = 20;
    public static final float BASE_PIECE_GRAVITY_FREQUENCY = 1f; // In seconds.
    public static final float PIECE_MOVE_INTERVAL_FIRST_STAGE = 0.18f;
    public static final float PIECE_MOVE_INTERVAL_SECOND_STAGE = 0.07f;
    public static final float PIECE_LOCK_DELAY = 0.5f;
    public static final float LINE_CLEAR_COLLAPSE_DELAY = 0.2f;
    public static final float PIECE_SHADOW_ALPHA = 0.25f;
}