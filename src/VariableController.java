import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VariableController extends JPopupMenu{

    public VariableController(String[] var, String[] views){

        //ActionListener to retrieve selected value
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String selectedValue = event.getActionCommand();
                System.out.println("Selected: " + selectedValue);

            }
        };

        for (String s : var) {
            JMenu variableMenu = new JMenu(s); // Create a new menu for each variable

            // Create submenu items inside this loop to ensure each menu gets its own set
            for (String view : views) {
                JMenuItem viewItem = new JMenuItem(view);
                viewItem.addActionListener(menuListener);
                variableMenu.add(viewItem); // Add directly to the current submenu
            }

            this.add(variableMenu); // Add submenu to the popup menu
        }
    }
}


