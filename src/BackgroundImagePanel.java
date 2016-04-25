package CheckersClient;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tyler on 8/15/2015.
 */
public class BackgroundImagePanel extends JPanel {

    // Resources
    private Image backgroundImage;

    /**
     * Constructor
     */
    public BackgroundImagePanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    // Repeat background image.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int imageWidth = backgroundImage.getWidth(this);
        int imageHeight = backgroundImage.getHeight(this);
        if (imageWidth > 0 && imageHeight > 0) {
            for (int x = 0; x < this.getWidth(); x += imageWidth) {
                for (int y = 0; y < this.getHeight(); y += imageHeight) {
                    g.drawImage(backgroundImage, x, y, imageWidth, imageHeight, this);
                }
            }
        }
    }
}
