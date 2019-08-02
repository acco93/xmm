package model;

import java.util.List;

public interface ModelInterface {

    void load(String path);
    void load(JSONVertex[] data);

    void addVertex(float x, float y, VertexType vertexType, int load);

    Vertex removeVertex(int x, int y);

    Vertex getDepot();

    List<Vertex> getVertices();

    void reset();

    int getTruckCapacity();

    void setTruckCapacity(int value);

    List<Vertex> searchForNodes(int x, int y);

    void serialize(String path);

    boolean isEmpty();
}
