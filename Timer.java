
import java.io.PrintStream;

public class Timer {
    private long startTime = 0L;
    private long pausedTime = 0L;
    private boolean isRunning = false;

    public void start() {
        if (!this.isRunning) {
            this.startTime = System.currentTimeMillis() - this.pausedTime;
            this.isRunning = true;
            System.out.println("Timer đã bắt đầu.");
        }

    }

    public void pause() {
        if (this.isRunning) {
            this.pausedTime = this.getElapsed();
            this.isRunning = false;
            PrintStream var10000 = System.out;
            long var10001 = this.getElapsed();
            var10000.println("Timer đã tạm dừng. Thời gian đã trôi qua: " + var10001 / 1000L + "s");
        }

    }

    public void resume() {
        if (!this.isRunning) {
            this.start();
            System.out.println("Timer đã tiếp tục.");
        }

    }

    public long getElapsed() {
        return this.isRunning ? System.currentTimeMillis() - this.startTime : this.pausedTime;
    }

    // Lưu thời gian cũ
    public void setElapsed(long elapsedMillis) {
        this.pausedTime = elapsedMillis;
        // Nếu timer đang chạy, ta phải cập nhật lại startTime để khớp với thời gian mới nạp vào
        if (this.isRunning) {
            this.startTime = System.currentTimeMillis() - elapsedMillis;
        }
    }
}
