import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        String pwd = System.getProperty("user.dir");

        System.out.println("Choose Active Sim:");
        JFileChooser actFC = new JFileChooser(pwd);
        actFC.setDialogTitle("Choose Active Sim CM");
        actFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int actResult = actFC.showOpenDialog(null);

        // Check if user clicked "Choose"
        if (actResult == JFileChooser.APPROVE_OPTION) {
            while (!actFC.getSelectedFile().getName().contains("CM")){
                System.out.println("Please select a colour-mapped file (MUST begin with CM_)");
                actFC.setDialogTitle("Choose Active Sim CM");
                actFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                actFC.showOpenDialog(null);
            }
            System.out.println("Selected file: " + actFC.getSelectedFile().getAbsolutePath());
        } else {
            System.out.println("No Active File Selected. Program Terminated");
            System.exit(0);
        }

        /*
        System.out.println("Choose BSL sim:");
        JFileChooser bslFC = new JFileChooser(pwd);
        bslFC.setDialogTitle("Choose BSL");
        bslFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int bslResult = bslFC.showOpenDialog(null);

        // Check if user clicked "Choose"
        if (bslResult == JFileChooser.APPROVE_OPTION) {
            while (!actFC.getSelectedFile().getName().contains("CM")){
                    System.out.println("Please select a colour-mapped file (MUST begin with CM_)");
                    actFC.setDialogTitle("Choose Active Sim CM");
                    actFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    actFC.showOpenDialog(null);
                }
            System.out.println("Selected file: " + bslFC.getSelectedFile().getAbsolutePath());
        } else {
            System.out.println("No Baseline File Selected. Program Terminated");
            System.exit(0);
        }

        System.out.println("Choose BSL sim:");
        JFileChooser deltaFC = new JFileChooser(pwd);
        deltaFC.setDialogTitle("Choose BSL");
        deltaFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int deltaResult = deltaFC.showOpenDialog(null);

        // Check if user clicked "Choose"
        if (deltaResult == JFileChooser.APPROVE_OPTION) {
            while (!actFC.getSelectedFile().getName().contains("CM")){
                System.out.println("Please select a colour-mapped file (MUST begin with CM_)");
                actFC.setDialogTitle("Choose Active Sim CM");
                actFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                actFC.showOpenDialog(null);
            }
            System.out.println("Selected file: " + deltaFC.getSelectedFile().getAbsolutePath());
        } else {
            System.out.println("No Delta File Selected. Program Terminated");
            System.exit(0);
        }
        */

        JFrame window = new JFrame();
        window.setTitle("SimComparisonTool");
        window.setSize(2000, 1000);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out application by default
        window.getContentPane().setBackground(new Color(25, 25, 25));

        SceneLoader active = new SceneLoader(actFC.getSelectedFile().getAbsolutePath());

        window.setVisible(true);


    }
}