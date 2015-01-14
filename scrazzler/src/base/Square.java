/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Tom
 */
public class Square extends JPanel {
    
    private int xPos;
    private int yPos;
    
    private JLabel labelLetter = null; 
    private JLabel labelScore = null;  

    
    private Box vertBox;
    private Box letterBox;
    private Box scoreBox;

    private boolean isBlank;
   


    public Square()
    {
    }
    public Square(int xPos, int yPos, Color c)
    {
        super();
        this.xPos = xPos;
        this.yPos = yPos;
        setBackground(c);
        setLayout(new BorderLayout());
        
        isBlank = true;

        labelLetter = new JLabel();
        labelLetter.setAlignmentX(0f);
        labelLetter.setAlignmentY(0f);
        labelScore = new JLabel();
        labelScore.setAlignmentX(0f);
        labelScore.setAlignmentY(0f);

        letterBox =  Box.createHorizontalBox();
        letterBox.setPreferredSize(new Dimension(20,20));
        scoreBox =  Box.createHorizontalBox();
        scoreBox.setPreferredSize(new Dimension(5,5));
        
        letterBox.add(labelLetter);
        scoreBox.add(labelScore);
         
        vertBox = Box.createVerticalBox();

        vertBox.add(letterBox);
        vertBox.add(scoreBox, -1);
        
        add(vertBox,BorderLayout.CENTER);
    }

    public int getXPos(){
        return xPos;
    }
    public int getYPos(){
        return yPos;
    }

    public void setIsBlank(boolean b){
        isBlank = b;
    }
    public boolean getIsBlank(){
        return isBlank;
    }


    //Letter
    public void setLetter(String s){
        labelLetter.setText(s.toUpperCase());
        letterBox.add(labelLetter);
        isBlank = false;
        validate();
    }
    public String getLetter(){
        return labelLetter.getText();
    }
    
    //Score
    public void setScore(int s){
        labelScore.setText(String.valueOf(s));
        scoreBox.add(labelScore, -1);
        validate();
    }
    public int getScore(){
        return Integer.parseInt(labelScore.getText());
    }
    
    //ResetColour
//    public Color getResetColour(){
//        return resetColour;
//    }
//    public void setResetColour(Color c){
//        resetColour = c;
//    }

    //Font
    public void setFontSize(int i){
        labelLetter.setFont(new Font("Arial",Font.BOLD,i-2));
        labelScore.setFont(new Font("Arial",Font.ITALIC,i-5));
    }
}