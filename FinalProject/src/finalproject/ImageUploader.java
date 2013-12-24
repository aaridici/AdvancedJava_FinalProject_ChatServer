/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arda
 */
/*
 * ImageUploader extends Thread
 * By extending Thread on multicore processors
 * uploadImage method can be run simultaneously
 * On single core processors uploadImage freezes the JFrame
 * until upload is complete
 */
public class ImageUploader extends Thread {
    /*
     * variables are decleared below
     */
    private boolean debug = false;
    private String username;
    private String image_path;
    private final String baseURL = "http://aaridici.com/chat/";//server url
    /*
     * ImageUploader constuctor
     * doesn't take parameters
     */
    public ImageUploader(){
        debug = false;
    }
    /*
     * Set Debug method sets debug for debugging
     */
    public void setDebug(boolean _debug){
        debug = _debug;
    }
    /*
     * Set Username method sets username for communicating with the server
     */
    public void setUsername(String _username){
        username = _username;
    }
    /*
     * Set image path method sets the image path for uploading the image
     */
    public void setImagePath(String _image_path){
        image_path = _image_path;
    }
    /*
     * upload image method doesn't take any parameters
     * uploads the image to the server
     */
    public void uploadImage(){
        try {
            //Print out the image path when debugging
            if(debug){
                System.out.println("Image path: "+image_path);
            }
            
            //Initialize the string name to null
            String fileName  ="";
            //Get the file name from windows file system
            if(image_path.lastIndexOf("\\")>0){
                //Remove the last backslash
                fileName = image_path.substring(image_path.lastIndexOf("\\")+1);
            }
            //Get the file name from unix file system
            else if(image_path.lastIndexOf("/")>0){
                //Remove the last slash
                fileName = image_path.substring(image_path.lastIndexOf("/")+1);
            }
            
            //Print out the stripped image path when debugging
            if(debug){
                System.out.println("Stripped image path: "+fileName);
            }
            
            //Encode the URL to ensure that all the characters send are valid
            String data = URLEncoder.encode("file_name", "UTF-8") + "=" + URLEncoder.encode(fileName, "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            
            //Construct URL
            URL url = new URL(baseURL+"upload.php?"+data);
            
            //Open HttpURLConnection from the URL
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            
            //Set output to true
            huc.setDoOutput(true);
            
            //Set method to post to upload to the server, PHP will receive it as $_POST
            huc.setRequestMethod("POST");
            
            //Create outputstream object to write the file
            OutputStream outs = huc.getOutputStream();
            
            
            //Create the file
            File f =new File(image_path);
            
            //Create bufferedInputStream to read the file by its bytes
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
            
            //Send each byte to the server
            for (int i = 0; i < f.length(); i++) {
                outs.write(bis.read());
            }

            outs.close();
            
            /*
             * BufferedReader is mandatory,
             * otherwise this method does not execute
             * The feedback from the server must be read
             */
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    huc.getInputStream()));

            //Read the feedback from the server
            String out = "";
            while (in.ready()) {
                //Reading the output is mandatory
                out += in.readLine()+"\r\n";
                
            }
            if(debug){
                    System.out.println(out);
            }
            in.close();
            bis.close();
        } catch (IOException ex) {
            Logger.getLogger(ProfilePicture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     * Run method is overwritten for running ImageUploader as a thread
     * On multi core processors this makes the chat programmer much smoother
     */
    public void run(){
        uploadImage();
    }
}
