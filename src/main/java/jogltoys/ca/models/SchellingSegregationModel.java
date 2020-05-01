package jogltoys.ca.models;

import jogltoys.ca.ModelIcons;
import jogltoys.ca.models.params.CellularAutomataParameter;
import jogltoys.ca.models.params.DoubleCellularAutomataParameter;
import jogltoys.ca.models.params.IntegerCellularAutomataParameter;

import java.util.*;

public class SchellingSegregationModel implements CellularAutomaton {

	public static final int PENNY = 0xff3373b8;

	public static final int NICKEL = 0xffc0c0c0;

	public static final int EMPTY = 0x00ffffff;

	public static final double DEFAULT_DENSITY = 0.95;

	public static final int DEFAULT_LIKENESS_PREFERENCE = 50;

	public static final String LIKENESS_PREFERENCE_NAME = "Same Coin Neighbor Preference";

	public static final String NUMBER_OF_COINS = "Coin Density";

	private final IntegerCellularAutomataParameter likenessPreference = new IntegerCellularAutomataParameter(
			LIKENESS_PREFERENCE_NAME, ModelIcons.COIN, this,
			DEFAULT_LIKENESS_PREFERENCE, 0, 100);

	private final DoubleCellularAutomataParameter coinDensity = new DoubleCellularAutomataParameter(
			NUMBER_OF_COINS, ModelIcons.COIN, this, DEFAULT_DENSITY, .5d, 1d,
			0, 1000);;

	private int width;

	private int height;

	private int[] cells;

	private final int[] cachedNeighborIndices = new int[8];

	private List<Integer> emptyCells;

	private boolean needsEmptyCellUpdate = true;

	private boolean needsCoinAllocation = true;

	@Override
	public void init(final int width, final int height)
			throws IllegalArgumentException {

		this.width = width;
		this.height = height;

		final int n = width * height;

		cells = new int[n];

		Arrays.fill(cells, EMPTY);

		needsCoinAllocation = true;
	}

	@Override
	public void iterate(Random rnd) {

		if (needsCoinAllocation) {
			allocateCoins(rnd);
		}

		if (needsEmptyCellUpdate) {
			updateEmptyCellCount(rnd);
		}

		for (int i = 0; i < width * height; i++) {

			final int index = rnd.nextInt(width * height);

			final int coinType = cells[index];

			if (cells[index] != EMPTY) {

				final boolean happy = calcFractionOfNeighborsOfSameType(index) >= likenessPreference
						.getValue() / 100d;

				if (!happy && !emptyCells.isEmpty()) {

					final int newIndex = emptyCells.set(
							rnd.nextInt(emptyCells.size()), index);

					cells[index] = EMPTY;
					cells[newIndex] = coinType;
				}
			}
		}
	}

	private void allocateCoins(Random rnd) {

		final int n = width * height;

		final List<Integer> indices = new ArrayList<Integer>(n);

		for (int index = 0; index < n; index++) {
			indices.add(index);
		}

		Collections.shuffle(indices, rnd);

		final int nCoins = (int) (coinDensity.getValue() * n);

		for (int i = 0; i < nCoins; i++) {
			cells[indices.get(i)] = i % 2 == 0 ? PENNY : NICKEL;
		}

		emptyCells = new ArrayList<Integer>(n);
		emptyCells.addAll(indices.subList(nCoins, indices.size()));

		needsCoinAllocation = false;
	}

	private double calcFractionOfNeighborsOfSameType(final int index) {

		final int coinType = cells[index];

		if (coinType == EMPTY) {

			return 1d;
		} else {

			LatticeUtils.calcMooreNeighborhoodIndices(index, width, height,
					cachedNeighborIndices);

			double neighborsOfSameType = 0d;
			double totalNeighbors = 0d;

			for (final int neighborIndex : cachedNeighborIndices) {

				neighborsOfSameType += cells[neighborIndex] == coinType ? 1d
						: 0d;
				totalNeighbors += cells[neighborIndex] != EMPTY ? 1d : 0d;
			}

			return (totalNeighbors > 0d) ? neighborsOfSameType / totalNeighbors
					: 1d;
		}
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

		return Arrays.asList(new CellularAutomataParameter<?>[] {
				likenessPreference, coinDensity });
	}

	private void updateEmptyCellCount(Random rnd) {

		final int n = width * height;

		final int newNumberEmpty = (int) ((1d - coinDensity.getValue()) * n);

		final int oldNumberOfEmpty = emptyCells.size();

		if (newNumberEmpty > oldNumberOfEmpty) {

			for (int i = oldNumberOfEmpty; i < newNumberEmpty; i++) {

				final int index = rnd.nextInt(n);

				if (cells[index] != EMPTY) {

					emptyCells.add(index);
					cells[index] = EMPTY;
				} else {
					i--;
				}
			}
		} else if (newNumberEmpty < oldNumberOfEmpty) {

			for (int i = newNumberEmpty; i < oldNumberOfEmpty; i++) {

				final int coinType = (n - emptyCells.size()) % 2 == 0 ? PENNY
						: NICKEL;
				final int index = emptyCells.remove(rnd.nextInt(emptyCells
						.size()));
				cells[index] = coinType;
			}
		}

		needsEmptyCellUpdate = false;
	}

	@Override
	public void notifyParameterChange(
			final CellularAutomataParameter<?> parameter) {

		if (parameter == coinDensity) {
			needsEmptyCellUpdate = true;
		}
	}
}
