//---------------------------------GRID CLASS---------------------------------//
//@author TitanJack
//@project Graph Plane
//The Grid class renders the axis and functions inputted by the user. The grid
//position and scale will be change dynamically based on the interactions of
//the user.

package Grapher;

import Functions.Function;

import java.awt.*;

public class Grid {

    private Point origin;
    private Axis axis;
    private Function[] functions;
    private Color[] colors;

    public Grid(Point origin) {
        functions = new Function[0];
        colors = new Color[]{Color.BLUE, Color.RED};
        axis = new Axis(origin);
        this.origin = origin;
    }

    //Function: Update Origin
    //@param origin             new point of origin
    //Updates the origin reference point of the grid
    public void updateOrigin(Point origin) {
        this.origin = origin;
        axis.updateOrigin(origin);
    }

    //Function: Update Functions
    //@param functions          new list of functions inputted by the user
    //       colors             the corresponding colors for each function
    //Updates the set of functions to be rendered by the grid
    public void updateFunctions(Function[] functions, Color[] colors) {
        this.functions = functions;
        this.colors = colors;
    }

    //Function: Paint
    //@param g                  the graphics component
    //Renders the axis and functions to the screen
    public void paint(Graphics g) {
        axis.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        int width = GraphProgram.getWindowWidth();
        int height = GraphProgram.getWindowHeight();
        //Note: at default zoom = 100, every 100 pixels will be a value 1 in the
        //coordinate system
        double zoom = GraphProgram.getZoom();
        g2d.setStroke(new BasicStroke(4));
        for (int i = 0; i < functions.length; i++) {
            if (functions[i] != null && colors[i] != null) {
                g2d.setColor(colors[i]);
                //The coordinate system relative to the program window
                int posX = -250;
                //The coordinate system relative to the origin
                double coordX = (posX - origin.x) / zoom;
                double lastVal = functions[i].compute(coordX);
                //From left to right (250 beyond the left edge and 250 beyond
                //the right edge), cycles through every pixel to render a piece
                //of the function
                while (posX < width + 250) {
                    posX++;
                    coordX = (posX - origin.x) / zoom;
                    double currentVal = functions[i].compute(coordX);
                    //Draws a line that connects the function values at the
                    //current and previous pixel position
                    g2d.drawLine(posX - 1, origin.y - (int) (lastVal
                        * zoom),  posX, origin.y - (int) (currentVal *
                            zoom));
                    lastVal = currentVal;
                }
            }
        }
    }
}
