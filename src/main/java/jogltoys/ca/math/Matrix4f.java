package jogltoys.ca.math;

public final class Matrix4f {

	private float m00 = 0f;
	private float m10 = 0f;
	private float m20 = 0f;
	private float m30 = 0f;
	private float m01 = 0f;
	private float m11 = 0f;
	private float m21 = 0f;
	private float m31 = 0f;
	private float m02 = 0f;
	private float m12 = 0f;
	private float m22 = 0f;
	private float m32 = 0f;
	private float m03 = 0f;
	private float m13 = 0f;
	private float m23 = 0f;
	private float m33 = 0f;

	private Matrix4f() {
	}

	public static Matrix4f setZero(Matrix4f dst) {
		dst.m00 = dst.m01 = dst.m02 = dst.m03 = dst.m10 = dst.m11 = dst.m12 = dst.m13 = dst.m20 = dst.m21 = dst.m22 = dst.m23 = dst.m30 = dst.m31 = dst.m32 = dst.m33 = 0.0f;
		return dst;
	}

	public Matrix4f setZero() {
		return setZero(this);
	}

	public static Matrix4f zero() {
		return new Matrix4f();
	}

	public static Matrix4f setIdentity(Matrix4f dst) {
		setZero(dst);
		dst.m00 = dst.m11 = dst.m22 = dst.m33 = 1.0f;
		return dst;
	}

	public Matrix4f setIdentity() {
		return setIdentity(this);
	}

	public static Matrix4f identity() {
		return setIdentity(new Matrix4f());
	}

	public static Matrix4f setTranslation(Matrix4f dst, final float tx,
                                          final float ty, final float tz) {

		setIdentity(dst);
		dst.m03 = tx;
		dst.m13 = ty;
		dst.m23 = tz;
		return dst;
	}

	public Matrix4f setTranslation(final float tx, final float ty,
                                   final float tz) {
		return setTranslation(this, tx, ty, tz);
	}

	public static Matrix4f translation(final float tx, final float ty,
                                       final float tz) {

		return setTranslation(new Matrix4f(), tx, ty, tz);
	}

	public static Matrix4f setRotation(Matrix4f dst, final Quat4f q) {

		float n = (q.x * q.x) + (q.y * q.y) + (q.z * q.z) + (q.w * q.w);
		float s = (n > 0f) ? (2f / n) : 0f;

		float xs = q.x * s;
		float ys = q.y * s;
		float zs = q.z * s;
		float wx = q.w * xs;
		float wy = q.w * ys;
		float wz = q.w * zs;
		float xx = q.x * xs;
		float xy = q.x * ys;
		float xz = q.x * zs;
		float yy = q.y * ys;
		float yz = q.y * zs;
		float zz = q.z * zs;

		dst.m00 = 1f - (yy + zz);
		dst.m01 = xy - wz;
		dst.m02 = xz + wy;
		dst.m03 = 0f;
		dst.m10 = xy + wz;
		dst.m11 = 1f - (xx + zz);
		dst.m12 = yz - wx;
		dst.m13 = 0f;
		dst.m20 = xz - wy;
		dst.m21 = yz + wx;
		dst.m22 = 1f - (xx + yy);
		dst.m23 = 0f;
		dst.m30 = 0f;
		dst.m31 = 0f;
		dst.m32 = 0f;
		dst.m33 = 1f;

		return dst;
	}

	public Matrix4f setRotation(final Quat4f q) {
		return setRotation(this, q);
	}

	public static Matrix4f rotation(final Quat4f q) {
		return setRotation(new Matrix4f(), q);
	}

	public static Matrix4f set(final Matrix4f dst, final Matrix4f src) {

		dst.m00 = src.m00;
		dst.m01 = src.m01;
		dst.m02 = src.m02;
		dst.m03 = src.m03;
		dst.m10 = src.m10;
		dst.m11 = src.m11;
		dst.m12 = src.m12;
		dst.m13 = src.m13;
		dst.m20 = src.m20;
		dst.m21 = src.m21;
		dst.m22 = src.m22;
		dst.m23 = src.m23;
		dst.m30 = src.m30;
		dst.m31 = src.m31;
		dst.m32 = src.m32;
		dst.m33 = src.m33;
		return dst;
	}

	public Matrix4f set(final Matrix4f src) {
		return set(this, src);
	}

	public static Matrix4f copyOf(final Matrix4f src) {
		return set(new Matrix4f(), src);
	}

	public Matrix4f copy() {
		return set(new Matrix4f(), this);
	}

