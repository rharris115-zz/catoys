package jogltoys.ca.models;

import com.google.common.base.Function;

public enum Model {

	NULL_MODEL(NullCellularAutomaton.class, ""),

	FIRE_MODEL(FireModel.class, "Forest Fire Model"),

	ISING_MODEL(IsingModel.class, "Ising Model"),

	SCHELLING_SEGREGATION_MODEL(SchellingSegregationModel.class,
			"Schelling Segregation Model");

	public static final Function<Model, Class<? extends CellularAutomaton>> GET_MODEL_CLASS = new Function<Model, Class<? extends CellularAutomaton>>() {
		@Override
		public Class<? extends CellularAutomaton> apply(Model model) {
			return model.modelClass;
		}
	};

	private final Class<? extends CellularAutomaton> modelClass;

	private final String modelName;

	private Model(final Class<? extends CellularAutomaton> modelClass,
			final String modelName) {

		this.modelClass = modelClass;
		this.modelName = modelName;
	}

	public Class<? extends CellularAutomaton> getModelClass() {
		return modelClass;
	}

	@Override
	public String toString() {
		return modelName;
	}
}
