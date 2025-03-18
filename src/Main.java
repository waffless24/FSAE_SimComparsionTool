import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {

        final String[] VIEWS = {"AftFore", "TopBottom", "Profile"};
        final String[] VARIABLES = {"Inwash", "Pressure", "Total Pressure", "Vorticity", "Velocity Z"};
        final boolean[] toggleFlag = {false}; //flag for toggling since swing repeatedly sends signals while a key is pressed
        final boolean[] deltaFlag = {false}; //flag for toggling delta; same reason
        String pwd = System.getProperty("user.dir");

        System.out.println("Choose Active Sim:");
        JFileChooser actFC = new JFileChooser(pwd);
        actFC.setDialogTitle("Choose Active Sim CM");
        actFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int actResult = actFC.showOpenDialog(null);

        // Check if user clicked "Choose"
        if (actResult == JFileChooser.APPROVE_OPTION) {
            while (!actFC.getSelectedFile().getName().contains("CM")) {
                System.out.println("Please select a colour-mapped file (MUST begin with CM_)");
                actFC.setDialogTitle("Choose Active Sim CM");
                actFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                actFC.showOpenDialog(null);
            }
            System.out.println("Selected file: " + actFC.getSelectedFile().getAbsolutePath());
        }
        else if (actResult == JFileChooser.CANCEL_OPTION) {
            System.out.println("Program Terminated by User");
            System.exit(0);
        }
        else {
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
            while (!actFC.getSelectedFile().getName().contains("CM")) {
                System.out.println("Please select a colour-mapped file (MUST begin with CM_)");
                actFC.setDialogTitle("Choose Active Sim CM");
                actFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                actFC.showOpenDialog(null);
            }
            System.out.println("Selected file: " + bslFC.getSelectedFile().getAbsolutePath());
        }
        else if (bslResult == JFileChooser.CANCEL_OPTION) {
            System.out.println("Program Terminated by User");
            System.exit(0);
        }
        else {
            System.out.println("No Baseline File Selected. Program Terminated");
            System.exit(0);
        }


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


        JFrame window = new JFrame();
        window.setTitle("SimComparisonTool");
        window.setSize(2000, 1000);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out application by default
        window.getContentPane().setBackground(new Color(25, 25, 25));

        SceneLoader act = new SceneLoader(actFC.getSelectedFile().getAbsolutePath());
        SceneLoader bsl = new SceneLoader(bslFC.getSelectedFile().getAbsolutePath());
        SceneLoader delta = new SceneLoader(deltaFC.getSelectedFile().getAbsolutePath());

        // Flag to check if the view is constant, to keep the same section when switching variables
        final String[] currentView = {VIEWS[0]};
        ImageDisplayPanel displayer = new ImageDisplayPanel(act.cptScenes.getImages(currentView[0]), bsl.cptScenes.getImages(currentView[0]), delta.cptScenes.getImages(currentView[0]),  0,
                actFC.getSelectedFile().getName(), bslFC.getSelectedFile().getName(), deltaFC.getSelectedFile().getName());

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
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    displayer.toggleActive();
                }

                if (e.getKeyCode() == KeyEvent.VK_2) {
                    displayer.toggleBaseline();
                }

                if (e.getKeyCode() == KeyEvent.VK_D) {
                    displayer.toggleDelta();
                }
            }

        });

        JPopupMenu mainMenu = new JPopupMenu();

        //ActionListener to retrieve selected value
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JMenuItem source = (JMenuItem) event.getSource(); // Get clicked item
                JPopupMenu parentMenu = (JPopupMenu) source.getParent();   // Get parent menu

                String selectedView = event.getActionCommand(); // View name (submenu item)
                String dummySelectedVariable = String.valueOf(parentMenu.getInvoker());// Variable name (parent menu)
                int index = dummySelectedVariable.indexOf(",text=");
                String selectedVariable = dummySelectedVariable.substring(index + 6, dummySelectedVariable.length() - 1);

                System.out.println("Selected Variable: " + selectedVariable);
                System.out.println("Selected View: " + selectedView);

                int count = 0;

                if (currentView[0].equals(selectedView)){
                    count = -1;
                }
                else {
                    currentView[0] = selectedView;
                }

                switch (selectedVariable){
                    case "Total Pressure":
                        displayer.switchVariable(act.cptScenes.getImages(selectedView), bsl.cptScenes.getImages(selectedView), delta.cptScenes.getImages(selectedView),  count);
                        break;
                    case "Pressure":
                        displayer.switchVariable(act.pressureScenes.getImages(selectedView), bsl.pressureScenes.getImages(selectedView), delta.pressureScenes.getImages(selectedView), count);
                        break;
                    case "Inwash":
                        displayer.switchVariable(act.inwashScenes.getImages(selectedView), bsl.inwashScenes.getImages(selectedView), delta.inwashScenes.getImages(selectedView), count);
                        break;
                    case "Velocity Z":
                        displayer.switchVariable(act.velZScenes.getImages(selectedView), bsl.velZScenes.getImages(selectedView), delta.velZScenes.getImages(selectedView), count);
                        break;
                    case "Vorticity":
                        displayer.switchVariable(act.vorticityScenes.getImages(selectedView), bsl.vorticityScenes.getImages(selectedView), delta.vorticityScenes.getImages(selectedView), count);
                        break;
                    default:
                        System.out.println("wtf");
                }

            }
        };

        for (String s : VARIABLES) {
            JMenu variableMenu = new JMenu(s); // Create a new menu for each variable

            // Create submenu items inside this loop to ensure each menu gets its own set
            for (String view : VIEWS) {
                JMenuItem viewItem = new JMenuItem(view);
                viewItem.addActionListener(menuListener);
                variableMenu.add(viewItem); // Add directly to the current submenu
            }

            mainMenu.add(variableMenu);// Add submenu to the popup menu
        }

        class PopupListener extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    mainMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        MouseListener popupListener = new PopupListener();
        displayer.addMouseListener(popupListener);
        displayer.add(mainMenu);
        window.add(displayer);
        window.setVisible(true);

    }
}