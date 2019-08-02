package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class MenuBar extends JMenuBar {

    private final JLabel coords;

    MenuBar(Controller controller, View view) {

        JMenu file = new JMenu("File");


        JMenuItem create = createLongJMenuItem("New");
        Action createAction = new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.create();
            }
        };
        createAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        create.setAction(createAction);

        JMenuItem load = createLongJMenuItem("Open...");
        Action loadAction = new AbstractAction("Open...") {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.load();
            }
        };
        loadAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        load.setAction(loadAction);


        JMenuItem save = createLongJMenuItem("Save");
        Action saveAction = new AbstractAction("Save") {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.save();
            }
        };
        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        save.setAction(saveAction);

        JMenuItem exit = createLongJMenuItem("Close");
        Action exitAction = new AbstractAction("Close") {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exit();
            }
        };
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        exit.setAction(exitAction);


        file.add(create);
        file.add(load);
        file.add(save);
        file.addSeparator();
        file.add(exit);

        this.add(file);

        this.add(Box.createHorizontalGlue());
        coords = new JLabel("0 0  ");
        coords.setFont(new Font(coords.getFont().getName(), coords.getFont().getStyle(), 11));

        this.add(coords);


    }

    private JMenuItem createLongJCheckBoxMenuItem(String text) {
        return new JCheckBoxMenuItem(text +"        ");
    }

    private JMenuItem createLongJMenuItem(String text) {
        return new JMenuItem(text+"        ");
    }

    public void setCoordinates(int x, int y) {
        coords.setText(x+" "+y+"  ");
    }

 
}
