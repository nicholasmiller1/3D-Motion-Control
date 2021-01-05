package data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class MOTION_STATE extends Structure {
    public float quatW;
    public float quatX;
    public float quatY;
    public float quatZ;
    public float accelX;
    public float accelY;
    public float accelZ;
    public float gravX;
    public float gravY;
    public float gravZ;


    public MOTION_STATE() {
        super();
    }

    public MOTION_STATE(Pointer peer) {
        super(peer);
    }

    @Override
    protected List getFieldOrder() {
        return Arrays.asList("quatW", "quatX", "quatY", "quatZ", "accelX", "accelY", "accelZ", "gravX", "gravY", "gravZ");
    }

    public static class ByReference extends MOTION_STATE implements Structure.ByReference {}

    public static class ByValue extends MOTION_STATE implements Structure.ByValue {}
}

