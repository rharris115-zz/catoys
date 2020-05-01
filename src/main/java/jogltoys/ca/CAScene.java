package jogltoys.ca;

import com.jogamp.opengl.util.Animator;
import jogltoys.ca.math.Matrix4f;
import jogltoys.ca.models.CellularAutomaton;
import jogltoys.ca.rotation.MouseRotator;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.nio.IntBuffer;
import java.util.*;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.GL_COMPILE;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.*;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class CAScene {

	public static final float TORUS_RADIUS = 5f;

	public static final float TORUS_RING_RADIUS = 2.5f;

	public static final float TILE_MAX_DIMENSION = 2 * (TORUS_RADIUS + TORUS_RING_RADIUS);

	public static final int N_TORUS_RINGS = 40;

	public static final int N_TORUS_RING_SEGMENTS = 40;

	private static final Set<Float> POSSIBLE_ASPECT_RATIOS = Collections
			.unmodifiableSet(new HashSet<Float>() {

				private static final long serialVersionUID = 448820469864134114L;

				{
					for (final int width : ModelManager.AVAILABLE_DIMENSIONS) {
						for (final int height : ModelManager.AVAILABLE_DIMENSIONS) {
							add((float) width / (float) height);
						}
					}
				}
			});

	public class CARenderer implements GLEventListener {

		private final ViewConfigurator[] configurators;

		private CARenderer(ViewConfigurator... configurators) {
			this.configurators = configurators;
		}

		@Override
		public void init(final GLAutoDrawable drawable) {

			final GL2 gl = drawable.getGL().getGL2();

			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			gl.glClearDepth(1.0f);
			gl.glDepthFunc(GL.GL_LEQUAL);
			gl.glEnable(GL.GL_DEPTH_TEST);

			gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);

			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

			torus = gl.glGenLists(1);

			gl.glNewList(torus, GL_COMPILE);
			TorusAndTileDrawer.drawTorus(gl, TORUS_RING_RADIUS, TORUS_RADIUS,
					N_TORUS_RING_SEGMENTS, N_TORUS_RINGS);
			gl.glEndList();

			for (final float aspectRatio : POSSIBLE_ASPECT_RATIOS) {

				final int tiledGeometry = gl.glGenLists(1);
				gl.glNewList(tiledGeometry, GL_COMPILE);
				TorusAndTileDrawer
						.drawTile(gl, TILE_MAX_DIMENSION, aspectRatio);
				gl.glEndList();
				cachedTileGeometries.put(aspectRatio, tiledGeometry);
			}

			gl.glEnable(GL_NORMALIZE);
			gl.glEnable(GL_TEXTURE_2D);

			gl.glEnable(GL_LIGHT0);
			gl.glEnable(GL_LIGHTING);

			gl.glLightfv(GL_LIGHT0, GL_POSITION, new float[] { 5.0f, 5.0f,
					10.0f, 0.0f }, 0);

			gl.glEnable(GL_COLOR_MATERIAL);

			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		}

		@Override
		public void dispose(final GLAutoDrawable drawable) {
		}

		@Override
		public void display(final GLAutoDrawable drawable) {

			final GL2 gl = drawable.getGL().getGL2();

			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			mouseRotator.rotate();

			final CellularAutomaton model = modelManager.getModel();

			final long time = System.currentTimeMillis();

			if (model.getWidth() == 0 || model.getHeight() == 0) {
				return;
			}

			final float aspectRatio = (float) model.getWidth()
					/ (float) model.getHeight();

			for (final ViewConfigurator configurator : configurators) {

				configurator.setViewPort(gl, glu);

				// Select The Projection Matrix
				gl.glMatrixMode(GL_PROJECTION);
				gl.glLoadIdentity();
				// Calculate The Aspect Ratio Of The Window
				configurator.setPerspective(gl, glu);
				configurator.look(glu);

				// Get back to the model matrix.
				gl.glMatrixMode(GL_MODELVIEW);
				gl.glLoadIdentity();

				gl.glTexImage2D(GL_TEXTURE_2D, 0, 4, model.getWidth(),
						model.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
						IntBuffer.wrap(model.getCells()));

				gl.glColor4f(1f, 1f, 1f, 1f);

				if (transitionStart + TRANSITION_LENGTH > time) {

					float coeff = (float) (time - transitionStart)
							/ (float) TRANSITION_LENGTH;

					coeff = coeff > 1f ? 1f : coeff;

					final float alpha = projection == Projection.TOROIDAL ? coeff
							: 1f - coeff;

					gl.glPushMatrix();
					gl.glMultMatrixf(mouseRotator.getIterpolatedMatrix(alpha),
							0);
					TorusAndTileDrawer.drawInterpolatedTorus(gl,
							TORUS_RING_RADIUS, TORUS_RADIUS, N_TORUS_RINGS,
							N_TORUS_RING_SEGMENTS, aspectRatio, alpha);
					gl.glPopMatrix();

				} else if (projection == Projection.TOROIDAL) {

					gl.glPushMatrix();
					gl.glMultMatrixf(mouseRotator.getMatrix(), 0);
					gl.glCallList(torus);
					gl.glPopMatrix();
				} else {

					final int tileGeometry = cachedTileGeometries
							.get(aspectRatio);

					gl.glCallList(tileGeometry);

					final float tileWidth = TorusAndTileDrawer
							.calcModifiedWidth(aspectRatio, TILE_MAX_DIMENSION);

					final float tileHeight = TorusAndTileDrawer
							.calcModifiedHeight(aspectRatio, TILE_MAX_DIMENSION);

					gl.glColor4f(1f, 1f, 1f, 0.5f);

					for (int i = -2; i <= 2; i++) {

						for (int j = -2; j <= 2; j++) {

							if (!(i == 0 && j == 0)) {

								tilingTranslator.setTranslation(i * tileWidth,
										j * tileHeight, 0f);
								tilingTranslator.get(tilingTranslationMatrix);
								gl.glPushMatrix();
								gl.glMultMatrixf(tilingTranslationMatrix, 0);
								gl.glCallList(tileGeometry);
								gl.glPopMatrix();
							}
						}
					}
				}
			}
			gl.glFlush();
		}

		@Override
		public void reshape(final GLAutoDrawable drawable, final int x,
				final int y, final int width, final int height) {

			for (ViewConfigurator configurator : configurators) {
				configurator.reshape(x, y, width, height);
			}
		}

	}

	public enum Projection {

		TOROIDAL() {
			@Override
			public String getDescription() {
				return "Toroidal";
			}
		},

		TILED() {
			@Override
			public String getDescription() {
				return "Tiled";
			}
		};

		public abstract String getDescription();
	};

	private final Map<Float, Integer> cachedTileGeometries = new HashMap<Float, Integer>();

	public static final Projection DEFAULT_PROJECTION = Projection.TILED;

	private final Matrix4f tilingTranslator = Matrix4f.identity();
	private final float[] tilingTranslationMatrix = new float[16];

	private final MouseRotator mouseRotator = new MouseRotator();

	private int torus;

	private final GLU glu = new GLU();

	private static long TRANSITION_LENGTH = 2000;

	private Projection projection = DEFAULT_PROJECTION;

	private long transitionStart = Long.MIN_VALUE;

	private final ModelManager modelManager;

	private final Animator animator = new Animator();

	{
		animator.start();
	}

	public CAScene(final ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	public void register(GLAutoDrawable drawable,
			ViewConfigurator... configurators) {
		mouseRotator.addMouseListener(drawable, configurators.length > 1);
		drawable.addGLEventListener(new CARenderer(configurators));
		animator.add(drawable);
	}

	public ModelManager getModelManager() {
		return modelManager;
	}

	public Projection getProjection() {
		return projection;
	}

	public void setProjection(final Projection projection) {

		this.projection = projection;
		transitionStart = System.currentTimeMillis();
	}
}
