public class Move {
    private int r;
    private int c;
    private int oldValue;
    private int newValue;

    public Move(int r, int c, int oldValue, int newValue) {
        this.r = r;
        this.c = c;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public int getR() {
        return this.r;
    }

    public int getC() {
        return this.c;
    }

    public int getOldValue() {
        return this.oldValue;
    }

    public int getNewValue() {
        return this.newValue;
    }

    public void execute(Board board) {
        board.setCellVal(this.r, this.c, this.newValue);
    }

    public void undo(Board board) {
        board.setCellVal(this.r, this.c, this.oldValue);
    }
}
