package com.company;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
//import java.util.TreeMap;

/**
 * Created by EdwinOrtega on 5/23/17.
 */
public class EServer{

    //port number for server.
    private static int portNum = 1955;
    //write names of the client on the app while checking that client name isn't in use already.
    private static HashSet<String> names = new HashSet<String>();
//    private static TreeMap<String,String> UserInfo;
    //all the print writers for all the clients.
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    public static void main(String[] args) throws Exception{
        System.out.println("Server is running and accepting connections on port: " + portNum);
        ServerSocket listener = new ServerSocket(portNum);
        //insert_users();
        //print_users();
        try{
            while(true){
                new Handler(listener.accept()).start();
            }
        }
        finally{
            listener.close();
        }
    }

/*    public static void insert_users(){
        try{
            BufferedReader in = new BufferedReader(new FileReader("src/user_info"));
            String [] info;
            String check;
            String username;
            String password;

            UserInfo = new TreeMap();

            while((check = in.readLine()) != null){
                info = check.split(":");
                username = info[0];
                password = info[1];
                UserInfo.put(username, password);
            }
            in.close();             //close the file
        }
        catch(IOException e){       //throw exception if file fails
            System.out.println("Failed to read file.\n");
        }
    }
    public static void print_users(){
        for(Map.Entry<String, String> entry : UserInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.printf("%s : %s\n", key, value);
        }
    }
*/
    //this class manages a single client & sends/receives messages.
    private static class Handler extends Thread{
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        //constructor
        public Handler(Socket socket){
            Login user = new Login();
            user.start();
            if(user.connect) {
                this.socket = socket;
            }
        }

        //make sure screen names are unique, then repeatedly gets inputs and broadcasts them.
        public void run(){
            try{
                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                //Get a name from this client.
                while(true){
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            System.out.println("User: " + name + " has logged in.");
                            names.add(name);
                            break;
                        }
                    }
                }

                //log in successful.
                out.println("NAMEACCEPTED");
                writers.add(out);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while(true){
                    String input = in.readLine();
                    if(input == null){
                        return;
                    }
                    for(PrintWriter writer : writers){
                        writer.println("MESSAGE " + name + ": " + input);
                        System.out.println("User: " + name + " sent a message.");
                    }
                }
            }
            catch(IOException e){
                System.out.println(e);
            }
            finally{
                System.out.println("User: " + name + " has logged off.");
                //client sign off, remove from lists.
                if(name != null){
                    names.remove(name);
                }
                if(out != null){
                    writers.remove(out);
                }
                try{
                    socket.close();
                }
                catch(IOException e){
                }
            }
        }
    }
}
