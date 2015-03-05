/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run;
import base.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.Border;

/**
 *
 * @author Tom
 */
public class PlayScreen extends JPanel{
    //--------DATA----------
    private Grid gridPlay;
    MainScreen mainScreen;
//    private int maxScore=0;
    private int totalScore=0;
//    private int fullTime;
    private boolean isFinish = false;
//    private Clock clock;
//    private Timer timer;

    private MersenneTwisterFast random;
    
//---------GUI------------
    private Box vertBox;
    private Box topBox;
    private Box bottomBox;
    private Box letterBox;
    private Box scoreBox;  
//    private Box timeBox;   
//    private Box boxButtons;
//    private JLabel scoreLabel = new JLabel("        ",new ImageIcon(this.getClass().getResource("/img/result.png")),JLabel.CENTER);
//    private JLabel timeLabel = new JLabel ("     ",new ImageIcon(this.getClass().getResource("/img/clock.png")),JLabel.CENTER);
    private JTextPane letterTextPane = new JTextPane();
    private JTextPane letterScoreTextPane = new JTextPane();
    private JTextPane scoreTextPane = new JTextPane();
//    private JTextPane timeTextPane = new JTextPane();  

    private Border brdThinGray = BorderFactory.createLineBorder(Color.BLACK,1);
    private static int MARGIN_GAP = 10;

