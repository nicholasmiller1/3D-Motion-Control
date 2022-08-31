package data.math;

import processing.core.PVector;

public class PositionMath {

    private PVector[] accelStream;
    private PVector velocityAccumulator;
    private PVector previousAccel;
    private PVector positionAccumulator;
    private PVector previousVel;

    public PositionMath(int smoothingStep) {
        this(new float[(2 * smoothingStep) + 1], new float[(2 * smoothingStep) + 1], new float[(2 * smoothingStep) + 1]);
    }

    public PositionMath(PVector[] accelStream) {
        this.accelStream = accelStream;
        this.velocityAccumulator = new PVector(0,0,0);
        this.previousAccel = new PVector(0,0,0);
        this.positionAccumulator = new PVector(0,0,0);
        this.previousVel = new PVector(0,0,0);
    }

    public PositionMath(float[] accelX, float[] accelY, float[] accelZ) {
        this.accelStream = new PVector[accelX.length];
        this.velocityAccumulator = new PVector(0,0,0);
        this.previousAccel = new PVector(0,0,0);
        this.positionAccumulator = new PVector(0,0,0);
        this.previousVel = new PVector(0,0,0);
        for (int i = 0; i < accelStream.length; i++) {
            accelStream[i] = new PVector(accelX[i], accelY[i], accelZ[i]);
        }
    }

    public PVector applyFilter() {
        PVector accelOut = new PVector(0,0,0);
        for (int i = 0; i < accelStream.length; i++) {
            accelOut.add(accelStream[i]);
        }
        return accelOut.div(accelStream.length);
    }

    public void shiftAccelStream(float accelX, float accelY, float accelZ) {
        shiftAccelStream(new PVector(accelX, accelY, accelZ));
    }

    public void shiftAccelStream(PVector accelIn) {
        for (int i = 0; i < accelStream.length - 1; i++) {
            accelStream[i] = accelStream[i+1];
        }
        accelStream[accelStream.length - 1] = accelIn;
    }

    public boolean pushAccel(float accelX, float accelY, float accelZ) {
        return pushAccel(new PVector(accelX, accelY, accelZ));
    }

    public boolean pushAccel(PVector accel) {
        for (int i = 0; i < accelStream.length; i++) {
            if (accelStream[i].mag() == 0) {
                accelStream[i] = accel;
                return true;
            }
        }

        return false;
    }

    public PVector getVelocity(PVector accel) {
        return previousAccel.add(accel).div(2.0f).add(velocityAccumulator);
    }

    public void updateVelocity(PVector prevAccel, PVector prevVel) {
        previousAccel = prevAccel;
        velocityAccumulator = prevVel;
    }

    public PVector getPosition(PVector vel) {
        return previousVel.add(vel).div(2.0f).add(positionAccumulator);
    }

    public void updatePosition(PVector prevVel, PVector prevPos) {
        previousVel = prevVel;
        positionAccumulator = prevPos;
    }

    public static PVector convertToGlobal(PVector accel, Quaternion orientation) {
        return orientation.applyRotation(accel).mult(-1);
    }

    public static PVector convertToGlobal(float accelX, float accelY, float accelZ, Quaternion orientation) {
        return convertToGlobal(new PVector(accelX, accelY, accelZ), orientation);
    }
}
