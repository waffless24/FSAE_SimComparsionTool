import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VariableScenes {
    private final ArrayList<File> sceneFiles;

    public VariableScenes(ArrayList<File> sceneFiles) {
        this.sceneFiles = sceneFiles;
    }

    public ArrayList<BufferedImage> getImages(String view) {
        ArrayList<BufferedImage> images = new ArrayList<>();
        String dir = "";

        for (File file : this.sceneFiles) {
            if (file.getName().contains(view)) {
                dir = file.getAbsolutePath();
            }
        }


        File imageDir = new File(dir + "/");
        File[] imageFiles = imageDir.listFiles();

        assert imageFiles != null;

        for (File image : imageFiles) {
            try {
                if (image.isFile()){
                    BufferedImage img = ImageIO.read(image);
                    if (img != null) {
                        images.add(img);
                    }
                    else {
                        System.err.println("Unsupported image format: " + image.getName());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading image: " + image.getName());
            }
        }
        return images;
    }

    public ArrayList<File> getSceneFiles() {
        return sceneFiles;
    }
}

