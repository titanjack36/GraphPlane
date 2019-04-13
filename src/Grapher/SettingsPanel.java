//-----------------------------INPUT PANEL CLASS------------------------------//
//@author TitanJack
//@project Graph Plane
//The settings panel allows the user to adjust properties of the graph program
//such as the color theme, change the zoom, show grid lines, etc. The settings
//panel is hidden in the beginning but can be displayed by clicking the settings
//button at the bottom right corner of the program.

package Grapher;

import NumberFormats.Numbers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

@SuppressWarnings("WeakerAccess")
public class SettingsPanel extends JPanel {

    private JButton closePanelBtn;
    private JButton decreaseZoomBtn;
    private JButton increaseZoomBtn;
    private JButton gridLineBtn;
    private JButton darkModeBtn;
    private JButton centerOriginBtn;
    private JButton settingsBtn;
    private JTextField zoomInput;

    private boolean invalidZoomInput;
    private boolean zoomInputSelected;
    private boolean hidden;
    private int posOffset;
    private int elapsedTime;

    public SettingsPanel(double posX, double posY, double width,
                         double height) {

        //To close/hide the settings panel
        closePanelBtn = new JButton("x");
        mountJButton(closePanelBtn, 35f, -1);
        closePanelBtn.addActionListener(e -> togglePanel());

        //Zooms out the graph
        decreaseZoomBtn = new JButton("-");
        mountJButton(decreaseZoomBtn, 35f, -1);
        decreaseZoomBtn.addActionListener(e -> {
            Point center = new Point(GraphProgram.getWindowWidth() / 2,
                    GraphProgram.getWindowHeight() / 2);
            GraphProgram.setZoom(GraphProgram.getZoom() / 1.5, center);
            GraphProgram.repaintGraph();
        });

        //Zooms into the graph
        increaseZoomBtn = new JButton("+");
        mountJButton(increaseZoomBtn, 35f, -1);
        increaseZoomBtn.addActionListener(e -> {
            Point center = new Point(GraphProgram.getWindowWidth() / 2,
                    GraphProgram.getWindowHeight() / 2);
            GraphProgram.setZoom(GraphProgram.getZoom() * 1.5, center);
            GraphProgram.repaintGraph();
        });

        //Display the settings menu
        settingsBtn = new JButton();
        mountJButton(settingsBtn, 25f, -1);
        settingsBtn.addActionListener(e -> togglePanel());

        //Center the graph to the origin
        centerOriginBtn = new JButton();
        mountJButton(centerOriginBtn, 25f, -1);
        centerOriginBtn.addActionListener(e -> {
            GraphProgram.recenterOrigin();
            GraphProgram.repaintGraph();
        });

        //Toggle visibility of grid lines on the graph
        gridLineBtn = new JButton("  Grid Lines");
        mountJButton(gridLineBtn, 25f, SwingConstants.CENTER);
        gridLineBtn.addActionListener(e -> {
            GraphProgram.toggleGridLine();
            GraphProgram.repaintGraph();
        });

        //Toggle color theme to be light or dark
        darkModeBtn = new JButton("  Dark Mode");
        mountJButton(darkModeBtn, 25f, SwingConstants.CENTER);
        darkModeBtn.addActionListener(e -> {
            GraphProgram.toggleDarkMode();
            GraphProgram.repaintGraph();
        });

        //Input for user specified zoom value
        zoomInput = new JTextField();
        zoomInput.setFont(zoomInput.getFont().deriveFont(30f));
        zoomInput.setHorizontalAlignment(SwingConstants.CENTER);
        zoomInput.addActionListener(e -> evaluateTextInput(
                zoomInput.getText()));
        zoomInput.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                zoomInputSelected = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                invalidZoomInput = false;
                zoomInputSelected = false;
                GraphProgram.repaintGraph();
            }
        });
        this.add(zoomInput);

        hidden = true;
        posOffset = 0;
        setBounds(posX, posY, width, height);
        this.setLayout(null);
        this.setOpaque(false);
    }

    //Function: Evaluate Text Input
    //@param input          the text added to the zoom text field by the user
    //Checks whether the input is a valid zoom value before setting it as
    //the zoom, or else set the state of the text field to be invalid
    private void evaluateTextInput(String input) {
        try {
            if (input.charAt(input.length() - 1) == '%')
                input = input.substring(0, input.length() - 1);
            Point center = new Point(GraphProgram.getWindowWidth() / 2,
                    GraphProgram.getWindowHeight() / 2);
            //Zoom in or out relative to the center point of the graph program
            GraphProgram.setZoom(Double.parseDouble(input), center);
            zoomInput.setText(Numbers.trimDigits(GraphProgram.getZoom(),
                    3) + "%");
            invalidZoomInput = false;
        } catch (Exception e) {
            invalidZoomInput = true;
        }
        GraphProgram.repaintGraph();
    }

    //Function: Toggle Panel
    //Animates the movement of the panel when it is being retracted from it's
    //visible state or being extended from it's hidden state
    private void togglePanel() {
        elapsedTime = 0;
        hidden = !hidden;
        if (!hidden) {
            settingsBtn.setVisible(false);
            centerOriginBtn.setVisible(false);
        }

        Timer panelCloseTimer = new Timer(1, null);
        panelCloseTimer.addActionListener(e -> {
            int width = getWidth(), height = getHeight();
            int hiddenPos = GraphProgram.getWindowWidth() - 100;
            //Uses the formula for a parabola to control the movement of the
            //panel, movement speed decreases as time increases
            if (hidden) {
                posOffset = (int)(-(elapsedTime - 21.2) * (elapsedTime - 21.2)
                        + 1);
                setBounds(hiddenPos, 0.0, width, height);
            } else {
                posOffset = (int)((elapsedTime - 21.2) * (elapsedTime - 21.2) -
                        width - 1);
                setBounds(hiddenPos, 0.0, width, height);
            }

            elapsedTime++;
            //When the panel reaches or crosses the end point, set it back to
            //the valid end point position
            if (getX() <= hiddenPos + 100 - width || getX() >= hiddenPos) {
                posOffset = getX() >= hiddenPos ? 0 : 100 - width;
                setBounds(hiddenPos, 0.0, width, height);
                panelCloseTimer.stop();
                if (hidden) {
                    settingsBtn.setVisible(true);
                    centerOriginBtn.setVisible(true);
                }
            }
        });
        panelCloseTimer.start();
    }

    //Function: Mouse JButton
    //@param btn            the button to be initialized
    //       fontSize       the font size of text within the button
    //       alignment      the alignment of content inside the button
    //Changes the properties of the button at the initialization stage to match
    //with the theme of the program
    private void mountJButton(JButton btn, float fontSize, int alignment) {
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFont(btn.getFont().deriveFont(fontSize));
        if (alignment != -1)
            btn.setHorizontalAlignment(alignment);
        //btn.setBorder(new RoundedBorder(20));
        //btn.setForeground(GraphProgram.getTheme().getTextColor());
        this.add(btn);
    }

    //Function: Is Hidden
    //@return           whether or not the settings panel is in it's hidden
    //                  state
    public boolean isHidden() {
        return hidden;
    }

    //Function: Set Bounds
    //@param posX               the new x position
    //       posY               the new y position
    //       width              the new width
    //       height             the new height
    //Modifies the position and dimensions of the settings panel, thereby
    //changing the state of the input list
    public void setBounds(double posX, double posY, double width,
                           double height) {
        posX += posOffset;
        super.setBounds((int)posX, (int)posY, (int)width,
                (int)height);
        int center = (getWidth() - 100) / 2 + 90;
        closePanelBtn.setBounds(120, 20, 60, 60);
        decreaseZoomBtn.setBounds(140, 150, 60, 60);
        increaseZoomBtn.setBounds((int)width - 125, 150, 60,
                60);
        centerOriginBtn.setBounds(0, (int)height - 230, 60, 60);
        settingsBtn.setBounds(0, (int)height - 150, 60, 60);
        gridLineBtn.setBounds(center - 170, (int)height - 230, 340, 60);
        darkModeBtn.setBounds(center - 170, (int)height - 150, 340, 60);
        zoomInput.setBounds(center - 100, 150, 200, 60);
    }

    //Function: Paint Component
    //@param g          the graphics component
    //Renders the contents of the settings panel, including buttons and text
    //fields for settings and the panel background
    @Override
    public void paintComponent(Graphics g) {
        GraphTheme theme = GraphProgram.getTheme();
        Color textColor = GraphProgram.getTheme().getTextColor();
        closePanelBtn.setForeground(textColor);
        decreaseZoomBtn.setForeground(textColor);
        increaseZoomBtn.setForeground(textColor);
        centerOriginBtn.setIcon(theme.getFocusIcon());
        gridLineBtn.setIcon(theme.getGridIcon());
        darkModeBtn.setIcon(theme.getDarkModeIcon());
        settingsBtn.setIcon(theme.getSettingsIcon());
        gridLineBtn.setForeground(textColor);
        darkModeBtn.setForeground(textColor);
        if (!zoomInputSelected)
            zoomInput.setText(Numbers.trimDigits(GraphProgram.getZoom(),
                    3) + "%");
        if (invalidZoomInput)
            zoomInput.setBackground(new Color(230, 143, 146));
        else
            zoomInput.setBackground(theme.getTextBoxColor());
        zoomInput.setForeground(theme.getTextBoxTextColor());
        super.paintComponent(g);
        g.setColor(theme.getPanelColor());
        g.fillRect(100, 0, getWidth() - 100, getHeight());
        g.setColor(theme.getTextColor());
        g.setFont(new Font("Arial", Font.BOLD, 40));
        String text = "Settings";
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (getWidth() - 100) / 2 + 100 - textWidth / 2,
                65);
    }
}
