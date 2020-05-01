package jogltoys.ca.models.params;

import jogltoys.ca.models.CellularAutomaton;

import javax.swing.*;

public class DoubleCellularAutomataParameter extends
		CellularAutomataParameter<Double> {

	private final double min, max;

	private final int sliderMin, sliderMax;

	public DoubleCellularAutomataParameter(final String name, final Icon icon,
			final CellularAutomaton model, final double value,
			final double min, final double max) {

		this(name, icon, model, value, min, max, 0, 100);
	}

	public DoubleCellularAutomataParameter(final String name, final Icon icon,
			final CellularAutomaton model, final double value,
			final double min, final double max, final int sliderMin,
			final int sliderMax) {

		super(name, icon, model, value);
		this.min = min;
		this.max = max;
		this.sliderMin = sliderMin;
		this.sliderMax = sliderMax;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public int calcSliderValue() {
		return sliderMin
				+ (int) ((super.getValue() - min) / (max - min) * (sliderMax - sliderMin));
	}

	public double calcParameterValue(final int sliderValue) {
		return min + (double) (sliderValue - sliderMin)
				/ (double) (sliderMax - sliderMin) * (max - min);
	}

	public int getSliderMin() {
		return sliderMin;
	}

	public int getSliderMax() {
		return sliderMax;
	}

}
