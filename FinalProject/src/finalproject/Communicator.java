package finalproject;

/**
 *
 * @author Arda
 */
/*
 * Imports the necessary classes below
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Communicator{
    /*
     * variables are decleared below
     */
    private boolean debug = false;
    private final String baseURL = "http://aaridici.com/chat/";//server url
    /*
     * Communicator constuctor
     * doesn't take parameters
     */
    public Communicator(){
        debug = false;
    }
    /*
     * setDebug functions sets debug for debugging purposes
     */
    public void setDebug(boolean _debug){
        debug = _debug;
    }
    /*
     * sendChat function
     * takes 3 parameters: from, to and test (message)
     * sends the message to the appropriate person
     */
    public boolean sendChat(String from, String to, String text){
        try  
        {  
            /*
             * URL is encoded below to ensure that it complies with the url standards
             * variables such as from, to or text might include characters that might otherwise
             * be invalid
             */
            String data = URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8");
            data += "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(to, "UTF-8");  
            data += "&" + URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8");  

            //Url is constructed  
            URL url=new URL(baseURL+"send_chat.php?"+data);
            
            if(debug){
                System.out.println(url);
            }
            
            //Date is sent
            URLConnection conn = url.openConnection();  
            //conn.connect();
            /*
             * It is required to read the feedback from the URL
             * otherwise sendChat doesn't work properly
             * BufferedReader is used to read the feedback from the URL
             */
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(in.ready()){
                /*
                 * WITHOUT THE in.readLine() sendChat is incomplete!
                 * Doesn't run without it!
                 * Mandatory!!!
                 */
                String nextLine = in.readLine();
                if(debug){
                    System.out.println(nextLine);
                }
            }
            in.close();
            return true;
        }
        catch(Exception e)  
        {  
            e.printStackTrace();
            return false;
        }
    }
    /*
     * receiveNewChat takes 2 parameters: from and to
     */
    public String receiveNewChat(String from, String to){
        String out = "";
        try  
        {  
            //Encode the URL to ensure that all the characters send are valid
            String data = URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8");
            data += "&" + URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(to, "UTF-8");
            
            //Construct URL
            URL url=new URL(baseURL+"receive_new_chat.php?"+data);
            
            if(debug){
                System.out.println(url);
            }
            //Send DATA
            URLConnection conn = url.openConnection();  
            //conn.connect();
            /*
             * It is required to read the feedback from the URL
             * BufferedReader is used to read the feedback from the URL
             */
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(in.ready()){
                /*
                 * The feedback from the URL is stored in the 'out' variable
                 * This variable is passed to the chatPanel to be displayed
                 */
                String nextLine = in.readLine();
                out+=nextLine;
                if(debug){
                    System.out.println(nextLine);
                }
            }
            in.close();
        }
        catch(Exception e)  
        {  
            e.printStackTrace();
        }
        finally{
            return out;
        }
    }
    /*
     * Get profile image takes 1 parameter: username
     */
    public String getProfileImage(String username){
        String out = baseURL;
        try  
        {  
            //Encode the URL to ensure that all the characters send are valid
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            
            //Construct URL
            URL url=new URL(baseURL+"get_profile_image.php?"+data);
            
            if(debug){
                System.out.println(url);
            }
            //Send DATA
            URLConnection conn = url.openConnection();  
            //conn.connect();
            /*
             * It is required to read the feedback from the URL
             * BufferedReader is used to read the feedback from the URL
             */
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while(in.ready()){
                /*
                 * The feedback from the URL is stored in the 'out' variable
                 */
                String nextLine = in.readLine();
                /*
                 * If there is no image associated with the user
                 * the server responds with false
                 */
                if(nextLine.equals("false")||nextLine.equals("")){
                    out = "false";
                }else{
                    out+=nextLine;
                }
                if(debug){
                    System.out.println(nextLine);
                }
            }
            in.close();
        }
        catch(Exception e)  
        {  
            e.printStackTrace();
        }
        finally{
            return out;
        }
    }
}
