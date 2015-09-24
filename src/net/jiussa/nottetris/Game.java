package net.jiussa.nottetris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game extends ApplicationAdapter {
    private Grid grid;
    public Piece activePiece;
    private Timer pieceGravityTimer;
    private RandomBag<Tetromino> pieceBag;
    private List<Integer> linesCleared;
    private DelayedTasks delayedTasks;
    private DelayedTask pieceLockTimer;

    private boolean pieceMovingLeft;
    private MovementStage movementStageLeft;
    private Timer pieceMovingLeftTimerFirstStage;
    private Timer pieceMovingLeftTimerSecondStage;

    private boolean pieceMovingRight;
    private MovementStage movementStageRight;
    private Timer pieceMovingRightTimerFirstStage;
    private Timer pieceMovingRightTimerSecondStage;

    private boolean pieceMovingDown;
    private MovementStage movementStageDown;
    private Timer pieceMovingDownTimerFirstStage;
    private Timer pieceMovingDownTimerSecondStage;

    private boolean pieceSpawnEnqued;
    private boolean inPieceLockDelayPeriod;
    private boolean drawingLineClearEffect;

    private Piece pieceShadow;

    private int totalLinesCleared;

    private ShapeRenderer rend;

    @Override
    public void create() {
        grid = new Grid(Constants.GRID_WIDTH, Constants.GRID_HEIGHT);
        activePiece = new Piece();
        pieceGravityTimer = new Timer(Constants.BASE_PIECE_GRAVITY_FREQUENCY);
        pieceBag = new RandomBag<>(Arrays.asList(Tetromino.values()));
        linesCleared = new ArrayList<>(4);
        delayedTasks = new DelayedTasks(100);
        pieceLockTimer = new DelayedTask();
        pieceLockTimer.init(Constants.PIECE_LOCK_DELAY, () -> {
            doPieceLock();
            cancelPieceLockTimer();
        });

        pieceMovingLeft = false;
        pieceMovingRight = false;
        pieceMovingDown = false;
        movementStageLeft = MovementStage.FIRST;
        movementStageRight = MovementStage.FIRST;
        movementStageDown = MovementStage.FIRST;
        pieceMovingLeftTimerFirstStage = new Timer(Constants.PIECE_MOVE_INTERVAL_FIRST_STAGE);
        pieceMovingRightTimerFirstStage = new Timer(Constants.PIECE_MOVE_INTERVAL_FIRST_STAGE);
        pieceMovingDownTimerFirstStage = new Timer(Constants.PIECE_MOVE_INTERVAL_FIRST_STAGE);
        pieceMovingLeftTimerSecondStage = new Timer(Constants.PIECE_MOVE_INTERVAL_SECOND_STAGE);
        pieceMovingRightTimerSecondStage = new Timer(Constants.PIECE_MOVE_INTERVAL_SECOND_STAGE);
        pieceMovingDownTimerSecondStage = new Timer(Constants.PIECE_MOVE_INTERVAL_SECOND_STAGE);

        inPieceLockDelayPeriod = false;
        drawingLineClearEffect = false;

        pieceShadow = new Piece();

        totalLinesCleared = 0;

        rend = new ShapeRenderer();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.input.setInputProcessor(new InputProcessor(this));

        enquePieceSpawn();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        if (pieceSpawnEnqued()) {
            spawnPiece();
        }

        delayedTasks.update(dt);

        if (pieceMovingLeft) {
            movePieceLeft(dt);
        }

        if (pieceMovingRight) {
            movePieceRight(dt);
        }

        if (pieceMovingDown) {
            movePieceDown(dt);
        }

        processPieceGravity(dt);

        checkPieceLock(dt);

        updatePieceShadow();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderGrid(grid, Constants.GRID_WIDTH, Constants.GRID_HEIGHT_VIS);
        renderPiece(pieceShadow, Constants.PIECE_SHADOW_ALPHA);
        renderPiece(activePiece, 1);

        if (drawingLineClearEffect) {
            renderLineClearEffect();
        }
    }

    private void checkPieceLock(float dt) {
        if (pieceShouldBeLocked()) {
            if (!inPieceLockDelayPeriod) {
                initiatePieceLockTimer();
            }
            pieceLockTimer.update(dt);
        } else {
            if (inPieceLockDelayPeriod) {
                cancelPieceLockTimer();
            }
        }
    }

    private void initiatePieceLockTimer() {
        pieceLockTimer.resetTimer();
        inPieceLockDelayPeriod = true;
    }

    private void cancelPieceLockTimer() {
        inPieceLockDelayPeriod = false;
    }

    private boolean pieceShouldBeLocked() {
        for (GridPosition cell : activePiece.currentRotationInGridSpace()) {
            if (cell.y < 1 || grid.getCell(cell.x, cell.y - 1) != null) {
                return true;
            }
        }
        return false;
    }

    private void doPieceLock() {
        for (GridPosition cell : activePiece.currentRotationInGridSpace()) {
            grid.setCell(cell.x, cell.y, activePiece.type);
        }
        checkLineClears();
        enquePieceSpawn();
    }

    private void checkLineClears() {
        for (int row = 0; row < Constants.GRID_HEIGHT; ++row) {
            if (grid.rowIsFull(row)) {
                grid.clearRow(row);
                linesCleared.add(row);
            }
        }
        if (linesCleared.size() > 0) {
            drawingLineClearEffect = true;
            totalLinesCleared += linesCleared.size();
            pieceGravityTimer.reset(Constants.BASE_PIECE_GRAVITY_FREQUENCY
                                        - 0.02f * totalLinesCleared);
            delayedTasks.add(Constants.LINE_CLEAR_COLLAPSE_DELAY, () -> {
                grid.collapseClearedRows(linesCleared);
                linesCleared.clear();
                drawingLineClearEffect = false;
            });
        }
    }

    public void enquePieceSpawn() {
        pieceSpawnEnqued = true;
    }

    private boolean pieceSpawnEnqued() {
        return pieceSpawnEnqued;
    }

    private void spawnPiece() {
        activePiece.init(pieceBag.nextItem());
        pieceSpawnEnqued = false;
    }

    public boolean checkPieceCollision(int gridPosX, int gridPosY, GridPosition[] rotation) {
        for (GridPosition cell : rotation) {
            // Check grid boundaries.
            if (gridPosX + cell.x < 0 || gridPosX + cell.x >= Constants.GRID_WIDTH) {
                return false;
            }
            // Check grid floor for use when hard dropping piece.
            if (gridPosY + cell.y < 0) {
                return false;
            }
            // Check locked piece collisions.
            if (grid.getCell(gridPosX + cell.x, gridPosY + cell.y) != null) {
                return false;
            }
        }
        return true;
    }

    public void startMovingPieceLeft() {
        boolean collisionOK = checkPieceCollision(activePiece.pos.x - 1, activePiece.pos.y,
                                                  activePiece.currentRotation());
        if (collisionOK) {
            activePiece.moveLeft();
        }

        pieceMovingLeft = true;
        movementStageLeft = MovementStage.FIRST;
        pieceMovingLeftTimerFirstStage.reset();
    }

    public void movePieceLeft(float dt) {
        boolean collisionOK = checkPieceCollision(activePiece.pos.x - 1, activePiece.pos.y,
                                                  activePiece.currentRotation());
        switch (movementStageLeft) {
            case FIRST: {
                if (pieceMovingLeftTimerFirstStage.update(dt)) {
                    if (collisionOK) {
                        activePiece.moveLeft();
                    }
                    movementStageLeft = MovementStage.SECOND;
                    pieceMovingLeftTimerSecondStage.reset();
                }
                break;
            }
            case SECOND: {
                if (pieceMovingLeftTimerSecondStage.update(dt)) {
                    if (collisionOK) {
                        activePiece.moveLeft();
                    }
                    pieceMovingLeftTimerSecondStage.reset();
                }
                break;
            }
        }
    }

    public void stopMovingPieceLeft() {
        pieceMovingLeft = false;
    }

    public void startMovingPieceRight() {
        boolean collisionOK = checkPieceCollision(activePiece.pos.x + 1, activePiece.pos.y,
                                                  activePiece.currentRotation());
        if (collisionOK) {
            activePiece.moveRight();
        }

        pieceMovingRight = true;
        movementStageRight = MovementStage.FIRST;
        pieceMovingRightTimerFirstStage.reset();
    }

    public void movePieceRight(float dt) {
        boolean collisionOK = checkPieceCollision(activePiece.pos.x + 1, activePiece.pos.y,
                                                  activePiece.currentRotation());
        switch (movementStageRight) {
            case FIRST: {
                if (pieceMovingRightTimerFirstStage.update(dt)) {
                    if (collisionOK) {
                        activePiece.moveRight();
                    }
                    movementStageRight = MovementStage.SECOND;
                    pieceMovingRightTimerSecondStage.reset();
                }
                break;
            }
            case SECOND: {
                if (pieceMovingRightTimerSecondStage.update(dt)) {
                    if (collisionOK) {
                        activePiece.moveRight();
                    }
                    pieceMovingRightTimerSecondStage.reset();
                }
                break;
            }
        }
    }

    public void stopMovingPieceRight() {
        pieceMovingRight = false;
    }

    public void startMovingPieceDown() {
        boolean collisionOK = checkPieceCollision(activePiece.pos.x, activePiece.pos.y - 1,
                                                  activePiece.currentRotation());
        if (collisionOK) {
            activePiece.moveDown();
        }

        pieceMovingDown = true;
        movementStageDown = MovementStage.FIRST;
        pieceMovingDownTimerFirstStage.reset();
    }

    public void movePieceDown(float dt) {
        boolean collisionOK = checkPieceCollision(activePiece.pos.x, activePiece.pos.y - 1,
                                                  activePiece.currentRotation());
        switch (movementStageDown) {
            case FIRST: {
                if (pieceMovingDownTimerFirstStage.update(dt)) {
                    if (collisionOK) {
                        activePiece.moveDown();
                    }
                    movementStageDown = MovementStage.SECOND;
                    pieceMovingDownTimerSecondStage.reset();
                }
                break;
            }
            case SECOND: {
                if (pieceMovingDownTimerSecondStage.update(dt)) {
                    if (collisionOK) {
                        activePiece.moveDown();
                    }
                    pieceMovingDownTimerSecondStage.reset();
                }
                break;
            }
        }
    }

    public void stopMovingPieceDown() {
        pieceMovingDown = false;
    }

    public void rotatePieceACW() {
        if (checkPieceCollision(activePiece.pos.x, activePiece.pos.y,
                                activePiece.peekACWRotation())) {
            activePiece.rotACW();
        }
    }

    public void rotatePieceCW() {
        if (checkPieceCollision(activePiece.pos.x, activePiece.pos.y,
                                activePiece.peekCWRotation())) {
            activePiece.rotCW();
        }
    }

    public void dropPiece(Piece piece) {
        for (int rowIdx = piece.pos.y - 1; true; --rowIdx) {
            if (!checkPieceCollision(piece.pos.x, rowIdx, piece.currentRotation())) {
                piece.dropToRow(rowIdx + 1);
                return;
            }
        }
    }

    public void hardDropPiece(Piece piece) {
        dropPiece(piece);
        doPieceLock();
    }

    private void processPieceGravity(float dt) {
        if (pieceGravityTimer.update(dt)) {
            if (checkPieceCollision(activePiece.pos.x, activePiece.pos.y - 1,
                                    activePiece.currentRotation())) {
                activePiece.moveDown();
            }
            pieceGravityTimer.reset();
        }
    }

    private void updatePieceShadow() {
        pieceShadow.syncWithPiece(activePiece);
        dropPiece(pieceShadow);
    }

    private void renderGrid(Grid g, int gridWidth, int gridHeight) {
        // Render filled sections.
        rend.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < gridWidth; ++x) {
            for (int y = 0; y < gridHeight; ++y) {
                Tetromino cell = g.getCell(x, y);
                if (cell == null) {
                    continue;
                }
                renderTetrominoFilled(cell, 0, 0, x, y, 1);
            }
        }
        rend.end();

        // Render highlights and shadows.
        rend.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < gridWidth; ++x) {
            for (int y = 0; y < gridHeight; ++y) {
                Tetromino cell = g.getCell(x, y);
                if (cell == null) {
                    continue;
                }
                renderTetrominoHighlights(cell, 0, 0, x, y, 1);
            }
        }
        rend.end();
    }

    private void renderPiece(Piece piece, float alpha) {
        // Filled sections.
        rend.begin(ShapeRenderer.ShapeType.Filled);
        for (GridPosition cellPos : piece.currentRotation()) {
            renderTetrominoFilled(piece.type, piece.pos.x, piece.pos.y, cellPos.x, cellPos.y,
                                  alpha);
        }
        rend.end();

        // Highlights.
        rend.begin(ShapeRenderer.ShapeType.Line);
        for (GridPosition cellPos : piece.currentRotation()) {
            renderTetrominoHighlights(piece.type, piece.pos.x, piece.pos.y, cellPos.x, cellPos.y,
                                      alpha);
        }
        rend.end();
    }

    private void renderTetrominoFilled(Tetromino type, int globalX, int globalY,
                                       int cellX, int cellY, float alpha) {
        rend.setColor(type.colour.r, type.colour.g, type.colour.b, alpha);
        rend.rect((globalX + cellX) * Constants.CELL_SIZE, (globalY + cellY) * Constants.CELL_SIZE,
                  Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    private void renderTetrominoHighlights(Tetromino type, int globalX, int globalY,
                                           int cellX, int cellY, float alpha) {
        int left = (globalX + cellX) * Constants.CELL_SIZE + 1;
        int right = (globalX + cellX + 1) * Constants.CELL_SIZE;
        int bottom = (globalY + cellY) * Constants.CELL_SIZE + 1;
        int top = (globalY + cellY + 1) * Constants.CELL_SIZE;

        // Highlights.
        rend.setColor(type.colourHighlight.r, type.colourHighlight.g, type.colourHighlight.b,
                      alpha);
        // Vertical.
        rend.line(left, bottom, left, top);
        // Horizontal.
        rend.line(left, top, right, top);

        // Shadows.
        rend.setColor(type.colourShadow.r, type.colourShadow.g, type.colourShadow.b, alpha);
        // Vertical.
        rend.line(right, bottom, right, top);
        // Horizontal.
        rend.line(left, bottom, right, bottom);
    }

    private void renderLineClearEffect() {
        rend.begin(ShapeRenderer.ShapeType.Filled);
        rend.setColor(Color.WHITE);
        for (int row : linesCleared) {
            rend.rect(0, row * Constants.CELL_SIZE, Constants.APP_WIDTH, Constants.CELL_SIZE);
        }
        rend.end();
    }

    @Override
    public void dispose() {
        rend.dispose();
    }
}