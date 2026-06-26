import java.util.Stack;

public class GameManager {
    private Board board;
    private Player player;
    private LeaderBoard leaderBoard = new LeaderBoard();
    private Timer timer = new Timer();
    private FileHandler fileHandler = new FileHandler();
    private Solver solver = new Solver();
    private Suggest suggest = new Suggest();
    private Stack<Move> moveHistory = new Stack<>();

    public GameManager() {
        // Constructor mặc định
    }

    /**
     * BẮT ĐẦU GAME VỚI DỮ LIỆU CỐ ĐỊNH (FIXED PUZZLE)
     */
    public void startNewFixedGame(String playerName, String level) {
        this.player = new Player(playerName);

        int[][] initialData;

        // Chọn dữ liệu dựa trên cấp độ
        switch (level.toUpperCase()) {
            case "HARD":
                initialData = PuzzleGenerator.FIXED_PUZZLE_HARD;
                break;
            case "EASY":
                initialData = PuzzleGenerator.FIXED_PUZZLE_EASY;
                break;
            case "MEDIUM":
            default:
                initialData = PuzzleGenerator.FIXED_PUZZLE_MEDIUM;
                break;
        }

        // Khởi tạo Board với dữ liệu cố định
        this.board = new Board(initialData);

        // Reset trạng thái game
        this.moveHistory.clear();
        this.timer = new Timer();
        this.timer.start();
    }

    public String finishGame() {
        long elapsedMs = timer.getElapsed();
        player.setFinishTime(elapsedMs);
        leaderBoard.addPlayer(player);
        return leaderBoard.getDisplayString();
    }

    public boolean makeMove(int r, int c, int val) {
        if (r < 0 || r >= board.getSIZE() || c < 0 || c >= board.getSIZE()) {
            return false;
        }

        Cell cell = this.board.getCells(r, c);
        if (cell.isFixed()) {
            return false;
        }

        int oldVal = this.board.getCellVal(r, c);

        if (val == 0) { // Xóa ô
            Move move = new Move(r, c, oldVal, 0);
            move.execute(this.board);
            this.moveHistory.push(move);
            return true;
        }

        if (!this.board.isValidMove(r, c, val)) {
            return false; // Nước đi vi phạm luật
        }

        Move move = new Move(r, c, oldVal, val);
        move.execute(this.board);
        this.moveHistory.push(move);
        return true;
    }

    public void undoMove() {
        if (!this.moveHistory.isEmpty()) {
            Move lastMove = this.moveHistory.pop();
            lastMove.undo(this.board);
        }
    }

    /**
     * TỰ ĐỘNG GIẢI GAME
     * Đã sửa: Xóa các nước đi của người chơi trước khi giải để tránh xung đột.
     */
    public void solveGame() {
        if (this.board == null) return;

        // 1. Reset toàn bộ các ô người dùng đã nhập về 0
        // Để đảm bảo Solver chạy trên nền tảng đề bài gốc
        for (int r = 0; r < board.getSIZE(); r++) {
            for (int c = 0; c < board.getSIZE(); c++) {
                if (!board.getCells(r, c).isFixed()) {
                    board.setCellVal(r, c, 0);
                }
            }
        }

        // 2. Chạy thuật toán giải
        this.solver.solver(this.board);

        // Lưu ý: Sau khi solve, moveHistory sẽ không còn đúng nữa, có thể clear nếu muốn
        this.moveHistory.clear();
    }
    public boolean suggest(int r, int c) {
        return this.suggest.suggest(this.board, r, c);
    }

    public int[] findFirstEmptyCell() {
        for (int r = 0; r < board.getSIZE(); r++) {
            for (int c = 0; c < board.getSIZE(); c++) {
                if (board.getCellVal(r, c) == 0 && !board.getCells(r, c).isFixed()) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }

    public void saveGame(String path) {
        if (board != null) {
            fileHandler.save(path, board);
        }
    }

    public void loadGame(String path) {
        Board loadedBoard = fileHandler.load(path);
        if (loadedBoard != null) {
            this.board = loadedBoard;
            this.moveHistory.clear();
            this.timer = new Timer();
            // Lưu ý: Timer sẽ reset về 0 khi load game mới
        }
    }

    public Board getBoard() {
        return this.board;
    }

    public Timer getLogicTimer() {
        return this.timer;
    }
}