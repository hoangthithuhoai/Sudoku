public class Player {
    private String name;
    private long startTime;
    private long finishTime;
    private String finishTimeStr;

    public Player(String name) {
        this.name = name;
        this.startTime = 0L;
        this.finishTime = 0L;
        this.finishTimeStr = "";
    }

    public String getName() {
        return this.name;
    }

    public String getFinishTime() {
        return this.finishTimeStr;
    }

    /**
     * Trả về thời gian hoàn thành (dạng long) để sắp xếp
     */
    public long getFinishTimeMillis() {
        return this.finishTime;
    }

    public void setStartTime(long time) {
        this.startTime = time;
    }

    public void setFinishTime(long time) {
        // time ở đây là tổng thời gian (elapsed time)
        this.finishTime = time;
        long elapsedTime = this.finishTime - this.startTime; // startTime là 0
        long seconds = elapsedTime / 1000L;
        long minutes = seconds / 60L;
        seconds %= 60L;
        this.finishTimeStr = String.format("%02d:%02d", minutes, seconds);
    }
}