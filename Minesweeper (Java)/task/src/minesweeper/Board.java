package minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Board {
    Row[] rows;
    List<int[]> minesCoordinates;

    public Board() {
        this.rows = new Row[9];
        this.minesCoordinates = new ArrayList<>();
    }



    public List<int[]> getMinesCoordinates() {
        return minesCoordinates;
    }

    public void setMinesCoordinates(List<int[]> minesCoordinates) {
        this.minesCoordinates = minesCoordinates;
    }

    public Row[] getRows() {
        return rows;
    }

    public void setRows(Row[] rows) {
        this.rows = rows;
    }
}
