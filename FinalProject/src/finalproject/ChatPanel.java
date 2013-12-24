package finalproject;

/*
 * Import necessary classes
 */
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

/**
 *
 * @author Arda
 */
public class ChatPanel extends JPanel{
    /*
     * Decleare variables
     */
    private final int DELAY = 1000;
    private final int WIDTH = 800;
    private final int HEIGHT = 400;
    private Communicator com;
    private boolean debug;
    private boolean running = true;
    private String username;
    private String connectedTo;
    private ExecutorService exe;

    //JComponents are below
    private JButton sendButton;
    private JButton setPicture;
    private JLabel chatHistory;
    private JLabel loginLabel;
    private JTextField chatField;
    private ProfilePicture pp;
    private ProfilePicture ppOther;
    /*
     * ChatPanel constructor takes 2 parameters: username and connectedTo
     * usernaem and connectedTo can be empty strings i.e. ""
     */
    public ChatPanel(String _username, String _connectedTo){
        //Set the chatPanel layout
        setLayout(null);
        
        /*
         * Initialize variables
         */
        username = _username;
        connectedTo = _connectedTo;
        com = new Communicator(); 
        chatField = new JTextField();
        chatHistory = new JLabel();
        sendButton = new JButton();
        setPicture = new JButton();
        pp = new ProfilePicture();
        ppOther = new ProfilePicture();
        
        //Set component texts
        chatHistory.setText("<html>");
        chatField.setText("Type your message here");
        sendButton.setText("Send");
        setPicture.setText("Change");
        
        //Set the inner panel layout and size
        setLayout(null);
        
        /*
         * Construct the sendListener object and attach it to the send button
         */
        ActionListener sendListener = new SendListener();  
        sendButton.addActionListener(sendListener);
        
        //Add the send button
        add(sendButton);
        sendButton.setLocation(400,240);
        sendButton.setSize(80,24);
        
        /*
         * Construct the EnterListener object and attach it to the chatField
         */
        EnterListener enterListener = new EnterListener();  
        chatField.addKeyListener(enterListener);
        
        //Add the chatField (holds the message to be send)
        add(chatField);
        chatField.setLocation(180,210);
        chatField.setSize(300,24);
        
        /*
         * A scroller is necessary for the chat history
         * Otherwise it wouldn't be possible to scroll through the chat history
         */
        //Set scroller properties
        JScrollPane scroller = new JScrollPane(chatHistory, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setSize(300,190);
        scroller.setLocation(180,10);
        
        //Set chatHistory properties
        chatHistory.setVerticalTextPosition(JLabel.TOP);
        chatHistory.setVerticalAlignment(JLabel.TOP);
        chatHistory.setAlignmentY(TOP_ALIGNMENT);
        
        //add the scroller
        add(scroller);
        
        //add the profile image
        add(pp);
        pp.setSize(100,100);
        pp.setLocation(30,130);
        
        //add the profile image of the other person
        add(ppOther);
        ppOther.setSize(100,100);
        ppOther.setLocation(30,20); 
        /*
         * Construct the ChangePictureListener object and attach it to the setPicture button
         */
        ChangePictureListener cpl = new ChangePictureListener();
        setPicture.addActionListener(cpl);
        
        //Add the setPicture button
        add(setPicture);
        setPicture.setLocation(40,240);
        setPicture.setSize(80,24);
        
        //set the size of the chatPanel
        setPreferredSize(new Dimension(500,300));
        setSize(500,300);
    }
    /*
     * setDebug method is used to set the debug variable which is used for debugging
     */
    public void setDebug(boolean _debug){
        debug = _debug;
    }
    /*
     * setUsername is used set the username after the login screen
     */
    public void setUsername(String _username){
        username = _username;
        pp.setUsername(username);
        pp.getProfileImage();
    }
    /*
     * setConnectedTo is used to set the connectedTo after the login screen
     */
    public void setConnectedTo(String _connectedTo){
        connectedTo = _connectedTo;
        ppOther.setUsername(connectedTo);
        /*
         * Create executor service to
         * continuesly run the ProfilePicture object
         * associated with the other user
         * This way, if the other user changes their image,
         * the image will auto update
         */
        exe = Executors.newFixedThreadPool(1);
        exe.execute(ppOther);
        //ppOther.getProfileImage();
    }
    /*
     * checkMessages method runs in a continues loop
     * as long as running is set to true
     * It is used to check if there is a new message sent to the client
     * if connectedTo is not set yet, it means that the user is still at the
     * login screeen. Therefore, it is not possible to check for the new messages
     * Until connectedTo is set to a non-empty string, receiveNewChat method is skipped
     */
    public void checkMessages(){
        if(debug){
            System.out.println("Running");
        }
        //Run in a continues loop
        while(running){
            //Don't check for new messages if connectedTo is not set yet
            if(connectedTo!=null&&!connectedTo.equals("")){
                String receivedMessage = com.receiveNewChat(connectedTo,username);
                if(debug){
                    System.out.println(receivedMessage);
                }
                if(!receivedMessage.equals("0")){
                    if(debug){
                        System.out.println(receivedMessage);
                    }
                    //Update chat history
                    chatHistory.setText(chatHistory.getText()+"<br>"+connectedTo+": "+receivedMessage);
                }
            }
            try
            {
                /*
                 * Put the thread to sleep to make sure that the new messages are check
                 * with an interval of DELAY
                 */
                Thread.sleep(DELAY);

            }catch (InterruptedException ie)
            {
                System.out.println(ie.getMessage());
            }
        }
    }
    /*
     * Send Listener mini class is used to listen to the send button
     * sends the typed message to the person at connectedTo
     */
    class SendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(debug){
                System.out.println("Test send button");
            }
            /*
             * The message is sent via the communicator object
             */
            if(com.sendChat(username,connectedTo,chatField.getText())){
                chatHistory.setText(chatHistory.getText()+"<br>You: "+chatField.getText());
                chatField.setText("");
            }
        }
    }
    /*
     * ChangePciture Listener mini class is used to listen to the setPicture button
     */
    class ChangePictureListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(debug){
                System.out.println("Test Change Picture button");
            }
            try{
                //Change Picture
                pp.changePicture();
            }catch(InterruptedException e2){
                System.out.println("Interrupted Exception:"+e2);
            }
        }
    }
    /*
     * Enter Listener mini class is used to listen to enter key
     * If enter is hit submit the message
     * but if shift enter is hit do not submit
     */
    class EnterListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println(e.getKeyCode());
        }
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==10&&e.getModifiers()==0){
                if(debug){
                    System.out.println("Test send button");
                }
                /*
                * The message is sent via the communicator object
                */
                if(com.sendChat(username,connectedTo,chatField.getText())){
                    chatHistory.setText(chatHistory.getText()+"<br>You: "+chatField.getText());
                    chatField.setText("");
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //System.out.println(e.getKeyCode());
        }
        
    }
}
