package model;

import java.util.Objects;

public class Vertex {
    private final float x;
    private final float y;
    private final VertexType type;
    private final int load;

    public Vertex(float x, float y, VertexType type, int load) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.load = load;
    }

    public int getX() {
        return Math.round(x);
    }

    public int getY() {
        return Math.round(y);
    }

    public float getRealX() {
        return x;
    }

    public float getRealY() {
        return y;
    }

    public VertexType getType() {
        return type;
    }

    public int getLoad() {
        return load;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return x == vertex.x &&
                y == vertex.y &&
                load == vertex.load &&
                type == vertex.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, type, load);
    }

    @Override
    public String toString() {
        switch(type) {
            case DEPOT:
                return "Depot (" + x +", " + y +" )";
            case SATELLITE:
                return "Satellite (" + x +", " + y +" )";
            case TRUCK_CUSTOMER:
                return "Truck customer (" + x +", " + y +" ) q = "+ load;
            case VEHICLE_CUSTOMER_NO_PARK:
                return "Vehicle customer without parking facility (" + x +", " + y +" ) q = "+ load;
            case VEHICLE_CUSTOMER_YES_PARK:
                return "Vehicle customer with parking facility (" + x +", " + y +" ) q = "+ load;
            default:
                return "Unknown";

        }
    }
}
