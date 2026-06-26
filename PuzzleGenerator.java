import java.util.Random;

public class PuzzleGenerator {
    /*private Random rand = new Random();

    public Board generate(int size, int level) {
        Board board = new Board(size);
        Solver solver = new Solver();
        solver.solver(board);
        this.removeCells(board, level);
        return board;
    }

    public void removeCells(Board board, int level) {
        int totalCells = board.getSIZE() * board.getSIZE();
        int cellsToRemove;
        switch (level) {
            case 1 -> cellsToRemove = 30;
            case 2 -> cellsToRemove = 45;
            case 3 -> cellsToRemove = 60;
            default -> cellsToRemove = 40;
        }

        while(cellsToRemove > 0) {
            int r = this.rand.nextInt(board.getSIZE());
            int c = this.rand.nextInt(board.getSIZE());
            Cell cell = board.getCells(r, c);
            if (cell.getValues() != 0) {
                cell.setValues(0);
                --cellsToRemove;
            }
        }

        for(int i = 0; i < board.getSIZE(); ++i) {
            for(int j = 0; j < board.getSIZE(); ++j) {
                Cell cell = board.getCells(i, j);
                int val = cell.getValues();
                board.getCells(i, j).setValues(val);
                if (val != 0) {
                    board.getCells(i, j).setValues(val);
                    board.getCells(i, j).setFixed(true);
                } else {
                    board.getCells(i, j).setValues(0);
                    board.getCells(i, j).setFixed(false);
                }
            }
        }

    }*/

        /**
         * Cấp độ DỄ (EASY)
         * Số ô ban đầu: 37 (Dễ dàng cho người mới bắt đầu)
         */
        public static final int[][] FIXED_PUZZLE_EASY = new int[][] {
                {0, 3, 4, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 4, 0, 3},
                {4, 0, 6, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 7, 4, 1, 9, 0, 0, 5},
                {0, 0, 5, 0, 8, 0, 0, 7, 9}
        };

        /**
         * Cấp độ TRUNG BÌNH (MEDIUM)
         * Số ô ban đầu: 30 (Yêu cầu suy luận và ghi chú)
         */
        public static final int[][] FIXED_PUZZLE_MEDIUM = new int[][] {
                {0, 0, 0, 6, 0, 0, 4, 0, 0},
                {7, 0, 0, 0, 0, 3, 6, 0, 0},
                {0, 0, 0, 0, 9, 1, 0, 8, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 5, 0, 1, 8, 0, 0, 0, 3},
                {0, 0, 0, 3, 0, 6, 0, 4, 5},
                {0, 4, 0, 2, 0, 0, 0, 6, 0},
                {9, 0, 3, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 7, 8}
        };

        /**
         * Cấp độ KHÓ (HARD)
         * Số ô ban đầu: 25 (Yêu cầu kỹ thuật giải nâng cao)
         */
        public static final int[][] FIXED_PUZZLE_HARD = new int[][] {
                {0, 2, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 6, 0, 0, 0, 0, 3},
                {0, 7, 4, 0, 8, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 3, 0, 0, 2},
                {0, 8, 0, 0, 4, 0, 0, 1, 0},
                {6, 0, 0, 5, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 7, 8, 0},
                {5, 0, 0, 0, 0, 9, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 4, 0}
        };

    public Board generate(int size, int level) {
        int[][] data;

        // Chuyển level (int) sang dữ liệu cố định tương ứng
        if (level == 3) { // Giả sử 3 là Hard
            data = FIXED_PUZZLE_HARD;
        } else if (level == 2) { // Giả sử 2 là Medium
            data = FIXED_PUZZLE_MEDIUM;
        } else { // Mặc định là Easy (level 1)
            data = FIXED_PUZZLE_EASY;
        }

        // Tạo một đối tượng Board mới từ dữ liệu cố định
        // Lưu ý: Board cần được import nếu nó không ở trong cùng package
        // return new Board(data);

        // Vì hàm generate() trong Board.java chỉ gán cells,
        // nên ta sẽ trả về dữ liệu để GameManager xử lý.
        // Tùy thuộc vào thiết kế cũ, ta có thể chỉ cần trả về ma trận.

        // TRONG TRƯỜNG HỢP NÀY, BẠN NÊN THAY ĐỔI LOGIC TRONG GAMEMANAGER
        // VÀ KHÔNG GỌI HÀM NÀY NỮA. HÃY DÙNG CÁC HẰNG SỐ BÊN TRÊN.

        return null; // Trả về null hoặc ném ngoại lệ nếu không dùng hàm này.
    }
}
