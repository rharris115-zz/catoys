package jogltoys.ca.math;

import java.util.Objects;

public final class Vector3f {

	private float x, y, z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(Vector3f a) {
		this(a.x, a.y, a.z);
	}

	public Vector3f() {
		this(0f, 0f, 0f);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3f set() {
		return set(0f, 0f, 0f);
	}

	public Vector3f set(Vector3f a) {
		return set(a.x, a.y, a.z);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector3f) {
			Vector3f other = (Vector3f) obj;
			return this.x == other.x && this.y == other.y && this.z == other.z;
		} else {
			return false;
		}
	}

	public static Vector3f add(Vector3f dest, Vector3f a, Vector3f b) {
		dest.x = a.x + b.x;
		dest.y = a.y + b.y;
		dest.z = a.z + b.z;
		return dest;
	}

	public static Vector3f add(Vector3f a, Vector3f b) {
		return add(new Vector3f(), a, b);
	}

	public Vector3f add(Vector3f b) {
		return add(this, this, b);
	}

	public static Vector3f subtract(Vector3f dest, Vector3f a, Vector3f b) {
		dest.x = a.x - b.x;
		dest.y = a.y - b.y;
		dest.z = a.z - b.z;
		return dest;
	}

	public static Vector3f subtract(Vector3f a, Vector3f b) {
		return subtract(new Vector3f(), a, b);
	}

	public Vector3f subtract(Vector3f b) {
		return subtract(this, this, b);
	}

	public static Vector3f multiply(Vector3f dest, Vector3f a, float s) {
		dest.x = a.x * s;
		dest.y = a.y * s;
		dest.z = a.z * s;
		return dest;
	}

	public static Vector3f multiply(Vector3f a, float s) {
		return multiply(new Vector3f(), a, s);
	}

	public Vector3f multiply(float s) {
		return multiply(this, this, s);
	}

	public static Vector3f divide(Vector3f dest, Vector3f a, float s) {
		dest.x = a.x / s;
		dest.y = a.y / s;
		dest.z = a.z / s;
		return dest;
	}

	public static Vector3f divide(Vector3f a, float s) {
		return divide(new Vector3f(), a, s);
	}

	public Vector3f divide(float s) {
		return divide(this, this, s);
	}

	public static float dot(Vector3f a, Vector3f b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	public float dot(Vector3f b) {
		return dot(this, b);
	}

	public static Vector3f cross(Vector3f dest, Vector3f a, Vector3f b) {
		dest.x = a.y * b.z - a.z * b.y;
		dest.y = a.z * b.x - a.x * b.z;
		dest.z = a.x * b.y - a.y * b.x;
		return dest;
	}

	public static Vector3f cross(Vector3f a, Vector3f b) {
		return cross(new Vector3f(), a, b);
	}

	public Vector3f cross(Vector3f b) {
		return cross(new Vector3f(x, y, z), this, b);
	}

	public float lengthSq() {
		return (x * x + y * y + z * z);
	}

	public float length() {
		return (float) Math.sqrt(lengthSq());
	}

	public static float distanceSq(Vector3f a, Vector3f b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		float dz = a.z - b.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public float distanceSq(Vector3f b) {
		return distanceSq(this, b);
	}

	public static float distance(Vector3f a, Vector3f b) {
		return (float) Math.sqrt(distanceSq(a, b));
	}

	public float distance(Vector3f b) {
		return distance(this, b);
	}

	public static float angle(Vector3f a, Vector3f b) {
		float div = (float) Math.sqrt(a.lengthSq() * b.lengthSq());
		return (float) Math.acos(dot(a, b)) / div;
	}

	public float angle(Vector3f b) {
		return angle(this, b);
	}

	public Vector3f normalise(Vector3f dest, Vector3f a) {
		dest.set(a);
		return dest.divide(dest.length());
	}

	public Vector3f normalise() {
		return divide(length());
	}

	public static Vector3f linterp(Vector3f dest, Vector3f a, Vector3f b,
                                   float alpha) {
		float alphaC = 1f - alpha;
		dest.x = a.x * alpha + b.x * alphaC;
		dest.y = a.y * alpha + b.y * alphaC;
		dest.z = a.z * alpha + b.z * alphaC;
		return dest;
	}

	public static Vector3f linterp(Vector3f a, Vector3f b, float alpha) {
		return linterp(new Vector3f(), a, b, alpha);
	}

	public static Vector3f projectTo(Vector3f dest, Vector3f a, Vector3f b) {
		return multiply(dest, b, dot(a, b) / b.lengthSq());
	}
}
