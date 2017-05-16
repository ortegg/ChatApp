package com.company;
import java.net.*;
import java.io.*;

/**
 * Created by EdwinOrtega on 5/9/17.
 */
public class Server {
    public static void main(String [] args) throws IOException {
        ServerSocket SS = null;
        System.out.println("Starting server....");
        try {
            SS = new ServerSocket(1955);
        }
        catch (IOException e) {
            System.err.println("Could not listen on port: 1955.");
            System.exit(1);
        }
        Socket clientSocket = null;
        try {
            clientSocket = SS.accept();
        }
        catch (IOException e) {
            System.err.println("Client-Server Accept Failed.");
            System.exit(1);
        }
        System.out.println("Server connected to client...");
        BufferedReader read_key = new BufferedReader(new InputStreamReader(System.in)); //keyboard read in
        OutputStream ostream = clientSocket.getOutputStream();  //Send output to client
        PrintWriter write_out = new PrintWriter(ostream, true); //Print to client

        InputStream istream = clientSocket.getInputStream();    //Get input from client
        BufferedReader read_in = new BufferedReader(new InputStreamReader(istream));    //read message from client

        String send, receive;
        while(true)
        {
            if((receive = read_in.readLine()) != null)
            {
                System.out.println(receive);
            }
            send = read_key.readLine();
            write_out.println(send);
            write_out.flush();
        }
    }
}
