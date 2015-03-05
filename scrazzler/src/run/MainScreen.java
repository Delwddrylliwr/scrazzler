package run;

import base.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;
import java.io.*;


public class MainScreen extends JFrame{

    private static int MIN_SIZE = 5;
    private static int MAX_SIZE = 20;
    
    // *************** GUI ************************//
    JPanel mainPanel = new JPanel();
    JLabel welcome = new JLabel(new ImageIcon(this.getClass().getResource("/img/welcome.jpg")));
    BorderLayout borderLayout1 = new BorderLayout();
    BorderLayout borderLayout2 = new BorderLayout();
    JMenuBar jMenuBar1 = new JMenuBar();
    JMenu mPlay = new JMenu("Play");
    JMenuItem mPlay_New = new JMenuItem("New",new ImageIcon(this.getClass().getResource("/img/new.png")));
    JMenuItem mPlay_Exit = new JMenuItem("Exit",new ImageIcon(this.getClass().getResource("/img/exit.png")));
    JMenu mHelp = new JMenu("Help");
    JMenuItem mHelp_Help = new JMenuItem("Help",new ImageIcon(this.getClass().getResource("/img/help.png")));
    JMenuItem mHelp_About = new JMenuItem("About",new ImageIcon(this.getClass().getResource("/img/about.png")));
    JMenuItem mHelp_Scores = new JMenuItem("Scores",new ImageIcon(this.getClass().getResource("/img/scores.png")));
// *************** GUI ************************//

    private HashMap<Integer, Integer> scoresHash = new HashMap<Integer, Integer>();
    
    public MainScreen() {
        try {
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        try {
        UIManager.setLookAndFeel(
            "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception e) { }

        new MainScreen();
    }
    
    private void jbInit() throws Exception {
        show();
       // this.getContentPane().setBackground(SystemColor.control);
        this.buildMenu();
        this.loadScores();
        setTitle("Scrazzle Reborn");
        getContentPane().setLayout(borderLayout1);
        setBounds(0, 0, 720, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new WinListener());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainPanel.add(welcome);
        mainPanel.setBackground(new Color(174, 190, 212));
        this.getContentPane().add(mainPanel);
        this.setIconImage(new ImageIcon(this.getClass().getResource("/img/logo.png")).getImage());
        setVisible(true);
        validate();
    }

    private void buildMenu()
    {
        setJMenuBar(jMenuBar1);
// add menu
        jMenuBar1.add(mPlay);
        jMenuBar1.add(mHelp);

        mPlay.add(mPlay_New);
        mPlay.addSeparator();
        mPlay.add(mPlay_Exit);
        mHelp.add(mHelp_About);
        mHelp.addSeparator();
        mHelp.add(mHelp_Help);
        mHelp.addSeparator();
        mHelp.add(mHelp_Scores);
//set menu
        jMenuBar1.setBackground(new Color(199, 223, 236));
        jMenuBar1.setBorder(BorderFactory.createLineBorder(Color.black));


//add listeners

        mPlay_New.addActionListener(new MenuListener());
        mPlay_Exit.addActionListener(new MenuListener());
        mHelp_About.addActionListener(new MenuListener());
        mHelp_Help.addActionListener(new MenuListener());
        mHelp_Scores.addActionListener(new MenuListener());
    }
    
    public void loadScores() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("scores.dat"));

            scoresHash = (HashMap<Integer, Integer>) ois.readObject();

            ois.close();
        } catch (Exception e) {
            scoresHash = new HashMap<Integer, Integer>();
        }
    }
    