    private int[] letterFreqs = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 
        2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1}; 
    private int[] letterCumFreqs = new int[26];
    private double endGameRatio = 0.05;
    
    public String getLetter(){
        return letterTextPane.getText();
    }
    
    public PlayScreen(int height, int width, MainScreen mainScreen){
        random = new MersenneTwisterFast(System.currentTimeMillis());
        
        this.mainScreen = mainScreen;
        this.setLayout(new BorderLayout());
        gridPlay = new Grid(height, width, this, endGameRatio);
        
        //build the cumulative letter frequencies, for generating letters
        letterCumFreqs[0] = letterFreqs[0];
        for(int letterIndex=1; letterIndex<26; letterIndex++){
            letterCumFreqs[letterIndex] = letterCumFreqs[letterIndex - 1] 
                    + letterFreqs[letterIndex];
        }
        
        displayGrid();
        
        //remove all listeners
//        gridPlay.removeListeners();
//        gridPlay.removeKeyListener(gridPlay.getKeyListeners()[0]);
        validate();

        try {
            jbInit();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        
        nextLetter();
        addListeners();

    }
    
    public void increaseScore(int increase){
        this.totalScore += increase;
        if(totalScore > mainScreen.getScore(gridPlay.getGridHeight())){
            scoreTextPane.setText(Integer.toString(totalScore) + " - new high score");
            mainScreen.setScore(gridPlay.getGridHeight(), totalScore);
        }
        else {
            scoreTextPane.setText(Integer.toString(totalScore));
        }
    }

    private void addListeners(){
        ArrayList squares = gridPlay.getSquares();
        for(int i=0; i<squares.size(); i++){
            Square s = (Square)squares.get(i);
            s.addMouseListener(new SquareListener());
        }
    }
//     private JTextPane createScore()
//    {
//        JTextPane temp = new JTextPane();
//        temp.setEditable(false);
//        StyledDocument doc=temp.getStyledDocument();
////        doc=setTimeStyle(doc);
//         try{
//            doc.insertString(0,"      0"+totalScore,doc.getStyle("special"));
//        }
//        catch(Exception e){}
//        return temp;
//    }
//      private JTextPane createTime()
//    {
//        JTextPane temp = new JTextPane();
//        clock =new Clock(10,0);
//        fullTime=clock.getMaxMin()*60+clock.getMaxSec();
//        temp.setEditable(false);
//        StyledDocument doc=temp.getStyledDocument();
//        doc=setTimeStyle(doc);
//         try{
//        doc.insertString(0,"  "+clock.toString(),doc.getStyle("special"));
//        }
//        catch(Exception e){}
//        return temp;
//    }
//    private StyledDocument setTimeStyle(StyledDocument doc)
//    {
//        Style special=doc.addStyle("special", StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE));
//        StyleConstants.setBold(special,true);
//        StyleConstants.setAlignment(special, StyleConstants.ALIGN_CENTER);
//        StyleConstants.setFontSize(special, 25);
//        return doc;
//    }
    private void displayGrid(){
        gridPlay.Build();
//        if(randomGrid){
//            gridPlay.Build();
//        }
//        else {
//            gridPlay.getGrid();
//        }
        add(gridPlay, BorderLayout.CENTER);
    }
    public void nextLetter(){
        char letter;
        int letterRandom = random.nextInt(letterCumFreqs[25]);
        for(letter = 'A'; letter <= 'Z'; letter++){
            if(letterRandom < letterCumFreqs[(int) letter - 'A']){
                break;
            }
        }
        letterTextPane.setText(Character.toString(Character.toUpperCase(letter)));
        letterScoreTextPane.setText(Integer.toString(
                gridPlay.getLetterScore(letterTextPane.getText())));
    }
//    private int timeUse()
//    {
//       return (fullTime-clock.getMin()*60-clock.getSec());
//    }
//    private int bonusScore()
//
//    {
//        int bonus=0;
//        if(keyTrue)
//        {
//        if (timeUse() <= fullTime/4) bonus= 10;
//        else if (timeUse()<=fullTime/2) bonus= 5;
//        }
//        return bonus;
//    }
//        private void playSound(String filename)
//        {
//           try{
//            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filename));
//            Clip buttonClickSound;
//            buttonClickSound = AudioSystem.getClip();
//            buttonClickSound.open(audioInput);
//            buttonClickSound.start();
//           }
//            catch (Exception ex){
//              ex.printStackTrace();
//            }
//
//        }
    private void jbInit() throws Exception{
        this.setBackground(new Color(174, 190, 212));

        vertBox = Box.createVerticalBox();
        topBox = Box.createHorizontalBox();
        bottomBox = Box.createHorizontalBox();
     
        gridPlay.setPreferredSize(new Dimension(522, 450));

        letterBox = Box.createHorizontalBox();
        letterBox.setPreferredSize(new Dimension(30, 25));

        scoreBox = Box.createHorizontalBox();
        scoreBox.setPreferredSize(new Dimension(30, 25));
   

//        timeBox = Box.createVerticalBox();
//        timeBox.setPreferredSize(new Dimension(60, 50));
// 
//
        letterTextPane.setBorder(brdThinGray);
        letterTextPane.setBackground(Color.yellow);
        letterTextPane.setEditable(false);
        letterTextPane.setFont(new Font("Arial",Font.CENTER_BASELINE,20));
//        letterTextPane.setText(gridPlay.getThemeName(cwPlay));
//
        letterScoreTextPane.setBorder(brdThinGray);
        letterScoreTextPane.setBackground(Color.yellow);
        letterScoreTextPane.setEditable(false);
        letterScoreTextPane.setFont(new Font("Arial",Font.CENTER_BASELINE,20));
//        letterScoreTextPane.setText(gridPlay.getThemeName(cwPlay));
//     
//        timeTextPane = createTime();
//        timeTextPane.setBackground(new Color(174, 190, 212));
//        scoreTextPane = createScore();
        
        scoreTextPane.setBorder(brdThinGray);
        scoreTextPane.setBackground(Color.cyan);
        scoreTextPane.setEditable(false);
        scoreTextPane.setFont(new Font("Arial",Font.CENTER_BASELINE,20));
//        scoreTextPane.setBackground(new Color(174, 190, 212));
      
//        timeLabel.setHorizontalTextPosition(JLabel.LEFT);
     
//        scoreLabel.setHorizontalTextPosition(JLabel.LEFT);


//        boxButtons = Box.createHorizontalBox();
//        boxButtons.setBackground(new Color(199, 223, 236));

//        btnFinish.setMaximumSize(new Dimension(55,19));
//        btnFinish.setPreferredSize(new Dimension(55,19));


        topBox.add(Box.createHorizontalStrut(MARGIN_GAP));
        topBox.add(gridPlay);
        topBox.add(Box.createHorizontalStrut(MARGIN_GAP));
        bottomBox.add(scoreBox);
            scoreBox.add(Box.createHorizontalStrut(MARGIN_GAP));
            scoreBox.add(scoreTextPane);
//            scoreBox.add(Box.createHorizontalStrut(MARGIN_GAP));

        bottomBox.add(letterBox);
            letterBox.add(Box.createHorizontalStrut(MARGIN_GAP));
            letterBox.add(letterTextPane);
            letterBox.add(Box.createHorizontalStrut(MARGIN_GAP));
            letterBox.add(letterScoreTextPane);
            
//        bottomBox.add(timeBox);
//            timeBox.add(timeLabel,CENTER_ALIGNMENT);
//            timeBox.add(timeTextPane);
//            timeBox.add(Box.createHorizontalStrut(MARGIN_GAP));
//        vertBox.add(Box.createVerticalStrut(MARGIN_GAP));
        vertBox.add(topBox);
        vertBox.add(Box.createVerticalStrut(MARGIN_GAP));
        vertBox.add(bottomBox);
//        vertBox.add(Box.createVerticalStrut(MARGIN_GAP));
//        vertBox.add(boxButtons);
//            boxButtons.add(Box.createHorizontalStrut(MARGIN_GAP));
//            boxButtons.add(btnCheck);
//            boxButtons.add(Box.createHorizontalStrut(MARGIN_GAP));
//            boxButtons.add(btnHint);
//            boxButtons.add(Box.createHorizontalStrut(MARGIN_GAP));
//            boxButtons.add(btnClue);
//            boxButtons.add(Box.createHorizontalStrut(MARGIN_GAP));
//            boxButtons.add(btnSolution);
//            boxButtons.add(Box.createHorizontalStrut(MARGIN_GAP));
//            boxButtons.add(btnFinish);
//            boxButtons.add(Box.createHorizontalStrut(MARGIN_GAP));
//        vertBox.add(Box.createVerticalStrut(MARGIN_GAP));
        add(vertBox, BorderLayout.CENTER);

//        TimeListener t=new TimeListener();
//        timer=new Timer(1000,t);
//        timer.start();

    }
//    class TimeListener implements ActionListener
//    {
//
//            StyledDocument doc=timeTextPane.getStyledDocument();
//             public void actionPerformed(ActionEvent e)
//             {
//                 try{
//                     if(clock.getMin()==1&&clock.getSec()==0)
//                         JOptionPane.showMessageDialog(topBox,"Bạn chỉ còn 1 phút nữa\n" +
//                                                       "Hãy kiểm tra những từ đã nhập");
//                     else if(clock.getMin()==0&&clock.getSec()==0)
//                      {
//                           timer.stop();
//                           isFinish=true;
//                           playSound("sound/time.wav");
//                           JOptionPane.showMessageDialog(topBox,
//                                                         "Bạn được   : "+totalScore+" điểm\n"+
//                                                         "Hoàn thành : "+(bonusScore()+totalScore)*100/maxScore+" %\n"
//                                                        ,"Time up",JOptionPane.INFORMATION_MESSAGE,new ImageIcon(this.getClass().getResource("/img/time.png")));
//                      }
//                    else
//                    {
//                         doc.remove(0,doc.getLength());
//                         doc.insertString(0,"  "+clock.decrease().toString(),doc.getStyle("special"));
//                    }
//                 }
//                    catch(Exception m){};
//             }
//    }
   class ButtonListener implements ActionListener
    {
        @SuppressWarnings("empty-statement")
        public void actionPerformed(ActionEvent e)
        {
            StyledDocument doc=scoreTextPane.getStyledDocument();
            try{
//                if (e.getSource() == btnCheck)
//                {
//                           if(gridPlay.checkIsCorrect()) {
//    //                           playSound("sound/true.wav");
//                               if(gridPlay.getSelectedWord().getWord().equals(gridPlay.getKey().getWord())){
//                               totalScore+=100;
//                               keyTrue=true;
//                               }
//                               else  switch(gridPlay.getClueUsed()){
//                                     case 1: totalScore+=15;break;
//                                     case 2: totalScore+=10;break;
//                                     case 3: totalScore+=5;break;
//                                }
//                             doc.remove(0,doc.getLength());
//                             doc.insertString(0,"     "+totalScore,doc.getStyle("special"));
//                          }
//    //                    else playSound("sound/false.wav");
//                }
    //            else if(e.getSource() == btnHint)
    //            {
    //                if(!gridPlay.getSelectedWord().getIsHint())
    //                {
    ////                     playSound("sound/hint.wav");
    //                     if(gridPlay.getSelectedWord()==gridPlay.getKey())
    //                     {
    ////                         letterTextPane.setText(gridPlay.getKeywordClue());
    //                         totalScore-=20 ;
    //                         gridPlay.getKey().setIsEditable(true);
    //                     }
    //                     else{
    //                         gridPlay.hintWord();
    //                         totalScore-=10 ;
    //                         gridPlay.getSelectedWord().setIsEditable(false);
    //                     }
    //                     gridPlay.getSelectedWord().setIsHint(true);
    //                     doc.remove(0,doc.getLength());
    //                     doc.insertString(0,"     "+totalScore,doc.getStyle("special"));
    //                }
    //            }
    //            else if(e.getSource() == btnClue)
    //            {
    //                nextClue();
    //                playSound("sound/clue.wav");
    //            }
    //            else if(e.getSource() == btnSolution)
    //            {
    //                 if(isFinish)
    //                 {
    //                  playSound("sound/solve.wav");
    //                 gridPlay.showSolution();
    //                 }
    //              else JOptionPane.showMessageDialog(null,"Bạn chưa kết thúc trò chơi\nChưa thể xem đáp án");
    //             }
    //            else if(e.getSource() == btnFinish)
    //            {

                if(!isFinish)
                    {
    //                timer.stop();   
                    isFinish=true;
    //                 playSound("sound/finish.wav");
                    JOptionPane.showMessageDialog(topBox,
                             "Bạn được   : "+totalScore+" điểm\n"
    //                         + "Điểm thưởng: "+ bonusScore()+" điểm\n" +
    //                         "Hoàn thành : "+(bonusScore()+totalScore)*100/maxScore+" %\n"+
    //                         "Thời gian  : "+timeUse()/60+" phút "+timeUse()%60+" giây"
                            ,"Finish",JOptionPane.INFORMATION_MESSAGE,new ImageIcon(this.getClass().getResource("/img/score.png")));
                   }
            } catch(Exception m){};
        }
    }

    class SquareListener implements MouseListener
    {
        public void mouseClicked(MouseEvent e)
        {
          Square sq = (Square) e.getSource();
//          if (sq.getBackground()==Color.BLACK||sq.getWord()==gridPlay.getKey())
//          {
//              letterTextPane.setFont(new Font("Arial",Font.CENTER_BASELINE,20));
//              letterTextPane.setText(gridPlay.getThemeName(cwPlay));
//          }
//         
//          else{
//              letterTextPane.setFont(new Font("Arial",Font.PLAIN,16));
//                          if(gridPlay.getClueUsed()==0||gridPlay.getClueUsed()==1)
//                                {
//                                String s1 = gridPlay.getSelectedClue1();
//                                letterTextPane.setText(s1);
//                                gridPlay.setClueUsed(1);
//                                }
//                          if(gridPlay.getClueUsed()==2)
//                                {
//                                String s2 = gridPlay.getSelectedClue2();
//                                letterTextPane.setText(s2);
//                                }
//                          if (gridPlay.getClueUsed()==3)
//                                {
//                                String s3 = gridPlay.getSelectedClue3();
//                                letterTextPane.setText(s3);
//                                }
//                           validate();
//          }
        }

        public void mouseEntered(MouseEvent e)
        {
        }

        public void mouseExited(MouseEvent e)
        {
        }

        public void mousePressed(MouseEvent e)
        {
        }

        public void mouseReleased(MouseEvent e)
        {
        }

    }

}
