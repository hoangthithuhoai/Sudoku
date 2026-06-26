public class FileHandler {
    public void save(String path, Board board) {
        System.out.println("FileHandler: Đang lưu Board vào: " + path);
        board.saveToFile(path);
    }

    public Board load(String path) {
        System.out.println("FileHandler: Đang tải Board từ: " + path);
        Board loadedBoard = new Board(9);
        loadedBoard.loadFromFile(path);
        return loadedBoard;
    }
}
