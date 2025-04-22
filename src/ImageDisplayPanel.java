import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImageDisplayPanel extends JPanel {
    private File[] actSceneFiles;
    private File[] bslSceneFiles;
    private File[] deltaSceneFiles;

    private BufferedImage currentActImage;
    private BufferedImage currentBslImage;
    private BufferedImage currentDeltaImage;

    private boolean actSceneToggle = true;
    private boolean deltaSceneToggle = false;
    private final String actSimName;
    private final String bslSimName;
    private final String deltaSimName;
    private int totalCount;
    private int streamCount;

    private final Font textFont = new Font("Arial", Font.BOLD, 20);

    public ImageDisplayPanel(File[] actScene, File[] bslScene, File[] deltaScene, int count, String actSimName, String bslSimName, String deltaSimName) {
        this.actSceneFiles = actScene;
        this.bslSceneFiles = bslScene;
        this.deltaSceneFiles = deltaScene;
        this.streamCount = count;
        this.totalCount = (actScene != null && actScene.length > 0) ? actScene.length - 1 : -1;
        this.actSimName = actSimName;
        this.bslSimName = bslSimName;
        this.deltaSimName = deltaSimName;

        loadCurrentImageAsync();
    }

    private void loadCurrentImageAsync() {
        if (totalCount < 0 || streamCount < 0 || streamCount > totalCount) {
            currentActImage = null;
            currentBslImage = null;
            currentDeltaImage = null;
            System.err.println("Warning: Invalid streamCount or file arrays empty/null.");
            repaint();
            return;
        }

        File actFile = actSceneFiles[streamCount];
        File bslFile = bslSceneFiles[streamCount];
        File deltaFile = deltaSceneFiles[streamCount];

        new SwingWorker<BufferedImage[], Void>() {
            @Override
            protected BufferedImage[] doInBackground() {
                BufferedImage act = loadImageFromFile(actFile);
                BufferedImage bsl = loadImageFromFile(bslFile);
                BufferedImage delta = loadImageFromFile(deltaFile);
                return new BufferedImage[]{act, bsl, delta};
            }

            @Override
            protected void done() {
                try {
                    BufferedImage[] result = get();
                    currentActImage = result[0];
                    currentBslImage = result[1];
                    currentDeltaImage = result[2];
                } catch (Exception e) {
                    System.err.println("Error retrieving images for Multithreading.");
                    e.printStackTrace();
                    currentActImage = null;
                    currentBslImage = null;
                    currentDeltaImage = null;
                }
                repaint();
            }
        }.execute();
    }

    private BufferedImage loadImageFromFile(File file) {
        if (file == null || !file.exists()) {
            System.err.println("Error: Image file does not exist: " + (file != null ? file.getPath() : "null"));
            return null;
        }
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error loading image: " + file.getPath());
            e.printStackTrace();
            return null;
        }
    }

    public void switchVariable(File[] actScene, File[] bslScene, File[] deltaScene, int count) {
        this.actSceneFiles = actScene;
        this.bslSceneFiles = bslScene;
        this.deltaSceneFiles = deltaScene;
        this.totalCount = (actScene != null && actScene.length > 0) ? actScene.length - 1 : -1;

        if (count != -1) {
            if (this.totalCount >= 0) {
                this.streamCount = Math.max(0, Math.min(count, this.totalCount));
            } else {
                this.streamCount = -1;
            }
        } else {
            if (this.totalCount >= 0) {
                this.streamCount = Math.max(0, Math.min(this.streamCount, this.totalCount));
            } else {
                this.streamCount = -1;
            }
        }

        loadCurrentImageAsync(); // Async load
    }

    public void toggleActive() {
        deltaSceneToggle = false;
        actSceneToggle = true;
        repaint();
    }

    public void toggleBaseline() {
        deltaSceneToggle = false;
        actSceneToggle = false;
        repaint();
    }

    public void toggleDelta() {
        deltaSceneToggle = true;
        actSceneToggle = false;
        repaint();
    }

    public void toggleStreamDown() {
        if (totalCount < 0) return;
        this.streamCount = (this.streamCount == this.totalCount) ? 0 : this.streamCount + 1;
        loadCurrentImageAsync(); // Async load
    }

    public void toggleStreamUp() {
        if (totalCount < 0) return;
        this.streamCount = (this.streamCount == 0) ? this.totalCount : this.streamCount - 1;
        loadCurrentImageAsync(); // Async load
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage imageToDraw = null;
        String textOverlay = "";
        String sceneSlice = "";
        File currentFile = null;

        if (deltaSceneToggle) {
            imageToDraw = currentDeltaImage;
            textOverlay = this.deltaSimName;
            currentFile = (deltaSceneFiles != null && streamCount >= 0 && streamCount < deltaSceneFiles.length) ? deltaSceneFiles[this.streamCount] : null;
        } else {
            if (actSceneToggle) {
                imageToDraw = currentActImage;
                textOverlay = this.actSimName;
                currentFile = (actSceneFiles != null && streamCount >= 0 && streamCount < actSceneFiles.length) ? actSceneFiles[this.streamCount] : null;
            } else {
                imageToDraw = currentBslImage;
                textOverlay = this.bslSimName;
                currentFile = (bslSceneFiles != null && streamCount >= 0 && streamCount < bslSceneFiles.length) ? bslSceneFiles[this.streamCount] : null;
            }
        }

        if (currentFile != null) {
            sceneSlice = currentFile.getName();
            int dotIndex = sceneSlice.lastIndexOf('.');
            if (dotIndex > 0) {
                sceneSlice = sceneSlice.substring(0, dotIndex);
            }
        } else {
            sceneSlice = "N/A";
        }

        if (imageToDraw != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imgWidth = imageToDraw.getWidth();
            int imgHeight = imageToDraw.getHeight();

            double scale = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
            int newWidth = (int) (imgWidth * scale);
            int newHeight = (int) (imgHeight * scale);
            int x = (panelWidth - newWidth) / 2;
            int y = (panelHeight - newHeight) / 2;

            g2d.drawImage(imageToDraw, x, y, newWidth, newHeight, this);
            g2d.setColor(Color.BLACK);
            g2d.setFont(textFont);

            int textX = 5;
            g2d.drawString(textOverlay, textX, 20);
            g2d.drawString(sceneSlice, textX, 45);

            g2d.dispose();
        } else {
            g.setColor(Color.RED);
            g.setFont(textFont);
            String errorMsg = "Image unavailable";
            FontMetrics fm = g.getFontMetrics();
            int msgWidth = fm.stringWidth(errorMsg);
            int msgHeight = fm.getAscent();
            g.drawString(errorMsg, (getWidth() - msgWidth) / 2, (getHeight() - msgHeight) / 2 + fm.getAscent());
        }
    }
}
