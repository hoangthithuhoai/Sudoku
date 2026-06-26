import java.util.ArrayList;
import java.util.List;

public class LeaderBoard {
    private List<Player> players = new ArrayList();

    public void addPlayer(Player player) {
        this.players.add(player);
        this.sortByTime();
    }

    /**
     * SỬA LẠI: Sắp xếp dựa trên thời gian (long)
     */
    public void sortByTime() {
        this.players.sort((p1, p2) -> Long.compare(
                p1.getFinishTimeMillis(),
                p2.getFinishTimeMillis()
        ));
    }

    // Bỏ hàm parseTime (không cần nữa)

    /**
     * HÀM MỚI: Trả về chuỗi BXH cho GUI
     */
    public String getDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Bảng Xếp Hạng ---\n");
        int count = Math.min(10, this.players.size());
        if (count == 0) {
            sb.append("Chưa có ai trên bảng xếp hạng.\n");
        }
        for (int i = 0; i < count; ++i) {
            Player p = (Player) this.players.get(i);
            sb.append(String.format("%d. %s - %s\n", i + 1, p.getName(), p.getFinishTime()));
        }
        return sb.toString();
    }

    // (Hàm display() cũ vẫn giữ nếu bạn muốn dùng cho console)
    public void display() {
        System.out.println(getDisplayString());
    }

    public void saveToFile(String path) {
        System.out.println("Đang lưu bảng xếp hạng vào: " + path);
    }

    public void loadFromFile(String path) {
        System.out.println("Đang tải bảng xếp hạng từ: " + path);
    }
}