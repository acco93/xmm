package view;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import model.VertexType;

public class Toolbar extends JToolBar {

    Toolbar(View view) {

        this.setProperties();
        this.addComponents(view);

    }

    private void setProperties() {
        this.setFloatable(false);
        this.setOrientation(VERTICAL);
        this.setOpaque(true);
    }

    private void addComponents(View view) {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        ButtonGroup group = new ButtonGroup();

        JToggleButton move = new JToggleButton(new ImageIcon(loader.getResource("move.png").getPath()));
        move.addActionListener(e->view.setSelectedVertex(null));
        move.setToolTipText("Move the map.");
        group.add(move);

        JToggleButton depot = new JToggleButton(new ImageIcon(loader.getResource("depot.png").getPath()));
        depot.addActionListener(e->view.setSelectedVertex(VertexType.DEPOT));
        depot.setToolTipText("Place the depot");
        group.add(depot);

        JToggleButton satellite = new JToggleButton(new ImageIcon(loader.getResource("satellite.png").getPath()));
        satellite.setToolTipText("Place a satellite");
        satellite.addActionListener(e->{view.setSelectedVertex(VertexType.SATELLITE);});
        group.add(satellite);

        JToggleButton truckCustomer = new JToggleButton(new ImageIcon(loader.getResource("truck_customer.png").getPath()));
        truckCustomer.setToolTipText("Place a truck customer");
        truckCustomer.addActionListener(e->{view.setSelectedVertex(VertexType.TRUCK_CUSTOMER);});
        group.add(truckCustomer);

        JToggleButton vehicleCustomerNoPark = new JToggleButton(new ImageIcon(loader.getResource("vehicle_customer_no_park.png").getPath()));
        vehicleCustomerNoPark.setToolTipText("Place a vehicle customer without parking facility");
        vehicleCustomerNoPark.addActionListener(e->{view.setSelectedVertex(VertexType.VEHICLE_CUSTOMER_NO_PARK);});
        group.add(vehicleCustomerNoPark);

        JToggleButton vehicleCustomerYesPark = new JToggleButton(new ImageIcon(loader.getResource("vehicle_customer_yes_park.png").getPath()));
        vehicleCustomerYesPark.setToolTipText("Place a vehicle customer with parking facility");
        vehicleCustomerYesPark.addActionListener(e->{view.setSelectedVertex(VertexType.VEHICLE_CUSTOMER_YES_PARK);});
        group.add(vehicleCustomerYesPark);


        move.setSelected(true);

        this.add(move);
        this.add(depot);
        this.add(satellite);
        this.add(truckCustomer);
        this.add(vehicleCustomerNoPark);
        this.add(vehicleCustomerYesPark);

        JButton truckCapacity = new JButton("Q1");
        truckCapacity.setToolTipText("Set the truck capacity");
        truckCapacity.addActionListener(e->{view.setTruckCapacity();});
        this.add(truckCapacity);


    }

    /*@Override
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
        if (comp instanceof JButton) {
            ((JButton) comp).setContentAreaFilled(false);
        }
    }*/

}
