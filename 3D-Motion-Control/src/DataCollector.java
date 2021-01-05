public class DataCollector {

    private static long startTime;

    public static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public static int getRunTimeSeconds() {
        return (int) ( Math.abs(startTime - System.currentTimeMillis() ) / 1000 );
    }

    public static float getMillis() {
        return Math.abs(startTime - System.currentTimeMillis());
    }

    public static void main(String[] args) {
        startTimer();
    }
}