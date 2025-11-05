package com.arielfriedman.arminesweeperproject.model;

public class Tile {
    private String id;
    private int row, col;
    private boolean wasRevealed = false;
    private boolean isMine = false;
    private boolean flagged = false;
    private int minesAround = 0;

    public Tile(String id ,int row, int col, boolean wasRevealed, boolean isMine, boolean flagged, int minesAround) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.wasRevealed = wasRevealed;
        this.isMine = isMine;
        this.flagged = flagged;
        this.minesAround = minesAround;
    }

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Tile() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    public boolean getWasRevealed() {
        return wasRevealed;
    }
    public void setWasRevealed(boolean wasRevealed) {
        this.wasRevealed = wasRevealed;
    }

    public boolean getIsMine() {
        return isMine;
    }
    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isFlagged() {
        return flagged;
    }
    public void toggleFlagged() {
        flagged = !flagged;
    }

    public int getMinesAround() {
        return minesAround;
    }
    public void setMinesAround(int count) {
        minesAround = count;
    }


    @Override
    public String toString() {
        return "Tile{" +
                "row=" + row +
                ", col=" + col +
                ", wasRevealed=" + wasRevealed +
                ", isMine=" + isMine +
                ", flagged=" + flagged +
                ", minesAround=" + minesAround +
                '}';
    }
}

