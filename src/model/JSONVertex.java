package model;

public class JSONVertex {

    public float x;
    public float y;
    public int q;
    char t;


    JSONVertex(float x, float y, int q, char t) {
        this.x = x;
        this.y = y;
        this.q = q;
    }

    @Override
    public String toString() {
        return "JSONVertex{" +
                "x=" + x +
                ", y=" + y +
                ", q=" + q +
                ", t=" + t+
                '}';
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public VertexType getType() {

        switch (t) {
            case 'c': return VertexType.TRUCK_CUSTOMER;
            case 'd': return VertexType.DEPOT;
            default: return null;
        }

    }

    public int getLoad() {
        return q;
    }
}
