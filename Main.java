import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuFrame frame = new SudokuFrame();
            frame.setVisible(true);
        });
    }
}