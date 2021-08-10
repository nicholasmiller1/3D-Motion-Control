import data.DataCollector;
import data.IMU_STATE;
import data.MOTION_STATE;
import data.math.Quaternion;
import processing.core.PApplet;
import processing.core.PVector;

import javax.xml.crypto.Data;

public class PositionWindow extends PApplet {

    private static final int SCREEN_SIZE = 800;
    private final float SCALE = 10;

    public static void main(String[] args) {
        DataCollector.init();
        PApplet.main("PositionWindow");
    }

    public void settings() {
        size(SCREEN_SIZE, SCREEN_SIZE, P2D);
    }

    public void setup() {
    }

    public void draw() {
        background(0);
        translate(width/2, height/2);

        IMU_STATE i = DataCollector.pollImuState(true);
        PVector p = DataCollector.getPositionFromAcceleration(new PVector(i.accelX, i.accelY, i.accelZ), true);

        stroke(255);
        strokeWeight(2);
//        line(-width/2, p.y * SCALE, width/2, p.y * SCALE);
//        line(p.x * SCALE, -height/2, p.x * SCALE, height/2);
        line(-width/2, 0, width/2, 0);
        line(0, -height/2, 0, height/2);

        stroke(255,0,0);
        strokeWeight(15);
        point(p.x * SCALE, p.y * SCALE);
    }
}
