//-----------------------------GRAPH THEME CLASS------------------------------//
//@author TitanJack
//@project Graph Plane
//The color specifications for every component in the graph program is stored
//inside the graph theme object. Depending on which theme is implemented, the
//colors for text and graphics will change.

package Grapher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings({"WeakerAccess"})
public class GraphTheme {

    private Color backgroundColor;
    private Color axisColor;
    private Color gridLineColor;
    private Color textColor;
    private Color panelColor;
    private Color textBoxColor;
    private Color textBoxTextColor;

    private ImageIcon gridDarkIcon;
    private ImageIcon gridLightIcon;
    private ImageIcon darkModeOnIcon;
    private ImageIcon darkModeOffIcon;
    private ImageIcon focusDarkIcon;
    private ImageIcon focusLightIcon;
    private ImageIcon settingsDarkIcon;
    private ImageIcon settingsLightIcon;

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

        gridDarkIcon = new ImageIcon(getImage("grid_dark.png"));
        gridLightIcon = new ImageIcon(getImage("grid_light.png"));
        darkModeOnIcon = new ImageIcon(getImage(
                "darkmode_on.png"));
        darkModeOffIcon = new ImageIcon(getImage(
                "darkmode_off.png"));
        focusDarkIcon = new ImageIcon(getImage("focus_dark.png"));
        focusLightIcon = new ImageIcon(getImage("focus_light.png"));
        settingsDarkIcon = new ImageIcon(getImage(
                "settings_dark.png"));
        settingsLightIcon = new ImageIcon(getImage(
                "settings_light.png"));
    }

    private Image getImage(String resourceFile) {
        try {
            return ImageIO.read(GraphProgram.class.getResource("/" +
                    resourceFile));
        } catch (Exception e) {
            return new BufferedImage(0, 0, 0);
        }
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

    //Function: Get Grid Icon
    //@return   the icon picture for the toggle grid button, with the color
    //          dependent on whether the graph is in dark theme
    public ImageIcon getGridIcon() {
        return GraphProgram.isDarkModeActive() ? gridLightIcon : gridDarkIcon;
    }

    //Function: Get Dark Mode
    //@return   the icon picture for the toggle dark mode button, with the color
    //          dependent on whether the graph is in dark theme
    public ImageIcon getDarkModeIcon() {
        return GraphProgram.isDarkModeActive() ? darkModeOnIcon :
                darkModeOffIcon;
    }

    //Function: Get Focus Icon
    //@return   the icon picture for the focus button, with the color
    //          dependent on whether the graph is in dark theme
    public ImageIcon getFocusIcon() {
        return GraphProgram.isDarkModeActive() ? focusLightIcon : focusDarkIcon;
    }

    //Function: Get Settings Icon
    //@return   the icon picture for the settings button, with the color
    //          dependent on whether the graph is in dark theme
    public ImageIcon getSettingsIcon() {
        return GraphProgram.isDarkModeActive() ? settingsLightIcon :
                settingsDarkIcon;
    }
}
