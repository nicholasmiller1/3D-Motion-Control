package data;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.util.Arrays;

public class JNAInterpreter {

    public static NativeJSL jsl = (NativeJSL) Native.load("data/JoyShockLibrary", NativeJSL.class);

    public interface NativeJSL extends Library {
        int JslConnectDevices();
        int JslGetConnectedDeviceHandles(int[] deviceHandleArray, int size);

        int JslGetControllerType(int deviceId);
        int JslGetControllerSplitType(int deviceId);

        JOY_SHOCK_STATE.ByValue JslGetSimpleState(int deviceId);
        IMU_STATE.ByValue JslGetIMUState(int deviceId);
        MOTION_STATE.ByValue JslGetMotionState(int deviceId);
    }

    public static int[] getDevices() {
        int devicesSize = jsl.JslConnectDevices();
        System.out.println("Number of Connected Devices: " + devicesSize);

        int[] deviceHandles = new int[devicesSize];
        jsl.JslGetConnectedDeviceHandles(deviceHandles, devicesSize);
        System.out.println("Device Handles: " + Arrays.toString(deviceHandles) + "\n");

        return deviceHandles;
    }

    public static void displayDeviceData(int deviceId) {
        String controllerType = getControllerType(deviceId);
        System.out.println("Controller Type: " + controllerType);

        String controllerSplitType = getControllerSplitType(deviceId);
        System.out.println("Controller Split Type: " + controllerSplitType + "\n");
    }

    public static JOY_SHOCK_STATE pollSimpleInput(int deviceId, boolean display) {
        JOY_SHOCK_STATE simpleInput = jsl.JslGetSimpleState(deviceId);
        if (display) {
            System.out.println("Simple State Input: " + simpleInput + "\n");
        }
        return simpleInput;
    }

    public static IMU_STATE pollImuState(int deviceId, boolean display) {
        IMU_STATE imuInput = jsl.JslGetIMUState(deviceId);
        if (display) {
            System.out.println("IMU State Input: " + imuInput + "\n");
        }
        return imuInput;
    }

    public static MOTION_STATE pollMotionState(int deviceId, boolean display) {
        MOTION_STATE motionInput = jsl.JslGetMotionState(deviceId);
        if (display) {
            System.out.println("Motion State Input: " + motionInput + "\n");
        }
        return motionInput;
    }

    private static String getControllerType(int deviceId) {
        int controllerTypeId = jsl.JslGetControllerType(deviceId);
        switch (controllerTypeId) {
            case 1: return "Left JoyCon";
            case 2: return "Right JoyCon";
            case 3: return "Switch Pro Controller";
            case 4: return "DualShock4";
            default: return "Device Not Recognized";
        }
    }

    private static String getControllerSplitType(int deviceId) {
        int controllerSplitTypeId = jsl.JslGetControllerSplitType(deviceId);
        switch (controllerSplitTypeId) {
            case 1: return "Left Half";
            case 2: return "Right Half";
            case 3: return "Full Controller";
            default: return "Split Type Not Recognized";
        }
    }
}