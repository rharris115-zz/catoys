package jogltoys.ca;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public interface ViewConfigurator {

	void setViewPort(GL2 gl, GLU glu);

	void setPerspective(GL2 gl, GLU glu);

	void look(GLU glu);

	void reshape(int x, int y, int width, int height);
}
