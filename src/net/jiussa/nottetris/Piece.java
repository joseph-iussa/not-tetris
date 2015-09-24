package net.jiussa.nottetris;

public class Piece {
    public GridPosition pos;
    public Tetromino type;
    private int currentRotationIdx;
    private GridPosition[] currentRotationInGridSpace;

    public Piece() {
        pos = new GridPosition(0, 0);
        type = null;
        currentRotationInGridSpace = new GridPosition[4];
        for (int i = 0; i < currentRotationInGridSpace.length; ++i) {
            currentRotationInGridSpace[i] = new GridPosition();
        }
    }

    public void init(Tetromino type) {
        this.type = type;
        pos.set(type.spawnPosition.x, type.spawnPosition.y);
        currentRotationIdx = 0;
        updateCurrentRotationInGridSpace();
    }

    public void syncWithPiece(Piece other) {
        pos.set(other.pos.x, other.pos.y);
        type = other.type;
        currentRotationIdx = other.currentRotationIdx;
        updateCurrentRotationInGridSpace();
    }

    public GridPosition[] currentRotation() {
        return type.rotations[currentRotationIdx];
    }

    public GridPosition[] peekCWRotation() {
        return type.rotations[(currentRotationIdx + 1) % 4];
    }

    public GridPosition[] peekACWRotation() {
        return type.rotations[(currentRotationIdx > 0) ? currentRotationIdx - 1 : 3];
    }

    public void moveLeft() {
        --pos.x;
        updateCurrentRotationInGridSpace();
    }

    public void moveRight() {
        ++pos.x;
        updateCurrentRotationInGridSpace();
    }

    public void moveDown() {
        for (GridPosition cell : currentRotationInGridSpace()) {
            if (cell.y < 1) {
                return;
            }
        }
        --pos.y;
        updateCurrentRotationInGridSpace();
    }

    public void rotCW() {
        currentRotationIdx = (currentRotationIdx + 1) % 4;
        updateCurrentRotationInGridSpace();
    }

    public void rotACW() {
        currentRotationIdx = (currentRotationIdx > 0) ? currentRotationIdx - 1 : 3;
        updateCurrentRotationInGridSpace();
    }

    public void dropToRow(int row) {
        pos.y = row;
        updateCurrentRotationInGridSpace();
    }

    public GridPosition[] currentRotationInGridSpace() {
        return currentRotationInGridSpace;
    }

    private void updateCurrentRotationInGridSpace() {
        GridPosition[] currRot = currentRotation();
        for (int i = 0; i < currRot.length; ++i) {
            currentRotationInGridSpace[i].set(pos.x + currRot[i].x, pos.y + currRot[i].y);
        }
    }
}