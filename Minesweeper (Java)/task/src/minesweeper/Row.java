package minesweeper;

public class Row {
    private String[] cells;

    public Row() {
        this.cells = new String[9];
    }

    public String[] getCells() {
        return cells;
    }

    public void setCells(String[] cells) {
        this.cells = cells;
    }
}
