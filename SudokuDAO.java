import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SudokuDAO {

    // --- PHẦN 1: BẢNG XẾP HẠNG ---

    public void saveScore(String name, long timeMillis) {
        String sql = "INSERT INTO leaderboard(name, time_millis) VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setLong(2, timeMillis);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLeaderboardString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- BẢNG XẾP HẠNG TOP 10 ---\n");
        String sql = "SELECT name, time_millis FROM leaderboard ORDER BY time_millis ASC LIMIT 10";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int rank = 1;
            while (rs.next()) {
                String name = rs.getString("name");
                long time = rs.getLong("time_millis");

                // Đổi mili giây sang phút:giây
                long minutes = (time / 1000) / 60;
                long seconds = (time / 1000) % 60;
                String timeStr = String.format("%02d:%02d", minutes, seconds);

                sb.append(String.format("%d. %s - %s\n", rank++, name, timeStr));
            }
        } catch (SQLException e) {
            return "Lỗi tải bảng xếp hạng.";
        }

        if (sb.length() < 40) return "Chưa có dữ liệu xếp hạng.";
        return sb.toString();
    }

    // --- PHẦN 2: SAVE / LOAD GAME ---

    // Class con để chứa dữ liệu khi Load
    public static class GameData {
        public String boardData;
        public String fixedData;
        public long timeElapsed;
        public GameData(String b, String f, long t) { boardData = b; fixedData = f; timeElapsed = t; }
    }

    public void saveGame(String saveName, Board board, long timeElapsed) {
        String sql = "INSERT INTO saved_games(save_name, board_data, fixed_data, time_elapsed) VALUES(?, ?, ?, ?)";

        StringBuilder sbVal = new StringBuilder();
        StringBuilder sbFixed = new StringBuilder();

        // Biến board thành chuỗi string
        for(int r=0; r<9; r++) {
            for(int c=0; c<9; c++) {
                sbVal.append(board.getCellVal(r, c)); // Lưu số
                sbFixed.append(board.getCells(r, c).isFixed() ? "1" : "0"); // Lưu trạng thái cố định
            }
        }

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, saveName);
            pstmt.setString(2, sbVal.toString());
            pstmt.setString(3, sbFixed.toString());
            pstmt.setLong(4, timeElapsed);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách tên các bản save để hiển thị lên Menu
    public List<String> getSavedGameNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT save_name FROM saved_games ORDER BY id DESC";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                names.add(rs.getString("save_name"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return names;
    }

    // Load game cụ thể theo tên
    public GameData loadGameByName(String saveName) {
        String sql = "SELECT board_data, fixed_data, time_elapsed FROM saved_games WHERE save_name = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, saveName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new GameData(
                        rs.getString("board_data"),
                        rs.getString("fixed_data"),
                        rs.getLong("time_elapsed")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}