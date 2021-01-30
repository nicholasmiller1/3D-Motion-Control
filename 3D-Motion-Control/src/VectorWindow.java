import data.*;
import data.math.PositionMath;
import data.math.Quaternion;
import processing.core.PApplet;
import processing.core.PVector;

public class VectorWindow extends PApplet {

    private static final int SCREEN_SIZE = 500;
    private final int SCALE = 100;

    private final PVector initialVector = new PVector(0,0,1);
    private final PVector[] initialBox = {
            new PVector(0.433f, 0.25f, 0.866f), // sqrt(3)/4, 1/4, sqrt(3)/2
            new PVector(0.433f, -0.25f, 0.866f),
            new PVector(-0.433f, -0.25f, 0.866f),
            new PVector(-0.433f, 0.25f, 0.866f),
            new PVector(0.433f, 0.25f, -0.866f),
            new PVector(0.433f, -0.25f, -0.866f),
            new PVector(-0.433f, -0.25f, -0.866f),
            new PVector(-0.433f, 0.25f, -0.866f),
    };

    public static void main(String[] args) {
        DataCollector.init();
        PApplet.main("VectorWindow");
    }

    public void settings() {
        size(SCREEN_SIZE, SCREEN_SIZE, P3D);
    }

    public void setup() {
        lights();
    }

    public void draw() {
        background(10);
        translate(SCREEN_SIZE/2, SCREEN_SIZE/2, 0);
        rotateX(-PI/6);
        rotateY(3*PI/4);
        drawAxes();

        MOTION_STATE m = DataCollector.pollCleanMotionState(false, false);
        Quaternion quat = new Quaternion(m.quatW, m.quatX, m.quatY, m.quatZ);

        // Orientation Box
        PVector[] boxPoints = updateBox(quat, initialBox);
        drawBox(boxPoints);

        // Orientation Vector
        PVector v = quat.applyRotation(initialVector);
        strokeWeight(7);
        stroke(148,0,211);
        point(v.x * SCALE, v.y * SCALE, v.z * SCALE);

        // Acceleration Vectors
        stroke(255, 255, 0);
        point(m.accelX * SCALE, m.accelY * SCALE, m.accelZ * SCALE);
    }

    private PVector[] updateBox(Quaternion q, PVector[] points) {
        PVector[] newPoints = new PVector[points.length];
        for (int i = 0; i < points.length; i++) {
            newPoints[i] = q.applyRotation(points[i]);
        }
        return newPoints;
    }

    private void drawBox(PVector[] points) {
        final int boxScale = 30;
        stroke(255);

        for (int i = 0; i < points.length / 2; i++) {
            strokeWeight(5);
            PVector v1 = scaleVector(points[i], boxScale);
            PVector v2 = scaleVector(points[i+4], boxScale);

            stroke(148,0,211);
            point(v1.x, v1.y, v1.z);
            stroke(255);
            point(v2.x, v2.y, v2.z);

            strokeWeight(2);
            line(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
        }

        for (int i = 0; i < points.length / 2; i++) {
            PVector v1 = scaleVector(points[i % 4], boxScale);
            PVector v2 = scaleVector(points[(i + 1) % 4], boxScale);

            stroke(148,0,211);
            line(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);

            PVector w1 = scaleVector(points[(i % 4) + 4], boxScale);
            PVector w2 = scaleVector(points[((i + 1) % 4) + 4], boxScale);

            stroke(255);
            line(w1.x, w1.y, w1.z, w2.x, w2.y, w2.z);
        }
    }

    private PVector scaleVector(PVector v, int scale) {
        return new PVector(v.x * scale, v.y * scale, v.z * scale);
    }

    private void drawAxes() {
        final int axesScale = 15;
        strokeWeight(1);

        // X-Axis
        stroke(255,0,0);
        line(-10 * axesScale,0,0,10 * axesScale,0,0);

        // Y-Axis
        stroke(0,255,0);
        line(0,-10 * axesScale,0,0,10 * axesScale,0);

        // Z-Axis
        stroke(0,0,255);
        line(0,0,-10 * axesScale,0,0,10 * axesScale);
    }
}
