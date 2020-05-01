package jogltoys.ca;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public enum Eye {

	LEFT {
		@Override
		public ViewConfigurator createConfigurator(float yFov,
                                                   float interOcularDistance) {
			return new EyeConfigurator(0f, 0.5f, yFov, interOcularDistance / 2);
		}
	},
	RIGHT {
		@Override
		public ViewConfigurator createConfigurator(float yFov,
                                                   float interOcularDistance) {
			return new EyeConfigurator(0.5f, 0.5f, yFov,
					-interOcularDistance / 2);
		}
	},
	CENTRE {
		@Override
		public ViewConfigurator createConfigurator(float yFov,
                                                   float interOcularDistance) {
			return new EyeConfigurator(0, 1f, yFov, 0f);
		}
	};

	private static class EyeConfigurator implements ViewConfigurator {

		private int x = 0;
		private int y = 0;
		private int width = 1;
		private int height = 1;

		private final float xFactor;
		private final float widthFactor;
		private final float yFov;
		private final float eyeX;

		private EyeConfigurator(float xFactor, float widthFactor, float yFov,
				float eyeX) {

			this.xFactor = xFactor;
			this.widthFactor = widthFactor;
			this.yFov = yFov;
			this.eyeX = eyeX;
		}

		private int getX() {
			return x + (int) (xFactor * width);
		}

		private int getWidth() {
			return (int) (widthFactor * width);
		}

		@Override
		public void setViewPort(GL2 gl, GLU glu) {
			gl.glViewport(getX(), y, getWidth(), height);
		}

		@Override
		public void setPerspective(GL2 gl, GLU glu) {
			glu.gluPerspective(yFov, (float) getWidth() / (float) (height), 1f,
					100f);
		}

		@Override
		public void look(final GLU glu) {
			glu.gluLookAt(eyeX, 0f, 20f, 0f, 0f, 0f, 0f, 1f, 0f);
		}

		@Override
		public void reshape(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	public abstract ViewConfigurator createConfigurator(float yFov,
                                                        float interOcularDistance);

}