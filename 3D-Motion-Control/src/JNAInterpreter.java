import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.util.Arrays;

public class JNAInterpreter {

    public static NativeJSL jsl;

    public interface NativeJSL extends Library {
        int JslConnectDevices();
        int JslGetConnectedDeviceHandles(int[] deviceHandleArray, int size);

        int JslGetControllerType(int deviceId);
        int JslGetControllerSplitType(int deviceId);

        int JslGetButtons(int deviceId);
        float JslGetLeftX(int deviceId);
    }

    public static void main(String[] args) {
        jsl = (NativeJSL) Native.load("JoyShockLibrary", NativeJSL.class);

        /* Count and Display All Connected Devices */

        int devicesSize = jsl.JslConnectDevices();
        System.out.println("Number of Connected Devices: " + devicesSize);

        int[] deviceHandles = new int[devicesSize];
        jsl.JslGetConnectedDeviceHandles(deviceHandles, devicesSize);
        System.out.println("Device Handles: " + Arrays.toString(deviceHandles));

        System.out.println("\n");

        /* Display Selected Device Info */

        int deviceHandle = deviceHandles[0];

        String controllerType = getControllerType(deviceHandle);
        System.out.println("Controller Type: " + controllerType);

        String controllerSplitType = getControllerSplitType(deviceHandle);
        System.out.println("Controller Split Type: " + controllerSplitType);

        System.out.println("\n");

        /* Display Selected Device Input */

        while(true) {
            updateSimpleInputs(deviceHandle);
        }

//        System.out.println("\n");
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

    private static void updateSimpleInputs(int deviceId) {
        int buttons = jsl.JslGetButtons(deviceId);
        System.out.println("Button Inputs: " + buttons);

        float leftStickX = jsl.JslGetLeftX(deviceId);
        System.out.println("Left Analog Stick X-Axis: " + leftStickX);

    }
}