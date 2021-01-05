import data.JNAInterpreter;
import data.JOY_SHOCK_STATE;
import data.MOTION_STATE;

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
        int[] deviceHandles = JNAInterpreter.getDevices();
        int selectedDeviceId = deviceHandles[0];
        JNAInterpreter.displayDeviceData(selectedDeviceId);

        JNAInterpreter.pollSimpleInput(selectedDeviceId, true);
        JNAInterpreter.pollImuState(selectedDeviceId, true);
        JNAInterpreter.pollMotionState(selectedDeviceId, true);

        while (true) {
            MOTION_STATE motionInput = JNAInterpreter.pollMotionState(selectedDeviceId, false);
            float accelX = motionInput.accelX;
            System.out.println(accelX);
        }
    }
}