    public void saveScores() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("scores.dat"));
            oos.writeObject(scoresHash);
            oos.close();
        } catch (Exception e) {
            System.out.print(e.getStackTrace());
        }
    }

    public void showPlay()
    {
        mainPanel.removeAll();
        this.setBounds(0, 0, 620, 720);
        this.setLocationRelativeTo(null);

        try
        {
            int size = Integer.parseInt(JOptionPane.showInputDialog(null,
                    "Please choose a grid size (5->20):", null));
           
            if (size >= MIN_SIZE && size <= MAX_SIZE ){
                mainPanel.removeAll();
                mainPanel.add(new PlayScreen(size, size, this));
                this.setBounds(0, 0, 620, 720);
                this.setLocationRelativeTo(null);
                validate();
            }
            else
            {
                JOptionPane.showMessageDialog(null,"You entered an invalid size","Size Error",
                                              JOptionPane.ERROR_MESSAGE);
                showPlay();
            }
        }catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null,"An invalid size was entered","Size Error",
                                              JOptionPane.ERROR_MESSAGE );
        }
    }

    private void showAbout()
    {
        JOptionPane.showMessageDialog(this,
               "* Scrazzle Reborn (build 1)\n" +
               "* Hacker :\n" +
                " Tom Wilkinson \n" +
               "* Many thanks to :\n" +
                "   - Nguyễn Đình Lộc\n" +
               "   - Nguyễn Minh Quân\n" +
               "   - Đào Trọng Hiếu\n" +
               "   - Nguyễn Anh Tuấn\n"+
               "   - Ben Westgarth\n" +
               "   - Duong Vu Xuan ",
               "About Us",JOptionPane.INFORMATION_MESSAGE,
               new ImageIcon(this.getClass().getResource("/img/about.png")));
    }

    private void showScores()
    {
        String scoresList = "Board Size \t High Score \n";
        for(int sizeIndex = 0; sizeIndex <= MAX_SIZE - MIN_SIZE; sizeIndex++){
            scoresList += String.valueOf(MIN_SIZE + sizeIndex) + "\t\t" 
                    + String.valueOf(scoresHash.getOrDefault(MIN_SIZE + sizeIndex, 0)) + "\n";
        }
        JOptionPane.showMessageDialog(this,
               new JTextArea(scoresList),
               "High Scores",JOptionPane.INFORMATION_MESSAGE,
               new ImageIcon(this.getClass().getResource("/img/scores.png")));
    }

     private void showUse()
    {
       
        JOptionPane.showMessageDialog(this,
            "------------------------ How to play ------------------------\n"+
            "1. Place the letter you are given (bottom right) in the grid,\n"+
            "2. Try to complete words with the adjacent vertical and horizontal tiles,\n"+
            "3. You will get different points for every letter in the completed word,\n"+
            "4. Those points will be multiplied, if you are placing the current letter\n"+
            "   on a Premium tile"+
                    "",
            "How to play",JOptionPane.INFORMATION_MESSAGE,
            new ImageIcon (this.getClass().getResource("/img/about.png")));

    }

     private void basicClose()
    {
        int i = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?","Confirm Action",
                JOptionPane.YES_NO_OPTION);
        if(i==JOptionPane.YES_OPTION){ 
            saveScores();
            System.exit(0);
        }
    }
     
    public int getScore(int size){
        return (int) scoresHash.getOrDefault(size, 0);
    }
    
    public void setScore(int size, int score){
        scoresHash.put(size, score);
    }


    class WinListener implements WindowListener
    {
        public void windowActivated(WindowEvent e)
        {
        }

        public void windowClosed(WindowEvent e)
        {
        }

        public void windowClosing(WindowEvent e)
        {
            basicClose(); 
        }

        public void windowDeactivated(WindowEvent e)
        {
        }

        public void windowDeiconified(WindowEvent e)
        {
        }

        public void windowIconified(WindowEvent e)
        {
        }

        public void windowOpened(WindowEvent e)
        {
        }
    }


    class MenuListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

               if(e.getSource().equals(mPlay_New))
            {
                   showPlay();
            }
             else if(e.getSource().equals(mPlay_Exit))
            {
                basicClose();
            }
             else if(e.getSource().equals(mHelp_About))
            {
                showAbout();
            }
              else if(e.getSource().equals(mHelp_Help))
            {
                showUse();
            }
              else if(e.getSource().equals(mHelp_Scores))
            {
                showScores();
            }
            validate();
        }
    }
}
