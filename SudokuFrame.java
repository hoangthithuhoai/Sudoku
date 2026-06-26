import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class SudokuFrame extends JFrame {

    // --- Hằng số cho giao diện ---
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int CELL_SIZE = 60; // Kích thước ô
    private static final int FRAME_WIDTH = CELL_SIZE * GRID_SIZE + 50;
    private static final int FRAME_HEIGHT = CELL_SIZE * GRID_SIZE + 150;

    // Font chữ
    private static final Font FONT_CELL = new Font("Arial", Font.BOLD, 28);
    private static final Font FONT_TIMER = new Font("Arial", Font.BOLD, 20);

    // Màu sắc
    private static final Color COLOR_FIXED_BG = new Color(220, 220, 220); // Nền ô cố định
    private static final Color COLOR_FIXED_FG = Color.BLACK;            // Chữ ô cố định
    private static final Color COLOR_USER_BG = Color.WHITE;             // Nền ô người dùng
    private static final Color COLOR_USER_FG = new Color(0, 0, 200);    // Chữ ô người dùng
    private static final Color COLOR_SELECTED_BG = new Color(200, 230, 255); // Nền khi ô được chọn
    private static final Color COLOR_GRID_BORDER = Color.BLACK;
    private static final Color COLOR_SUBGRID_BORDER = new Color(100, 100, 100);

    // --- Thành phần giao diện ---
    private JTextField[][] cells = new JTextField[GRID_SIZE][GRID_SIZE];
    private JPanel gridPanel;
    private JPanel controlPanel;
    private JLabel timerLabel;

    // --- Logic Game & Timer ---
    private GameManager gameManager;
    private javax.swing.Timer swingTimer; // Timer của Swing để cập nhật UI
    private Timer logicTimer; // Timer logic của bạn

    // --- Biến theo dõi ô đang chọn ---
    private int selectedRow = -1;
    private int selectedCol = -1;

    public SudokuFrame() {
        this.gameManager = new GameManager();
        this.logicTimer = gameManager.getLogicTimer();

        initFrame();
        initTopPanel();
        initGridPanel();
        initControlPanel();
        initSwingTimer();

        startGame();
    }

    // 1. Thiết lập cửa sổ chính (Frame)
    private void initFrame() {
        setTitle("Sudoku Game");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null); // Giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Layout chính (Bắc, Trung, Nam)
    }

    // 2. Thiết lập Panel trên cùng (chứa Timer)
    private void initTopPanel() {
        JPanel topPanel = new JPanel();
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(FONT_TIMER);
        topPanel.add(timerLabel);
        add(topPanel, BorderLayout.NORTH);
    }

    // 3. Thiết lập Lưới Sudoku
    private void initGridPanel() {
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SUBGRID_SIZE, SUBGRID_SIZE));
        gridPanel.setBorder(BorderFactory.createLineBorder(COLOR_GRID_BORDER, 3));
        gridPanel.setPreferredSize(new Dimension(CELL_SIZE * GRID_SIZE, CELL_SIZE * GRID_SIZE));

        for (int subRow = 0; subRow < SUBGRID_SIZE; subRow++) {
            for (int subCol = 0; subCol < SUBGRID_SIZE; subCol++) {
                JPanel subGrid = new JPanel();
                subGrid.setLayout(new GridLayout(SUBGRID_SIZE, SUBGRID_SIZE));
                subGrid.setBorder(BorderFactory.createLineBorder(COLOR_SUBGRID_BORDER, 1));

                for (int r = 0; r < SUBGRID_SIZE; r++) {
                    for (int c = 0; c < SUBGRID_SIZE; c++) {
                        int globalRow = subRow * SUBGRID_SIZE + r;
                        int globalCol = subCol * SUBGRID_SIZE + c;

                        JTextField cell = createCell(globalRow, globalCol);
                        cells[globalRow][globalCol] = cell;
                        subGrid.add(cell);
                    }
                }
                gridPanel.add(subGrid);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }

    // 3.1. Helper: Tạo một ô JTextField (ĐÃ SỬA ĐỔI QUAN TRỌNG)
    private JTextField createCell(int r, int c) {
        JTextField cell = new JTextField();
        cell.setFont(FONT_CELL);
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        cell.putClientProperty("row", r);
        cell.putClientProperty("col", c);

        cell.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                // Chỉ cho nhập số từ 1-9 và chỉ 1 ký tự
                if (ch < '1' || ch > '9' || cell.getText().length() >= 1) {
                    e.consume();
                }
            }

            // --- [FIX] THÊM SỰ KIỆN NÀY ĐỂ BẮT LỖI NGAY KHI NHẢ PHÍM ---
            @Override
            public void keyReleased(KeyEvent e) {
                // Gọi hàm xử lý ngay khi người dùng nhập xong số
                handleCellInput(cell);
            }
            // -----------------------------------------------------------
        });

        cell.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Khi click vào ô -> Lưu tọa độ
                selectedRow = r;
                selectedCol = c;

                // Đổi màu nền để biết đang chọn (nếu không phải ô cố định)
                Cell logicCell = gameManager.getBoard().getCells(r, c);
                if (!logicCell.isFixed()) {
                    cell.setBackground(COLOR_SELECTED_BG);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Khi rời khỏi ô -> Trả lại màu cũ
                Cell logicCell = gameManager.getBoard().getCells(r, c);
                if (logicCell.isFixed()) {
                    cell.setBackground(COLOR_FIXED_BG);
                } else {
                    cell.setBackground(COLOR_USER_BG);
                }

                // Vẫn giữ xử lý ở đây phòng trường hợp copy-paste hoặc thao tác chuột khác
                handleCellInput(cell);
            }
        });

        return cell;
    }

    // 4. Thiết lập Panel điều khiển
    private void initControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnNewGame = new JButton("New Game");
        btnNewGame.addActionListener(e -> startGame());

        JButton btnSuggest = new JButton("Suggest");
        btnSuggest.addActionListener(e -> handleSuggest());

        JButton btnUndo = new JButton("Undo");
        btnUndo.addActionListener(e -> handleUndo());

        JButton btnSolve = new JButton("Solve");
        btnSolve.addActionListener(e -> handleSolve());

        JButton btnSave = new JButton("Save Game");
        btnSave.addActionListener(e -> handleSave());

        JButton btnLoad = new JButton("Load Game");
        btnLoad.addActionListener(e -> handleLoad());

        controlPanel.add(btnNewGame);
        controlPanel.add(btnSuggest);
        controlPanel.add(btnUndo);
        controlPanel.add(btnSolve);
        controlPanel.add(btnSave);
        controlPanel.add(btnLoad);

        add(controlPanel, BorderLayout.SOUTH);
    }

    // 5. Thiết lập Timer
    private void initSwingTimer() {
        swingTimer = new javax.swing.Timer(1000, e -> updateTimerLabel());
    }

    // --- CÁC HÀM XỬ LÝ LOGIC ---

    private void startGame() {
        String playerName = JOptionPane.showInputDialog(this, "Enter your name:", "New Game", JOptionPane.PLAIN_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player 1";
        }

        String[] options = {"Easy", "Medium", "Hard"};
        String selectedLevel = (String) JOptionPane.showInputDialog(this,
                "Choose difficulty level:",
                "New Game",
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);

        String levelString = (selectedLevel != null) ? selectedLevel.toUpperCase() : "MEDIUM";

        gameManager.startNewFixedGame(playerName, levelString);

        logicTimer = gameManager.getLogicTimer();
        logicTimer.start();
        swingTimer.start();

        // Reset lại biến selected khi new game
        selectedRow = -1;
        selectedCol = -1;

        updateBoardUI();
    }

    private void updateBoardUI() {
        Board board = gameManager.getBoard();
        if (board == null) return;

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                JTextField uiCell = cells[r][c];
                Cell logicCell = board.getCells(r, c);
                int value = logicCell.getValues();

                if (value == 0) {
                    uiCell.setText("");
                } else {
                    uiCell.setText(String.valueOf(value));
                }

                if (logicCell.isFixed()) {
                    uiCell.setEditable(false);
                    uiCell.setBackground(COLOR_FIXED_BG);
                    uiCell.setForeground(COLOR_FIXED_FG);
                } else {
                    uiCell.setEditable(true);
                    uiCell.setBackground(COLOR_USER_BG);
                    uiCell.setForeground(COLOR_USER_FG);
                }
            }
        }
    }

    private void updateTimerLabel() {
        long elapsedMs = logicTimer.getElapsed();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMs);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMs) % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    private void handleCellInput(JTextField cell) {
        int r = (int) cell.getClientProperty("row");
        int c = (int) cell.getClientProperty("col");

        String inputText = cell.getText();
        int value;

        if (inputText.isEmpty()) {
            value = 0;
        } else {
            try {
                value = Integer.parseInt(inputText);
            } catch (NumberFormatException e) {
                value = 0;
            }
        }

        int oldValue = gameManager.getBoard().getCellVal(r, c);

        // Nếu giá trị không đổi thì không cần xử lý (tránh spam logic)
        if (value == oldValue) return;

        boolean success = gameManager.makeMove(r, c, value);

        if (success) {
            // Kiểm tra chiến thắng ngay sau khi nước đi hợp lệ
            if (gameManager.getBoard().isComplete()) {
                swingTimer.stop();
                logicTimer.pause();
                updateBoardUI();

                String leaderboardInfo = gameManager.finishGame();

                JOptionPane.showMessageDialog(this,
                        "Chúc mừng! Bạn đã thắng!\n\n" + leaderboardInfo,
                        "You Win!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            if (value != 0) {
                JOptionPane.showMessageDialog(this,
                        "Nước đi không hợp lệ! (Xung đột theo luật Sudoku)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                // Reset lại giá trị cũ nếu nhập sai
                SwingUtilities.invokeLater(() -> {
                    cell.setText(oldValue == 0 ? "" : String.valueOf(oldValue));
                });
            }
        }
    }

    private void handleSuggest() {
        if (selectedRow == -1 || selectedCol == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng click vào một ô cần gợi ý trước!",
                    "Chưa chọn ô",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        logicTimer.pause();
        swingTimer.stop();

        boolean success = gameManager.suggest(selectedRow, selectedCol);

        if (success) {
            updateBoardUI();
            // Kiểm tra luôn xem gợi ý xong có thắng luôn không (trường hợp còn 1 ô cuối)
            if (gameManager.getBoard().isComplete()) {
                swingTimer.stop();
                logicTimer.pause();
                updateBoardUI();

                // 1. Lưu điểm người chơi vào DB
                // (Giả sử bạn đã có biến playerName khi bắt đầu game, nếu chưa thì hỏi lại)
                String inputName = JOptionPane.showInputDialog(this, "Bạn đã thắng! Nhập tên để lưu bảng xếp hạng:");
                if (inputName == null || inputName.isEmpty()) inputName = "Ẩn danh";

                dao.saveScore(inputName, logicTimer.getElapsed());

                // 2. Lấy Bảng xếp hạng từ DB và hiển thị
                String leaderboardStr = dao.getLeaderboardString();

                JOptionPane.showMessageDialog(this,
                        "Chúc mừng! Bạn đã thắng!\n\n" + leaderboardStr,
                        "You Win!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Không thể gợi ý cho ô này (Ô cố định hoặc đã điền)!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }

        logicTimer.start();
        swingTimer.start();
    }

    private void handleUndo() {
        gameManager.undoMove();
        updateBoardUI();
    }

    private void handleSolve() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn tự động giải không? Thời gian sẽ dừng lại.",
                "Giải quyết Sudoku",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            gameManager.solveGame();
            swingTimer.stop();
            logicTimer.pause();
            updateBoardUI();
        }
    }

    private void handleSave() {
        logicTimer.pause();
        swingTimer.stop();

        String saveName = JOptionPane.showInputDialog(this, "Đặt tên cho bản lưu (VD: Van 1):");
        if (saveName != null && !saveName.trim().isEmpty()) {
            long currentTime = logicTimer.getElapsed();

            // Gọi DAO lưu vào SQLite
            dao.saveGame(saveName, gameManager.getBoard(), currentTime);

            JOptionPane.showMessageDialog(this, "Đã lưu thành công!");
        }

        logicTimer.resume();
        swingTimer.start();
    }

    private void handleLoad() {
        // Lấy danh sách tên bản lưu từ DB
        java.util.List<String> savedNames = dao.getSavedGameNames();

        if (savedNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có bản lưu nào!");
            return;
        }

        // Hiện danh sách cho người dùng chọn
        Object[] choices = savedNames.toArray();
        String selectedName = (String) JOptionPane.showInputDialog(this,
                "Chọn bản lưu:", "Tải Game",
                JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

        if (selectedName != null) {
            SudokuDAO.GameData data = dao.loadGameByName(selectedName);

            if (data != null) {
                // 1. Nạp dữ liệu bàn cờ
                gameManager.getBoard().loadFromDBData(data.boardData, data.fixedData);

                // 2. Nạp lại thời gian cũ vào Timer
                logicTimer.pause(); // Tạm dừng timer cũ
                logicTimer.setElapsed(data.timeElapsed); // Sử dụng hàm mới tạo ở Bước 1
                logicTimer.resume(); // Tiếp tục chạy từ mốc thời gian cũ

                // 3. Cập nhật giao diện
                updateBoardUI();
                JOptionPane.showMessageDialog(this, "Đã tải ván chơi: " + selectedName
                        + "\nThời gian cũ: " + TimeUnit.MILLISECONDS.toSeconds(data.timeElapsed) + " giây");
            }
        }
    }
    private SudokuDAO dao = new SudokuDAO();
}