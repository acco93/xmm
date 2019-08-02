package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {


    static Instance xsttrp(String path) {

        Instance instance = new Instance(ProblemType.XSTTRP);

        File file = new File(path);

        try {

            Scanner scanner = new Scanner(file);

            String line = scanner.nextLine().trim();

            // do whatever processing at the end of each line
            String[] tokens = line.split("\\s");

            int truckCustomers = Integer.parseInt(tokens[0]);
            int satellites = Integer.parseInt(tokens[1]) - 1;
            int vehicleCustomers = 0;
            int vehicleCustomersParkingFacility = 0;

            try {

                vehicleCustomers = Integer.parseInt(tokens[2]);
                vehicleCustomersParkingFacility = Integer.parseInt(tokens[3]);

            } catch (Exception e) {
                vehicleCustomers = 0;
                vehicleCustomersParkingFacility = 0;

            }

            instance.setTruckCapacity(scanner.nextInt());
            instance.setTrailerCapacity(scanner.nextInt());

            instance.addVertex(scanner.nextInt(), scanner.nextInt(), VertexType.DEPOT, 0);

            for (int i = 0; i < satellites; i++) {
                instance.addVertex(scanner.nextInt(), scanner.nextInt(), VertexType.SATELLITE, 0);
            }

            for (int i = 0; i < truckCustomers; i++) {
                instance.addVertex(scanner.nextInt(), scanner.nextInt(), VertexType.TRUCK_CUSTOMER, scanner.nextInt());
            }

            for (int i = 0; i < vehicleCustomers; i++) {
                instance.addVertex(scanner.nextInt(), scanner.nextInt(), VertexType.VEHICLE_CUSTOMER_NO_PARK, scanner.nextInt());
            }

            for (int i = 0; i < vehicleCustomersParkingFacility; i++) {
                instance.addVertex(scanner.nextInt(), scanner.nextInt(), VertexType.VEHICLE_CUSTOMER_YES_PARK, scanner.nextInt());
            }

            scanner.close();

        }catch (Exception e) {

            instance = null;

        }

        return instance;
    }

    static Instance mdvrp(String path) {

        Instance instance = new Instance(ProblemType.MDVRP);

        File file = new File(path);

        try {
            Scanner sc = new Scanner(file);

            String line = sc.nextLine().trim();

            // do whatever processing at the end of each line
            String[] tokens = line.split("\\s+");

            int type = Integer.parseInt(tokens[0]);

            int vehicles = Integer.parseInt(tokens[1]);
            int truckCustomers = Integer.parseInt(tokens[2]);
            int satellites = Integer.parseInt(tokens[3]);
            int vehicleCustomers = 0;
            int vehicleCustomersParkingFacility = 0;

            tokens = sc.nextLine().trim().split("\\s+");
            instance.setTruckCapacity(Integer.parseInt(tokens[1]));
            instance.setTrailerCapacity(9999999);

            for (int i = 1; i < satellites; i++) {
                sc.nextLine().trim().split("\\s");
            }

            // add a dummy depot
            instance.addVertex(-111110, -111110, VertexType.DEPOT, 0);

            for (int i = 0; i < truckCustomers; i++) {
                tokens = sc.nextLine().trim().split("\\s+");
                instance.addVertex(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), VertexType.TRUCK_CUSTOMER,
                        Integer.parseInt(tokens[4]));
            }

            for (int i = 0; i < satellites; i++) {
                tokens = sc.nextLine().trim().split("\\s+");
                instance.addVertex(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), VertexType.SATELLITE, 0);
            }

            sc.close();

        } catch (Exception e) {

            instance = null;

        }

        return instance;

    }

    static Instance lrpTuzunAndBurke(String path) {

        Instance instance = new Instance(ProblemType.LRP);

        File file = new File(path);

        try {

            Scanner sc = new Scanner(file);

            String line = sc.nextLine().trim();
            String[] tokens = line.split("\\s+");
            int truckCustomers = Integer.parseInt(tokens[0]);

            line = sc.nextLine().trim();
            tokens = line.split("\\s+");
            int satellites = Integer.parseInt(tokens[0]);

            line = sc.nextLine();

            float[] xSatellites = new float[satellites];
            float[] ySatellites = new float[satellites];

            for (int k = 0; k < satellites; k++) {

                line = sc.nextLine().trim();
                tokens = line.split("\\s+");

                xSatellites[k] = Float.parseFloat(tokens[0]);
                ySatellites[k] = Float.parseFloat(tokens[1]);

            }

            float[] xCustomers = new float[truckCustomers];
            float[] yCustomers = new float[truckCustomers];
            int[] qCustomers = new int[truckCustomers];

            for (int i = 0; i < truckCustomers; i++) {

                line = sc.nextLine().trim();
                tokens = line.split("\\s+");

                xCustomers[i] = Float.parseFloat(tokens[0]);
                yCustomers[i] = Float.parseFloat(tokens[1]);

            }

            line = sc.nextLine().trim();

            line = sc.nextLine().trim();
            tokens = line.split("\\s+");
            instance.setTruckCapacity((int) Float.parseFloat(tokens[0]));
            instance.setTrailerCapacity(9999999);

            line = sc.nextLine().trim();

            for (int k = 0; k < satellites; k++) {
                line = sc.nextLine().trim();
            }

            line = sc.nextLine().trim();

            for (int i = 0; i < truckCustomers; i++) {

                line = sc.nextLine().trim();
                tokens = line.split("\\s+");

                qCustomers[i] = Integer.parseInt(tokens[0]);


            }


            instance.addVertex(0, 0, VertexType.DEPOT, 0);
            for(int k = 0; k < satellites; k++) {
                instance.addVertex((int)xSatellites[k], (int)ySatellites[k], VertexType.SATELLITE, 0);
            }

            for(int i = 0; i < truckCustomers; i++) {
                instance.addVertex((int)xCustomers[i], (int)yCustomers[i], VertexType.TRUCK_CUSTOMER, 0);
            }

            sc.close();

        } catch (Exception e) {
            instance = null;
        }

        return instance;

    }

    static Instance cvrp(String path) {

        Instance instance = new Instance(ProblemType.CVRP);

        File file = new File(path);

        try {
            Scanner sc = new Scanner(file);

            // name
            sc.nextLine();
            // comment
            sc.nextLine();
            // type
            sc.nextLine();

            // n
            String[] tokens = sc.nextLine().trim().split("\\s+");
            int customersNum = Integer.parseInt(tokens[2]);

            // edge type
            sc.nextLine();

            // capacity
            tokens = sc.nextLine().trim().split("\\s+");
            instance.setTruckCapacity(Integer.parseInt(tokens[2]));
            instance.setTrailerCapacity(999999);

            // node coord section
            sc.nextLine();

            ArrayList<Integer> x = new ArrayList<>(), y = new ArrayList<>(), q = new ArrayList<>();

            for (int i = 0; i < customersNum; i++) {
                tokens = sc.nextLine().trim().split("\\s+");
                x.add(Integer.parseInt(tokens[1]));
                y.add(Integer.parseInt(tokens[2]));
            }

            // demand section
            sc.nextLine();

            for (int i = 0; i < customersNum; i++) {
                tokens = sc.nextLine().trim().split("\\s+");
                q.add(Integer.parseInt(tokens[1]));
            }

            instance.addVertex(x.get(0), y.get(0), VertexType.DEPOT, 0);
            instance.addVertex(x.get(0), y.get(0), VertexType.SATELLITE, 0);
            for (int i = 1; i < customersNum; i++) {
                instance.addVertex(x.get(i), y.get(i), VertexType.TRUCK_CUSTOMER, q.get(i));
            }

            sc.close();


        } catch (Exception e) {
            instance = null;
        }

        return instance;

    }

}

