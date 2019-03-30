//---------------------------------GRID CLASS---------------------------------//
//@author TitanJack
//@project Graph Plane
//The Grid class renders the axis and functions inputted by the user. The grid
//position and scale will be change dynamically based on the interactions of
//the user.

package Grapher;

import java.awt.*;
import java.text.DecimalFormat;

@SuppressWarnings({"WeakerAccess"})
public class Grid {

    private Axis axis;
    private FunctionSet[] functions;

    public Grid() {
        functions = new FunctionSet[0];
        axis = new Axis();
    }

    //Function: Update Functions
    //@param functions          new list of functions inputted by the user
    //       colors             the corresponding colors for each function
    //Updates the set of functions to be rendered by the grid
    public void updateFunctions(FunctionSet[] functions) {
        this.functions = functions;
    }

    //Function: Trim Digits
    //@param num                the number to be trimmed
    //       numOfDigits        how many decimal places to be trimmed to
    //@return                   the trimmed number
    //Removes digits from the number which are larger than the specified number
    //of digits
    @SuppressWarnings("SameParameterValue")
    private String trimDigits(double num, int numOfDigits) {
        if (numOfDigits == 0) {
            return (int)(num + 0.5) + "";
        } else {
            String format = "#.";
            for (int i = 0; i < numOfDigits; i++) format = format.concat("#");
            DecimalFormat df = new DecimalFormat(format);
            return df.format(num);
        }
    }

    //Function: Paint
    //@param g                  the graphics component
    //Renders the axis and functions to the screen
    public void paintGraph(Point mousePos, Graphics g) {
        Point origin = GraphProgram.getOrigin();
        g.setColor(GraphProgram.getTheme().getBackgroundColor());
        g.fillRect(0, 0, GraphProgram.getWindowWidth(),
                GraphProgram.getWindowHeight());
        axis.paint(g);

        //Note: at default zoom = 100, every 100 pixels will be a value 1 in
        //the coordinate system
        double zoom = GraphProgram.getZoom();
        double minOffset = 0, targetY = 0;
        int selectedIndex = -1;
        for (int i = 0; i < functions.length; i++) {
            if (functions[i].canBeDisplayed()) {
                functions[i].paint(zoom, origin, g);

                //Finds the function that is closest to the mouse vertically
                //at the current x position of the mouse
                if (mousePos != null) {
                    //The y value of the function with respect to the pixel
                    //coordinate system
                    double yVal = origin.y - functions[i].getFunction().
                            compute((mousePos.x - origin.x) / zoom) * zoom;
                    double offset = Math.abs(mousePos.y - yVal);
                    //If the function is the first visible one on the list or if
                    //it is the closest
                    if (selectedIndex == -1 || offset < minOffset) {
                        minOffset = offset;
                        selectedIndex = i;
                        targetY = yVal;
                    }
                }
            }
        }

        //The closest graph must be within 100 pixels to be considered close to
        //the mouse
        if (selectedIndex != -1 && minOffset < 100) {

            //Print the coordinate numbers of the point on the function closest
            //to the mouse
            String coordText = "(" + trimDigits((mousePos.x - origin.x)
                    / zoom, 2) + ", " + trimDigits((origin.y -
                    targetY) / zoom, 2) + ")";
            g.setColor(GraphProgram.getTheme().getPanelColor());
            g.setFont(new Font("Arial", Font.BOLD, 20));
            int textHeight = g.getFontMetrics().getHeight();
            g.fillRect(mousePos.x + 8, (int)targetY - textHeight - 8,
                    g.getFontMetrics().stringWidth(coordText) + 4,
                    textHeight + 5);
            g.setColor(GraphProgram.getTheme().getTextColor());
            g.drawString(coordText, mousePos.x + 10, (int)targetY - 10);
            //Draw the point on the function
            g.setColor(functions[selectedIndex].getColor());
            g.fillOval(mousePos.x - 8, (int)targetY - 8,
                    16, 16);
        }
    }
}
