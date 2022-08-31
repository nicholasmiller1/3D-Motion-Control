package data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class IMU_STATE extends Structure {
    public float accelX;
    public float accelY;
    public float accelZ;
    public float gyroX;
    public float gyroY;
    public float gyroZ;

    public IMU_STATE() {
        super();
    }

    public IMU_STATE(Pointer peer) {
        super(peer);
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("accelX", "accelY", "accelZ", "gyroX", "gyroY", "gyroZ");
    }

    protected String formattedString() {
        return "[" + accelX + ", " + accelY + ", " + accelZ + ", " + gyroX + ", " + gyroY + ", " + gyroZ + "]";
    }

    public static class ByReference extends IMU_STATE implements Structure.ByReference {}

    public static class ByValue extends IMU_STATE implements Structure.ByValue {}
}