	public static Matrix4f interpolate(final Matrix4f dst, final Matrix4f a,
                                       final Matrix4f b, final float alpha) {

		final float oneMinusAlpha = 1f - alpha;

		dst.m00 = alpha * a.m00 + oneMinusAlpha * b.m00;
		dst.m01 = alpha * a.m01 + oneMinusAlpha * b.m01;
		dst.m02 = alpha * a.m02 + oneMinusAlpha * b.m02;
		dst.m03 = alpha * a.m03 + oneMinusAlpha * b.m03;
		dst.m10 = alpha * a.m10 + oneMinusAlpha * b.m10;
		dst.m11 = alpha * a.m11 + oneMinusAlpha * b.m11;
		dst.m12 = alpha * a.m12 + oneMinusAlpha * b.m12;
		dst.m13 = alpha * a.m13 + oneMinusAlpha * b.m13;
		dst.m20 = alpha * a.m20 + oneMinusAlpha * b.m20;
		dst.m21 = alpha * a.m21 + oneMinusAlpha * b.m21;
		dst.m22 = alpha * a.m22 + oneMinusAlpha * b.m22;
		dst.m23 = alpha * a.m23 + oneMinusAlpha * b.m23;
		dst.m30 = alpha * a.m30 + oneMinusAlpha * b.m30;
		dst.m31 = alpha * a.m31 + oneMinusAlpha * b.m31;
		dst.m32 = alpha * a.m32 + oneMinusAlpha * b.m32;
		dst.m33 = alpha * a.m33 + oneMinusAlpha * b.m33;

		return dst;
	}

	public Matrix4f interpolate(final Matrix4f a, final Matrix4f b,
                                final float alpha) {

		return interpolate(this, a, b, alpha);
	}

	public static Matrix4f interpolate2(final Matrix4f a, final Matrix4f b,
                                        final float alpha) {

		return interpolate(new Matrix4f(), a, b, alpha);
	}

	public static Matrix4f multiply(final Matrix4f dst, final Matrix4f a,
                                    final Matrix4f b) {

		dst.m00 = a.m00 * b.m00 + a.m01 * b.m10 + a.m02 * b.m20 + a.m03 * b.m30;
		dst.m01 = a.m00 * b.m01 + a.m01 * b.m11 + a.m02 * b.m21 + a.m03 * b.m31;
		dst.m02 = a.m00 * b.m02 + a.m01 * b.m12 + a.m02 * b.m22 + a.m03 * b.m32;
		dst.m03 = a.m00 * b.m03 + a.m01 * b.m13 + a.m02 * b.m23 + a.m03 * b.m33;
		dst.m10 = a.m10 * b.m00 + a.m11 * b.m10 + a.m12 * b.m20 + a.m13 * b.m30;
		dst.m11 = a.m10 * b.m01 + a.m11 * b.m11 + a.m12 * b.m21 + a.m13 * b.m31;
		dst.m12 = a.m10 * b.m02 + a.m11 * b.m12 + a.m12 * b.m22 + a.m13 * b.m32;
		dst.m13 = a.m10 * b.m03 + a.m11 * b.m13 + a.m12 * b.m23 + a.m13 * b.m33;
		dst.m20 = a.m20 * b.m00 + a.m21 * b.m10 + a.m22 * b.m20 + a.m23 * b.m30;
		dst.m21 = a.m20 * b.m01 + a.m21 * b.m11 + a.m22 * b.m21 + a.m23 * b.m31;
		dst.m22 = a.m20 * b.m02 + a.m21 * b.m12 + a.m22 * b.m22 + a.m23 * b.m32;
		dst.m23 = a.m20 * b.m03 + a.m21 * b.m13 + a.m22 * b.m23 + a.m23 * b.m33;
		dst.m30 = a.m30 * b.m00 + a.m31 * b.m10 + a.m32 * b.m20 + a.m33 * b.m30;
		dst.m31 = a.m30 * b.m01 + a.m31 * b.m11 + a.m32 * b.m21 + a.m33 * b.m31;
		dst.m32 = a.m30 * b.m02 + a.m31 * b.m12 + a.m32 * b.m22 + a.m33 * b.m32;
		dst.m33 = a.m30 * b.m03 + a.m31 * b.m13 + a.m32 * b.m23 + a.m33 * b.m33;
		return dst;
	}

	public Matrix4f multiply(final Matrix4f a, final Matrix4f b) {
		return multiply(this, a, b);
	}

	public static Matrix4f set(Matrix4f dst, final float m00, final float m01,
                               final float m02, final float m03, final float m10, final float m11,
                               final float m12, final float m13, final float m20, final float m21,
                               final float m22, final float m23, final float m30, final float m31,
                               final float m32, final float m33) {

		dst.m00 = m00;
		dst.m01 = m01;
		dst.m02 = m02;
		dst.m03 = m03;
		dst.m10 = m10;
		dst.m11 = m11;
		dst.m12 = m12;
		dst.m13 = m13;
		dst.m20 = m20;
		dst.m21 = m21;
		dst.m22 = m22;
		dst.m23 = m23;
		dst.m30 = m30;
		dst.m31 = m31;
		dst.m32 = m32;
		dst.m33 = m33;

		return dst;
	}

	public void get(final float[] dstArray) {

		dstArray[0] = m00;
		dstArray[1] = m10;
		dstArray[2] = m20;
		dstArray[3] = m30;
		dstArray[4] = m01;
		dstArray[5] = m11;
		dstArray[6] = m21;
		dstArray[7] = m31;
		dstArray[8] = m02;
		dstArray[9] = m12;
		dstArray[10] = m22;
		dstArray[11] = m32;
		dstArray[12] = m03;
		dstArray[13] = m13;
		dstArray[14] = m23;
		dstArray[15] = m33;
	}
}
