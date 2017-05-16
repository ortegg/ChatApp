package com.company;
import java.net.*;
import java.io.*;
import javax.swing.JFrame;

/**
 * Created by EdwinOrtega on 5/9/17.
 */
public class Client extends JFrame {
    public static void main(String[] args) throws Exception
    {
        Socket sock = new Socket("127.0.0.1", 1955);
        // reading from keyboard with read_key
        BufferedReader read_key = new BufferedReader(new InputStreamReader(System.in));
        // send to client with write_out
        OutputStream ostream = sock.getOutputStream();
        PrintWriter write_out = new PrintWriter(ostream, true);

        // receiving from server ( receiveRead  object)
        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

        System.out.println("Logged in... Type and press the ENTER key to chat"); //user friendly message

        String receive, send;
        while(true)
        {
            send = read_key.readLine();  // keyboard reading
            write_out.println(send);     // sending to server
            write_out.flush();           // flush the buffer
            if((receive = receiveRead.readLine()) != null) //receive from server
                System.out.println(receive); // displaying at DOS prompt
        }
    }
}
