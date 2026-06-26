import java.io.PrintStream;

public class GameUI {
    public void showBoard(Board board) {
        System.out.println("\n--- Hiển thị Bàn cờ ---");

        for(int r = 0; r < board.getSIZE(); ++r) {
            for(int c = 0; c < board.getSIZE(); ++c) {
                PrintStream var10000 = System.out;
                int var10001 = board.getCellVal(r, c);
                var10000.print(var10001 + " ");
            }

            System.out.println();
        }

    }

    public void showMenu() {
        System.out.println("\n--- Menu Chính ---");
        System.out.println("1. Bắt đầu Game mới");
        System.out.println("2. Tải Game");
        System.out.println("3. Bảng Xếp Hạng");
        System.out.println("4. Thoát");
    }

    public String readCommand() {
        return "command";
    }

    public void showMessage(String message) {
        System.out.println("[Tin nhắn]: " + message);
    }

    public void renderGrid() {
    }

    public String inputPlayerName() {
        return "Người chơi Mới";
    }
}
