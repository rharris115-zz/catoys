package jogltoys.ca;

import com.google.common.collect.FluentIterable;
import com.google.common.eventbus.Subscribe;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import jogltoys.ca.CAScene.Projection;
import jogltoys.ca.ModelManager.TimerEvent;
import jogltoys.ca.models.CellularAutomaton;
import jogltoys.ca.models.Model;
import jogltoys.ca.models.params.CellularAutomataParameter;
import jogltoys.ca.models.params.DoubleCellularAutomataParameter;
import jogltoys.ca.models.params.IntegerCellularAutomataParameter;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map.Entry;

public class CAFrame extends JFrame {

	private static final long serialVersionUID = -913829868679881344L;

	private final ModelManager modelManager = new ModelManager(FluentIterable
			.<Model> from(Arrays.asList(Model.values()))
			.transform(Model.GET_MODEL_CLASS).toSet());

	{
		modelManager.getEventBus().register(new Object() {

			@Subscribe
			public void handle(final TimerEvent e) {

				switch (e) {

				case STARTED:
					startButton.doClick();
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
					break;

				case STOPPED:
					stopButton.doClick();
					startButton.setEnabled(true);
					stopButton.setEnabled(false);
					break;

				default:
					break;
				}
			}
		});
	}

	private final CAScene modelDisplay = new CAScene(modelManager);

	private final JComboBox<Model> modelChooser = new JComboBox<Model>(
			Model.values()) {

		private static final long serialVersionUID = -3534855659404367248L;

		@Override
		public Dimension getMaximumSize() {
			return super.getMinimumSize();
		}
	};

