package controller;

import com.google.gson.Gson;
import model.*;
import view.MessageType;
import view.View;
import view.ViewInterface;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

public class Controller {

    ModelInterface model;
    ViewInterface view;
    private boolean unsavedChanges;
    private String basePath = null;
    private String savedFilePath = null;
    private Process executableProcess = null;

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
               view.notify(MessageType.SUCCESS, "Opened " + path);
           }
       }

       view.unlock();

   }

    public void save() {

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

        } else {
            // overwrite

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
            if(result == JOptionPane.NO_OPTION) {
                return false;
            }
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

    public void addVertex(int x, int y, VertexType vertexType, int load) {
        view.notify(MessageType.INFO, "Added " + vertexType.toString() + " with load " + load +" in ("+x+", "+y+")");
        model.addVertex(x, y, vertexType, load);
        notifyChanges();
    }

    public void removeVertex(int x, int y) {
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

    public void bindToExecutable() {

        Optional.ofNullable(view.showBindingInterface()).ifPresent(bindingBundle -> {

                new Thread(() -> {

                    try {

                    Runtime runtime = Runtime.getRuntime();
                    String command = bindingBundle.getExecutable() +" "+ bindingBundle.getArguments();
                    System.out.println(command);

                    view.notify(MessageType.INFO, "Running "+ bindingBundle.getExecutable() + " ... ");
                    view.lock();
                    view.lockRunExternalProgram();

                    executableProcess = runtime.exec(command);

                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(executableProcess.getInputStream()));

                    // read the output from the command

                    String s = null;
                    Gson gson = new Gson();


                    while ((s = stdInput.readLine()) != null) {

                        System.out.println(s);

                        if(s.startsWith("instance")){
                            s = s.replaceFirst("instance", "");

                            try {
                                JSONVertex[] data = gson.fromJson(s, JSONVertex[].class);
                                model.load(data);

                            } catch (Exception e) {
                                view.notify(MessageType.WARNING, e.getMessage());
                            }


                            view.notify(MessageType.SUCCESS, "Instance successfull loaded from json");

                        } else if(s.startsWith("solution")) {

                            s = s.replaceFirst("solution", "");


                            try {
                                JSONVertex[] data = gson.fromJson(s, JSONVertex[].class);
                                view.setSolution(data);

                            } catch (Exception e) {
                                view.notify(MessageType.WARNING, e.getMessage());
                            }




                        }

                    }


                    view.notify(MessageType.SUCCESS, "Run completed!");
                    view.unlock();
                    view.unlockRunExternalProgram();

                } catch (Exception e) {
                     //   e.printStackTrace();
                    view.notify(MessageType.ERROR, e.getMessage());

                }

                }).start();




        });


    }

    public void killExecutable() {
        executableProcess.destroy();
        view.notify(MessageType.WARNING, "Run aborted");
        view.unlock();
        view.unlockRunExternalProgram();
    }
}
