package jogltoys.ca;

import javax.swing.*;
import java.awt.*;

public enum ModelIcons implements Icon {

    LIGHTNING(new ImageIcon(ModelIcons.class.getClassLoader().getResource(
            "icons/lightningbolt.gif"))),

    FIRE(new ImageIcon(ModelIcons.class.getClassLoader().getResource(
            "icons/flame.gif"))),

    GROWTH(new ImageIcon(ModelIcons.class.getClassLoader().getResource(
            "icons/leaf.gif"))),

    TEMPERATURE(new ImageIcon(ModelIcons.class.getClassLoader().getResource(
            "icons/thermometer.gif"))),

    MAGNET(new ImageIcon(ModelIcons.class.getClassLoader().getResource(
            "icons/magnet.gif"))),

    COIN(new ImageIcon(ModelIcons.class.getClassLoader().getResource(
            "icons/coins.gif")));

    private Icon icon;

    private ModelIcons(final Icon icon) {
        this.icon = icon;
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
