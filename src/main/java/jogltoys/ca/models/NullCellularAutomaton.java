package jogltoys.ca.models;

import jogltoys.ca.models.params.CellularAutomataParameter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NullCellularAutomaton implements CellularAutomaton {

	private int width;
	private int height;
	private int[] cells;

	@Override
	public void init(final int width, final int height)
			throws IllegalArgumentException {
		this.width = width;
		this.height = height;
		cells = new int[width * height];
	}

	@Override
	public void iterate(Random rnd) {
	}

	@Override
	public int[] getCells() {
		return cells;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public List<CellularAutomataParameter<?>> getParameters() {
		return Collections.emptyList();
	}

	@Override
	public void notifyParameterChange(
			final CellularAutomataParameter<?> parameter) {
	}
}
