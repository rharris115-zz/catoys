package jogltoys.ca.models;

import jogltoys.ca.models.params.CellularAutomataParameter;

import java.util.List;
import java.util.Random;

public interface CellularAutomaton {

	void init(int width, int height) throws IllegalArgumentException;

	void iterate(Random rnd);

	int[] getCells();

	int getWidth();

	int getHeight();

	List<CellularAutomataParameter<?>> getParameters();

	void notifyParameterChange(CellularAutomataParameter<?> parameter);
}