package jogltoys.ca;

import javax.swing.*;
import java.awt.*;

public enum VCRIcons implements Icon {

	REWIND(new Icon() {

		private final Polygon poly1 = new Polygon(new int[] { WIDTH / 2,
				WIDTH / 2, 0 }, new int[] { 0, HEIGHT, HEIGHT / 2 }, 3);
		private final Polygon poly2 = new Polygon(new int[] { WIDTH, WIDTH,
				WIDTH / 2 }, new int[] { 0, HEIGHT, HEIGHT / 2 }, 3);

		@Override
		public int getIconWidth() {
			return WIDTH;
		}

		@Override
		public int getIconHeight() {
			return HEIGHT;
		}

		@Override
		public void paintIcon(final Component c, final Graphics g, final int x,
				final int y) {

			g.setColor(resolveColor(c));

			g.translate(x, y);
			g.fillPolygon(poly1);
			g.fillPolygon(poly2);
			g.translate(-x, -y);
		}
	}),

	REVERSE(new Icon() {

		private final Polygon poly = new Polygon(new int[] { WIDTH / 2, 0,
				WIDTH / 2 }, new int[] { 0, HEIGHT / 2, HEIGHT }, 3);

		@Override
		public int getIconWidth() {
			return WIDTH / 2;
		}

		@Override
		public int getIconHeight() {
			return HEIGHT;
		}

		@Override
		public void paintIcon(final Component c, final Graphics g, final int x,
				final int y) {

			g.setColor(resolveColor(c));

			g.translate(x, y);
			g.fillPolygon(poly);
			g.translate(-x, -y);
		}
	}),

	NUDGE_BACKWARDS(new Icon() {

		private final Polygon poly = new Polygon(new int[] { WIDTH / 2,
				WIDTH / 2, 0 }, new int[] { 0, HEIGHT, HEIGHT / 2 }, 3);

		@Override
		public int getIconWidth() {
			return WIDTH;
		}

		@Override
		public int getIconHeight() {
			return HEIGHT;
		}

		@Override
		public void paintIcon(final Component c, final Graphics g, final int x,
				final int y) {

			g.setColor(resolveColor(c));

			g.translate(x, y);
			g.fillPolygon(poly);
			g.translate(-x, -y);
			g.fillRect(x + 2 * WIDTH / 3, y, WIDTH / 3, WIDTH);
		}
	}),

	PAUSE(new Icon() {

		@Override
		public int getIconWidth() {
			return WIDTH;
		}

		@Override
		public int getIconHeight() {
			return HEIGHT;
		}

		@Override
		public void paintIcon(final Component c, final Graphics g, final int x,
				final int y) {

			g.setColor(resolveColor(c));

			g.fillRect(x, y, WIDTH / 3, HEIGHT);
			g.fillRect(x + 2 * WIDTH / 3, y, WIDTH / 3, HEIGHT);
		}
	}),

	NUDGE_FORWARD(new Icon() {

		private final Polygon poly = new Polygon(new int[] { WIDTH / 2, WIDTH,
				WIDTH / 2 }, new int[] { 0, HEIGHT / 2, HEIGHT }, 3);

		@Override
		public int getIconWidth() {
			return WIDTH;
		}

		@Override
		public int getIconHeight() {
			return HEIGHT;
		}

		@Override
		public void paintIcon(final Component c, final Graphics g, final int x,
				final int y) {

			g.setColor(resolveColor(c));

			g.translate(x, y);
			g.fillPolygon(poly);
			g.translate(-x, -y);
			g.fillRect(x, y, WIDTH / 3, HEIGHT);
		}
	}),

	PLAY(new Icon() {

		private final Polygon poly = new Polygon(new int[] { 0, WIDTH / 2, 0 },
				new int[] { 0, HEIGHT / 2, HEIGHT }, 3);

		@Override
		public int getIconWidth() {
			return WIDTH / 2;
		}

		@Override
		public int getIconHeight() {
			return HEIGHT;
		}

		@Override
		public void paintIcon(final Component c, final Graphics g, final int x,
				final int y) {

			g.setColor(resolveColor(c));

			g.translate(x, y);
			g.fillPolygon(poly);
			g.translate(-x, -y);
		}
	}),

	FAST_FORWARD(new Icon() {

		private final Polygon poly1 = new Polygon(
				new int[] { 0, WIDTH / 2, 0 }, new int[] { 0, HEIGHT / 2,
						HEIGHT }, 3);
		private final Polygon poly2 = new Polygon(new int[] { WIDTH / 2, WIDTH,
				WIDTH / 2 }, new int[] { 0, HEIGHT / 2, HEIGHT }, 3);

		@Override
		public int getIconWidth() {
			return WIDTH;
		}

		@Override
		public int getIconHeight() {
			return HEIGHT;
		}

		@Override
		public void paintIcon(final Component c, final Graphics g, final int x,
				final int y) {

			g.setColor(resolveColor(c));

			g.translate(x, y);
			g.fillPolygon(poly1);
			g.fillPolygon(poly2);
			g.translate(-x, -y);
		}
	});

	public static final int WIDTH = 20;

	public static final int HEIGHT = 20;

	private final Icon icon;

	private VCRIcons(final Icon icon) {
		this.icon = icon;
	}

	private static final Color resolveColor(final Component c) {

		final Color color = c.getForeground();

		if (c.isEnabled()) {
			return color;
		} else {
			return Color.GRAY;
		}
	}

	@Override
	public void paintIcon(final Component c, final Graphics g, final int x,
			final int y) {
		icon.paintIcon(c, g, x, y);
	}

	@Override
	public int getIconWidth() {
		return icon.getIconWidth();
	}

	@Override
	public int getIconHeight() {
		return icon.getIconHeight();
	}
}