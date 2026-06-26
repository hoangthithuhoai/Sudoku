import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Board {
    private final int SIZE;
    private Cell[][] cells;

    public Board(int size) {
        this.SIZE = size;
        this.cells = new Cell[this.SIZE][this.SIZE];

        for(int r = 0; r < this.SIZE; ++r) {
            for(int c = 0; c < this.SIZE; ++c) {
                this.cells[r][c] = new Cell(r, c, 0, false);
            }
        }

    }

    public Board(int[][] initial) {
        this.SIZE = 9;
        this.cells = new Cell[this.SIZE][this.SIZE];

        for(int r = 0; r < this.SIZE; ++r) {
            for(int c = 0; c < this.SIZE; ++c) {
                int val = initial[r][c];
                this.cells[r][c] = new Cell(r, c, val, val != 0);
            }
        }

    }

    public boolean isValidMove(int r, int c, int val) {

    // 1. Kiểm tra giới hạn giá trị
        if (val < 1 || val > this.SIZE) return false;

    // 2. Kiểm tra Hàng và Cột
        for(int i = 0; i < this.SIZE; ++i) {

        // Kiểm tra Hàng: Bỏ qua chính ô (r, c) đang xét
            if (i != c && this.cells[r][i].getValues() == val) {
                return false;
            }

        // Kiểm tra Cột: Bỏ qua chính ô (r, c) đang xét
            if (i != r && this.cells[i][c].getValues() == val) {
                return false;
            }
        }

    // 3. Kiểm tra Khối 3x3
        int boxRow = r / 3 * 3;
        int boxCol = c / 3 * 3;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 3; ++j) {
                int checkR = boxRow + i;
                int checkC = boxCol + j;

            // Bỏ qua chính ô đang xét
                if (checkR != r || checkC != c) {
                    if (this.cells[checkR][checkC].getValues() == val) {
                        return false;
                }
            }
        }
    }

    // Nước đi hợp lệ (không xung đột với ô nào khác)
    return true;
    }

    public boolean isComplete() {
        for(int i = 0; i < this.SIZE; ++i) {
            for(int j = 0; j < this.SIZE; ++j) {
                if (this.cells[i][j].getValues() == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public void printBoard() {
        for(int i = 0; i < this.SIZE; ++i) {
            if (i % 3 == 0) {
                System.out.println("+-------+-------+-------+");
            }

            for(int j = 0; j < this.SIZE; ++j) {
                if (j % 3 == 0) {
                    System.out.print("| ");
                }

                int v = this.cells[i][j].getValues();
                System.out.print(v == 0 ? ". " : v + " ");
            }

            System.out.println("|");
        }

        System.out.println("+-------+-------+-------+");
    }

    public int getSIZE() {
        return this.SIZE;
    }

    public Cell getCells(int r, int c) {
        return this.cells[r][c];
    }

    public void setCellVal(int r, int c, int val) {
        Cell cell = this.cells[r][c];
        if (!cell.isFixed()) {
            cell.setValues(val);
        } else {
            System.out.println("⚠️ Ô này là ô cố định, không thể thay đổi!");
        }

    }

    public int getCellVal(int r, int c) {
        return this.cells[r][c].getValues();
    }


    public void saveToFile(String path) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            for(int r = 0; r < this.SIZE; ++r) {
                for(int c = 0; c < this.SIZE; ++c) {
                    writer.print(this.cells[r][c].getValues());
                    if (c < this.SIZE - 1) {
                        writer.print(" ");
                    }
                }

                writer.println();
            }

            System.out.println(" Board đã được lưu vào file: " + path);
        } catch (IOException e) {
            System.out.println(" Lỗi khi lưu file: " + e.getMessage());
        }

    }

    public void loadFromFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            for(int r = 0; r < this.SIZE; ++r) {
                String[] values = reader.readLine().trim().split(" ");

                for(int c = 0; c < this.SIZE; ++c) {
                    int val = Integer.parseInt(values[c]);
                    this.cells[r][c].setValues(val);
                }
            }

            System.out.println(" Board đã được tải từ file: " + path);
        } catch (IOException e) {
            System.out.println(" Lỗi khi đọc file: " + e.getMessage());
        }

    }
    //database
    public Board(Board other) {
        this.SIZE = other.SIZE;
        this.cells = new Cell[this.SIZE][this.SIZE];
        for (int r = 0; r < this.SIZE; r++) {
            for (int c = 0; c < this.SIZE; c++) {
                Cell oldCell = other.getCells(r, c);
                // Chỉ copy giá trị và trạng thái cố định
                this.cells[r][c] = new Cell(r, c, oldCell.getValues(), oldCell.isFixed());
            }
        }
    }

    // Nạp dữ liệu từ database vào bàn cờ
    public void loadFromDBData(String valData, String fixedData) {
        if (valData.length() != SIZE * SIZE || fixedData.length() != SIZE * SIZE) {
            System.out.println("Dữ liệu game bị lỗi!");
            return;
        }

        int index = 0;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                // 1. Nạp giá trị số
                int val = valData.charAt(index) - '0';

                // 2. Nạp trạng thái cố định (đề bài)
                boolean isFixed = (fixedData.charAt(index) == '1');

                // Cập nhật Cell
                this.cells[r][c].setFixed(false); // Mở khóa tạm để set giá trị
                this.cells[r][c].setValues(val);
                this.cells[r][c].setFixed(isFixed); // Khóa lại nếu là đề bài

                index++;
            }
        }
    }

}
