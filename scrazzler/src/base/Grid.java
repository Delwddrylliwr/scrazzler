/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base;
//import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout; 
import java.awt.event.*;
import java.util.ArrayList;
//import java.util.Map;
//import java.util.HashMap;
import javax.swing.*;
import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import run.PlayScreen;
import base.MersenneTwisterFast;

/**
 *
 * @author Tom
 */
public class Grid extends JPanel{
    protected Square selectedSquare;
    
    /********************************/

    protected ArrayList squares;

    private Dawg dawgDic;
    
    private MersenneTwisterFast random;
    
    private PlayScreen playScreen;
    
    private int gridHeight;
    private int gridWidth;
//    private static Map<Character, Integer> letterScores = new HashMap<Character, Integer>(){
//        {
//            put('A', 1);
//            put('B', 1);
//        };
//    };
//    private HashMap<Character, Integer> letterFreqs;
    private int[] letterScores = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 
        3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
    // now for premium tile frequencies, with order: 
    // regular, double letter, triple letter, double word, triple word
    private int[] premiumFreqs = {164, 24, 12, 17, 8};
    private int[] premiumCumFreqs = new int[5];
    
    public int getGridHeight(){ return gridHeight; }
    public int getGridWidth(){ return gridWidth; }
    public int getLetterScore(String letter){ return letterScores[(int) letter.charAt(0) - 'A']; }
    
