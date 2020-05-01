package jogltoys.ca;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL2.GL_QUAD_STRIP;

public class TorusAndTileDrawer {

	private static final float TWO_PI = (float) Math.PI * 2f;

	public static final float calcModifiedWidth(final float aspectRatio,
			final float maxDimension) {
		return aspectRatio < 1f ? maxDimension * aspectRatio : maxDimension;
	}

	public static final float calcModifiedHeight(final float aspectRatio,
			final float maxDimension) {
		return aspectRatio > 1f ? maxDimension * aspectRatio : maxDimension;
	}

	public static void drawTile(final GL2 gl, final float maxDimension,
			final float aspectRatio) {

		final float flatWidth = calcModifiedWidth(aspectRatio, maxDimension);
		final float flatHeight = calcModifiedHeight(aspectRatio, maxDimension);

		final float sx = flatWidth / 2;
		final float sy = flatHeight / 2;

		gl.glBegin(GL_QUAD_STRIP);

		gl.glTexCoord2f(0f, 0f);
		gl.glNormal3f(0, 0, 1f);
		gl.glVertex3f(-sx, -sy, 0f);

		gl.glTexCoord2f(0f, 1f);
		gl.glNormal3f(0, 0, 1f);
		gl.glVertex3f(-sx, sy, 0f);

		gl.glTexCoord2f(1f, 0f);
		gl.glNormal3f(0, 0, 1f);
		gl.glVertex3f(sx, -sy, 0f);

		gl.glTexCoord2f(1f, 1f);
		gl.glNormal3f(0, 0, 1f);
		gl.glVertex3f(sx, sy, 0f);

		gl.glEnd();
	}

	public static void drawInterpolatedTorus(final GL2 gl, final float r,
			final float R, final int ringSides, final int rings,
			final float aspectRatio, final float alpha) {

		final float one_minus_alpha = (1f - alpha);

		final float flatWidth = calcModifiedWidth(aspectRatio, 2 * (r + R));
		final float flatHeight = calcModifiedHeight(aspectRatio, 2 * (r + R));

		final float sx = flatWidth / 2;
		final float sy = flatHeight / 2;

		for (int i = 0; i < rings; i++) {

			final float t = (float) i / rings;
			final float theta = t * TWO_PI;

			final float cosTheta = (float) Math.cos(theta);
			final float sinTheta = (float) Math.sin(theta);

			final float t1 = (float) (i + 1) / rings;
			final float theta1 = t1 * TWO_PI;

			final float cosTheta1 = (float) Math.cos(theta1);
			final float sinTheta1 = (float) Math.sin(theta1);

			gl.glBegin(GL_QUAD_STRIP);

			for (int j = 0; j <= ringSides; j++) {

				final float p = (float) j / ringSides;

				final float phi = (p + 0.5f) * TWO_PI;

				final float cosPhi = (float) Math.cos(phi);
				final float sinPhi = (float) Math.sin(phi);

				final float dist = R + r * cosPhi;

				gl.glTexCoord2f(p, t1);

				gl.glNormal3f(alpha * cosTheta1 * cosPhi, -alpha * sinTheta1
						* cosPhi, one_minus_alpha + alpha * sinPhi);

				gl.glVertex3f(one_minus_alpha * (2f * p - 1f) * sx + alpha
						* cosTheta1 * dist, one_minus_alpha * (2f * t1 - 1f)
						* sy - alpha * sinTheta1 * dist, alpha * r * sinPhi);

				gl.glTexCoord2f(p, t);

				gl.glNormal3f(alpha * cosTheta * cosPhi, -alpha * sinTheta
						* cosPhi, one_minus_alpha + alpha * sinPhi);

				gl.glVertex3f(one_minus_alpha * (2f * p - 1f) * sx + alpha
						* cosTheta * dist, one_minus_alpha * (2f * t - 1f) * sy
						- alpha * sinTheta * dist, alpha * r * sinPhi);

			}

			gl.glEnd();
		}
	}

	public static void drawTorus(final GL2 gl, final float r, final float R,
			final int ringSides, final int rings) {

		for (int i = 0; i < rings; i++) {

			final float t = (float) i / rings;
			final float theta = t * TWO_PI;

			final float cosTheta = (float) Math.cos(theta);
			final float sinTheta = (float) Math.sin(theta);

			final float t1 = (float) (i + 1) / rings;
			final float theta1 = t1 * TWO_PI;

			final float cosTheta1 = (float) Math.cos(theta1);
			final float sinTheta1 = (float) Math.sin(theta1);

			gl.glBegin(GL_QUAD_STRIP);

			for (int j = 0; j <= ringSides; j++) {

				final float p = (float) j / ringSides;

				final float phi = (p + 0.5f) * TWO_PI;

				final float cosPhi = (float) Math.cos(phi);
				final float sinPhi = (float) Math.sin(phi);

				final float dist = R + r * cosPhi;

				gl.glTexCoord2f(p, t1);
				gl.glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
				gl.glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi);

				gl.glTexCoord2f(p, t);
				gl.glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
				gl.glVertex3f(cosTheta * dist, -sinTheta * dist, r * sinPhi);
			}

			gl.glEnd();
		}
	}

}
