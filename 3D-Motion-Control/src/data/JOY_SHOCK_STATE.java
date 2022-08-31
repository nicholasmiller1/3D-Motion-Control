package data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class JOY_SHOCK_STATE extends Structure {
    public int buttons;
    public float lTrigger;
    public float rTrigger;
    public float stickLX;
    public float stickLY;
    public float stickRX;
    public float stickRY;

    public JOY_SHOCK_STATE() {
        super();
    }

    public JOY_SHOCK_STATE(Pointer peer) {
        super(peer);
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("buttons", "lTrigger", "rTrigger", "stickLX", "stickLY", "stickRX", "stickRY");
    }

    protected String formattedString() {
        return "[" + buttons + ", " + lTrigger + ", " + rTrigger + ", " + stickLX + ", " + stickLY + ", " + stickRX + ", " + stickRY + "]";
    }

    public static class ByReference extends JOY_SHOCK_STATE implements Structure.ByReference {}

    public static class ByValue extends JOY_SHOCK_STATE implements Structure.ByValue {}
}

