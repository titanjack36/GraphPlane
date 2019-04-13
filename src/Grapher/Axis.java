//---------------------------------AXIS CLASS---------------------------------//
//@author TitanJack
//@project Graph Plane
//Renders the x and y axis including the number scale for each axis which will
//change depending on the scale of the graph.

package Grapher;

import NumberFormats.Numbers;

import java.awt.*;

@SuppressWarnings({"WeakerAccess"})
public class Axis {

    private int prevIndex;
    private double prevRenderedZoom;
    private double increment;
    private double spacing;

    public Axis() {
        prevIndex = 0;
        prevRenderedZoom = 0;
        increment = 0;
        spacing = 0;
    }

    //Function: Paint
    //@param g                  the graphics component
    //Renders the axis with respect to the origin
    public void paint(Graphics g) {

        Point origin = GraphProgram.getOrigin();
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D)g;
        Color axisColor = GraphProgram.getTheme().getAxisColor();

        int width = GraphProgram.getWindowWidth();
        int height = GraphProgram.getWindowHeight();
        double zoom = GraphProgram.getZoom();
        //Only update increment and spacing if it is the first render or if
        //the zoom has changed
        if (prevRenderedZoom == 0 || zoom != prevRenderedZoom) {
            prevRenderedZoom = zoom;
            int index = prevIndex;
            boolean spacingMatched = false;
            //Depending on whether the user zoomed in or out, will increase or
            //decrease the increment until the spacing is not too big or not
            //too small
            while (!spacingMatched && index < 30 && index > -30) {
                //The difference between each consecutive value on the axis
                //line, determined by amount of zoom
                increment = (Math.pow((((index % 3) + 3) % 3), 2) + 1) *
                        Math.pow(10, Math.floor(index / 3.0));
                //The distance/number of pixels between each consecutive axis
                //value, calculated by converting the increment amount from
                //coordinate system to pixel system by using the zoom ratio
                //(pixels per value in the coordinate system)
                spacing = zoom * increment;
                spacingMatched = spacing >= 100 && spacing < 200;
                if (!spacingMatched)
                    index = (GraphProgram.getPrevZoom() > zoom ? index + 1 :
                            index - 1);
            }
            if (spacingMatched) {
                prevIndex = index;
            } else {
                index = prevIndex;
                increment = (Math.pow((index % 3.0), 2) + 1) * Math.pow(10,
                        Math.floor(index / 3.0));
                spacing = zoom * increment;
            }
        }
        //The distance of the starting position (250 beyond the left border)
        //from the position of the origin
        double dxorigin = -250 - origin.x;
        //The coordinate value (must be in whole increments) close to the
        //starting point, cannot be exactly at the starting point or else
        //amount of increments won't be a whole number
        double coordX = (int)(dxorigin / spacing) * increment;
        //The position of this starting coordinate value
        double posX = (int)(dxorigin / spacing) * spacing;

        g.setColor(axisColor);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("0", origin.x - 10, origin.y +
                g.getFontMetrics().getHeight());

        //Cycle through each coordinate value on the x axis which needs to be
        //displayed on screen
        while (posX + origin.x < width + 250) {
            if (Math.abs(coordX) > 1e-10) {
                g.setColor(axisColor);
                g.drawString(Numbers.trimDigits(coordX, 3), (int) (posX + origin.x),
                        origin.y + g.getFontMetrics().getHeight());
            }
            if (GraphProgram.isGridLineActive()) {
                g2d.setColor(GraphProgram.getTheme().getGridLineColor());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine((int) (posX + origin.x), -250, (int) (posX +
                        origin.x), height + 250);
                g2d.setStroke(new BasicStroke(1));
                for (int i = 1; i < 4; i++) {
                    g2d.drawLine((int)(posX + origin.x + spacing * i / 4.0),
                            -250, (int)(posX + origin.x + spacing * i / 4.0),
                            height + 250);
                }
            }
            coordX += increment;
            posX += spacing;
        }

        //Starting from 250 beyond the top of the program window
        double dyorigin = -250 - origin.y;
        double coordY = (int)(dyorigin / spacing) * increment;
        double posY = (int)(dyorigin / spacing) * spacing;
        //Displays all coordinate values on the y axis
        while (posY + origin.y < height + 250) {
            if (Math.abs(coordY) > 1e-10) {
                g.setColor(axisColor);
                g.drawString(Numbers.trimDigits(-coordY, 3), origin.x + 10,
                        (int) (posY + origin.y));
            }
            if (GraphProgram.isGridLineActive()) {
                g2d.setColor(GraphProgram.getTheme().getGridLineColor());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(-250, (int) (posY + origin.y),
                        width + 250, (int) (posY + origin.y));
                g2d.setStroke(new BasicStroke(1));
                for (int i = 1; i < 4; i++) {
                    g2d.drawLine(-250, (int)(posY + origin.y + spacing *
                            i / 4.0), width + 250, (int)(posY + origin.y
                            + spacing * i / 4.0));
                }
            }

            coordY += increment;
            posY += spacing;
        }

        g2d.setStroke(new BasicStroke(3));
        g.setColor(axisColor);
        //Draws the axis lines
        if (origin.x > -250 && origin.x < width + 250)
            g2d.drawLine(origin.x, -250, origin.x, height + 250);
        if (origin.y > -250 && origin.y < height + 250)
            g2d.drawLine(-2, origin.y, width + 250, origin.y);
    }
}
