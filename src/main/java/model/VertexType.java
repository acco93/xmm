package model;

public enum VertexType {
    DEPOT{
        public String toString() {
            return "depot";
        }
    }, TRUCK_CUSTOMER{
        public String toString() {
            return "truck customer";
        }
    }, SATELLITE{
        public String toString() {
            return "satellite";
        }
    }, VEHICLE_CUSTOMER_NO_PARK{
        public String toString() {
            return "vehicle customer without parking facility";
        }
    }, VEHICLE_CUSTOMER_YES_PARK{
        public String toString() {
            return "vehicle customer without parking facility";
        }
    }
}
