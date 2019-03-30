//-----------------------------INPUT BLOCK CLASS------------------------------//
//@author TitanJack
//@project Graph Plane
//An individual input element in the list of inputs from the input panel. Each
//blocks contain a color identifier, text field input, and a button to delete
//the input block itself.

package Grapher;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

@SuppressWarnings({"WeakerAccess"})
public class InputBlock extends JPanel {

    private double width;
    private double height;
    private int id;
    //The section containing the text field and the button
    private JPanel textBlock;
    private JTextField textField;
    private JButton deleteBtn;
    private JButton colorBtn;
    private Color color;
    private boolean invalidInput;

    public InputBlock(int id) {
        color = getRandomColor();
        textField = new JTextField(7);
        textField.setFont(textField.getFont().deriveFont(40f));
        deleteBtn = new JButton("x");
        deleteBtn.setFocusPainted(false);
        deleteBtn.setContentAreaFilled(false);
        colorBtn = new JButton();
        colorBtn.setBackground(color);

        //Give the entire section the look a text field to make it look like the
        //button exists inside of the text field
        textBlock = new JPanel();
        textBlock.add(textField);
        textBlock.add(deleteBtn);
        textBlock.setBackground(textField.getBackground());
        textBlock.setBorder(textField.getBorder());
        textField.setBorder(null);

        this.id = id;
        this.setLayout(null);
        this.add(textBlock);
        this.add(colorBtn);
        this.setOpaque(false);
    }

    //Function: Get Random Color
    //@return                   a color selected at random out of a set of
    //                          choices
    private Color getRandomColor() {
        Random rand = new Random();
        int selection = rand.nextInt(6);
        switch (selection) {
            case 0: return new Color(225, 143, 0);
            case 1: return new Color(0, 191, 0);
            case 2: return new Color(78, 85, 238);
            case 3: return new Color(225, 0, 11);
            case 4: return new Color(71, 204, 255);
            default: return new Color(177, 5, 212);
        }
    }

    //Function: Set Id
    //@param id             the new id to be set
    //Sets a new id to the input block
    public void setId(int id) {
        this.id = id;
    }

    //Set Bounds
    //@param posX           a new x position
    //       posY           a new y position
    //       width          a new width
    //       height         a new height
    //Modifies the position and dimensions of the input block
    public void setBounds(double posX, double posY, double width,
                          double height) {
        super.setBounds((int)posX, (int)posY, (int)width, (int)height);
        this.width = width;
        this.height = height;

        double fieldWidth = width - 150, fieldHeight = height * 0.65;
        colorBtn.setBounds(20, (int)(height / 2 - 20), 40, 40);
        textBlock.setBounds((int)(width - 20 - fieldWidth), (int)
                ((height / 2) - (fieldHeight / 2)), (int)fieldWidth,
                (int)fieldHeight);
    }

    //Function: Set Invalid Input
    //@param invalidInput   whether the text inputted into the text field
    //                      resembles a valid expression
    //Sets the text field to be in a valid/invalid state
    public void setInvalidInput(boolean invalidInput) {
        this.invalidInput = invalidInput;
    }

    //Function: Set New Random Color
    //Updates the function color of the input block to a new random color
    public void setNewRandomColor() {
        color = getRandomColor();
        colorBtn.setBackground(color);
    }

    //Function: Get Text Field
    //@return               the text field object
    public JTextField getTextField() {
        return textField;
    }

    //Function: Get Id
    //@return               the id of this input block
    public int getId() {
        return id;
    }

    //Function: Get Delete Btn
    //@return               the delete button object
    public JButton getDeleteBtn() {
        return deleteBtn;
    }

    //Function: Get Color Btn
    //@return               the button representing the function's color
    public JButton getColorBtn() {
        return colorBtn;
    }

    //Function: Get Color
    //@return               the color of the function for this input block
    public Color getColor() {
        return color;
    }

    //Function: Paint Component
    //@param g              the graphics component
    //Renders the elements in the input block
    public void paintComponent(Graphics g) {
        textField.setBackground(GraphProgram.getTheme().getTextBoxColor());
        textField.setForeground(GraphProgram.getTheme().getTextBoxTextColor());
        textBlock.setBackground(textField.getBackground());
        textBlock.setBorder(textField.getBorder());
        super.paintComponent(g);
        g.setColor(new Color(225, 45, 51, 100));
        //The input block will be highlighted red when an entered expression
        //is invalid
        if (invalidInput) {
            g.fillRect(0, 0, (int)width, (int)height);
        }
        g.setColor(GraphProgram.getTheme().getTextColor());
        g.setFont(new Font("Arial", Font.PLAIN, 40));
        //Equation prompt
        g.drawString("y=", 75, (int)(height / 2 + g.getFontMetrics().
                getHeight() / 2) - 7);
    }
}
