import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class VariableScenes {
    private final ArrayList<File> sceneFiles;

    public VariableScenes(ArrayList<File> sceneFiles) {
        this.sceneFiles = sceneFiles;
    }

    public File[] getImages(String view) {
        ArrayList<File> images = new ArrayList<>();
        String dir = "";

        for (File file : this.sceneFiles) {
            if (file.getName().contains(view)) {
                dir = file.getAbsolutePath();
            }
        }

        File imageDir = new File(dir + File.separator);
        File[] imageFiles = imageDir.listFiles();
        assert imageFiles != null;
        Arrays.sort(imageFiles, Comparator.comparing(File::getName));

        return imageFiles;
    }

    public ArrayList<File> getSceneFiles() {
        return sceneFiles;
    }
}

