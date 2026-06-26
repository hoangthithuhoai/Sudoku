public class Suggest {

    /**
     * Logic: Tạo bản sao -> Xóa nước đi của người chơi trên bản sao -> Giải -> Lấy kết quả.
     */
    public boolean suggest(Board board, int targetRow, int targetCol) {
        Cell targetCell = board.getCells(targetRow, targetCol);

        // 1. Nếu là ô cố định (đề bài), không cần gợi ý
        if (targetCell.isFixed()) {
            System.out.println("⚠️ Ô này là ô cố định!");
            return false;
        }
        // 2. Tạo bản sao của bàn cờ hiện tại để tính toán ngầm
        Board copy = new Board(board);

        // 3. QUAN TRỌNG: Xóa sạch các nước đi của người chơi trên bản sao.
        // Lý do: Nếu người chơi trước đó điền sai (nhưng hợp lệ theo luật Sudoku),
        // thuật toán sẽ bị kẹt. Ta cần giải từ trạng thái gốc của đề bài.
        for (int r = 0; r < copy.getSIZE(); r++) {
            for (int c = 0; c < copy.getSIZE(); c++) {
                if (!copy.getCells(r, c).isFixed()) {
                    copy.getCells(r, c).setValues(0); // Reset về 0
                }
            }
        }

        // 4. Dùng Solver để giải bản sao "sạch" này
        Solver solver = new Solver();

        // solver.solver trả về true nếu tìm được lời giải
        if (solver.solver(copy)) {
            // Lấy giá trị đúng từ bản sao đã giải xong
            int suggestedValue = copy.getCells(targetRow, targetCol).getValues();

            // 5. Áp dụng giá trị này vào bàn cờ thật
            // (Sử dụng setCellVal của board để đảm bảo tính đồng bộ nếu có logic khác)
            board.setCellVal(targetRow, targetCol, suggestedValue);

            System.out.println("💡 Gợi ý thành công: " + suggestedValue);
            return true;
        } else {
            System.out.println("❌ Không tìm được lời giải (Có lỗi nghiêm trọng trong đề bài)!");
            return false;
        }
    }
}