public class Cell {
    private int row;
    private int col;
    private int values;
    private boolean fixed;

    public Cell(int row, int col, int values, boolean fixed) {
        this.row = row;
        this.col = col;
        this.values = values;
        this.fixed = fixed;
    }

    public int getValues() {
        return this.values;
    }

    public void setValues(int v) {
        if (!this.fixed) {
            this.values = v;
        }

    }

    public boolean isFixed() {
        return this.fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }
}
