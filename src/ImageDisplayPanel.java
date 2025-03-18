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
    private File[] deltaScene;
    private boolean actSceneToggle = true;
    private boolean deltaSceneToggle = false;
    private final String actSimName;
    private final String bslSimName;
    private final String deltaSimName;
    private int totalCount;
    private int streamCount;
    private BufferedImage imageToDraw;
    private String textOverlay;

    public ImageDisplayPanel(File[] actScene, File[] bslScene, File[] deltaScene, int count, String actSimName, String bslSimName, String deltaSimName){
        this.actScene = actScene;
        this.bslScene = bslScene;
        this.deltaScene = deltaScene;
        this.streamCount = count;
        this.totalCount = actScene.length - 1;
        this.actSimName = actSimName;
        this.bslSimName = bslSimName;
        this.deltaSimName = deltaSimName;
        repaint();
    }

    public void switchVariable(File[] actScene, File[] bslScene, File[] deltaScene, int count){
        this.actScene = actScene;
        this.bslScene = bslScene;
        this.deltaScene = deltaScene;
        if (count != -1) {
            this.totalCount = actScene.length - 1;
            this.streamCount = count;
        }
        repaint();
    }

    public void toggleActive(){
        deltaSceneToggle = false;
        actSceneToggle = true;
        repaint();
    }

    public void toggleBaseline(){
        deltaSceneToggle = false;
        actSceneToggle = false;
        repaint();
    }

    public void toggleDelta(){
        deltaSceneToggle = true;
        actSceneToggle = false;
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

        BufferedImage deltaImage = null;
        try {
            deltaImage = ImageIO.read(deltaScene[this.streamCount]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (deltaSceneToggle){
            this.imageToDraw = deltaImage;
            this.textOverlay = this.deltaSimName;
        }
        else{
            this.imageToDraw = actSceneToggle ? actImage : bslImage;
            this.textOverlay = actSceneToggle ? this.actSimName : this.bslSimName;
        }

        if (this.imageToDraw != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Enable smooth scaling
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            Point textLoc = getLocation();

            int imgWidth = this.imageToDraw.getWidth();
            int imgHeight = this.imageToDraw.getHeight();

            // Maintain aspect ratio
            double widthRatio = (double) panelWidth / imgWidth;
            double heightRatio = (double) panelHeight / imgHeight;
            double scale = Math.min(widthRatio, heightRatio);

            int newWidth = (int) (imgWidth * scale);
            int newHeight = (int) (imgHeight * scale);

            int x = (panelWidth - newWidth) / 2;
            int y = (panelHeight - newHeight) / 2;

            g2d.drawImage(this.imageToDraw, x, y, newWidth, newHeight, this);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString(this.textOverlay, textLoc.x, textLoc.y + 20);
            g2d.dispose();
        }
    }
}
