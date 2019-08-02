package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import model.Model;
import model.ModelInterface;
import model.Vertex;
import model.VertexType;
import view.MessageType;
import view.View;
import view.ViewInterface;

public class Controller {

    private ModelInterface model;
    private ViewInterface view;
    private boolean unsavedChanges;
    private String basePath = null;
    private String savedFilePath = null;
    private boolean readOnly = false;

    public Controller() {

        model = new Model(this);
        view = new View(this);

   }

   public void load() {

       view.lock();

       boolean discard = discardUnsavedChangesIfAny("Discard unsaved changes?");

       if(discard) {

           String path = askForPath();

           if(path != null) {
               savedFilePath = path;
               model.load(path);
               view.refresh();
               unsavedChanges = false;
               readOnly = true;
               view.notify(MessageType.SUCCESS, "Opened " + path);
           }
       }

       view.unlock();

   }

    public void save() {

        if (!unsavedChanges) {
            view.notify(MessageType.INFO, "Nothing to save");
            return;
        }

        if(savedFilePath == null) {
            File file = view.askForSavingFile(basePath);

            if(file == null) {
                return;
            }

            if(file.exists()) {
                boolean discard = discardUnsavedChangesIfAny("Overwrite the existing file?");
                if(!discard) {
                    return;
                }
            }

            savedFilePath = file.getAbsolutePath();

        }


        model.serialize(savedFilePath);
        view.notify(MessageType.SUCCESS, "Saved " + savedFilePath);
        notifySaving();


    }

    private String askForPath() {

        File file = view.askForExistingFile(basePath);

        if(file == null) {
            return null;
        }

        basePath = file.getParent();
        return file.getAbsolutePath();

    }

    public void exit() {

        boolean discard = discardUnsavedChangesIfAny("Discard unsaved changes?");

        if(!discard) {
            return;
        }

        System.exit(0);
    }

    private boolean discardUnsavedChangesIfAny(String message) {
        if(isThereAnythingNotSaved()) {
            int result = JOptionPane.showConfirmDialog(null, message,  "Warning", JOptionPane.YES_NO_OPTION);
            return result != JOptionPane.NO_OPTION;
        }
        return true;
    }

    private void notifyChanges() {
        unsavedChanges = true;
        view.refresh();
    }

    private void notifySaving() {
        unsavedChanges = false;
    }

    private boolean isThereAnythingNotSaved() {
        return unsavedChanges && !model.isEmpty();
    }

    private boolean askForDisablingReadOnly() {

        int value = askForPossibilities("Read-only", "The map is currently in read-only mode. Would you like to unlock the possibility of editing it?",
                new ArrayList<>(Arrays.asList("Yes, right now", "Maybe later")));

        if (value == 0) {
            readOnly = false;
            return false;
        } else {
            return true;
        }

    }

    public void addVertex(int x, int y, VertexType vertexType, int load) {

        if (readOnly && askForDisablingReadOnly()) {
            return;
        }

        view.notify(MessageType.INFO, "Added " + vertexType.toString() + " with load " + load +" in ("+x+", "+y+")");
        model.addVertex(x, y, vertexType, load);
        notifyChanges();
    }

    public void removeVertex(int x, int y) {

        if (readOnly && askForDisablingReadOnly()) {
            return;
        }

        Vertex vertex = model.removeVertex(x, y);
        if(vertex != null) {
            view.notify(MessageType.INFO, "Removed " + vertex.getType() + " with load " + vertex.getLoad() + " in (" + vertex.getX() + ", " + vertex.getY() + ")");
            notifyChanges();
        }
    }

    public int askForPossibilities(String title, String description, List<String> options) {
        return view.askForPossibilities(title, description, options);

    }

    public Vertex getDepot() {
        return model.getDepot();
    }

    public List<Vertex> getVertices() {
        return model.getVertices();
    }

    public void create() {
        boolean discard = discardUnsavedChangesIfAny("Discard unsaved changes?");

        if(!discard) {
            return;
        }

        model.reset();
        view.refresh();
        unsavedChanges = false;

        view.notify(MessageType.INFO, "New empty map");

    }

    public void notifyError(String error) {

        view.notify(MessageType.ERROR, error);

    }


    public int getTruckCapacity() {
        return model.getTruckCapacity();
    }

    public void setTruckCapacity(int value) {
        model.setTruckCapacity(value);
    }

    public List<Vertex> searchForNodes(int x, int y) {
        return model.searchForNodes(x, y);
    }

}
