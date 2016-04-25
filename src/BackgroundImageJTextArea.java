package CheckersClient;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tyler on 4/24/2016.
 */
public class BackgroundImageJTextArea extends JTextArea {
    // Resources
    private Image backgroundImage;

    /**
     * Constructor
     */
    public BackgroundImageJTextArea (String startText, Image backgroundImage) {
        super(startText);
        super.setBackground(new Color(0, 0, 0, 0));
        super.setOpaque(false);
        setOpaque(false);

        this.backgroundImage = backgroundImage;
    }

    // Repeat background image.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int imageWidth = backgroundImage.getWidth(this);
        int imageHeight = backgroundImage.getHeight(this);
        g.drawImage(backgroundImage, 0, 0, imageWidth, imageHeight, null);
    }
}
