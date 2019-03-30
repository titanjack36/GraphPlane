//-----------------------------GRAPH THEME CLASS------------------------------//
//@author TitanJack
//@project Graph Plane
//The color specifications for every component in the graph program is stored
//inside the graph theme object. Depending on which theme is implemented, the
//colors for text and graphics will change.

package Grapher;

import java.awt.*;

@SuppressWarnings({"WeakerAccess"})
public class GraphTheme {

    private Color backgroundColor;
    private Color axisColor;
    private Color gridLineColor;
    private Color textColor;
    private Color panelColor;
    private Color textBoxColor;
    private Color textBoxTextColor;

    public GraphTheme(Color backgroundColor, Color axisColor, Color
            gridLineColor, Color textColor, Color panelColor, Color
            textBoxColor, Color textBoxTextColor) {
        this.backgroundColor = backgroundColor;
        this.axisColor = axisColor;
        this.gridLineColor = gridLineColor;
        this.textColor = textColor;
        this.panelColor = panelColor;
        this.textBoxColor = textBoxColor;
        this.textBoxTextColor = textBoxTextColor;
    }

    //Function: Get Background Color
    //@return   the color for the graph background
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    //Function: Get Axis Color
    //@return   the color for the graph's axis lines and numbers
    public Color getAxisColor() {
        return axisColor;
    }

    //Function: Get Grid Line Color
    //@return   the color for grid lines on the graph
    public Color getGridLineColor() {
        return gridLineColor;
    }

    //Function: Get Text Color
    //@return   the color of text displayed in the program
    public Color getTextColor() {
        return textColor;
    }

    //Function: Get Panel Color
    //@return   the color of the input panel and any other background panel
    public Color getPanelColor() {
        return panelColor;
    }

    //Function: Get Text Box Color
    //@return   the color for the input text boxes
    public Color getTextBoxColor() {
        return textBoxColor;
    }

    //Function: Get Text Box Text Color
    //@return   the color of text inside the text box
    public Color getTextBoxTextColor() {
        return textBoxTextColor;
    }
}
