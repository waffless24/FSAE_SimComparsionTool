import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        /*
        String pwd = System.getProperty("user.dir");

        System.out.println("Choose Active Sim:");
        JFileChooser actFC = new JFileChooser(pwd);
        actFC.setDialogTitle("Choose Active Sim CM");
        actFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int actResult = actFC.showOpenDialog(null);

        if (actResult == JFileChooser.APPROVE_OPTION) { // Check if user clicked "Open"
            System.out.println("Selected file: " + actFC.getSelectedFile().getAbsolutePath());
        } else {
            System.out.println("No Active File Selected. Program Terminated");
            System.exit(0);
        }

        System.out.println("Choose BSL sim:");
        JFileChooser bslFC = new JFileChooser(pwd);
        bslFC.setDialogTitle("Choose BSL");
        bslFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int bslResult = bslFC.showOpenDialog(null);

        if (bslResult == JFileChooser.APPROVE_OPTION) { // Check if user clicked "Open"
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

        if (deltaResult == JFileChooser.APPROVE_OPTION) { // Check if user clicked "Open"
            System.out.println("Selected file: " + deltaFC.getSelectedFile().getAbsolutePath());
        } else {
            System.out.println("No Delta File Selected. Program Terminated");
            System.exit(0);
        }
        */

        JFrame window = new JFrame();
        window.setTitle("SimComparisonTool");
        window.setSize(1920, 1080);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out application by default
        window.getContentPane().setBackground(new Color(25, 25, 25));

        customPanel topPanel = new customPanel();
        customPanel bottomPanel = new customPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        Border border = BorderFactory.createLineBorder(new Color(15, 15, 15), 5);
        splitPane.setBorder(border);
        splitPane.setResizeWeight(0.5); // Split the window into two halves
        splitPane.setDividerSize(0); // Sets an adequate divider size

        window.add(splitPane, BorderLayout.CENTER);

        window.setVisible(true);

    }
}