package finalproject;

/**
 *
 * @author Arda
 */

/*
 * Import the necessary classes
 */

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/*
 * ProfilePicture class extends JComponent
 * Draws the profile picture on to the Component
 */
public class ProfilePicture extends JComponent implements Runnable{
    /*
     * Decleare variables
     */ 
    private String path;
    private Image profileImage;
    private String username;
    private boolean running;
    /*
     * Initialize final variables
     */
    private final String defaultPath = "default.png";
    private final int maxWidth = 100;
    private final int maxHeight = 100;
    private final int DELAY = 30*1000;
    
    /*
     * ProfilePicture Constructor with no parameters
     */
    public ProfilePicture(){
        //Initialize variables
        username = "";
        path = defaultPath;
        running = true;
        try{
            //Read the image file
            profileImage = ImageIO.read(new File(path));
        }catch(IOException e){
            System.out.println("A problem occurred. "
                + "Your profile picture can not be set ");
        }  
    }
    /*
     * Set Username method sets the username
     */
    public void setUsername(String _username){
        username = _username;
    }
    /*
     * paintComponent is overwritte
     * Draws the resized picture on to the component
     */
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        //Resize the image
        Image scaledImage = profileImage.getScaledInstance(maxWidth, maxHeight, Image.SCALE_FAST);
        g2.drawImage(scaledImage,null,null);
        //g2.drawString(description, 75, 430);
        //new AffineTransform(1f,0f,0f,1f, 50, 50), null
    }
    /*
     * Change picture methods
     * Changes the profile picture
     * throws Interrupted Exception because ImageUploader is used as a Thread
     * This exception is caught in the ChatPanel
     */
    public void changePicture() throws InterruptedException{
        //Get Image Name
        path = getImageName();
        
        //If image exists try to change the image
        if(path!=null&&!path.equals("")){
            try{
                //Read in the new file
                profileImage = ImageIO.read(new File(path));
                //Paint the new image
                repaint();

            }catch(IOException e){
                //If the new image causes problems, set the image to default
                path = defaultPath;
                try{
                    profileImage = ImageIO.read(new File(path));
                    repaint();
                }catch(Exception ex){
                    System.out.println("A problem occurred. "
                            + "Your profile picture can not be set ");
                }
            }
            finally{
                /*
                 * Initalize maxThreads to the number of cores
                 * This is used to run the ImageUploader as a asyncronous thread
                 * on multicore machines. Otherwise, the program freezes until the
                 * upload is completed on single core machines
                 */
                int maxThreads = Runtime.getRuntime().availableProcessors();
                
                //If the machine is multicore, run the ImageUploader as a seperate thread
                if(maxThreads>1){ 
                    
                    /*
                     * Initilize the executor service and add the ImageUploader as a single thread
                     */
                    ExecutorService tpool = Executors.newFixedThreadPool(maxThreads);
                    ImageUploader iu = new ImageUploader();
                    iu.setUsername(username);
                    iu.setImagePath(path);
                    //iu.setDebug(true);
                    
                    //Run the image uploader asyncronously
                    tpool.execute(iu);
                    
                }else{
                    /*
                     * Alert the user that they only have 1 core
                     * single core machines freezes until the upload is completed
                     * 
                     */
                    JOptionPane.showInternalMessageDialog(this.getParent().getParent(),
                        "You have only a single core processor.\r\n"
                            + "Your window might become unresponsive\r\n"
                            + "while the image is being uploaded...",
                        "Caution", JOptionPane.INFORMATION_MESSAGE);
                    if(profileImage!=null){
                        //Upload the image in the same thread
                        //This freezes the program until upload is completed
                        ImageUploader iu = new ImageUploader();
                        iu.setUsername(username);
                        iu.setImagePath(path);
                        iu.uploadImage();
                    }
                }
            }
        }
    }
    /*
     * Get Image Name gets the image name via a dialog window
     */
    public String getImageName() {
        FileDialog fd = new FileDialog(new Frame(), "Open File", FileDialog.LOAD);
        fd.setVisible(true);
        if (fd.getFile() == null){
            return null;
        }
        return fd.getDirectory() + fd.getFile();
    }
    /*
     * Get other person's profile image from the server
     */
    public void getProfileImage(){
        //Create communicator object and receive the url to the image
        Communicator com = new Communicator();
        String imagePath = com.getProfileImage(username);
        //if the image path is not or false, try to get the image
        if(imagePath!=null&&!imagePath.equals("false")){
            try {
                //Try to set the image to the url
                URL imageUrl = new URL(imagePath);
                try{
                    profileImage = ImageIO.read(imageUrl);
                }catch(IOException e2){
                    //if no image, default to the default iamge
                    profileImage = ImageIO.read(new File(defaultPath));
                }

            } catch (IOException ex) {
                Logger.getLogger(ProfilePicture.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if(profileImage!=null){
                    repaint();
                }
            }
        }
    }

    @Override
    public void run() {
        while(running){
            try{
                getProfileImage();
                Thread.sleep(DELAY);
            }
            catch (InterruptedException ex) {
                Logger.getLogger(ProfilePicture.class.getName()).log(Level.SEVERE, null, ex);
            }        }
    }
}
