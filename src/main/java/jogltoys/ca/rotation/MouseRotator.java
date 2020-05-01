package jogltoys.ca.rotation;

import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.opengl.GLWindow;
import jogltoys.ca.math.Matrix4f;
import jogltoys.ca.math.Point2f;
import jogltoys.ca.math.Quat4f;
import jogltoys.ca.math.Vector3f;

import javax.media.opengl.GLAutoDrawable;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseRotator {

	private static final float EPSILON = 1e-5f;

	private final Quat4f rotation = new Quat4f();

	private static final Matrix4f UNITY_MATRIX = Matrix4f.identity();

	private final Matrix4f interpolatedRotation = Matrix4f.identity();

	private final Matrix4f lastRotation = Matrix4f.identity();
	private final Matrix4f thisRotation = Matrix4f.identity();
	private final Matrix4f rotationCopy = thisRotation.copy();

	private final float[] matrix = new float[16];

	private final Vector3f lastDragVector = new Vector3f();

	private boolean mouseDown = false;
	private boolean draggingPaused = true;

	private void mapToSphere(final float x, final float y, final float width,
			final float height, final Vector3f vector) {

		final Point2f tempPoint = new Point2f((2 * x / (width - 1f)) - 1f,
				1f - (2 * y / (height - 1f)));

		final float length = tempPoint.getDistanceToOriginSquared();

		if (length > 1f) {
			final float norm = (float) (1f / Math.sqrt(length));
			vector.set(tempPoint.getX() * norm, tempPoint.getY() * norm, 0f);
		} else {
			vector.set(tempPoint.getX(), tempPoint.getY(),
					(float) Math.sqrt(1f - length));
		}
	}

	private void mousePressedImpl(final int x, final int y, final int width,
			final int height, final boolean sideBySideStereo) {

		rotation.set();
		final int viewportWidth = sideBySideStereo ? width / 2 : width;
		mapToSphere(x % viewportWidth, y, viewportWidth, height, lastDragVector);
		mouseDown = true;

	}

	private void mouseDraggedImpl(final int x, final int y, final int width,
			final int height, final boolean sideBySideStereo) {

		final Vector3f dragVector = new Vector3f();

		final int viewportWidth = sideBySideStereo ? width / 2 : width;
		mapToSphere(x % viewportWidth, y, viewportWidth, height, dragVector);

		final Vector3f perp = new Vector3f();
		Vector3f.cross(perp, lastDragVector, dragVector);

		if (perp.length() > EPSILON) {
			rotation.set(perp, Vector3f.dot(lastDragVector, dragVector));
		} else {
			rotation.set();
		}

		lastDragVector.set(dragVector);

		draggingPaused = false;
	}

	private void mouseReleasedImpl() {
		mouseDown = false;
	}

	public void addMouseListener(final GLAutoDrawable drawable,
			final boolean sideBySideStereo) {

		if (drawable instanceof Component) {

			final java.awt.event.MouseAdapter m = new java.awt.event.MouseAdapter() {

				@Override
				public void mousePressed(final MouseEvent e) {

					mousePressedImpl(e.getX(), e.getY(), drawable.getWidth(),
							drawable.getHeight(), sideBySideStereo);
				}

				@Override
				public void mouseReleased(final MouseEvent e) {
					mouseReleasedImpl();
				}

				@Override
				public void mouseDragged(final MouseEvent e) {

					mouseDraggedImpl(e.getX(), e.getY(), drawable.getWidth(),
							drawable.getHeight(), sideBySideStereo);
				}
			};

			((Component) drawable).addMouseListener(m);
			((Component) drawable).addMouseMotionListener(m);

		} else if (drawable instanceof GLWindow) {

			((GLWindow) drawable).addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(
						final com.jogamp.newt.event.MouseEvent e) {
					mouseReleasedImpl();
				}

				@Override
				public void mousePressed(
						final com.jogamp.newt.event.MouseEvent e) {
					mousePressedImpl(e.getX(), e.getY(), drawable.getWidth(),
							drawable.getHeight(), sideBySideStereo);
				}

				@Override
				public void mouseDragged(
						final com.jogamp.newt.event.MouseEvent e) {
					mouseDraggedImpl(e.getX(), e.getY(), drawable.getWidth(),
							drawable.getHeight(), sideBySideStereo);
				}
			});
		}
	}

	public void rotate() {

		synchronized (this) {

			// Calculate the rotation.
			thisRotation.setRotation(rotation).multiply(
					rotationCopy.set(thisRotation), lastRotation);

			// Save the rotation matrix.
			lastRotation.set(thisRotation);
		}

		if (mouseDown) {

			if (draggingPaused) {
				rotation.set();
			}

			draggingPaused = true;
		}
	}

	public void init() {

		lastRotation.setIdentity();
		thisRotation.setIdentity();
	}

	public float[] getMatrix() {

		synchronized (this) {
			thisRotation.get(matrix);
		}

		return matrix;
	}

	public float[] getIterpolatedMatrix(final float alpha) {

		interpolatedRotation.interpolate(thisRotation, UNITY_MATRIX, alpha);

		synchronized (this) {
			interpolatedRotation.get(matrix);
		}

		return matrix;
	}
}
