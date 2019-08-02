package model;

import controller.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Model implements ModelInterface {

    private final Controller controller;
    private Instance instance;

    public Model(Controller controller) {
        this.controller = controller;
        this.instance = new Instance(ProblemType.UNDEFINED);
    }

    @Override
    public void load(String path) {

        Instance previous = instance;

        List<Instance> possibleInstances = new ArrayList<>();

        Optional.ofNullable(Parser.xsttrp(path)).ifPresent(possibleInstances::add);
        Optional.ofNullable(Parser.mdvrp(path)).ifPresent(possibleInstances::add);
        Optional.ofNullable(Parser.lrpTuzunAndBurke(path)).ifPresent(possibleInstances::add);
        Optional.ofNullable(Parser.cvrp(path)).ifPresent(possibleInstances::add);


        if(possibleInstances.size() == 0) {

            instance = previous;
            controller.notifyError("Unable to open the instance.");


        } else if(possibleInstances.size() == 1) {

            instance = possibleInstances.get(0);

        } else {

            List<String> possibleProblems = new ArrayList<>();

            for(Instance instance : possibleInstances) {
                possibleProblems.add(instance.getProblemType().toString());
            }

            int option = controller.askForPossibilities("Oops...", "Cannot chose the correct instance type among the following:", possibleProblems);

            instance = possibleInstances.get(option);

        }


    }

    @Override
    public void load(JSONVertex[] data) {

        reset();

        System.out.println("qua");
        Arrays.deepToString(data);

        for(JSONVertex jsonVertex : data) {
            jsonVertex.toString();
            addVertex(jsonVertex.getX(), jsonVertex.getY(), jsonVertex.getType(), jsonVertex.getLoad());
        }

    }


    @Override
    public void addVertex(float x, float y, VertexType vertexType, int load) {
        instance.addVertex(x, y, vertexType, load);
    }

    @Override
    public Vertex removeVertex(int x, int y) {
        return instance.removeVertex(x, y);
    }

    @Override
    public Vertex getDepot() {
        return instance.getDepot();
    }

    @Override
    public List<Vertex> getVertices() {
        return instance.getVertices();
    }

    @Override
    public void reset() {
        instance = new Instance(ProblemType.UNDEFINED);
    }

    @Override
    public int getTruckCapacity() {
        return instance.getTruckCapacity();
    }

    @Override
    public void setTruckCapacity(int value) {
        instance.setTruckCapacity(value);
    }

    @Override
    public List<Vertex> searchForNodes(int x, int y) {
        return instance.searchForNodes(x, y);
    }

    @Override
    public void serialize(String path) {

        List<ProblemType> possibilities = new ArrayList<>();

        if(Serializer.xsttrpCompatible(instance)){
            possibilities.add(ProblemType.XSTTRP);
        }

        if(Serializer.lrpCompatible(instance)) {
            possibilities.add(ProblemType.XSTTRP);
        }

        if(Serializer.vrpCompatible(instance)) {
            possibilities.add(ProblemType.XSTTRP);
        }

        ProblemType type = null;

        if(possibilities.size() == 1) {

            type = possibilities.get(0);

        } else {

            List<String> possibleProblems = new ArrayList<>();

            for(ProblemType problemType : possibilities) {
                possibleProblems.add(problemType.toString());
            }

            int option = controller.askForPossibilities("Oops...", "What format do you prefer?", possibleProblems);

            type = possibilities.get(option);

        }

        switch (type) {

            case UNDEFINED:
                break;
            case XSTTRP:
                Serializer.xsttrp(instance, path);
                break;
            case MDVRP:
                break;
            case LRP:
                break;
        }


    }

    @Override
    public boolean isEmpty() {
        return instance.isEmpty();
    }


}
