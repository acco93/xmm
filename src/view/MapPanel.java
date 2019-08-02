package view;

import controller.Controller;
import model.JSONVertex;
import model.Solution;
import model.Vertex;
import model.VertexType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapPanel extends JPanel {

    private final View view;
    private final Controller controller;
    private int currentZoom = 16;
    private int topDistance = 0;
    private int leftDistance = 0;
    private Point startDragPoint = new Point(0,0);
    private Solution solution;
    private volatile boolean locked = false;
    private boolean autohideToolbar = false;
    private List<Vertex> verticesToShow;
    private JSONVertex[] simpleSolution;


    public MapPanel(Controller controller, View view) {
        
        this.controller = controller;
        this.view = view;

        this.propertiesSetup();

    }


    private void propertiesSetup() {

        //this.setBackground(Color.white);
        this.setFocusable(true);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {

                int x = (int) Math.floor(mouseEvent.getPoint().x / currentZoom) - leftDistance;
                int y = (int) Math.floor(mouseEvent.getPoint().y / currentZoom) - topDistance;

                if (SwingUtilities.isLeftMouseButton(mouseEvent) && !((mouseEvent.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) && view.getSelectedVertex() != null) {

                    if(locked) {
                        view.notify(MessageType.WARNING, "The map is currently locked");
                    } else {
                        addVertex(x, y);
                    }


                } else if (SwingUtilities.isRightMouseButton(mouseEvent)) {

                    if(locked) {
                        view.notify(MessageType.WARNING, "The map is currently locked");
                    } else {
                        removeVertex(x, y);
                    }

                } else if (SwingUtilities.isMiddleMouseButton(mouseEvent) || ((mouseEvent.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) || view.getSelectedVertex() == null) {

                    startDragPoint = new Point(x, y);

                }

                grabFocus();
                repaint();

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                super.mouseWheelMoved(mouseWheelEvent);
                int notches = mouseWheelEvent.getWheelRotation();
                if (notches < 0) {
                    zoomIn();
                } else {
                    zoomOut();
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                if (SwingUtilities.isMiddleMouseButton(mouseEvent) || ((mouseEvent.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) || view.getSelectedVertex() == null) {

                    int x = (int) Math.floor(mouseEvent.getPoint().x / currentZoom) - leftDistance;
                    int y = (int) Math.floor(mouseEvent.getPoint().y / currentZoom) - topDistance;

                    int xOffset = x - startDragPoint.x;
                    int yOffset = y - startDragPoint.y;

                    leftDistance += xOffset;
                    topDistance += yOffset;

                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                super.mouseMoved(mouseEvent);

                int x = (int) Math.floor(mouseEvent.getPoint().x / currentZoom) - leftDistance;
                int y = (int) Math.floor(mouseEvent.getPoint().y / currentZoom) - topDistance;

                view.setCoordinates(x, y);

                verticesToShow = controller.searchForNodes(x, y);

                repaint();

                if(verticesToShow != null)
                view.notify(MessageType.INFO, Arrays.deepToString(verticesToShow.toArray()));

            }
        };


        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        this.addMouseWheelListener(mouseAdapter);


    }


    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        boolean showGrid = true;
        if (showGrid && currentZoom > 10) {
            paintGrid(graphics);
        }


        paintSolution(graphics);
        paintInstance(graphics);


    }


    private void paintInstance(Graphics graphics) {

        List<Vertex> vertices = controller.getVertices();

        for(Vertex vertex : vertices) {

            int x = this.xValueOf(vertex.getX());
            int y = this.yValueOf(vertex.getY());
            printVertex(graphics, vertex.getType(), x, y, this.currentZoom);

        }

    }

    private void printVertex(Graphics graphics, VertexType type, int x, int y, int zoom) {
        switch (type) {

            case DEPOT:
                drawDepot(graphics, x, y, zoom);
                break;
            case TRUCK_CUSTOMER:
                drawTruckCustomer(graphics, x, y, zoom);
                break;
            case SATELLITE:
                drawSatellite(graphics, x, y, zoom);
                break;
            case VEHICLE_CUSTOMER_NO_PARK:
                drawVehicleCustomerNoPark(graphics, x, y, zoom);
                break;
            case VEHICLE_CUSTOMER_YES_PARK:
                drawVehicleCustomerYesPark(graphics, x, y, zoom);
                break;
        }

    }

    private void drawVehicleCustomerYesPark(Graphics graphics, int x, int y, int zoom) {

        graphics.setColor(Color.black);
        graphics.fillPolygon(new int[] { x, x + zoom / 2, x + zoom }, new int[] { y + zoom, y - zoom / 4, y + zoom }, 3);
    }

    private void drawVehicleCustomerNoPark(Graphics graphics, int x, int y, int zoom) {
        graphics.setColor(Color.black);
        graphics.fillOval(x, y, zoom, zoom);
    }

    private void drawSatellite(Graphics graphics, int x, int y, int zoom) {

        graphics.setColor(Color.white);
        graphics.fillPolygon(new int[] { x, x + zoom / 2, x + zoom }, new int[] { y + zoom, y - zoom / 4, y + zoom }, 3);
        graphics.setColor(Color.black);
        graphics.drawPolygon(new int[] { x, x + zoom / 2, x + zoom }, new int[] { y + zoom, y - zoom / 4, y + zoom }, 3);
    }

    private void drawTruckCustomer(Graphics graphics, int x, int y, int zoom) {

        graphics.setColor(Color.white);
        graphics.fillOval(x, y, zoom, zoom);
        graphics.setColor(Color.black);
        graphics.drawOval(x, y, zoom, zoom);
    }

    private void drawDepot(Graphics graphics, int x, int y, int zoom) {

        graphics.setColor(Color.WHITE);
        graphics.fillRect(x, y, zoom, zoom);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, zoom, zoom);
    }

    private void paintSolution(Graphics graphics) {
        if(simpleSolution == null) {
            return;
        }

        for(int n = 0; n < simpleSolution.length; n++) {

            JSONVertex i = simpleSolution[n];
            JSONVertex j = simpleSolution[(n+1) % simpleSolution.length];

            graphics.drawLine(xValueOf((int)i.getX()), yValueOf((int)i.getY()), xValueOf((int)j.getX()),yValueOf((int)j.getY()));

        }

    }

    private void paintGrid(Graphics graphics) {

        //creates a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) graphics.create();

        //set the stroke of the copy, not the original
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
        g2d.setStroke(dashed);

        g2d.setColor(Color.lightGray);
        for (int i = currentZoom / 2; i < getWidth(); i += currentZoom) {
            g2d.drawLine(i, 0, i, this.getHeight());
        }
        for (int j = currentZoom / 2; j < this.getHeight(); j += currentZoom) {
            g2d.drawLine(0, j, this.getWidth(), j);
        }


        g2d.setStroke(new BasicStroke(3));

        g2d.setColor(Color.lightGray);
        g2d.drawLine(this.xValueOf(0) + currentZoom / 2, this.yValueOf(0) + currentZoom / 2, this.xValueOf(0) + currentZoom / 2,
                this.getHeight());
        g2d.drawLine(this.xValueOf(0) + currentZoom / 2, this.yValueOf(0) + currentZoom / 2, this.getWidth(),
                this.yValueOf(0) + currentZoom / 2);

        //gets rid of the copy
        g2d.dispose();

    }

    private int yValueOf(int y) {
        return y * currentZoom + topDistance * currentZoom;
    }

    private int xValueOf(int x) {
        return x * currentZoom + leftDistance * currentZoom;
    }

    private void zoomOut() {
        if (this.currentZoom == 40) {
            return;
        }
        this.currentZoom++;
    }

    private void zoomIn() {
        if (this.currentZoom == 1) {
            return;
        }
        this.currentZoom--;
    }

    private void addVertex(int x, int y) {

        int load = 0;

        VertexType vertexType = view.getSelectedVertex();

        if (vertexType != VertexType.DEPOT && vertexType != VertexType.SATELLITE) {

            String loadString = JOptionPane.showInputDialog("Load = ");
            try {
                load = Integer.parseInt(loadString);
            } catch (NumberFormatException exc) {
                return;
            }

        }
        
        controller.addVertex(x, y, vertexType, load);
        

    }

    private void removeVertex(int x, int y) {

        controller.removeVertex(x, y);
        view.refresh();
    }

    void refresh() {
        this.repaint();
    }

    void lock() {
        this.locked = true;
    }

    void unlock() {
        this.locked = false;
    }

    void toggleAutohideToolbar() {
        autohideToolbar = !autohideToolbar;
        if(!autohideToolbar) {
            view.showToolbar();
        }
    }

    void setSolution(JSONVertex[] data) {

        SwingUtilities.invokeLater(()->{
            simpleSolution = data;
        });

    }
}
