package jogltoys.ca;

import com.google.common.eventbus.EventBus;
import jogltoys.ca.models.CellularAutomaton;
import jogltoys.ca.models.NullCellularAutomaton;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class ModelManager {

	public enum TimerEvent {
		STARTED, STOPPED;
	}

	public static final Integer[] AVAILABLE_DIMENSIONS = { 4, 8, 16, 32, 64,
			128, 256, 512, 1024 };

	public static final int DEFAULT_WIDTH = AVAILABLE_DIMENSIONS[6];
	public static final int DEFAULT_HEIGHT = AVAILABLE_DIMENSIONS[7];

	public static final long DEFAULT_CYCLE_TIME = 10l;

	private int width = DEFAULT_WIDTH;
	private int height = DEFAULT_HEIGHT;

	private final Map<Class<? extends CellularAutomaton>, CellularAutomaton> models = new LinkedHashMap<Class<? extends CellularAutomaton>, CellularAutomaton>();

	private CellularAutomaton model = new NullCellularAutomaton();

	private long cycleTime = DEFAULT_CYCLE_TIME;

	private final Random rnd = new Random();

	private final EventBus eventBus = new EventBus();

	private Timer timer;

	public ModelManager(
			final Collection<Class<? extends CellularAutomaton>> modelClasses) {

		for (Class<? extends CellularAutomaton> modelClass : modelClasses) {
			try {
				models.put(modelClass, checkNotNull(modelClass).newInstance());
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void start() {
		if (timer == null) {
			timer = new Timer(true);
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					model.iterate(rnd);
				}
			}, 0, cycleTime);
			eventBus.post(TimerEvent.STARTED);
		}
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
			eventBus.post(TimerEvent.STOPPED);
		}
	}

	public void step() {
		stop();
		if (timer == null) {
			model.iterate(rnd);
		}
	}

	public void setModelClass(
			final Class<? extends CellularAutomaton> modelClass) {

		checkArgument(models.containsKey(modelClass));

		stop();

		// Deflate the old model.
		model.init(0, 0);

		// Inflate the new model.
		model = models.get(modelClass);
		model.init(width, height);
	}

	public void reset() {
		stop();
		model.init(width, height);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
		reset();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
		reset();
	}

	public CellularAutomaton getModel() {
		return model;
	}

	public long getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(final long cycleTime) {
		this.cycleTime = cycleTime;
	}

	public CellularAutomaton getModel(
			Class<? extends CellularAutomaton> modelClass) {
		return models.get(modelClass);
	}
}
