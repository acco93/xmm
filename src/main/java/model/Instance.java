package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Instance {

    private ProblemType problemType;

    private int truckCapacity;
    private int trailerCapacity;

    private Vertex depot;
    private List<Vertex> satellites;
    private List<Vertex> truckCustomers;
    private List<Vertex> vehicleCustomersNoPark;
    private List<Vertex> vehicleCustomersYesPark;

    private Map<Position, List<Vertex>> mapping;
    private List<Vertex> vertices;


    Instance(ProblemType type) {


        this.problemType = type;
        this.mapping = new HashMap<>();
        this.truckCapacity = 0;
        this.trailerCapacity = 0;
        this.depot = null;
        this.truckCustomers = new ArrayList<>();
        this.satellites = new ArrayList<>();
        this.vehicleCustomersNoPark = new ArrayList<>();
        this.vehicleCustomersYesPark = new ArrayList<>();
        this.vertices = new ArrayList<>();



    }

    void addVertex(float x, float y, VertexType vertexType, int load) {

        Vertex vertex = new Vertex(x, y, vertexType, load);

        switch (vertexType) {
            case DEPOT:

                if(this.depot != null) {
                    // remove the depot if it exists
                    mapping.get(new Position(depot.getX(), depot.getY())).remove(depot);
                }

                this.depot = vertex;

                break;
            case TRUCK_CUSTOMER:
                this.truckCustomers.add(vertex);
                break;
            case SATELLITE:
                this.satellites.add(vertex);
                break;
            case VEHICLE_CUSTOMER_NO_PARK:
                this.vehicleCustomersNoPark.add(vertex);
                break;
            case VEHICLE_CUSTOMER_YES_PARK:
                this.vehicleCustomersYesPark.add(vertex);
                break;
        }

        Position position = new Position(vertex.getX(), vertex.getY());

        List<Vertex> list = mapping.computeIfAbsent(position, k -> new ArrayList<>());

        list.add(vertex);

        updateCachedMatrices();

    }

    Vertex removeVertex(int x, int y) {

        Position position = new Position(x, y);

        List<Vertex> list = mapping.get(position);

        if(list == null) {
            return null;
        }

        Vertex vertex = list.get(list.size() - 1);

        list.remove(list.size()-1);

        if(list.isEmpty()) {
            this.mapping.remove(position);
        }

        switch(vertex.getType()) {

            case DEPOT:
                depot = null;
                break;
            case TRUCK_CUSTOMER:
                truckCustomers.remove(vertex);
                break;
            case SATELLITE:
                satellites.remove(vertex);
                break;
            case VEHICLE_CUSTOMER_NO_PARK:
                vehicleCustomersNoPark.remove(vertex);
                break;
            case VEHICLE_CUSTOMER_YES_PARK:
                vehicleCustomersYesPark.remove(vertex);
                break;
        }

        updateCachedMatrices();

        return vertex;

    }

    private void updateCachedMatrices() {

        vertices.clear();

        vertices.addAll(truckCustomers);
        vertices.addAll(vehicleCustomersNoPark);
        vertices.addAll(vehicleCustomersYesPark);
        vertices.addAll(satellites);
        if(depot!=null) {
            vertices.add(depot);
        }

    }

    void setTruckCapacity(int value) {
        this.truckCapacity = value;
    }

    void setTrailerCapacity(int value) {
        this.trailerCapacity = value;
    }

    ProblemType getProblemType() {
        return problemType;
    }

    Vertex getDepot() {
        return depot;
    }

    int getTruckCapacity() {
        return truckCapacity;
    }

    int getTrailerCapacity() {
        return trailerCapacity;
    }

    List<Vertex> getSatellites() {
        return satellites;
    }

    List<Vertex> getTruckCustomers() {
        return truckCustomers;
    }

    List<Vertex> getVehicleCustomersNoPark() {
        return vehicleCustomersNoPark;
    }

    List<Vertex> getVehicleCustomersYesPark() {
        return vehicleCustomersYesPark;
    }

    List<Vertex> getVertices() {
        return vertices;
    }

    List<Vertex> searchForNodes(int x, int y) {
        return mapping.get(new Position(x, y));
    }

    boolean isEmpty() {
        return vertices.isEmpty();
    }
}
