import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.ArrayList;

public class ImageDisplayPanel extends JPanel {
    private final ArrayList<BufferedImage> actScene;
    private final ArrayList<BufferedImage> bslScene;
    private boolean actSceneToggle = true;
    private int streamCount;

    public ImageDisplayPanel(ArrayList<BufferedImage> actScene, ArrayList<BufferedImage> bslScene, int count){
        this.actScene = actScene;
        this.bslScene = bslScene;
        this.streamCount = count;
        repaint();
    }

    public void toggleScene(){
        actSceneToggle = !actSceneToggle;
        repaint();
    }

    public void toggleStreamDown(){
        streamCount++;
    }

    public void toggleStreamUp(){
        streamCount--;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        BufferedImage imageToDraw = actSceneToggle ? actScene.get(1) : bslScene.get(1);
        if (imageToDraw != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Enable smooth scaling
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelWidth = getWidth();
            int panelHeight = getHeight();

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
            g2d.dispose();
        }
    }
}
