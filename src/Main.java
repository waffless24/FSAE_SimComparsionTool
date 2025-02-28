import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {

        final String[] VIEWS = {"AftFore", "TopBottom", "Profile"};
        final String[] VARIABLES = {"Inwash", "Pressure", "Total Pressure", "Vorticity", "Velocity Z"};
        final boolean[] toggleFlag = {false}; //flag for toggling since swing repeatedly sends signals while a key is pressed
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


        System.out.println("Choose BSL sim:");
        JFileChooser bslFC = new JFileChooser(pwd);
        bslFC.setDialogTitle("Choose BSL sim");
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

        /*
        System.out.println("Choose Delta sim:");
        JFileChooser deltaFC = new JFileChooser(pwd);
        deltaFC.setDialogTitle("Choose Delta sim");
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

        SceneLoader act = new SceneLoader(actFC.getSelectedFile().getAbsolutePath());
        SceneLoader bsl = new SceneLoader(bslFC.getSelectedFile().getAbsolutePath());
        ImageDisplayPanel displayer = new ImageDisplayPanel(act.cptScenes.getImages(VIEWS[2]), bsl.cptScenes.getImages(VIEWS[2]), 0);

        // Toggling mechanism
        window.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                // Moving downstream with right arrow
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    displayer.toggleStreamDown();
                }
                // Moving upstream with left arrown
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    displayer.toggleStreamUp();
                }

                // Toggling between active and basline
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !toggleFlag[0]) {
                    displayer.toggleScene();
                    toggleFlag[0] = true;
                }
            }
            public void keyReleased(KeyEvent e){
                // Toggling between active and basline
                if (e.getKeyCode() == KeyEvent.VK_SPACE && toggleFlag[0]) {
                    displayer.toggleScene();
                    toggleFlag[0] = false;
                }
            }
        });

        VariableController menu = new VariableController(VARIABLES, VIEWS);

        class PopupListener extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        MouseListener popupListener = new PopupListener();
        displayer.addMouseListener(popupListener);
        displayer.add(menu);
        window.add(displayer);
        window.setVisible(true);

    }
}