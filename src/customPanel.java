import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class customPanel extends JPanel{

    customPanel(){
        this.setBackground(new Color(40, 40, 40));
        Border divider = BorderFactory.createLineBorder(new Color(15, 15, 15), 5);
        this.setBorder(divider);
    }

}
