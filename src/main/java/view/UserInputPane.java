package view;

import controller.BindingBundle;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class UserInputPane extends JPanel {

    private JTextField fileName;
    private final JTextField arguments;
    private File selectedFile;

    UserInputPane() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel executableLabel = new JLabel("Executable: ");

        fileName = new JTextField(50);
        fileName.setText("/home/acco/CLionProjects/vrp/cmake-build-release/vrp");
        JButton openFileButton = new JButton("...");

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        add(executableLabel, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(fileName, gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.NONE;
        add(openFileButton, gbc);



        openFileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            int returnValue = chooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                fileName.setText(selectedFile.getAbsolutePath());
            }
        });


        JLabel argumentsLabel = new JLabel("Arguments: ");
        arguments = new JTextField(50);
        arguments.setText("--optimize --seed 1 --input /home/acco/CLionProjects/vrp/instances/X/X-n200-k36.vrp --threads 1");

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        add(argumentsLabel, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(arguments, gbc);
        gbc.gridx++;



    }

    BindingBundle get() {

        return new BindingBundle(fileName.getText(), arguments.getText());

    }

}
