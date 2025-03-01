import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class ImageDisplayPanel extends JPanel {
    private File[] actScene;
    private File[] bslScene;
    private boolean actSceneToggle = true;
    private final String actSimName;
    private final String bslSimName;
    private int totalCount;
    private int streamCount;

    public ImageDisplayPanel(File[] actScene, File[] bslScene, int count, String actSimName, String bslSimName){
        this.actScene = actScene;
        this.bslScene = bslScene;
        this.streamCount = count;
        this.totalCount = actScene.length - 1;
        this.actSimName = actSimName;
        this.bslSimName = bslSimName;
        repaint();
    }

    public void switchVariable(File[] actScene, File[] bslScene, int count){
        this.actScene = actScene;
        this.bslScene = bslScene;
        if (count != -1) {
            this.totalCount = actScene.length - 1;
            this.streamCount = count;
        }
        repaint();
    }

    public void toggleScene(){
        actSceneToggle = !actSceneToggle;
        repaint();
    }

    public void toggleStreamDown(){
        if (this.streamCount == this.totalCount) {
            this.streamCount = 0;
        }
        else {
            this.streamCount++;
        }
        repaint();
    }

    public void toggleStreamUp(){
        if (this.streamCount == 0) {
            this.streamCount = this.totalCount;
        }
        else {
            this.streamCount--;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        BufferedImage actImage = null;
        try {
            actImage = ImageIO.read(actScene[this.streamCount]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage bslImage = null;
        try {
            bslImage = ImageIO.read(bslScene[this.streamCount]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedImage imageToDraw = actSceneToggle ? actImage : bslImage;
        String textOverlay = actSceneToggle ? this.actSimName : this.bslSimName;
        if (imageToDraw != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Enable smooth scaling
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            Point textLoc = getLocation();

            int imgWidth = imageToDraw.getWidth();
            int imgHeight = imageToDraw.getHeight();

            // Maintain aspect ratio
            double widthRatio = (double) panelWidth / imgWidth;
            double heightRatio = (double) panelHeight / imgHeight;
            double scale = Math.min(widthRatio, heightRatio);

            int newWidth = (int) (imgWidth * scale);
            int newHeight = (int) (imgHeight * scale);

            int x = (panelWidth - newWidth) / 2;
            int y = (panelHeight - newHeight) / 2;

            g2d.drawImage(imageToDraw, x, y, newWidth, newHeight, this);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString(textOverlay, textLoc.x, textLoc.y + 20);
            g2d.dispose();
        }
    }
}
