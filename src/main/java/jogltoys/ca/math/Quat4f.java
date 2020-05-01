package jogltoys.ca.math;

public final class Quat4f {

	public float x, y, z, w;

	public void set(final float x, final float y, final float z, final float w) {

		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public void set() {
		x = y = z = w = 0f;
	}

	public void set(final Vector3f vector, final float w) {

		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
		this.w = w;
	}
}
