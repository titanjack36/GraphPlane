//----------------------------GRAPH PROGRAM CLASS-----------------------------//
//@author TitanJack
//@version 1.0 (2019-04-13)
//Graph Plane is a user friendly 2D graphing software that plots simple
//functions inputted by the user. The program implements the MathTools API which
//helps to parse and compute expressions entered by the user. The Java swing
//graphics library is also used to create an intuitive user interface.

package Grapher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings({"WeakerAccess"})
public class GraphProgram extends JPanel {

    private static int width;
    private static int height;
    private static double zoom;
    private static double prevZoom;
    private static Point origin;
    private static boolean gridLine;
    private static boolean darkMode;
    private static JFrame window;
    private static GraphProgram graph;
    private static GraphTheme defaultTheme;
    private static GraphTheme darkTheme;

    private Grid grid;
    private InputPanel inputPanel;
    private SettingsPanel settingsPanel;

    private Point mousePos;

    private GraphProgram() {
        //Center point of the program window
        grid = new Grid();

        inputPanel = new InputPanel(0, 0, 450, height);
        inputPanel.setBorder(new EmptyBorder(75, 50, 10, 0));
        add(inputPanel);

        settingsPanel = new SettingsPanel(width - 100, 0,
                550, height);
        add(settingsPanel);

        //Use layout defined by (x,y) coordinates instead
        setLayout(null);
        setBounds(width, height);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int shiftX = e.getX() - mousePos.x;
                int shiftY = e.getY() - mousePos.y;
                //When dragged, set the origin to follow the motion of the mouse
                origin.setLocation(origin.x + shiftX, origin.y + shiftY);
                mousePos = e.getPoint();
                repaint();
            }
        });
        this.addMouseWheelListener(e -> {
            double prevZoom = zoom;
            double zoomRate = Math.pow(1.05, e.getScrollAmount());

            Rectangle inputPanelBounds = inputPanel.getBounds();
            inputPanelBounds.width -= 50;
            if (e.getWheelRotation() > 0) {

                //If mouse is touching input panel
                if (pointInBounds(mousePos, inputPanelBounds)) {
                    //Attempt to scroll down
                    inputPanel.scroll(zoomRate * 50);
                } else {
                    //Zoom out
                    setZoom(prevZoom / zoomRate, mousePos);
                }
            } else {
                if (pointInBounds(mousePos, inputPanelBounds)) {
                    //Attempt to scroll down
                    inputPanel.scroll(-zoomRate * 50);
                } else {
                    //Zoom in
                    setZoom(prevZoom * zoomRate, mousePos);
                }
            }
            repaint();
        });
    }

    //Update Grid Functions
    //@param functions          the set of functions inputted by the user
    //       colors             the corresponding colors for each function
    //Updates the function and color list to be rendered by the grid
    public void updateGridFunctions(FunctionSet[] functions) {
        grid.updateFunctions(functions);
    }

    public boolean isSettingsPanelHidden() {
        return settingsPanel.isHidden();
    }

    //Function: Set Bounds
    //@param width              the new width
    //       height             the new height
    //Updates the dimensions of the input panel
    private void setBounds(double width, double height) {
        inputPanel.setBounds(450, height);
        settingsPanel.setBounds(width - 100, 0, 550, height);
    }

    //Function: Paint Component
    //@param g                  the graphics component
    //Renders the grid, functions on the grid and user elements in the program
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid.paintGraph(mousePos, g);
    }

    //----------------------------STATIC FUNCTIONS----------------------------//
    //FUNCTION LIST:
    //private static boolean pointInBounds(Point point, Rectangle bounds)
    //public static GraphProgram getGraph()
    //public static void repaintGraph()
    //public static void setZoom(double zoom, Point refPos)
    //public static void recenterOrigin()
    //public static void toggleGridLine()
    //public static void toggleDarkMode()
    //public static double getZoom()
    //public static double getPrevZoom()
    //public static int getWindowWidth()
    //public static int getWindowHeight()
    //public static Point getOrigin()
    //public static boolean isGridLineActive()
    //public static boolean isDarkModeActive()
    //public static GraphTheme getTheme()

    //Function: Point In Bounds
    //@param point              the position to be evaluated
    //       bounds             the boundary to be checked for the point
    //@return                   whether the point exists within the boundary
    private static boolean pointInBounds(Point point, Rectangle bounds) {
        return point.x > bounds.getX() && point.x < bounds.getX() +
                bounds.getWidth() && point.y > bounds.getY() && point.y <
                bounds.getY() + bounds.getHeight();
    }

    //Function: Get Graph
    //@return                   the graph object
    public static GraphProgram getGraph() {
        return graph;
    }

    //Function: Repaint Graph
    //Re-renders all elements in the graph program
    public static void repaintGraph() {
        graph.repaint();
    }

    //Function: Set Zoom
    //@param zoom               the amount by which the graph is zoomed
    //       refPos             a reference position which the graph zooms
    //                          according to
    //Sets how zoomed in/out the coordinate plane will be
    public static void setZoom(double zoom, Point refPos) {

        if (zoom > 1e-7 && zoom < 1e10) {
            double zoomRate = zoom / GraphProgram.zoom;
            prevZoom = GraphProgram.zoom;
            GraphProgram.zoom = zoom;
            if (refPos != null) {
                //Because the graph zooms in at where the reference location is
                //and not at the origin, the origin must adjust its position
                //to appear as if the graph is focused on where the reference
                //position is
                if (!pointInBounds(refPos, new Rectangle(origin.x - 50,
                        origin.y - 50, 100, 100)))
                    origin.setLocation((origin.x - refPos.x) * zoomRate +
                        refPos.x, (origin.y - refPos.y) * zoomRate +
                        refPos.y);
            }
        }
    }

    //Function: Recenter Origin
    //Moves the origin position of the graph to the center of the program
    public static void recenterOrigin() {
        Point center = new Point(width / 2, height / 2);
        origin.setLocation(center.x, center.y);
    }

    //Function: Toggle Grid Line
    //Sets grid lines to be visible or invisible
    public static void toggleGridLine() {
        gridLine = !gridLine;
    }

    //Function: Toggle Dark Mode
    //Turns dark mode on and off
    public static void toggleDarkMode() {
        darkMode = !darkMode;
    }

    //Function: Get Zoom
    //@return                   the amount by which the graph is zoomed
    public static double getZoom() {
        return zoom;
    }

    public static double getPrevZoom() {
        return prevZoom;
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

    //Function: Get Origin
    //@return                   the position of the origin in the pixel/graphic
    //                          coordinate system
    public static Point getOrigin() {
        return origin;
    }

    //Function: Is Grid Line Active
    //@return                   whether grid lines should be displayed on the
    //                          graph
    public static boolean isGridLineActive() {
        return gridLine;
    }

    //Function: Is Dark Mode Active
    //@return                   whether dark theme is enabled for the graph
    //                          program
    public static boolean isDarkModeActive() {
        return darkMode;
    }

    //Function: Get Theme
    //@return                   the color specifications for each component
    //                          in the graph program
    public static GraphTheme getTheme() {
        return darkMode ? darkTheme : defaultTheme;
    }

    public static void main(String[] args) {
        defaultTheme = new GraphTheme(
                new Color(245, 245, 245), new Color(105, 105, 105),
                new Color(215, 215, 215), new Color(114, 114, 114),
                new Color(235, 235, 235, 200), Color.WHITE,
                new Color(114, 114, 114));
        darkTheme = new GraphTheme(
                new Color(50, 54, 62), new Color(155, 155, 155),
                new Color(105, 105, 105), new Color(215, 215, 215),
                new Color(65, 72, 80, 200),
                new Color(215, 215, 215), new Color(55, 55, 55));

        width = 1500;
        height = 1000;
        zoom = 100;
        Point center = new Point(width / 2, height / 2);
        origin = new Point(center.x, center.y);
        graph = new GraphProgram();
        gridLine = false;
        darkMode = false;

        window = new JFrame();
        window.add(graph);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(new Dimension(width, height));
        window.setTitle("Graph Plane");
        window.setVisible(true);

        window.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                Rectangle newWindow = window.getBounds();
                width = (int)newWindow.getWidth();
                height = (int)newWindow.getHeight();
                graph.setBounds(width, height);
            }
        });
    }
}
