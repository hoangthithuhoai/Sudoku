/* public class Solver {
    public boolean solver(Board board) {
        return this.backtrack(board, 0);
    }

    private boolean backtrack(Board board, int pos) {
        if (pos == board.getSIZE() * board.getSIZE()) {
            return true;
        } else {
            int row = pos / board.getSIZE();
            int col = pos % board.getSIZE();
            Cell cell = board.getCells(row, col);
            if (!cell.isFixed() && cell.getValues() == 0) {
                for(int num = 1; num <= board.getSIZE(); ++num) {
                    if (board.isValidMove(row, col, num)) {
                        cell.setValues(num);
                        if (this.backtrack(board, pos + 1)) {
                            return true;
                        }

                        cell.setValues(0);
                    }
                }

                return false;
            } else {
                return this.backtrack(board, pos + 1);
            }
        }
    }

    public void solve(Board board) {
    }
}*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; // <<< 3 DÒNG IMPORT MỚI CẦN THIẾT

public class Solver {
    public boolean solver(Board board) {
        return this.backtrack(board, 0);
    }
//thuat toan quay lui
    private boolean backtrack(Board board, int pos) {
        if (pos == board.getSIZE() * board.getSIZE()) {
            return true;
        } else {
            int row = pos / board.getSIZE();
            int col = pos % board.getSIZE();
            Cell cell = board.getCells(row, col);
            if (!cell.isFixed() && cell.getValues() == 0) {

                // TẠO DANH SÁCH SỐ TỪ 1 ĐẾN 9
                List<Integer> numbers = new ArrayList<>();
                for (int i = 1; i <= board.getSIZE(); i++) {
                    numbers.add(i);
                }
                // XÁO TRỘN ĐỂ TẠO SỰ NGẪU NHIÊN
                Collections.shuffle(numbers);
                // THỬ CÁC SỐ THEO THỨ TỰ NGẪU NHIÊN
                for(int num : numbers) {
                    if (board.isValidMove(row, col, num)) {
                        cell.setValues(num);
                        if (this.backtrack(board, pos + 1)) {
                            return true;
                        }

                        cell.setValues(0);
                    }
                }

                return false;
            } else {
                return this.backtrack(board, pos + 1);
            }
        }
    }
    public void solve(Board board) {
    }
}