import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // File database sẽ tên là sudoku.db, nằm ngay trong thư mục dự án
    private static final String URL = "jdbc:sqlite:sudoku.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            createTables(conn); // Kiểm tra và tạo bảng ngay khi kết nối
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối DB: " + e.getMessage());
        }
        return conn;
    }

    // Tạo 2 bảng: leaderboard (lưu điểm) và saved_games (lưu game)
    private static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        // 1. Bảng Xếp Hạng
        String sqlLeaderboard = "CREATE TABLE IF NOT EXISTS leaderboard ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "time_millis INTEGER NOT NULL,"
                + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ");";

        // 2. Bảng Lưu Game
        // board_data: Lưu các con số (VD: "53007...")
        // fixed_data: Lưu trạng thái cố định (1 là cố định, 0 là ko) để khi load ko bị mất đề bài
        String sqlSavedGame = "CREATE TABLE IF NOT EXISTS saved_games ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "save_name TEXT NOT NULL,"
                + "board_data TEXT NOT NULL,"
                + "fixed_data TEXT NOT NULL,"
                + "time_elapsed INTEGER NOT NULL,"
                + "saved_at DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ");";

        stmt.execute(sqlLeaderboard);
        stmt.execute(sqlSavedGame);
    }
}