import data.DataCollector;
import data.MOTION_STATE;
import data.math.Quaternion;
import org.gicentre.utils.stat.*;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;

public class GraphWindow extends PApplet {

    private static final int[] SCREEN_SIZE = new int[]{800, 300};
    private final boolean EXPANDED = false;

    private XYChart[] accelPlots;
    private XYChart[] velPlots;
    private XYChart[] posPlots;

    private List<PVector>[] accelData;
    private List<PVector>[] velData;
    private List<PVector>[] posData;

    public static void main(String[] args) {
        DataCollector.init();
        PApplet.main("GraphWindow");
    }

    public void settings() {
        size(SCREEN_SIZE[0], SCREEN_SIZE[1] * (EXPANDED ? 3 : 1), P2D);
    }

    public void setup() {
        initAccelPlots();
        
        if (EXPANDED) {
            initVelPlots();
            initPosPlots();
        }
    }

    public void draw() {
        if (accelData[0].size() == 500) {
            for (int i = 0; i < accelData.length; i++) {
                accelData[i].clear();
                
                if (EXPANDED) {
                    velData[i].clear();
                    posData[i].clear();
                }
            }

            background(200);
        }

        MOTION_STATE m = DataCollector.pollMotionState(false);
        PVector accel = new PVector(m.accelX, m.accelY, m.accelZ);
        PVector vel = new PVector();
        PVector pos = new PVector();
        
        if (EXPANDED) {
            vel = DataCollector.getVelocity(accel, true);
            pos = DataCollector.getPositionFromVelocity(vel, true);
        }

        for (int i = 0; i < accelPlots.length; i++) {
            accelData[i].add(new PVector(accelData[i].size(), accel.array()[i]));
            accelPlots[i].setData(accelData[i]);

            if (EXPANDED) {
                velData[i].add(new PVector(velData[i].size(), vel.array()[i]));
                velPlots[i].setData(velData[i]);

                posData[i].add(new PVector(posData[i].size(), pos.array()[i]));
                posPlots[i].setData(posData[i]);
            }
        }

        float divisor = EXPANDED ? 3 : 1;
        accelPlots[0].draw(0, 0, width, height / divisor);
        accelPlots[1].draw(accelPlots[0].getLeftSpacing(), 0, width, height / divisor);
        accelPlots[2].draw(accelPlots[0].getLeftSpacing(), 0, width, height / divisor);

        if (EXPANDED) {
            velPlots[0].draw(0, height / divisor, width, height / divisor);
            velPlots[1].draw(velPlots[0].getLeftSpacing(), height / divisor, width, height / divisor);
            velPlots[2].draw(velPlots[0].getLeftSpacing(), height / divisor, width, height / divisor);

            posPlots[0].draw(0, 2 * height / divisor, width, height / divisor);
            posPlots[1].draw(posPlots[0].getLeftSpacing(), 2 * height / divisor, width, height / divisor);
            posPlots[2].draw(posPlots[0].getLeftSpacing(), 2 * height / divisor, width, height / divisor);
        }
    }
    
    private void initAccelPlots() {
        final float SCALE = 0.1f;
        accelPlots = new XYChart[3];
        accelData = new LinkedList[3];

        for (int i = 0; i < accelPlots.length; i++) {
            accelPlots[i] = new XYChart(this);
            accelData[i] = new LinkedList<PVector>();
            accelPlots[i].setData(accelData[i]);

            accelPlots[i].setLineWidth(2);
            accelPlots[i].setPointSize(0);

            accelPlots[i].setXAxisAt(0);
            accelPlots[i].setMinX(0);
            accelPlots[i].setMaxX(500);

            accelPlots[i].setMinY(-1 * SCALE);
            accelPlots[i].setMaxY(1 * SCALE);

            if (i > 0) {
                accelPlots[i].showXAxis(false);
                accelPlots[i].showYAxis(false);
            }
        }

        accelPlots[0].setLineColour(color(255,0,0));
        accelPlots[1].setLineColour(color(0,255,0));
        accelPlots[2].setLineColour(color(0,0,255));

        accelPlots[0].showXAxis(true);
        accelPlots[0].showYAxis(true);
        accelPlots[0].setXAxisLabel("Time (ms)");
        accelPlots[0].setYAxisLabel("Acceleration (m/s^2)");
    }

    private void initVelPlots() {
        final float SCALE = 1;
        velPlots = new XYChart[3];
        velData = new LinkedList[3];

        for (int i = 0; i < velPlots.length; i++) {
            velPlots[i] = new XYChart(this);
            velData[i] = new LinkedList<PVector>();
            velPlots[i].setData(velData[i]);

            velPlots[i].setLineWidth(2);
            velPlots[i].setPointSize(0);

            velPlots[i].setXAxisAt(0);
            velPlots[i].setMinX(0);
            velPlots[i].setMaxX(500);

            velPlots[i].setMinY(-1 * SCALE);
            velPlots[i].setMaxY(1 * SCALE);

            if (i > 0) {
                velPlots[i].showXAxis(false);
                velPlots[i].showYAxis(false);
            }
        }

        velPlots[0].setLineColour(color(255,0,0));
        velPlots[1].setLineColour(color(0,255,0));
        velPlots[2].setLineColour(color(0,0,255));

        velPlots[0].showXAxis(true);
        velPlots[0].showYAxis(true);
    }

    private void initPosPlots() {
        final float SCALE = 1;
        posPlots = new XYChart[3];
        posData = new LinkedList[3];

        for (int i = 0; i < posPlots.length; i++) {
            posPlots[i] = new XYChart(this);
            posData[i] = new LinkedList<PVector>();
            posPlots[i].setData(posData[i]);

            posPlots[i].setLineWidth(2);
            posPlots[i].setPointSize(0);

            posPlots[i].setXAxisAt(0);
            posPlots[i].setMinX(0);
            posPlots[i].setMaxX(500);

            posPlots[i].setMinY(-1 * SCALE);
            posPlots[i].setMaxY(1 * SCALE);

            if (i > 0) {
                posPlots[i].showXAxis(false);
                posPlots[i].showYAxis(false);
            }
        }

        posPlots[0].setLineColour(color(255,0,0));
        posPlots[1].setLineColour(color(0,255,0));
        posPlots[2].setLineColour(color(0,0,255));

        posPlots[0].showXAxis(true);
        posPlots[0].showYAxis(true);
    }
}
