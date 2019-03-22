//---------------------------------AXIS CLASS---------------------------------//
//@author TitanJack
//@project Graph Plane
//Renders the x and y axis including the number scale for each axis which will
//change depending on the scale of the graph.

package Grapher;

import javax.swing.*;
import java.awt.*;

public class Axis extends JPanel {

    private Point origin;

    public Axis(Point origin) {
        this.origin = origin;
    }

    public void updateOrigin(Point origin) {
        this.origin = origin;
    }

    //Function: Round To Powers Of Two
    //@param doubleNum          the number to be rounded
    //@return                   the nearest next power of two to the number
    //Algorithm referenced from
    //https://stackoverflow.com/questions/466204/rounding-up-to-next-power-of-2
    private static int roundToPowersOf2(double doubleNum) {
        int num = (int)Math.round(doubleNum);
        num--;
        num |= num >> 1;
        num |= num >> 2;
        num |= num >> 4;
        num |= num >> 8;
        num |= num >> 16;
        return num + 1;
    }

    //Function: Paint
    //@param g                  the graphics component
    //Renders the axis with respect to the origin
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(3));

        //Is the graph enlarged or shrinked from its original state (zoom = 100)
        boolean zoomOut = false;
        int width = GraphProgram.getWindowWidth();
        double zoom = GraphProgram.getZoom();
        if (zoom < 100) {
            zoom = 10000 / zoom; //invert zoom
            zoomOut = true;
        }
        //The difference between each consecutive value on the axis line,
        //determined by amount of zoom
        //Increment will always go by powers of two in order to keep a
        //consistent scheme
        double increment = zoomOut ? roundToPowersOf2(zoom / 100) :
                1.0 / roundToPowersOf2(zoom / 100);
        //The distance/number of pixels between each consecutive axis value,
        //calculated by converting the increment amount from coordinate system
        //to pixel system by using the zoom ratio (pixels per value in the
        //coordinate system)
        double spacing = GraphProgram.getZoom() * increment;
        //The distance of the starting position (250 beyond the left border)
        //from the position of the origin
        double dxorigin = -250 - origin.x;
        //The coordinate value (must be in whole increments) close to the
        //starting point, cannot be exactly at the starting point or else
        //amount of increments won't be a whole number
        double coordX = (int)(dxorigin / spacing) * increment;
        //The position of this starting coordinate value
        double posX = (int)(dxorigin / spacing) * spacing;

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("0", origin.x - 10, origin.y +
                g.getFontMetrics().getHeight());

        //Cycle through each coordinate value on the x axis which needs to be
        //displayed on screen
        while (posX + origin.x < width + 250) {
            if (coordX != 0) {
                g.drawString(coordX + "", (int) (posX + origin.x),
                        origin.y + g.getFontMetrics().getHeight());
            }
            coordX += increment;
            posX += spacing;
        }

        int height = GraphProgram.getWindowHeight();
        //Starting from 250 beyond the top of the program window
        double dyorigin = -250 - origin.y;
        double coordY = (int)(dyorigin / spacing) * increment;
        double posY = (int)(dyorigin / spacing) * spacing;
        //Displays all coordinate values on the y axis
        while (posY + origin.y < height + 250) {
            if (coordY != 0) {
                g.drawString((-coordY) + "", origin.x + 10,
                        (int) (posY + origin.y));
            }
            coordY += increment;
            posY += spacing;
        }

        //Draws the axis lines
        if (origin.x > -250 && origin.x < GraphProgram.getWindowWidth()
                + 250) {
            g2d.drawLine(origin.x, -250, origin.x,
                    GraphProgram.getWindowHeight() + 250);
        }
        if (origin.y > -250 && origin.y < GraphProgram.getWindowHeight()
                + 250) {
            g2d.drawLine(-2, origin.y, GraphProgram.getWindowWidth()
                    + 250, origin.y);
        }
    }
}