	{

		modelChooser.setSelectedIndex(-1);
		modelChooser.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					Model model = (Model) e.getItem();
					modelDisplay.getModelManager().setModelClass(
							model.getModelClass());
					((CardLayout) sliderPane.getLayout()).show(sliderPane,
							model.name());
				}
			}
		});

	}

	private final JComboBox<Integer> widthChooser = new JComboBox<Integer>(
			ModelManager.AVAILABLE_DIMENSIONS) {

		private static final long serialVersionUID = 971084966447635624L;

		@Override
		public Dimension getMaximumSize() {
			return super.getMinimumSize();
		}
	};

	{
		widthChooser.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					modelDisplay.getModelManager().setWidth(
							((Number) widthChooser.getSelectedItem())
									.intValue());
				}
			}
		});

		widthChooser.setSelectedItem(new Integer(ModelManager.DEFAULT_WIDTH));

	}

	private final JComboBox<Integer> heightChooser = new JComboBox<Integer>(
			ModelManager.AVAILABLE_DIMENSIONS) {

		private static final long serialVersionUID = -2044211939370824197L;

		@Override
		public Dimension getMaximumSize() {
			return super.getMinimumSize();
		}
	};

	{
		heightChooser.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					modelDisplay.getModelManager().setHeight(
							((Number) heightChooser.getSelectedItem())
									.intValue());
				}
			}
		});

		heightChooser.setSelectedItem(new Integer(ModelManager.DEFAULT_HEIGHT));

	}

	private final JButton modelResetter = new JButton(new AbstractAction(
			"Reset Model") {

		private static final long serialVersionUID = 3040788999737908779L;

		@Override
		public void actionPerformed(final ActionEvent e) {
			modelDisplay.getModelManager().reset();
		}

	});

	{
		modelResetter.setAlignmentX(CENTER_ALIGNMENT);
	}

	private final JPanel modelChooserPane = new JPanel();

	{
		modelChooserPane.setLayout(new BoxLayout(modelChooserPane,
				BoxLayout.Y_AXIS));
		modelChooserPane.setBorder(BorderFactory
				.createTitledBorder("Model Chooser"));

		modelChooserPane.add(modelChooser);
		modelChooserPane.add(Box.createVerticalStrut(20));

		final Box widthBox = Box.createVerticalBox();
		widthBox.add(new JLabel("Width"));
		widthBox.add(widthChooser);

		final Box heightBox = Box.createVerticalBox();
		heightBox.add(new JLabel("Height"));
		heightBox.add(heightChooser);

		final Box dimensionBox = Box.createHorizontalBox();
		dimensionBox.add(widthBox);
		dimensionBox.add(heightBox);

		modelChooserPane.add(dimensionBox);
		modelChooserPane.add(Box.createVerticalStrut(20));
		modelChooserPane.add(modelResetter);
	}

	private final EnumMap<CAScene.Projection, JRadioButton> projectionButtons = new EnumMap<CAScene.Projection, JRadioButton>(
			CAScene.Projection.class);

	{

		for (final Projection projection : CAScene.Projection.values()) {

			projectionButtons.put(projection, new JRadioButton(
					new AbstractAction(projection.getDescription()) {

						private static final long serialVersionUID = 1L;

						@Override
						public void actionPerformed(final ActionEvent e) {

							if (!(modelDisplay.getProjection() == projection)) {
								modelDisplay.setProjection(projection);
							}
						}
					}));
		}
	}

	private final ButtonGroup projectionGroup = new ButtonGroup();
	{

		for (final Entry<Projection, JRadioButton> e : projectionButtons
				.entrySet()) {

			projectionGroup.add(e.getValue());
		}

		projectionGroup.setSelected(
				projectionButtons.get(CAScene.DEFAULT_PROJECTION).getModel(),
				true);
	}

	private final JPanel projectionPane = new JPanel();

	{
		projectionPane
				.setLayout(new BoxLayout(projectionPane, BoxLayout.X_AXIS));
		projectionPane.setBorder(BorderFactory
				.createTitledBorder("View Projection"));

		projectionPane.add(Box.createHorizontalGlue());

		for (final Entry<Projection, JRadioButton> e : projectionButtons
				.entrySet()) {

			projectionPane.add(e.getValue());
		}

		projectionPane.add(Box.createHorizontalGlue());
	}

	private final JPanel sliderPane = new JPanel(new CardLayout());

	{

		for (Model model : Model.values()) {

			Box sliderBox = Box.createVerticalBox();
			sliderBox.setAlignmentX(CENTER_ALIGNMENT);

			CellularAutomaton modelInstance = modelManager.getModel(model
					.getModelClass());

			for (final CellularAutomataParameter<?> parameter : modelInstance
					.getParameters()) {

				final JLabel label = new JLabel(parameter.getName(),
						parameter.getIcon(), SwingConstants.CENTER);

				label.setAlignmentX(LEFT_ALIGNMENT);

				final JSlider slider = new JSlider();
				slider.setAlignmentX(LEFT_ALIGNMENT);

				if (parameter instanceof DoubleCellularAutomataParameter) {

					final DoubleCellularAutomataParameter doubleParamater = (DoubleCellularAutomataParameter) parameter;

					slider.setMinimum(doubleParamater.getSliderMin());
					slider.setMaximum(doubleParamater.getSliderMax());

					slider.setValue(doubleParamater.calcSliderValue());

					slider.addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(final ChangeEvent e) {

							doubleParamater.setValue(doubleParamater
									.calcParameterValue(slider.getValue()));
						}
					});
				} else if (parameter instanceof IntegerCellularAutomataParameter) {

					final IntegerCellularAutomataParameter integerParamater = (IntegerCellularAutomataParameter) parameter;

					slider.setMinimum(integerParamater.getMin());
					slider.setMaximum(integerParamater.getMax());

					slider.setValue(integerParamater.getValue());

					slider.addChangeListener(new ChangeListener() {

						@Override
						public void stateChanged(final ChangeEvent e) {

							integerParamater.setValue(slider.getValue());
						}
					});
				}

				sliderBox.add(label);
				sliderBox.add(slider);
			}

			sliderPane.add(sliderBox, model.name());
		}
	}

	private final JToggleButton stopButton = new JToggleButton(
			new AbstractAction(new String(), VCRIcons.PAUSE) {

				private static final long serialVersionUID = -4928364378648075754L;

				@Override
				public void actionPerformed(final ActionEvent e) {
					modelManager.stop();
				}
			});

	private final JToggleButton stepButton = new JToggleButton(
			new AbstractAction(new String(), VCRIcons.NUDGE_FORWARD) {

				private static final long serialVersionUID = -8310462941588989017L;

				@Override
				public void actionPerformed(final ActionEvent e) {
					modelManager.step();
				}

			});

	private final JToggleButton startButton = new JToggleButton(
			new AbstractAction(new String(), VCRIcons.PLAY) {

				private static final long serialVersionUID = -8310462941588989017L;

				@Override
				public void actionPerformed(final ActionEvent e) {
					modelManager.start();
				}

			});

	{

		final ButtonGroup animatorGroup = new ButtonGroup();
		animatorGroup.add(startButton);
		animatorGroup.add(stepButton);
		animatorGroup.add(stopButton);
		animatorGroup.setSelected(stopButton.getModel(), true);

		stopButton.setEnabled(false);
		stepButton.setEnabled(true);
		startButton.setEnabled(true);

	}

	private final JPanel vcrBox = new JPanel();
	{
		vcrBox.setLayout(new BoxLayout(vcrBox, BoxLayout.X_AXIS));
		vcrBox.setBorder(BorderFactory.createTitledBorder("Time Controls"));
		vcrBox.add(Box.createHorizontalGlue());
		vcrBox.add(stopButton);
		vcrBox.add(stepButton);
		vcrBox.add(startButton);
		vcrBox.add(Box.createHorizontalGlue());
	}

	{
		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(modelChooserPane);
		getContentPane().add(projectionPane);
		getContentPane().add(sliderPane);
		getContentPane().add(vcrBox);
		getContentPane().add(Box.createVerticalGlue());

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(final String[] args) {

		GLProfile.initSingleton();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		final CAFrame frame = new CAFrame();
		frame.pack();
		frame.setVisible(true);

		frame.createFrame().setVisible(true);
	}

	private GLWindow createGLWindow() {

		final GLWindow window = GLWindow.create(new GLCapabilities(GLProfile
				.getDefault()));

		int width = 1920 / 2;
		int height = 1080 / 2;

		float interOcularDistance = 0.08f;
		float yFov = 60f;

		window.setUndecorated(true);
		window.setPosition(100, 100);
		window.setSize(width, height);
		modelDisplay.register(window,
				Eye.LEFT.createConfigurator(yFov, interOcularDistance),
				Eye.RIGHT.createConfigurator(yFov, interOcularDistance));

		window.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					window.setFullscreen(!window.isFullscreen());
				}
			}
		});

		return window;
	}

	@SuppressWarnings("unused")
	private GLWindow createFrame() {

		final GLWindow window = GLWindow.create(new GLCapabilities(
				GLProfile.getDefault()));

		int width = 1920;
		int height = 1080;
		float interOcularDistance = 0.08f;
		float yFov = 45f;

		modelDisplay.register(window,
				Eye.CENTRE.createConfigurator(yFov, interOcularDistance));

		window.setSize(width, height);

		window.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					window.setFullscreen(!window.isFullscreen());
					// final GraphicsDevice device = window
					// .getGraphicsConfiguration().getDevice();
					// final boolean isFullScreen = device.getFullScreenWindow()
					// == frame;
					// device.setFullScreenWindow(isFullScreen ? null : frame);
				}
			}
		});

		return window;
	}
}
