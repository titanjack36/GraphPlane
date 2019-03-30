//-----------------------------FUNCTION SET CLASS-----------------------------//
//@author TitanJack
//@project Graph Plane
//The Function Set class holds the function being displayed on the graph as well
//as it's properties such as color and whether it is hidden

package Grapher;

import Functions.Function;

import java.awt.*;

@SuppressWarnings({"WeakerAccess"})
public class FunctionSet {

    private Function function;
    private Color color;
    private boolean hidden;

    public FunctionSet(Color color) {
        function = null;
        this.color = color;
        hidden = false;
    }

    //Function: Can Be Displayed
    //@return                   whether the function contains all of it's
    //                          properties to be properly displayed on the graph
    public boolean canBeDisplayed() {
        return function != null && color != null && !hidden;
    }

    //Function: Get Function
    //@return                   the function object
    public Function getFunction() {
        return function;
    }

    //Function: Get Color
    //@return                   the color of the function
    public Color getColor() {
        return color;
    }

    //Function: Is Hidden
    //@return                   whether or not the function should be displayed
    public boolean isHidden() {
        return hidden;
    }

    //Function: Set Function
    //@param function           new function object
    //Assigns new function object
    public void setFunction(Function function) {
        this.function = function;
    }

    //Function: Set Color
    //@param color              new color
    //Assigns new color
    @SuppressWarnings("unused")
    public void setColor(Color color) {
        this.color = color;
    }

    //Function: Toggle Hidden
    //Cycles between hidden and not hidden
    public void toggleHidden() {
        hidden = !hidden;
    }

    //Function: Paint
    //@param zoom               the current zoom ratio of the graph
    //       origin             the graphical coordinate points of the origin
    //       g                  the graphics component
    public void paint(double zoom, Point origin, Graphics g) {

        int width = GraphProgram.getWindowWidth();
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(color);
        //The coordinate system relative to the program window
        int posX = -250;
        //The coordinate system relative to the origin
        double coordX = (posX - origin.x) / zoom;
        double lastVal = function.compute(coordX);
        //From left to right (250 beyond the left edge and 250
        //beyond the right edge), cycles through every pixel to
        //render a piece of the function
        while (posX < width + 250) {
            posX++;
            coordX = (posX - origin.x) / zoom;
            double currentVal = function.compute(coordX);
            //Draws a line that connects the function values at the
            //current and previous pixel position
            g2d.drawLine(posX - 1, origin.y - (int) (lastVal
                    * zoom), posX, origin.y - (int) (currentVal *
                    zoom));
            lastVal = currentVal;
        }
    }
}
