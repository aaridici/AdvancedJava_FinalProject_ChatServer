package finalproject;

/**
 *
 * @author Arda
 */

/*
 * Import necessary classes below
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;

/*
 * Defined MessengerClient Class
 * extends JFrame
 */
public class MessengerClient extends JFrame{
    /*
     * Variables are decleared below
     */
    private final int DELAY = 1000;
    private final int WIDTH = 800;
    private final int HEIGHT = 400;
    private Communicator com;
    private boolean debug; //Debug is used to debug the code
    private boolean running = true;//Should be true while program is runing
    private String username;
    private String connectedTo;
    
    //Below are the JComponent variable declarations
    private Container frame;
    private JButton sendButton;
    private JButton loginButton;
    private JLabel chatHistory;
    private JLabel loginLabel;
    private JLabel usernameLabel;
    private JLabel toLabel;
    private JPanel loginPanel;
    private ChatPanel cp;
    private JTextField chatField;
    private JTextField usernameField;
    private JTextField toField;
    
    /*
     * MessengerClient Constructor
     */
    public MessengerClient(){
        //Initialize variables below
        debug = false;
        com = new Communicator();
        frame = getContentPane();
        username = "";
        connectedTo = "";
        
        //Set Frame Properties
        setVisible(true);
        setResizable(false);
        setTitle("Arda's Messenger");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //Initialize the components for the login screen
        initLoginComponents();
        
        
        //Initialize the chatPanel, hold it ready at hand
        cp = new ChatPanel(username, connectedTo);
        /*
         * Check messages is used to check for new messages
         * Check messages will wait until username 
         * and connectedTo are no longer empty strings
         */
        cp.checkMessages();
    }
    /*
     * Set debug function is used to print out debug comments
     * By default debug is false
     */
    public void setDebug(boolean _debug){
        debug = _debug;
    }
    /*
     * initLoginComponents initalizes the JComponents
     * necessary for the login screen and puts them on the screen
     */
    private void initLoginComponents(){
        //Initialize the components
        usernameField = new JTextField();
        toField = new JTextField();
        loginButton = new JButton();
        loginPanel = new JPanel();
        loginLabel = new JLabel();
        usernameLabel = new JLabel();
        toLabel = new JLabel();
        
        //Set their text
        //usernameField.setText("Type your name here");
        //toField.setText("Type the name of the contact you are trying to reach");
        loginButton.setText("Login");
        
        //Action listener is used to listen for the login button
        ActionListener loginListener = new LoginListener();  
        loginButton.addActionListener(loginListener);
        
        /*
         * loginPanel settings are set
         * Other components are added to JPanel 1
         */
        loginPanel.setLayout(null);
        loginPanel.setSize(500,300);
        loginPanel.setLocation(0,0);
        
        //Add the login button
        loginPanel.add(loginButton);
        loginButton.setLocation(320,200);
        loginButton.setSize(80,24);
        
        //Add the usernameField
        loginPanel.add(usernameField);
        usernameField.setLocation(100,150);
        usernameField.setSize(300,24);
        
        //Add the toField, used for connectedTo
        loginPanel.add(toField);
        toField.setLocation(100,100);
        toField.setSize(300,24);
        
        //Set Login instructions and them to the panel
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setText("Welcome! Please login below.");
        loginPanel.add(loginLabel);
        loginLabel.setLocation(100,40);
        loginLabel.setSize(300,24);
        
        //Set the username instructions
        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        usernameLabel.setText("Please type your username below");
        loginPanel.add(usernameLabel);
        usernameLabel.setLocation(100,78);
        usernameLabel.setSize(300,24);
        
        //Set the connected to instructions
        toLabel.setHorizontalAlignment(SwingConstants.LEFT);
        toLabel.setText("Please type the username you are trying to reach");
        loginPanel.add(toLabel);
        toLabel.setLocation(100,128);
        toLabel.setSize(300,24);
        
        //Add the panel to the frame
        add(loginPanel);
        
        //Set the frame dimensions
        setPreferredSize(new Dimension(500,300));
        setSize(500,300);      
    }
    /*
     * intiChatComponents function passes the username
     * and connectedTo values to the chatPanel
     * and refreshes the layout of the frame
     */
    private void initChatComponents() {
        cp.setUsername(username);
        cp.setConnectedTo(connectedTo);
        add(cp);
        display();
        refreshLayout();
    }
    /*
     * Display is used to pack the new components added
     * and show them
     */
    public void display(){
        pack();
        show();
    }
    /*
     * refreshLayout revalidates the components in the frame
     * necessary for displaying the chatPanel
     */
    public void refreshLayout(){
        frame.revalidate();
        frame.repaint();
        frame.show();
    }
    /*
     * LoginListener is a mini class
     * implements ActionListener
     * used to listen for the login button clcik
     */
    class LoginListener implements ActionListener{
        @Override
        /*
         * actionPerformed is used for the login button
         * gets the username and to fields and sets them to
         * username and connectedTo variables
         * then remoes the login panel
         * and refreshes the layout
         * then calls the initChatComponents
         */
        public void actionPerformed(ActionEvent e) {
            if(debug){
                System.out.println("Login button clicked");
            }
            /*
             * Check to see if both of the fields have strings in them
             * make sure they are not empty strings nor the prepopulated strings!
             */
            if(!usernameField.getText().equals("")
                    &&!usernameField.getText().equals("Please type your username here")
                    &&!toField.getText().equals("")
                    &&!toField.getText().equals("Please type other person's username here")){
                //Set the variables to the new strings
                username = usernameField.getText();
                connectedTo = toField.getText();
                
                /*
                 * Strip unwanted characters
                 * \W for anything that is not a word character
                 * \s for anything that is a space character
                 * note that is is capital W and lower case s
                 */
                username = username.replaceAll("\\W","");
                username = username.replaceAll("\\s","");
                connectedTo = connectedTo.replaceAll("\\W","");
                connectedTo = connectedTo.replaceAll("\\s","");
                try{
                    //Remove the loginPanel
                    remove(loginPanel);
                    refreshLayout();
                    initChatComponents();
                }catch(Exception e2){
                    System.out.println("Exception while trying to change the layout: "+e2);
                }
            }else{
                //If no username is set, warn the user
                if(usernameField.getText().equals("")){
                    usernameField.setText("Please type your username here");
                }
                //If no other person's username is set, warn the user
                if(toField.getText().equals("")){
                   toField.setText("Please type other person's username here"); 
                }
            }
        }
    }
}
