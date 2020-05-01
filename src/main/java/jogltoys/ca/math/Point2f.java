package jogltoys.ca.math;

public class Point2f {

	private final float x, y;

	public Point2f(final float x, final float y) {

		this.x = x;
		this.y = y;
	}

	public float getDistanceToOriginSquared() {
		return x * x + y + y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
