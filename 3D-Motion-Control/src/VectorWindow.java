import processing.core.PApplet;
import processing.core.PVector;

public class VectorWindow extends PApplet {

    private static final int SCREEN_SIZE = 500;
    private float rotationCounter = 0.0f;

    public static void main(String[] args) {
//        PApplet.main("VectorWindow");
        PVector u = new PVector(1, 2, 3);
        PVector v = new PVector(4, 5, 6);
//        u.normalize();
//        v.normalize();
        Quaternion quat = new Quaternion(u, v);
        PVector rotP = quat.applyRotation(u);
        System.out.println(u + " | " + v + " | " + quat + " | " + rotP);
        v.normalize();
        rotP.normalize();
        System.out.println(v + " | " + rotP);
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
//        drawAxes();

        stroke(255);
//        fill(130);
        noFill();
        rotateY(rotationCounter);
        box(25,10,60);

        rotationCounter += 0.012;
    }

    private void drawVector(PVector v) {
        beginShape();
        vertex(-2.5f, 0, -1.5f);
        vertex(-2.5f, 0, 1.5f);
        vertex(2.5f, 0, 1.5f);
        vertex(2.5f, 0, -1.5f);
        vertex(2.5f, v.mag(), -1.5f);
        vertex(-2.5f, v.mag(), -1.5f);
        vertex(-2.5f, v.mag(), 1.5f);
        vertex(2.5f, v.mag(), 1.5f);
        endShape();
    }

    private void drawAxes() {
        // X-Axis
        stroke(255,0,0);
        line(-1000,0,0,1000,0,0);

        // Y-Axis
        stroke(0,255,0);
        line(0,-1000,0,0,1000,0);

        // Z-Axis
        stroke(0,0,255);
        line(0,0,-1000,0,0,1000);
    }
}
