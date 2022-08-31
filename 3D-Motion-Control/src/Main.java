import data.DataCollector;
import data.math.PositionMath;
import processing.core.PApplet;

public class Main {

    private static long startTime;
    private static PositionMath pmath;

    public static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public static int getRunTimeSeconds() {
        return (int) ( Math.abs(startTime - System.currentTimeMillis() ) / 1000 );
    }

    public static float getMillis() {
        return Math.abs(startTime - System.currentTimeMillis());
    }

    public static void main(String[] args) {
        startTimer();
        DataCollector.init();

        String[] vArgs = {"VectorWindow"};
        VectorWindow v = new VectorWindow();
        PApplet.runSketch(vArgs, v);

        String[] gArgs = {"GraphWindow"};
        GraphWindow g = new GraphWindow();
        PApplet.runSketch(gArgs, g);

        String[] pArgs = {"PositionWindow"};
        PositionWindow p = new PositionWindow();
        PApplet.runSketch(pArgs, p);
    }
}