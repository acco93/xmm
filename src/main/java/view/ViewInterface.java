package view;

import controller.BindingBundle;
import model.JSONVertex;

import java.io.File;
import java.util.List;

public interface ViewInterface {

    void refresh();

    int askForPossibilities(String title, String description, List<String> options);

    void lock();

    void unlock();

    File askForExistingFile(String basePath);

    File askForSavingFile(String basePath);

    void notify(MessageType type, String text);

    BindingBundle showBindingInterface();

    void lockRunExternalProgram();

    void unlockRunExternalProgram();

    void setSolution(JSONVertex[] data);
}
