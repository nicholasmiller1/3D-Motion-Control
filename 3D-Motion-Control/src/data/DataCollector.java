package data;

import com.sun.jna.Library;
import com.sun.jna.Native;
import data.math.PositionMath;
import data.math.Quaternion;
import processing.core.PVector;

import java.io.*;
import java.util.Arrays;

public class DataCollector {

    private static NativeJSL jsl = (NativeJSL) Native.load("data/JoyShockLibrary", NativeJSL.class);
    private static int selectedDeviceId;
    private static PositionMath pmath;

    public interface NativeJSL extends Library {
        int JslConnectDevices();
        int JslGetConnectedDeviceHandles(int[] deviceHandleArray, int size);
        void JslDisconnectAndDisposeAll();

        int JslGetControllerType(int deviceId);
        int JslGetControllerSplitType(int deviceId);
        float JslGetPollRate(int deviceId);

        JOY_SHOCK_STATE.ByValue JslGetSimpleState(int deviceId);
        IMU_STATE.ByValue JslGetIMUState(int deviceId);
        MOTION_STATE.ByValue JslGetMotionState(int deviceId);
    }

    public static void init() {
        // Connect devices and get device data
        selectedDeviceId = 0;
        try {
            int[] deviceHandles = DataCollector.getDevices();
            selectedDeviceId = deviceHandles[0];
            DataCollector.displayDeviceData(selectedDeviceId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Clear output files for writing
        DataCollector.clearFile("simple_output.txt");
        DataCollector.clearFile("imu_output.txt");
        DataCollector.clearFile("motion_output.txt");

        // Prepare acceleration input stream
        final int smoothingStep = 3;
        pmath = new PositionMath(smoothingStep);
    }

    public static int[] getDevices() throws Exception {
        int devicesSize = jsl.JslConnectDevices();
        if (devicesSize == 0) {
            throw new Exception("No Connected Devices");
        }
        System.out.println("Number of Connected Devices: " + devicesSize);

        int[] deviceHandles = new int[devicesSize];
        jsl.JslGetConnectedDeviceHandles(deviceHandles, devicesSize);
        System.out.println("Device Handles: " + Arrays.toString(deviceHandles) + "\n");

        return deviceHandles;
    }

    public static void disconnectAll() {
        jsl.JslDisconnectAndDisposeAll();
    }

    public static void reset() {
        disconnectAll();
        init();
    }

    public static void displayDeviceData(int deviceId) {
        String controllerType = getControllerType(deviceId);
        System.out.println("Controller Type: " + controllerType);

        String controllerSplitType = getControllerSplitType(deviceId);
        System.out.println("Controller Split Type: " + controllerSplitType);

        System.out.println("Poll Rate: " + jsl.JslGetPollRate(deviceId) + "/s \n");
    }

    public static JOY_SHOCK_STATE pollSimpleState(boolean write) {
        return pollSimpleState(selectedDeviceId, write);
    }

    public static JOY_SHOCK_STATE pollSimpleState(int deviceId, boolean write) {
        JOY_SHOCK_STATE simpleInput = jsl.JslGetSimpleState(deviceId);
        if (write) {
            writeData(simpleInput.formattedString(), "simple");
        }
        return simpleInput;
    }

    public static IMU_STATE pollImuState(boolean write) {
        return pollImuState(selectedDeviceId, write);
    }

    public static IMU_STATE pollImuState(int deviceId, boolean write) {
        IMU_STATE imuInput = jsl.JslGetIMUState(deviceId);
        if (write) {
            writeData(imuInput.formattedString(), "imu");
        }
        return imuInput;
    }

    public static MOTION_STATE pollMotionState(boolean write) {
        return pollMotionState(selectedDeviceId, write);
    }

    public static MOTION_STATE pollMotionState(int deviceId, boolean write) {
        MOTION_STATE motionInput = jsl.JslGetMotionState(deviceId);
        if (write) {
            writeData(motionInput.formattedString(), "motion");
        }
        return motionInput;
    }

    public static MOTION_STATE pollCleanMotionState(boolean write, boolean shift) {
        return pollCleanMotionState(selectedDeviceId, write, shift);
    }

    public static MOTION_STATE pollCleanMotionState(int deviceId, boolean write, boolean shift) {
        MOTION_STATE m = pollMotionState(deviceId, write);
        if (shift) {
            Quaternion quat = new Quaternion(m.quatW, m.quatX, m.quatY, m.quatZ);
            pmath.shiftAccelStream(PositionMath.convertToGlobal(m.accelX, m.accelY, m.accelZ, quat));
        }

        PVector cleanAccel = pmath.applyFilter();
        m.accelX = cleanAccel.x;
        m.accelY = cleanAccel.y;
        m.accelZ = cleanAccel.z;
        return m;
    }

    public static PVector getVelocity(PVector accelIn, boolean update) {
        PVector vel =  pmath.getVelocity(accelIn);

        if (update) {
            pmath.updateVelocity(accelIn, vel);
        }
        return vel;
    }

    public static PVector getPositionFromVelocity(PVector velIn, boolean update) {
        PVector pos = pmath.getPosition(velIn);

        if (update) {
            pmath.updatePosition(velIn, pos);
        }
        return pos;
    }

    public static PVector getPositionFromAcceleration(PVector accelIn, boolean update) {
        return getPositionFromVelocity(getVelocity(accelIn, update), update);
    }

    private static void writeData(String data, String type) {
        try {
            File file = new File("out/readings/" + type + "_output.txt");
            if (file.createNewFile()) {
                System.out.println("File created: out/readings/" + type + "_output.txt");
            }

            FileWriter writer = new FileWriter(file, true);
            writer.write(data + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile(String filename) {
        try {
            PrintWriter writer = new PrintWriter("out/readings/" + filename);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            if (e.getClass().equals(FileNotFoundException.class)) {
                System.out.println("Couldn't clear file \"out/readings/" + filename + "\". File does not exist");
            } else {
                e.printStackTrace();
            }
        }
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