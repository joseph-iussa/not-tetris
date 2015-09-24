package net.jiussa.nottetris;

import java.util.Iterator;
import java.util.List;

public class Grid implements Iterable<Tetromino> {
    public final int columnsN;
    public final int rowsN;
    private Tetromino[] grid;
    private final GridIterator iter;
    private final GridPartIterator partIter;

    public Grid(int columnsN, int rowsN) {
        this.columnsN = columnsN;
        this.rowsN = rowsN;
        grid = new Tetromino[columnsN * rowsN];
        iter = new GridIterator(grid);
        partIter = new GridPartIterator(grid, columnsN, rowsN);
    }

    public Tetromino getCell(int column, int row) {
        return grid[column * rowsN + row];
    }

    public void setCell(int column, int row, Tetromino p) {
        grid[column * rowsN + row] = p;
    }

    @Override
    public Iterator<Tetromino> iterator() {
        iter.init();
        return iter;
    }

    public Iterable<Tetromino> getRow(int row) {
        partIter.init(GridPartIterator.IterType.ROW, row);
        return partIter;
    }

    public Iterable<Tetromino> getColumn(int column) {
        partIter.init(GridPartIterator.IterType.COLUMN, column);
        return partIter;
    }

    public boolean rowIsFull(int row) {
        for (Tetromino cell : getRow(row)) {
            if (cell == null) {
                return false;
            }
        }
        return true;
    }

    public boolean rowIsEmpty(int row) {
        for (Tetromino cell : getRow(row)) {
            if (cell != null) {
                return false;
            }
        }
        return true;
    }

    public void clearRow(int row) {
        for (int col = 0; col < columnsN; ++col) {
            setCell(col, row, null);
        }
    }

    public void collapseClearedRows(List<Integer> clearedRows) {
        for (int rowIdx = clearedRows.size() - 1; rowIdx >= 0; --rowIdx) {
            int clearedRowIdx = clearedRows.get(rowIdx);
            for (int row = clearedRowIdx + 1; row < rowsN; ++row) {
                if (rowIsEmpty(row)) {
                    continue;
                }
                for (int col = 0; col < columnsN; ++col) {
                    setCell(col, row - 1, getCell(col, row));
                    setCell(col, row, null);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(grid.length);
        // Print rows in reverse order otherwise they'll be upside down.
        for (int row = rowsN - 1; row >= 0; --row) {
            for (Tetromino p : getRow(row)) {
                sb.append(p).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

class GridIterator implements Iterator<Tetromino> {
    private Tetromino[] grid;
    private int index;

    public GridIterator(Tetromino[] grid) {
        this.grid = grid;
        index = 0;
    }

    public void init() {
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < grid.length;
    }

    @Override
    public Tetromino next() {
        return grid[index++];
    }
}

class GridPartIterator implements Iterator<Tetromino>, Iterable<Tetromino> {
    public enum IterType {COLUMN, ROW}

    private final Tetromino[] grid;
    private final int columnsN;
    private final int rowsN;

    private IterType iterType;
    private int partIndex;
    private int currentIterIndex;

    public GridPartIterator(Tetromino[] grid, int columnsN, int rowsN) {
        this.grid = grid;
        this.columnsN = columnsN;
        this.rowsN = rowsN;

        iterType = IterType.COLUMN;
        partIndex = 0;
        currentIterIndex = 0;
    }

    public void init(IterType iterType, int partIndex) {
        this.iterType = iterType;
        this.partIndex = partIndex;
        currentIterIndex = 0;
    }

    @Override
    public Iterator<Tetromino> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        switch (iterType) {
            case COLUMN: return currentIterIndex < rowsN;
            case ROW: return currentIterIndex < columnsN;
            default: throw new RuntimeException("Shouldn't be here");
        }
    }

    @Override
    public Tetromino next() {
        switch (iterType) {
            case COLUMN: return grid[partIndex * rowsN + currentIterIndex++];
            case ROW: return grid[currentIterIndex++ * rowsN + partIndex];
            default: throw new RuntimeException("Shouldn't be here");
        }
    }
}