    public Grid(int gridHeight, int gridWidth, PlayScreen playScreen){
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.playScreen = playScreen;
        setFocusable(true); //allow the jpanel to receive focus
        squares = new ArrayList();
        
        random = new MersenneTwisterFast(System.currentTimeMillis());
        
        //calculate the cumulative premiums
        premiumCumFreqs[0] = premiumFreqs[0];
        for(int premiumIndex=1; premiumIndex<5; premiumIndex++){
            premiumCumFreqs[premiumIndex] = premiumCumFreqs[premiumIndex - 1] 
                    + premiumFreqs[premiumIndex];
        }
        
        //get the relative path for the working directory
        String filePath = new File("").getAbsolutePath();
        System.out.println(filePath);
        try{
            dawgDic = new Dawg(filePath + "\\Traditional_Dawg_For_Word-List.dat");
//            dawgDic = new Dawg(filePath + "\\dawg_dic_en.dat");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // allocate squares to the grid
        setLayout(new GridLayout(gridHeight, gridWidth));
//        addKeyListener(new KListener());
        this.setBackground(Color.BLUE);
    }
    
    public void Build(){
        for (int i = 0; i < gridHeight; i++){
            for (int j = 0; j < gridWidth; j++){
                Square s;
                int premiumRand = random.nextInt(premiumCumFreqs[4]);
                if(premiumRand < premiumCumFreqs[0]){
                    s = new Square(i, j, Color.cyan);
                }
                else if(premiumRand < premiumCumFreqs[1]){
                    s = new Square(i, j, Color.blue);
                    s.setLetter("DL");
                    s.setScore(2);
                }
                else if(premiumRand < premiumCumFreqs[2]){
                    s = new Square(i, j, Color.green);
                    s.setLetter("TL");
                    s.setScore(3);
                }
                else if(premiumRand < premiumCumFreqs[3]){
                    s = new Square(i, j, Color.red);
                    s.setLetter("DW");
                    s.setScore(2);
                }
                else {
                    s = new Square(i, j, Color.orange);
                    s.setLetter("TW");
                    s.setScore(3);
                }
                
                s.setIsBlank(true);
//                s.setResetColour(Color.yellow);
                s.addMouseListener(new SquareListener());
//                s.addMouseMotionListener(new SquareMotionListener());
                s.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
                squares.add(s);
                s.setFontSize(250/gridHeight);
                add(s);
            }
        }

    }

    public ArrayList getSquares(){
        return squares;
    }


    public Square findSquare(int x, int y){
        Square s = null;
        for (Object square : squares) {
            Square s2 = (Square) square;
            if (s2.getXPos() == x && s2.getYPos() == y){
                s = s2;
            }
        }
        return s;
    }



    public void setLetter(Square s, String newLetter){
        
        if(!s.getIsBlank()) return;
        
        String xWord = newLetter;
        String yWord = newLetter;
        int newLetterScore = letterScores[(int) newLetter.charAt(0) - 'A'];
        int xScore = newLetterScore;
        int yScore = newLetterScore;
        
        //Apply letter Score premiums
        if(s.getBackground() == Color.blue){
            xScore *= 2;
            yScore *= 2;
        }
        else if(s.getBackground() == Color.green){
            xScore *= 3;
            yScore *= 3;
        }
        
        //first check the vertical word
        for(int i=s.getXPos() + 1; i<gridWidth; i++){
            Square nextSquare = this.findSquare(i, s.getYPos());
            if(nextSquare.getIsBlank()){
                break;
            }
            else{
                xWord = xWord + nextSquare.getLetter();
                xScore += nextSquare.getScore();
            }
        }
        for(int i=s.getXPos() - 1; i>=0; i--){
            Square previousSquare = this.findSquare(i, s.getYPos());
            if(previousSquare.getIsBlank()){
                break;
            }
            else{
                xWord = previousSquare.getLetter() + xWord;
                xScore += previousSquare.getScore();
            }
        }
        
        //now, check the horizontal word
        for(int j=s.getYPos() + 1; j<gridWidth; j++){
            Square nextSquare = this.findSquare(s.getXPos(), j);
            if(nextSquare.getIsBlank()){
                break;
            }
            else{
                yWord = yWord + nextSquare.getLetter();
                yScore += nextSquare.getScore();
            }
        }
        for(int j=s.getYPos() - 1; j>=0; j--){
            Square previousSquare = this.findSquare(s.getXPos(), j);
            if(previousSquare.getIsBlank()){
                break;
            }
            else{
                yWord = previousSquare.getLetter() + yWord;
                yScore += previousSquare.getScore();
            }
        }
        
        if(s.getBackground() == Color.red){
            xScore *= 2;
            yScore *= 2;
        }
        else if(s.getBackground() == Color.orange){
            xScore *= 3;
            yScore *= 3;
        }
        
        //Update the scores, if valid words have been made
        if(dawgDic.validateString(xWord)){
            //score needs to be calculated/fed upwards
            playScreen.increaseScore(xScore);
        }
        if(dawgDic.validateString(yWord)){
            //score needs to be calculated/fed upwards
            playScreen.increaseScore(yScore);
        }
        
        //stick the current letter on this tile
        s.setIsBlank(false);
        s.setLetter(newLetter);
        s.setScore(newLetterScore);
        s.setBackground(Color.yellow);
        
        //generate a new letter
        playScreen.nextLetter();
        
        // alternative arrangement prevents letters from being placed if they 
        // make no valid words
//        if((xWord.length() == 1 || dawgDic.validateString(xWord))
//                && (yWord.length() == 1 || dawgDic.validateString(yWord))){
//            s.setLetter(newLetter);
//            s.setBackground(Color.yellow);
//            //score needs to be calculated/fed upwards
//            playScreen.increaseScore(1);
//            //generate a new letter
//            playScreen.nextLetter();
//        }
//        else {
//            JOptionPane.showMessageDialog(null,"This letter would make an invalid word here");
//        }
    }
    

    public void removeListeners(){
        for (Object square : squares) {
            Square s = (Square) square;
            SquareListener ml = (SquareListener) s.getMouseListeners()[0];
            s.removeMouseListener(ml);
        }
    }


    class SquareListener implements MouseListener {

       @Override
       public void mouseClicked(MouseEvent e) {
             requestFocus();
             
            Square sq = (Square) e.getSource();
            Grid.this.setLetter(sq, Grid.this.playScreen.getLetter());
        }
       
       @Override
        public void mouseEntered(MouseEvent e){
            Square sq = (Square) e.getSource();
            if(sq.getBackground() == Color.cyan){
                sq.setBackground(Color.white);
            }
            validate();
       }

       @Override
      public void mouseExited(MouseEvent e) {
            Square sq = (Square) e.getSource();
            if(sq.getBackground() == Color.white){
                sq.setBackground(Color.cyan);
            }
            validate();
       }

       @Override
        public void mousePressed(MouseEvent e){
        }
       @Override
        public void mouseReleased(MouseEvent e) {

        }
    }
}
