package jogltoys.ca.models;

public class LatticeUtils {

	private LatticeUtils() {
	}

	public static final boolean areWidthAndHeightPowersOfTwo(final int width,
			final int height) {
		return (width & (width - 1)) == 0 && (height & (height - 1)) == 0;
	}

	public static final void calcVonNeumanaNeighborhoodIndices(final int index,
			final int width, final int height, final int[] result) {

		if (result.length == 4) {

			final int x = calcX(index, width);
			final int y = calcY(index, width);

			final int xp = (width + x + 1) % width;
			final int xm = (width + x - 1) % width;
			final int yp = (height + y + 1) % height;
			final int ym = (height + y - 1) % height;

			result[0] = calcIndex(xp, y, width);
			result[1] = calcIndex(xm, y, width);
			result[2] = calcIndex(x, yp, width);
			result[3] = calcIndex(x, ym, width);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static final void calcMooreNeighborhoodIndices(final int index,
			final int width, final int height, final int[] result) {

		if (result.length == 8) {

			final int x = calcX(index, width);
			final int y = calcY(index, width);

			final int xp = (width + x + 1) % width;
			final int xm = (width + x - 1) % width;
			final int yp = (height + y + 1) % height;
			final int ym = (height + y - 1) % height;

			result[0] = calcIndex(xp, y, width);
			result[1] = calcIndex(xp, ym, width);
			result[2] = calcIndex(x, ym, width);
			result[3] = calcIndex(xm, ym, width);
			result[4] = calcIndex(xm, y, width);
			result[5] = calcIndex(xm, yp, width);
			result[6] = calcIndex(x, yp, width);
			result[7] = calcIndex(xp, yp, width);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static final int calcIndex(final int i, final int j, final int width) {
		return j * width + i;
	}

	public static final int calcX(final int index, final int width) {
		return index % width;
	}

	public static final int calcY(final int index, final int width) {
		return index / width;
	}
}
