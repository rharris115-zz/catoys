package jogltoys.ca.models.params;

import jogltoys.ca.models.CellularAutomaton;

import javax.swing.*;

public class CellularAutomataParameter<T extends Number> {

	private final String name;

	private final Icon icon;

	private final CellularAutomaton model;

	private T value;

	public CellularAutomataParameter(final String name, final Icon icon,
                                     final CellularAutomaton model, final T value) {

		this.name = name;
		this.icon = icon;
		this.model = model;
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(final T value) {

		if (this.value != value) {

			this.value = value;
			model.notifyParameterChange(this);
		}
	}

	public String getName() {
		return name;
	}

	public Icon getIcon() {
		return icon;
	}
}
