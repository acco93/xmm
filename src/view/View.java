package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.Controller;
import model.JSONVertex;
import model.VertexType;

public class View extends JFrame implements ViewInterface {

    private final Controller controller;
    private MapPanel map;
    private MenuBar menu;
    private Toolbar toolbar;
    private VertexType currentVertexType;

    public View(Controller controller) {

        this.controller = controller;
        
        SwingUtilities.invokeLater(()->{

            try {
                //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            this.propertiesSetup(controller);

            this.componentsSetup(controller);

            //this.setExtendedState(MAXIMIZED_BOTH);

            this.setVisible(true);

            map.requestFocusInWindow();

        });


    }

    private void componentsSetup(Controller controller) {

        menu = new MenuBar(controller, this);
        this.setJMenuBar(menu);

        map = new MapPanel(controller, this);
        this.add(map, BorderLayout.CENTER);

        toolbar = new Toolbar(this);
        this.add(toolbar, BorderLayout.EAST);

    }

    private void propertiesSetup(Controller controller) {
        this.setTitle("XSTTRP Map Maker");
        this.setSize(800,600);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                controller.exit();
            }
        });
    }

    public VertexType getSelectedVertex() {
       return currentVertexType;
    }

    @Override
    public void refresh() {
        SwingUtilities.invokeLater(()->{
            map.refresh();
        });
    }

    @Override
    public int askForPossibilities(String title, String description, List<String> options) {
        return OptionsPanel.showMessageDialog(this, title, description, options);
    }

    @Override
    public void lock() {
        SwingUtilities.invokeLater(()->{
            map.lock();
        });
    }

    @Override
    public void unlock() {
        SwingUtilities.invokeLater(()->{
            map.unlock();
        });
    }

    @Override
    public File askForExistingFile(String basePath) {

        JFileChooser jfc = new JFileChooser(basePath);

        int returnValue = jfc.showOpenDialog(this);

        File selectedFile = null;

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();
        }
        return selectedFile;
    }



    @Override
    public File askForSavingFile(String basePath) {
        JFileChooser fileChooser = new JFileChooser();

        File selected = null;

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            selected = fileChooser.getSelectedFile();
        }

        return selected;
    }


    public void notify(MessageType type, String text) {



        SwingUtilities.invokeLater(()->{

            Color color = null;
            int timeout = 2000;

            switch (type) {

                case INFO:
                    color = new Color(64, 127, 127);
                    break;
                case WARNING:
                    color = new Color(212, 154, 106);
                    break;
                case ERROR:
                    color = new Color(212, 106, 106);
                    break;
                case SUCCESS:
                    color = new Color(85, 170, 85);
                    break;
            }

            JLabel label = new JLabel(text, SwingConstants.CENTER);
            label.setOpaque(true);
            Font font = label.getFont();
            label.setBackground(color);
            label.setForeground(Color.white);

            Font boldFont = new Font(font.getFontName(), Font.BOLD, 15);
            label.setFont(boldFont);
            add(label, BorderLayout.SOUTH);

            revalidate();

            new Thread(()->{
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SwingUtilities.invokeLater(()->{
                    remove(label);
                    revalidate();
                });

            }).start();

        });

    }

    @Override
    public void setSolution(JSONVertex[] data) {
        map.setSolution(data);
        map.refresh();
    }

    public void showToolbar() {
        toolbar.setVisible(true);
    }

    public void hideToolbar() {
        toolbar.setVisible(false);
    }

    public void autohideToolbar() {
        map.toggleAutohideToolbar();
        refresh();
    }


    public void setSelectedVertex(VertexType type) {
        currentVertexType = type;
    }

    public void setTruckCapacity() {

        String string = JOptionPane.showInputDialog(map, "Truck Capacity = ", controller.getTruckCapacity());

        if(string == null || "".equals(string.trim())){
            return;
        }

        try {
            int value = Integer.parseInt(string);
            controller.setTruckCapacity(value);

        } catch (Exception exc) {
            notify(MessageType.ERROR, "An error occurred while setting the truck capacity!");
        }
        
    }

    public void setCoordinates(int x, int y) {
        menu.setCoordinates(x, y);
    }
}
