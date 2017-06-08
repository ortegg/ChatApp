package com.company;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by EdwinOrtega on 5/23/17.
 */
public class EServer{

    //port number for server.
    private static int portNum = 1955;
    //write names of the client on the app while checking that client name isn't in use already.
    private static HashSet<String> names = new HashSet<String>();
    //all the print writers for all the clients.
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    public static void main(String[] args) throws Exception{
        System.out.println("Server is running and accepting connections on port: " + portNum);
        ServerSocket listener = new ServerSocket(portNum);
        try{
            while(true){
                new Handler(listener.accept()).start();
            }
        }
        finally{
            listener.close();
        }
    }
    //this class manages a single client & sends/receives messages.
    private static class Handler extends Thread{
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        //constructor
        public Handler(Socket socket){
            this.socket = socket;
        }
        //make sure screen names are unique, then repeatedly gets inputs and broadcasts them.
        public void run(){
            try {
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
                            System.out.println("User: " + name + " is online.");
                            out.println(name + " is online");
                            names.add(name);
                            break;
                        }
                    }
                }
                //log in successful.
                out.println("NAMEACCEPTED");
                out.println("MESSAGE " + name + " is online.");
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
                out.println("MESSAGE " + name + " has logged off.");
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
