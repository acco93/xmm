package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class Serializer {

    static void xsttrp(Instance instance, String path) {

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(path));

            pw.printf("%d\t%d\t%d\t%d\n", instance.getTruckCustomers().size(), instance.getSatellites().size() + 1,
                    instance.getVehicleCustomersNoPark().size(), instance.getVehicleCustomersYesPark().size());
            pw.printf("%d\t%d\n", instance.getTruckCapacity(), instance.getTrailerCapacity());
            if(instance.getDepot() != null) {
                pw.printf("%d\t%d\n", instance.getDepot().getX(), instance.getDepot().getY());
            } else {
                pw.printf("0\t0\n"); // add a dummy depot in 0 0
            }
            for (Vertex satellite : instance.getSatellites()) {
                pw.printf("%d\t%d\n", satellite.getX(), satellite.getY());
            }
            for (Vertex customer : instance.getTruckCustomers()) {
                pw.printf("%d\t%d\t%d\n", customer.getX(), customer.getY(), customer.getLoad());
            }
            for (Vertex customer : instance.getVehicleCustomersNoPark()) {
                pw.printf("%d\t%d\t%d\n", customer.getX(), customer.getY(), customer.getLoad());
            }
            for (Vertex customer : instance.getVehicleCustomersYesPark()) {
                pw.printf("%d\t%d\t%d\n", customer.getX(), customer.getY(), customer.getLoad());
            }

            pw.close();

        } catch (IOException e) {

            e.printStackTrace();
        }


    }

    static boolean xsttrpCompatible(Instance instance) {
        return true;
    }

    static boolean lrpCompatible(Instance instance) {
        return false;
    }

    public static boolean vrpCompatible(Instance instance) {
//        return instance.getVehicleCustomersNoPark().isEmpty() && instance.getVehicleCustomersYesPark().isEmpty() && instance.getSatellites().isEmpty();
        return false;
    }
}
