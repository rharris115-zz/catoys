package jogltoys.ca.models;

import jogltoys.ca.ModelIcons;
import jogltoys.ca.models.params.CellularAutomataParameter;
import jogltoys.ca.models.params.DoubleCellularAutomataParameter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IsingModel implements CellularAutomaton {

	public static final int UP = 0xffffffff;

	public static final int DOWN = 0xff000099;

	public static final double DEFAULT_TEMPERATURE = 2.4d;

	public static final double DEFAULT_MAGNETIC_FIELD = 0d;

	public static final String TEMPERATURE_NAME = "Temperature";

	public static final String MAGNETIC_FIELD_NAME = "Magnetic Field";

	private final DoubleCellularAutomataParameter temperature = new DoubleCellularAutomataParameter(
			TEMPERATURE_NAME, ModelIcons.TEMPERATURE, this,
			DEFAULT_TEMPERATURE, 0d, 4d);

	private final DoubleCellularAutomataParameter magneticField = new DoubleCellularAutomataParameter(
			MAGNETIC_FIELD_NAME, ModelIcons.MAGNET, this,
			DEFAULT_MAGNETIC_FIELD, -1d, 1d);

	private int width;

	private int height;

	private int[] state;

	private final int[] neighboringIndicesCache = new int[4];

	@Override
	public void init(final int width, final int height)
			throws IllegalArgumentException {

		this.width = width;
		this.height = height;

		state = new int[width * height];

		Arrays.fill(state, DOWN);
	}

	@Override
	public void iterate(Random rnd) {

		final int n = state.length;

		for (int i = 0; i < n; i++) {

			final int index = rnd.nextInt(state.length);

			LatticeUtils.calcVonNeumanaNeighborhoodIndices(index, width,
					height, neighboringIndicesCache);

			int spinSum = 0;

			for (final int neighborIndex : neighboringIndicesCache) {
				spinSum += state[neighborIndex] == UP ? 1 : -1;
			}

			final double dE = (state[index] == UP ? 2 : -2)
					* (spinSum + magneticField.getValue());

			final boolean accept = dE < 0 ? true : rnd.nextDouble() < Math
					.exp(-dE / temperature.getValue());

			if (accept) {
				state[index] = state[index] == UP ? DOWN : UP;
			}
		}
	}

	@Override
	public int[] getCells() {
		return state;
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
		return Arrays.asList(new CellularAutomataParameter<?>[] { temperature,
				magneticField });
	}

	@Override
	public void notifyParameterChange(
			final CellularAutomataParameter<?> parameter) {

	}

}
