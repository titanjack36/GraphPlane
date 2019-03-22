//----------------------------GRAPH PROGRAM CLASS-----------------------------//
//@author TitanJack
//@version 0.1 (2019-03-22)
//Graph Plane is a user friendly 2D graphing software that plots simple
//functions inputted by the user. The program implements the MathTools API which
//helps to parse and compute expressions entered by the user. The Java swing
//graphics library is also used to create an intuitive user interface.

package Grapher;

import Functions.Function;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class GraphProgram extends JPanel {

    private static int width;
    private static int height;
    private static double zoom;
    private static JFrame window;
    private static GraphProgram graph;

    private Grid grid;
    private InputPanel inputPanel;
    private Point mousePos;
    private Point origin;

    public GraphProgram() {
        //Center point of the program window
        Point center = new Point(GraphProgram.getWindowWidth() / 2,
                GraphProgram.getWindowHeight() / 2);
        origin = new Point(center.x, center.y);
        grid = new Grid(origin);
        inputPanel = new InputPanel(0, 0, 450, height);
        inputPanel.setBorder(new EmptyBorder(75, 50, 10, 0));
        updateGridOrigin(origin);

        //Use layout defined by (x,y) coordinates instead
        setLayout(null);
        add(inputPanel);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int shiftX = e.getX() - mousePos.x;
                int shiftY = e.getY() - mousePos.y;
                //When dragged, set the origin to follow the motion of the mouse
                origin.setLocation(origin.x + shiftX, origin.y + shiftY);
                updateGridOrigin(origin);
                mousePos = e.getPoint();
                repaint();
            }
        });
        this.addMouseWheelListener(e -> {
            double prevZoom = getZoom();
            double zoomRate = Math.pow(1.05, e.getScrollAmount());
            double offsetX = window.getInsets().left;
            double offsetY = window.getInsets().top;
            mousePos.setLocation(mousePos.x, mousePos.y);

            if (e.getWheelRotation() > 0) {

                //If mouse is touching input panel
                if (pointInBounds(mousePos, inputPanel.getBounds())) {
                    //Attempt to scroll down
                    inputPanel.scroll(zoomRate * 50);
                } else {
                    //Zoom out
                    setZoom(prevZoom / zoomRate);
                    //Because the graph zooms in at where the mouse location is
                    //and not at the origin, the origin must adjust its position
                    //to appear as if the graph is focused on where the mouse
                    //position is
                    origin.setLocation((origin.x - mousePos.x) / zoomRate +
                            mousePos.x, (origin.y - mousePos.y) / zoomRate +
                            mousePos.y);
                    updateGridOrigin(origin);
                }
            } else {
                if (pointInBounds(mousePos, inputPanel.getBounds())) {
                    //Attempt to scroll down
                    inputPanel.scroll(-zoomRate * 50);
                } else {
                    //Zoom in
                    setZoom(prevZoom * zoomRate);
                    origin.setLocation((origin.x - mousePos.x) * zoomRate +
                            mousePos.x, (origin.y - mousePos.y) * zoomRate +
                            mousePos.y);
                    updateGridOrigin(origin);
                }
            }
            repaint();
        });
    }

    //Function: Point In Bounds
    //@param point              the position to be evaluated
    //       bounds             the boundary to be checked for the point
    //@return                   whether the point exists within the boundary
    private boolean pointInBounds(Point point, Rectangle bounds) {
        return point.x > bounds.getX() && point.x < bounds.getX() +
                bounds.getWidth() && point.y > bounds.getY() && point.y <
                bounds.getY() + bounds.getHeight();
    }

    //Update Grid Origin
    //@param origin             the new origin point
    //Sets the grid origin to the new origin point
    public void updateGridOrigin(Point origin) {
        grid.updateOrigin(origin);
    }

    //Update Grid Functions
    //@param functions          the set of functions inputted by the user
    //       colors             the corresponding colors for each function
    //Updates the function and color list to be rendered by the grid
    public void updateGridFunctions(Function[] functions, Color[] colors) {
        grid.updateFunctions(functions, colors);
    }

    //Function: Set Bounds
    //@param width              the new width
    //       height             the new height
    //Updates the dimensions of the input panel
    public void setBounds(double width, double height) {
        inputPanel.setBounds(0, 0, width, height);
    }

    //Function: Paint Component
    //@param g                  the graphics component
    //Renders the grid, functions on the grid and user elements in the program
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid.paint(g);
    }

    //----------------------------STATIC FUNCTIONS----------------------------//
    //FUNCTION LIST:
    //public static void updateFunctions(Function[] functions, Color[] colors)
    //public static void repaintGraph()
    //public static void setZoom(double zoom)
    //public static double getZoom()
    //public static int getWindowWidth()
    //public static int getWindowHeight()

    //Function: Update Functions
    //@param functions          the list of functions inputted by the user
    //       colors             the corresponding colors of the functions
    //Relays the list of functions inputted by the user to the input panel onto
    //the graph
    public static void updateFunctions(Function[] functions, Color[] colors) {
        graph.updateGridFunctions(functions, colors);
    }

    //Function: Repaint Graph
    //Re-renders all elements in the graph program
    public static void repaintGraph() {
        graph.repaint();
    }

    //Function: Set Zoom
    //@param zoom               the amount by which the graph is zoomed
    //Sets how zoomed in/out the coordinate plane will be
    public static void setZoom(double zoom) {
        GraphProgram.zoom = zoom;
    }

    //Function: Get Zoom
    //@return                   the amount by which the graph is zoomed
    public static double getZoom() {
        return zoom;
    }

    //Function: Get Window Width
    //@return                   the width of the program window
    public static int getWindowWidth() {
        return width;
    }

    //Function: Get Window Height
    //@return                   the height of the program window
    public static int getWindowHeight() {
        return height;
    }

    public static void main(String[] args) {
        width = 1500;
        height = 1000;
        zoom = 100;
        graph = new GraphProgram();

        window = new JFrame();
        window.add(graph);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(new Dimension((int)width, (int)height));
        window.setTitle("Graph Plane");
        window.setVisible(true);

        window.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                Rectangle newWindow = window.getBounds();
                width = (int)newWindow.getWidth();
                height = (int)newWindow.getHeight();
                graph.setBounds(450, height);
            }
        });
    }
}
