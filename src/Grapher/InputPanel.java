//-----------------------------INPUT PANEL CLASS------------------------------//
//@author TitanJack
//@project Graph Plane
//Contains the list of input boxes and stores the functions entered by the user.
//The input panel will allow the user to add, remove and manipulate the input
//boxes to create multiple functions on the graph

package Grapher;

import Functions.Function;
import Functions.FunctionConstructor;
import Functions.InvalidExpressionException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess"})
public class InputPanel extends JPanel {

    private double width;
    private double height;
    private int scrollPos;

    private ArrayList<InputBlock> inputBlocks;
    private ArrayList<FunctionSet> functions;
    private JButton addBtn;
    private JButton togglePanelBtn;
    private boolean hidden;

    public InputPanel(double posX, double posY, double width, double height) {

        inputBlocks = new ArrayList<>();
        functions = new ArrayList<>();
        scrollPos = 0;
        hidden = false;

        addBtn = new JButton("+");
        addBtn.setFocusPainted(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setFont(addBtn.getFont().deriveFont(30f));
        this.add(addBtn);
        addBtn.addActionListener(e -> addInputBlock());

        togglePanelBtn = new JButton("<");
        togglePanelBtn.setFocusPainted(false);
        togglePanelBtn.setContentAreaFilled(false);
        togglePanelBtn.setBorderPainted(false);
        togglePanelBtn.setFont(addBtn.getFont().deriveFont(30f));
        this.add(togglePanelBtn);
        togglePanelBtn.addActionListener(e -> {
            hidden = !hidden;
            if (hidden) {
                togglePanelBtn.setText(">");
                setBounds(-this.width, 0, this.width, this.height);
            } else {
                togglePanelBtn.setText("<");
                setBounds(0, 0, this.width, this.height);
            }
        });

        addInputBlock();
        setBounds(posX, posY, width, height);
        this.setLayout(null);
        this.setOpaque(false);
    }

    //Function: Add Input Block
    //Adds a new block to the list of input blocks on the input panel
    private void addInputBlock() {
        //The input block tracks its position within the list
        InputBlock inputBlock = new InputBlock(inputBlocks.size());
        //No two consecutive input blocks on the list will have the same
        //function color
        while (inputBlocks.size() > 0 && inputBlock.getColor().equals(
                inputBlocks.get(inputBlocks.size() - 1).getColor())) {
            inputBlock.setNewRandomColor();
        }

        JTextField textField = inputBlock.getTextField();
        //Listen for text input
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                evaluateTextInput(inputBlock);
            }
            public void removeUpdate(DocumentEvent e) {
                evaluateTextInput(inputBlock);
            }
            public void insertUpdate(DocumentEvent e) {
                evaluateTextInput(inputBlock);
            }
        });

        JButton deleteBtn = inputBlock.getDeleteBtn();
        //Listen for deleting the input block
        deleteBtn.addActionListener(e -> removeInputBlock(inputBlock));

        inputBlocks.add(inputBlock);
        //Add place holders for the function and color of the input block
        functions.add(new FunctionSet(inputBlock.getColor()));
        this.add(inputBlock);
        this.revalidate();

        JButton colorBtn = inputBlock.getColorBtn();
        colorBtn.addActionListener(e -> {
            FunctionSet function = functions.get(inputBlock.getId());
            function.toggleHidden();
            colorBtn.setBackground(function.isHidden() ? new Color(0, 0,
                    0, 0) : function.getColor());
            GraphProgram.updateFunctions(functions.toArray(new FunctionSet[0]));
            GraphProgram.repaintGraph();
        });

        //Scrolls page down if the new element added to the list goes beyond
        //the bottom of the page
        if (getListHeight() - scrollPos > height) {
            scrollPos = (int) (getListHeight() - height);
        }
        if (scrollPos < 0) scrollPos = 0;
        updateListPositions();
    }

    //Function: Remove Input Block
    //@param inputBlock             the input block to be removed
    //Disposes the specified input block from the list, including the function
    //it contained
    private void removeInputBlock(InputBlock inputBlock) {
        //If its the only input block, it will not be disposed, only emptied
        if (inputBlocks.size() == 1) {
            inputBlock.getTextField().setText("");
        } else {
            int index = inputBlock.getId();
            inputBlocks.remove(index);
            functions.remove(index);
            this.remove(inputBlock);
            this.revalidate();

            GraphProgram.updateFunctions(functions.toArray(new FunctionSet[0]));
            //Shifts the indexes stored by the input blocks below up
            for (int i = index; i < inputBlocks.size(); i++)
                inputBlocks.get(i).setId(i);

            //Scrolls the list up if the list no longer occupies the bottom
            //of the window but still has a height greater than that of the
            //window
            if (scrollPos > 0 && getListHeight() - scrollPos < height) {
                scrollPos = (int)(getListHeight() - height);
            }
            updateListPositions();
            GraphProgram.repaintGraph();
        }
    }

    //Function: Evaluate Text Input
    //@param inputBlock             verification of the text inputted into this
    //                              block
    //Checks the text entered into the text field of this block to see if it
    //is a proper expression
    private void evaluateTextInput(InputBlock inputBlock) {

        JTextField textField = inputBlock.getTextField();
        int index = inputBlock.getId();
        FunctionConstructor fc = new FunctionConstructor();
        Function function = null;
        try {
            function = fc.toFunction(textField.getText());
            inputBlock.setInvalidInput(false);
        } catch (InvalidExpressionException e) {
            if (textField.getText().length() == 0)
                inputBlock.setInvalidInput(false);
            else
                inputBlock.setInvalidInput(true);
        }
        functions.get(index).setFunction(function);
        GraphProgram.updateFunctions(functions.toArray(new FunctionSet[0]));
        GraphProgram.repaintGraph();
    }

    //Function: Scroll
    //@param scrollAmt          the distance to be scrolled
    //Scrolls the list of input boxes up or down by a specified amount
    public void scroll(double scrollAmt) {
        int listHeight = getListHeight();

        if (listHeight > height) {
            scrollPos += scrollAmt;
            //Negative scroll means the page scrolled too far up
            if (scrollPos < 0) {
                scrollPos = 0;
            }
            //This means the page has scrolled too far down
            if (listHeight - scrollPos < height && listHeight - (scrollPos
                    - scrollAmt) > height) {
                scrollPos = (int) (listHeight - height);
            }
        }

        updateListPositions();
        GraphProgram.repaintGraph();
    }

    //Function: Set Bounds
    //@param width              the new width
    //       height             the new height
    //Modifies the dimensions of the input panel while keeping its position
    //the same
    public void setBounds(double width, double height) {
        setBounds(getX(), getY(), width, height);
    }

    //Function: Set Bounds
    //@param posX               the new x position
    //       posY               the new y position
    //       width              the new width
    //       height             the new height
    //Modifies the position and dimensions of the input panels, thereby
    //changing the state of the input list
    public void setBounds(double posX, double posY, double width, double
            height) {

        super.setBounds((int)posX, (int)posY, (int)width + 50, (int)height);
        this.width = width;
        this.height = height;

        //If the window height has increased such that there is empty space
        //below the list, then scroll the list up if it is in the scrolled
        //down state
        if (scrollPos > 0 && getListHeight() - scrollPos < height) {
            scrollPos = (int) (getListHeight() - height);
        }
        if (scrollPos < 0) scrollPos = 0;

        togglePanelBtn.setBounds((int)(width) - 15, 0, 75, 75);
        updateListPositions();
    }

    //Function: Update List Positions
    //Modifies the positions of each element in the input panel list according
    //to their position in the list and the amount of scrolling done by the user
    private void updateListPositions() {
        for (int i = 0; i < inputBlocks.size(); i++) {
            inputBlocks.get(i).setBounds(0, 100 + (i * 100) -
                    scrollPos, width, 100);
        }
        addBtn.setBounds((int)(width / 2) - 200, 100 + inputBlocks.size()
                * 100 - scrollPos, 400, 50);
    }

    //Function: Get List Height
    //@return                   the height of the list containing all scrollable
    //                          elements in the input panel
    private int getListHeight() {
        return (300 + inputBlocks.size() * 100);
    }

    //Function: Paint Component
    //@param g                  the graphics component
    //Renders the list of input block components and other panel graphics
    @Override
    public void paintComponent(Graphics g) {
        Color btnTextColor = GraphProgram.getTheme().getTextColor();
        addBtn.setForeground(btnTextColor);
        togglePanelBtn.setForeground(btnTextColor);
        super.paintComponent(g);

        g.setColor(GraphProgram.getTheme().getPanelColor());
        g.fillRect(0, 0, (int)width, (int)height);
        g.fillRect((int)width, 0, 50, 75);
        g.setColor(GraphProgram.getTheme().getTextColor());
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Graph Plane", 20, 55 - scrollPos);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Early Dev Edition V0.2", 25, 90 - scrollPos);
    }
}
