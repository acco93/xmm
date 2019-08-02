package view;

import model.Instance;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class OptionsPanel {

    static int showMessageDialog(Component component, String title, String description, List<String> options) {

        final JPanel panel = new JPanel(new GridLayout(3, 1));

        panel.add(new JLabel(description));

        List<JRadioButton> radios = new ArrayList<>();

        ButtonGroup group = new ButtonGroup();

        for (String name : options) {
            final JRadioButton button = new JRadioButton(name);
            radios.add(button);
            panel.add(button);
            group.add(button);
        }

        radios.get(0).setSelected(true);

        int result = JOptionPane.showOptionDialog(component, panel, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"Ok"}, null);


        for (int n = 0; n < radios.size(); n++) {
            if (radios.get(n).isSelected()) {
                return n;
            }
        }

        return -1;

    }

}
