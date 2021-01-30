package data.math;

import processing.core.PVector;

public class Quaternion {

    private float q0, q1, q2, q3;

    // Creates a default quaternion
    public Quaternion() {
        this.q0 = 1;
        this.q1 = 0;
        this.q2 = 0;
        this.q3 = 0;
    }

    // Creates a unit quaternion with the given values
    public Quaternion(float q0, float q1, float q2, float q3) {
        this.q0 = q0;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.normalize();
    }

    // Creates a quaternion representing the rotation from vector u to vector v
    public Quaternion(PVector u, PVector v) {
        double magProduct = u.mag() * v.mag();
        if (magProduct == 0) {
            throw new IllegalArgumentException("Zero magnitude for vector");
        }

        double dotProduct = (u.x * v.x) + (u.y * v.y) + (u.z * v.z);

        if (dotProduct == (-magProduct)) {
            // special case: u = -v (cross product won't work)
            // Arbitrary rotation vector orthogonal to u
            // Rotate PI angle
            PVector rotationVector = orthogonalVector(u);
            this.q0 = 0.0f; // cos(PI / 2) = 0
            this.q1 = -rotationVector.x; // sin(PI / 2) = 1
            this.q2 = -rotationVector.y;
            this.q3 = -rotationVector.z;

        } else {
            // (u,v) defines a plane, rotate about an axis orthogonal to this plane

            // q0 = cos(theta / 2) -> derived from scalar projection formula
            this.q0 = (float) Math.sqrt(0.5 * (1.0 + (dotProduct / magProduct)));

            // Combination of sin(theta / 2) and vector normalization
            double coeff = 1.0 / (2.0 * q0 * magProduct);

            // Take cross product to find orthogonal vector
            // Multiply by coeff to normalize and convert to quaternion
            this.q1 = (float) (coeff * ((u.y * v.z) - (u.z * v.y)));
            this.q2 = (float) (coeff * ((u.z * v.x) - (u.x * v.z)));
            this.q3 = (float) (coeff * ((u.x * v.y) - (u.y * v.x)));
        }
    }

    public void normalize() {
        double inv = 1.0 / Math.sqrt((q0 * q0) + (q1 * q1) + (q2 * q2) + (q3 * q3));
        this.q0 *= inv;
        this.q1 *= inv;
        this.q2 *= inv;
        this.q3 *= inv;
    }

    public Quaternion reverse() {
        // Same axis of rotation, opposite rotation
        return new Quaternion(-q0, q1, q2, q3);
    }

    public float getQ0() {
        return q0;
    }

    public float getQ1() {
        return q1;
    }

    public float getQ2() {
        return q2;
    }

    public float getQ3() {
        return q3;
    }

    public PVector applyRotation(PVector v) {
        // Apply quaternion multiplication: q * v * q^-1
        // Formula derived for unit quaternions only
        double dot = q1 * v.x + q2 * v.y + q3 * v.z;

        return new PVector((float) (2 * (q0 * (v.x * q0 + (q2 * v.z - q3 * v.y)) + dot * q1) - v.x),
                (float) (2 * (q0 * (v.y * q0 + (q3 * v.x - q1 * v.z)) + dot * q2) - v.y),
                (float) (2 * (q0 * (v.z * q0 + (q1 * v.y - q2 * v.x)) + dot * q3) - v.z));
    }

    private PVector orthogonalVector(PVector v) {
        double threshold = 0.6 * v.mag();
        if (threshold == 0) {
            throw new ArithmeticException("Zero magnitude vector");
        }

        // Determine which axis to ignore using threshold
        // (Ignoring an axis simplifies calculations in a single plane)
        if (v.x >= -threshold && v.x <= threshold) {
            // Prepare inverse for normalization
            double inverse = 1.0 / Math.sqrt((v.y * v.y) + (v.z * v.z));
            // Swap non-ignored values and invert one (results in zero dot product = orthogonal)
            // Apply normalization
            return new PVector(0, (float) (inverse * v.z), (float) (-inverse * v.y));
        } else if (v.y >= -threshold && v.y <= threshold) {
            double inverse = 1.0 / Math.sqrt((v.x * v.x) + (v.z * v.z));
            return new PVector((float) (-inverse * v.z), 0, (float) (inverse * v.x));
        }
        double inverse = 1.0 / Math.sqrt((v.x * v.x) + (v.y * v.y));
        return new PVector((float) (inverse * v.y), (float) (-inverse * v.x), 0);
    }

    @Override
    public String toString() {
        return "[ " + q0 + ", " + q1 + ", " + q2 + ", " + q3 + " ]";
    }
}
