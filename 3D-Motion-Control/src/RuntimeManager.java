import data.JNAInterpreter;
import data.JOY_SHOCK_STATE;
import data.IMU_STATE;
import data.MOTION_STATE;

public class RuntimeManager {

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

//        while (true) {
//            MOTION_STATE motionState = JNAInterpreter.pollMotionState(selectedDeviceId, false);
//            double mag = Math.sqrt(Math.pow(motionState.quatW, 2) + Math.pow(motionState.quatX, 2) + Math.pow(motionState.quatY, 2) + Math.pow(motionState.quatZ, 2));
//            System.out.printf("( %.3f, %.3f, %.3f, %.3f ) | %.4f \n", motionState.quatW, motionState.quatX, motionState.quatY, motionState.quatZ, mag);
//        }

//        VectorWindow.main(args);
    }
}