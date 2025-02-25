import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class SceneLoader {

    public static final String EXTENSION = ".png";
    public static final String SCENES_2D = "2D scenes";
    public static final String[] VARIABLES = {"Inwash", "Pressure", "Total Pressure", "Vorticity", "Velocity Z"};
    public final VariableScenes inwashScenes;
    public final VariableScenes pressureScenes;
    public final VariableScenes cptScenes;
    public final VariableScenes vorticityScenes;
    public final VariableScenes velZScenes;
    public String dir;

    public SceneLoader(String dir){

        File sim = new File(dir + "/" + SCENES_2D + "/");
        File[] allScenes = sim.listFiles();
        String[] allScenesNames = new String[allScenes.length];

        for (int i = 0; i < allScenes.length; i++) {
            allScenesNames[i] = allScenes[i].getName();
        }

        this.inwashScenes = new VariableScenes(getVariableScenes(VARIABLES[0], allScenes));
        this.pressureScenes = new VariableScenes(getVariableScenes(VARIABLES[1], allScenes));
        this.cptScenes = new VariableScenes(getVariableScenes(VARIABLES[2], allScenes));
        this.vorticityScenes = new VariableScenes(getVariableScenes(VARIABLES[3], allScenes));
        this.velZScenes = new VariableScenes(getVariableScenes(VARIABLES[4], allScenes));
    }

    private ArrayList<File> getVariableScenes(String variable, File[] allScenes){
        ArrayList<Integer> index = new ArrayList<>();
        ArrayList<File> variableScenes = new ArrayList<File>();

        for (int i = 0; i < allScenes.length; i++){
            if (allScenes[i].getName().contains(variable)){
                index.add(i);
            }
        }
        for (Integer integer : index) {
            variableScenes.add(allScenes[integer]);
        }
        return variableScenes;
    }
}
