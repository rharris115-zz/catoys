package jogltoys.ca.models;

import jogltoys.ca.ModelIcons;
import jogltoys.ca.models.params.CellularAutomataParameter;
import jogltoys.ca.models.params.DoubleCellularAutomataParameter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FireModel implements CellularAutomaton {

	public static final int FIRE = 0xff0099ff;

	public static final int GROWTH = 0xff009900;

	public static final int BURNT = 0x00000000;

	public static final double MIN_REGROWTH_RATE = 0d;
	public static final double MAX_REGROWTH_RATE = 0.06d;
	public static final double DEFAULT_REGROWTH_RATE = MAX_REGROWTH_RATE / 2;

	public static final double MIN_LIGHTNING_RATE = 0d;
	public static final double MAX_LIGHTNING_RATE = 0.000006d;
	public static final double DEFAULT_LIGHTNING_RATE = MAX_LIGHTNING_RATE / 2;

	public static final String REGROWTH_RATE_NAME = "Regrowth Rate";

	public static final String LIGHTNING_RATE_NAME = "Lightning Rate";

	private final CellularAutomataParameter<Double> regrowthRate = new DoubleCellularAutomataParameter(
			REGROWTH_RATE_NAME, ModelIcons.GROWTH, this, DEFAULT_REGROWTH_RATE,
			MIN_REGROWTH_RATE, MAX_REGROWTH_RATE);

	private final CellularAutomataParameter<Double> lightningRate = new DoubleCellularAutomataParameter(
			LIGHTNING_RATE_NAME, ModelIcons.LIGHTNING, this,
			DEFAULT_LIGHTNING_RATE, MIN_LIGHTNING_RATE, MAX_LIGHTNING_RATE);

	private int width;

	private int height;

	private int[][] state;

	private int time = 0;

	private final int[] cachedNeighborIndices = new int[4];

	@Override
	public void init(final int width, final int height)
			throws IllegalArgumentException {

		this.width = width;
		this.height = height;

		state = new int[2][width * height];

		Arrays.fill(state[0], BURNT);
		Arrays.fill(state[1], BURNT);
	}

	@Override
	public void iterate(Random rnd) {

		final int[] sBefore = state[time % 2];

		time++;

		final int[] sNow = state[time % 2];

		for (int i = 0; i < width; i++) {

			for (int j = 0; j < height; j++) {

				final int index = LatticeUtils.calcIndex(i, j, width);

				switch (sBefore[index]) {

				case (BURNT):

					sNow[index] = rnd.nextDouble() < regrowthRate.getValue() ? GROWTH
							: BURNT;
					break;

				case (FIRE):

					sNow[index] = BURNT;
					break;

				case (GROWTH):

					boolean willBurn = false;

					LatticeUtils.calcVonNeumanaNeighborhoodIndices(index,
							width, height, cachedNeighborIndices);

					for (final int neighborIndex : cachedNeighborIndices) {
						willBurn |= sBefore[neighborIndex] == FIRE;
					}

					willBurn |= rnd.nextDouble() < lightningRate.getValue();

					sNow[index] = willBurn ? FIRE : GROWTH;
					break;

				}
			}
		}
	}

	@Override
	public int[] getCells() {
		return state[time % 2];
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

		return Arrays.asList(new CellularAutomataParameter<?>[] { regrowthRate,
				lightningRate });
	}

	@Override
	public void notifyParameterChange(
			final CellularAutomataParameter<?> parameter) {

	}
}
