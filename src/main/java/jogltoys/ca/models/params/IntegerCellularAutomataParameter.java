package jogltoys.ca.models.params;

import jogltoys.ca.models.CellularAutomaton;

import javax.swing.*;

public class IntegerCellularAutomataParameter extends
		CellularAutomataParameter<Integer> {

	private final int min, max;

	public IntegerCellularAutomataParameter(final String name, final Icon icon,
			final CellularAutomaton model, final int value, final int min,
			final int max) {

		super(name, icon, model, value);
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
