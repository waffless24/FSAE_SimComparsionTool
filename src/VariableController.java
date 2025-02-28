import javax.swing.*;

public class VariableController extends JPopupMenu{

    public VariableController(String[] var, String[] views){

        for (String s : var) {
            JMenu variableMenu = new JMenu(s); // Create a new menu for each variable

            // Create submenu items inside this loop to ensure each menu gets its own set
            for (String view : views) {
                JMenuItem viewItem = new JMenuItem(view);
                variableMenu.add(viewItem); // Add directly to the current submenu
            }

            this.add(variableMenu); // Add submenu to the popup menu
        }
    }
}


